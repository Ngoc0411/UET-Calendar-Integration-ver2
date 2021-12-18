package com.team2.routes;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.team2.model.EventsEntity;
import com.team2.model.MyEvent;
import com.team2.model.UetCoursesAccount;
import com.team2.model.UetExportToken;
import com.team2.model.User;
import com.team2.repository.EventRepository;
import com.team2.security.UserDetailsImpl;

@Component
public class UetCoursesCalendarRoute extends RouteBuilder {
	@Autowired
	EventRepository eventRepository;

	@Override
	public void configure() throws Exception {

		onException(Exception.class)
			.handled(true)
			.to("direct:rest-response/failure");
		
		
		from("direct:uet-courses-calendar")
			.process(e -> e.getIn().setHeader("userid", String.valueOf(e.getIn().getBody(UetCoursesAccount.class).getUserid())))
			.process(e -> e.getIn().setHeader("wstoken", e.getIn().getBody(UetCoursesAccount.class).getToken()))
			.setHeader(Exchange.HTTP_METHOD, constant(org.apache.camel.component.http.HttpMethods.POST))
			.setHeader(Exchange.CONTENT_TYPE, constant("application/x-www-form-urlencoded"))
			
			.process(e -> 
					e.getIn().setBody("wstoken=" + e.getIn().getHeader("wstoken") 
							+ "&wsfunction=core_calendar_get_calendar_export_token"))
			
			.to("https://courses.uet.vnu.edu.vn/webservice/rest/server.php?moodlewsrestformat=json&bridgeEndpoint=true")
			.unmarshal(new JacksonDataFormat(UetExportToken.class))
			.setHeader(Exchange.HTTP_METHOD, constant(org.apache.camel.component.http.HttpMethods.GET))
			.process(e -> e.getIn().setHeader("exporttoken", e.getIn().getBody(UetExportToken.class).getToken()))
			.to("log:com.team2.routes?level=INFO")	
			.setBody(p -> "")		
			.toD("https://courses.uet.vnu.edu.vn/calendar/export_execute.php?userid=${header.userid}&authtoken=${header.exporttoken}&preset_what=all&preset_time=monthnow&bridgeEndpoint=true")
			.process(e -> {
				String strIn = e.getIn().getBody(String.class);
				
				String[] arrayEvent = strIn.split("BEGIN:VEVENT");
				List<EventsEntity> listEvents = new ArrayList<>();
				
				//get user login:
				UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				Long userId = userDetails.getId();
				
				for(int i = 0; i < arrayEvent.length; i ++) {
					if(arrayEvent[i].contains("METHOD:PUBLISH")) continue;
					
					String eventId = getValueByStringRegexFromEventUETCourses("UID:", arrayEvent[i]);
					String title = getValueByStringRegexFromEventUETCourses("SUMMARY:", arrayEvent[i]);
//					String start = getValueByStringRegexFromEventUETCourses("DTSTART:", arrayEvent[i]);
					String end = getValueByStringRegexFromEventUETCourses("DTEND:", arrayEvent[i]);
					String _end = formatTimeFromEventUETCourses(end, 0);
					String _start = formatTimeFromEventUETCourses(end, -1);
					
					EventsEntity eventsExists = eventRepository.findByEventIdAndUserId(eventId, userId.intValue());
					if(eventsExists != null) continue;
					
					EventsEntity event = new EventsEntity(eventId, "Deadline: "+ title, _start, _end, 1, userId.intValue());
					
					//save event to database
					eventRepository.save(event);
					
					//add to list
					listEvents.add(event);
					
					System.out.println(event.toString());
				}
				
				e.getOut().setBody(listEvents);
				e.getOut().setHeader("loop", listEvents.size());
			})
			.loop(header("loop")).copy()
				.process(e -> {
					int i = (Integer) e.getProperty(Exchange.LOOP_INDEX);
					List<MyEvent> listEvents = e.getIn().getBody(List.class);
					MyEvent event = listEvents.get(i);
					e.getOut().setBody(event);
				})
				.to("direct:google-calendar-push-event")
			.end()
			.setBody(p -> "Synchonize with Google Calendar completed!!!")
			.to("log:com.team2.routes?level=INFO");
	}
	
	public static String getValueByStringRegexFromEventUETCourses(String from, String data) {
		
		Pattern pattern = Pattern.compile(from + "(.*?)" + "\r");
        Matcher matcher = pattern.matcher(data);
        if (matcher.find())
        {
            return matcher.group(1);
        }
        return "";
	}
	
	public static JSONObject getJSONObjectFile(String path) throws FileNotFoundException, IOException, ParseException {
		
		JSONParser parser = new JSONParser();
		
		Object obj = parser.parse(new FileReader(path));
		 
		JSONObject jsonObject = (JSONObject) obj;
		
		return jsonObject;
	}
	
	public static String formatTimeFromEventUETCourses(String time, int sub_hour) throws java.text.ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
		Date timestamp = dateFormat.parse(time);
		
		if (sub_hour != 0) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(timestamp);
			cal.add(Calendar.HOUR, sub_hour);
			timestamp = cal.getTime();
		}
		String formattedDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(timestamp);
		return formattedDate;

//        return year + "-" + month + "-" + day + " " + hour + ":" + minutes + ":" + second + ".0000";
//        return year + "-" + month + "-" + day;
    }
}