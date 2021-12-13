package com.team2.routes;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.team2.model.MyEvent;
import com.team2.model.UetExportToken;


public class UetCoursesCalendarRoute extends RouteBuilder {
	
	// TODO: Fill this function
	public static boolean check_event_exist(String event_id) {
		return false;
	}

	@Override
	public void configure() throws Exception {

		onException(Exception.class)
			.handled(true)
			.to("direct:rest-response/failure");
		
		
		from("direct:uet-courses-calendar")
			.setHeader(Exchange.HTTP_METHOD, constant(org.apache.camel.component.http.HttpMethods.POST))
			.setHeader(Exchange.CONTENT_TYPE, constant("application/x-www-form-urlencoded"))
			
			.process(e -> 
					e.getIn().setBody("wstoken=" + (String) getJSONObjectFile("./src/data/uet_auth_token.json")
									.get("token") + "&wsfunction=core_calendar_get_calendar_export_token"))
			
			.to("https://courses.uet.vnu.edu.vn/webservice/rest/server.php?moodlewsrestformat=json&bridgeEndpoint=true")
			.unmarshal(new JacksonDataFormat(UetExportToken.class))
			.process(e -> 
			{
				int userid = ((Long) getJSONObjectFile("./src/data/uet_auth_info.json")
						.get("userid")).intValue();
				e.getIn().setHeader("exporttoken", e.getIn().getBody(UetExportToken.class).getToken());
				e.getIn().setHeader("userid", userid);
			})	
			.setHeader(Exchange.HTTP_METHOD, constant(org.apache.camel.component.http.HttpMethods.GET))
			.to("log:com.team2.routes?level=INFO")	
			.setBody(p -> "")		
			.toD("https://courses.uet.vnu.edu.vn/calendar/export_execute.php?userid=${header.userid}&authtoken=${header.exporttoken}&preset_what=all&preset_time=monthnow&bridgeEndpoint=true")
			.process(e -> {
				String strIn = e.getIn().getBody(String.class);
				
				String[] arrayEvent = strIn.split("BEGIN:VEVENT");
				List<MyEvent> listEvents = new ArrayList<>();
				
				
				for(int i = 0; i < arrayEvent.length; i ++) {
					if(arrayEvent[i].contains("METHOD:PUBLISH")) continue;
					
					String event_id = getValueByStringRegexFromEventUETCourses("UID:", arrayEvent[i]);
					
					if (check_event_exist(event_id)) {
						continue;
					}
					
					String title = getValueByStringRegexFromEventUETCourses("SUMMARY:", arrayEvent[i]);
//					String start = getValueByStringRegexFromEventUETCourses("DTSTART:", arrayEvent[i]);
					String end = getValueByStringRegexFromEventUETCourses("DTEND:", arrayEvent[i]);
					
//					start = formatTimeFromEventUETCourses(start);
					String _end = formatTimeFromEventUETCourses(end, 0);
					String _start = formatTimeFromEventUETCourses(end, -1);
					
					MyEvent event = new MyEvent("Deadline: "+ title, _start, _end);
					listEvents.add(event);
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