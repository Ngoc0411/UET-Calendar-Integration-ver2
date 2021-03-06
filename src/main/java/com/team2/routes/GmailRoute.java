package com.team2.routes;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;

import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.team2.model.MyEvent;
import com.team2.model.UetCoursesAccount;
import com.team2.repository.EventRepository;
import com.team2.repository.UserRepository;
import com.team2.model.EventsEntity;
import com.team2.model.GoogleAccount;

@Component
public class GmailRoute extends RouteBuilder {
	public static final String CLIENT_ID = "905434550263-fe4nhl3ec5u3r1tnkd77pq64053ddb6m.apps.googleusercontent.com";
	public static final String CLIENT_SECRET = "client_secret_905434550263-fe4nhl3ec5u3r1tnkd77pq64053ddb6m.apps.googleusercontent.com.json";
	private static final NetHttpTransport NET_HTTP_TRANSPORT = new NetHttpTransport();
	private static final GsonFactory GSON_FACTORY = GsonFactory.getDefaultInstance();
	private static final String APPLICATION_NAME = "Gmail Route";
	
	@Autowired
	EventRepository eventRepository;
	
	@Autowired
	UserRepository userRepository;
		
	public static List<String> getDate(String mail) {
        Matcher m = Pattern.compile("(\\d{1,2}[-/]\\d{1,2}[-/]\\d{4})", Pattern.CASE_INSENSITIVE).matcher(mail);
        List<String> dates = new ArrayList<String>();
        while (m.find()) {
            dates.add(m.group(1).replaceAll("/", "-"));
//            System.out.println(m.group(1));
        }
        return dates;
    }
	
	public static JSONObject getJSONObjectFile(String path) throws FileNotFoundException, IOException, ParseException {
		
		JSONParser parser = new JSONParser();
		
		Object obj = parser.parse(new FileReader(path));
		 
		JSONObject jsonObject = (JSONObject) obj;
		
		return jsonObject;
	}
	
		
	public static String formatTime(String time, int sub_hour) throws java.text.ParseException {
		time = time + " 00-00-00";
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss");
		Date timestamp = dateFormat.parse(time);
		
		if (sub_hour != 0) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(timestamp);
			cal.add(Calendar.HOUR, sub_hour);
			timestamp = cal.getTime();
		}
		String formattedDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(timestamp);
		return formattedDate;
    }
	
	
	@Override
	public void configure() throws Exception {
		
		onException(Exception.class)
		.handled(true)
		.to("direct:rest-response/failure");
	
	
		from("direct:google-gmail")
		.process(e -> {
			
			Long integrationUserId = e.getIn().getBody(GoogleAccount.class).getIntegrationUserId();
			e.getIn().setHeader("integration_user_id", String.valueOf(integrationUserId));
			
			String accessToken = e.getIn().getBody(GoogleAccount.class).getToken();
			String refreshToken = e.getIn().getBody(GoogleAccount.class).getRefreshToken();
			Long google_id =  e.getIn().getBody(GoogleAccount.class).getId();
			
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
			
			Gmail service = new Gmail.Builder(NET_HTTP_TRANSPORT, GSON_FACTORY, c)
		            .setApplicationName(APPLICATION_NAME)
		            .build();
		    
			// Recursively take UNREAD email only
		    ListMessagesResponse response = service.users().messages().list("me").setQ("is:unread in:inbox").execute();
	        List<Message> messages = new ArrayList<Message>();
	        while (response.getMessages() != null) {
	            messages.addAll(response.getMessages());
	            if (response.getNextPageToken() != null) {
	                String pageToken = response.getNextPageToken();
	                response = service.users().messages().list("me").setQ("is:unread in:inbox").setPageToken(pageToken).execute();
	            } else {
	                break;
	            }
	        }
	        
	        List<EventsEntity> listEvents = new ArrayList<>();
		    
		    if (messages.isEmpty()) {
		        System.out.println("No messages found.");
		    } else {
		        System.out.println("Messages:");
		        for (Message message : messages) {
		        	Message detail = service.users().messages().get("me", message.getId()).setFormat("full").execute();
		            System.out.printf("- %s\n", detail.getSnippet());
		            // check event exists in db 
		            // exists -> continue

		            EventsEntity eventsExists = eventRepository.findByEventIdAndUserId(message.getId(), integrationUserId.intValue());
					if(eventsExists != null) continue;


		            List<String> dates = getDate(detail.getSnippet());
		            if (dates.size() == 1) {
		                String end_time = dates.get(0);
		                EventsEntity event = new EventsEntity(message.getId(), detail.getSnippet().substring(0, Math.min(detail.getSnippet().length(), 64)) + "...", 
		                		formatTime(end_time, -1), formatTime(end_time, 0),
		                		2, integrationUserId.intValue());
		                
		                listEvents.add(event);
		                // push to calendar
		            }
		            else if (dates.size() >= 2) {
		                String start_time = dates.get(0);
		                String end_time = dates.get(1);
		                EventsEntity event = new EventsEntity(message.getId(), detail.getSnippet().substring(0, Math.min(detail.getSnippet().length(), 64)) + "...", 
		                		formatTime(end_time, -1), formatTime(end_time, 0),
		                		2, integrationUserId.intValue());
		                //save event to database
						eventRepository.save(event);
		                
		                listEvents.add(event);
		                // push to calendar
		            }
		            else {
		                System.out.println("Khong co ngay thang trong mail");
		            }
		        }
		    }
		    e.getOut().setBody(listEvents);
			e.getOut().setHeader("loop", listEvents.size());
			e.getOut().setHeader("integration_user_id", integrationUserId.intValue());
		})
		.loop(header("loop")).copy()
			.process(e -> {
				int i = (Integer) e.getProperty(Exchange.LOOP_INDEX);
				int user_id = (int) e.getIn().getHeader("integration_user_id");
				List<EventsEntity> listEvents = e.getIn().getBody(List.class);
				EventsEntity event = listEvents.get(i);
				e.getOut().setHeader("integration_user_id", user_id);
				e.getOut().setBody(event);
			})
			.to("direct:google-calendar-push-event")
		.end()
		.setBody(p -> "Synchonize Gmail with Google Calendar completed!!!")
		.to("log:com.team2.routes?level=INFO");
	}

}