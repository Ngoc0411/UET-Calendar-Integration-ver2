package com.team2.routes;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.camel.builder.RouteBuilder;

import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.Events;
import com.google.api.services.gmail.Gmail;
import com.google.gson.Gson;

import com.team2.model.MyEvent;

@Component
public class GoogleCalendarRoute extends RouteBuilder {
	public static final String CLIENT_ID = "905434550263-fe4nhl3ec5u3r1tnkd77pq64053ddb6m.apps.googleusercontent.com";
	public static final String CLIENT_SECRET = "client_secret_905434550263-fe4nhl3ec5u3r1tnkd77pq64053ddb6m.apps.googleusercontent.com.json";
	private static final NetHttpTransport NET_HTTP_TRANSPORT = new NetHttpTransport();
	private static final GsonFactory GSON_FACTORY = GsonFactory.getDefaultInstance();
	private static final String APPLICATION_NAME = "Calendar Route";
	
	public static JSONObject getJSONObjectFile(String path) throws FileNotFoundException, IOException, ParseException {
		
		JSONParser parser = new JSONParser();
		
		Object obj = parser.parse(new FileReader(path));
		 
		JSONObject jsonObject = (JSONObject) obj;
		
		return jsonObject;
	}
	
	
	public Calendar init_connection() throws Exception {
		String accessToken = "ya29.a0ARrdaM9auCr7SDsKaT9d9hTs-mhj6i5amFyuMkep21rMJmjyqSKeTD34FfDFrpiLZsXcCqtjynEwYwmv6veeHD3Xg2OskAFhu2vjc9MyDu8Jk3JDqcaNEclM8msIOhsszL2Vvc5xXTon6RZAhXJdU5pz3s4Q";
		String refreshToken = "1//0eYOiTu4V_LNwCgYIARAAGA4SNwF-L9IrdNVBD96Bf1OoccrXdylpEj-rkhvCONktUZdomrK1_XaGgi9drfHO2wMiT1IkW9u2IZA";

		InputStream inputStream = this.getContext().getClassResolver().loadResourceAsStream(CLIENT_SECRET);
		
    	GoogleClientSecrets clientSecrets = GsonFactory.getDefaultInstance()
    			.fromInputStream(inputStream, GoogleClientSecrets.class);
    	
		GoogleCredential c = new GoogleCredential.Builder()
				.setTransport(NET_HTTP_TRANSPORT)
				.setClientSecrets(clientSecrets)
				.setJsonFactory(GSON_FACTORY)
				.build()
				.setAccessToken(accessToken)
				.setRefreshToken(refreshToken);
		
		Calendar service = new Calendar.Builder(NET_HTTP_TRANSPORT, GSON_FACTORY, c)
	            .setApplicationName(APPLICATION_NAME)
	            .build();
		return service;
	}
	
	
	@Override
	public void configure() throws Exception {
		
		onException(Exception.class)
		.handled(true)
		.to("direct:rest-response/failure");
		
		from("direct:google-calendar-push-event")
		.process(e -> {
			System.out.println("START PUSHING EVENT TO CALENDAR");
			Calendar service = init_connection();
			MyEvent my_event = e.getIn().getBody(MyEvent.class);
			
			Event event = new Event()
				    .setSummary(my_event.getTitle());
			
			DateTime startDateTime = new DateTime(my_event.getStart());
			EventDateTime start = new EventDateTime()
			    .setDateTime(startDateTime)
			    .setTimeZone("Asia/Ho_Chi_Minh");
			event.setStart(start);

			DateTime endDateTime = new DateTime(my_event.getEnd());
			EventDateTime end = new EventDateTime()
			    .setDateTime(endDateTime)
			    .setTimeZone("Asia/Ho_Chi_Minh");
			event.setEnd(end);
			
			String calendarId = "primary";
			event = service.events().insert(calendarId, event).execute();
			
		}).to("log:com.team2.routes?level=INFO");
	
	
		from("direct:google-calendar")
		.to("direct:google-gmail", "direct:uet-courses-calendar")
		.process(e -> {
			Calendar service = init_connection();

			// List the next 10 events from the primary calendar.
		    DateTime now = new DateTime(System.currentTimeMillis());
		    Events events = service.events().list("primary")
//		            .setMaxResults(30)
		            .setTimeMin(now)
		            .setOrderBy("startTime")
		            .setSingleEvents(true)
		            .execute();
		    List<Event> items = events.getItems();
		    List<String> listEvents = new ArrayList<>();
		    if (items.isEmpty()) {
		        System.out.println("No upcoming events found.");
		    } else {
		        System.out.println("Upcoming events");
		        for (Event event : items) {
		            DateTime start = event.getStart().getDateTime();
		            if (start == null) {
		                start = event.getStart().getDate();
		            }
		            DateTime end = event.getEnd().getDateTime();
		            if (end == null) {
		                end = event.getStart().getDate();
		            }
		            MyEvent _event = new MyEvent(event.getSummary(), start.toString().split("T")[0], end.toString().split("T")[0]);
	                Gson gson = new Gson();
					String jsonObjectEvent = gson.toJson(_event);
	                listEvents.add(jsonObjectEvent);
	                
		        }     
		        e.getOut().setBody(listEvents);
		    }
		})
		.to("log:com.team2.routes?level=INFO");
	}

}