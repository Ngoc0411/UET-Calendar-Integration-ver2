package com.team2.routes;

import java.io.InputStream;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.springframework.stereotype.Component;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.team2.model.GoogleAuthToken;
import com.team2.model.UetAuthInfo;
import com.team2.model.UetAuthToken;

@Component
public class GoogleAuthRoute extends RouteBuilder {
	
	private static final NetHttpTransport NET_HTTP_TRANSPORT = new NetHttpTransport();
	private static final GsonFactory GSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String APPLICATION_NAME = "Google Auth";
    public static final String CLIENT_SECRET = "client_secret_905434550263-fe4nhl3ec5u3r1tnkd77pq64053ddb6m.apps.googleusercontent.com.json";
	
	@Override
	public void configure() throws Exception {
	
		onException(Exception.class)
			.handled(true)
			.to("direct:rest-response/failure");
	
		from("direct:google-auth")
			//Get uet auth token:
			.process(e -> {
				InputStream inputStream = this.getContext().getClassResolver().loadResourceAsStream(CLIENT_SECRET);
				
		    	GoogleClientSecrets clientSecrets = GsonFactory.getDefaultInstance()
		    			.fromInputStream(inputStream, GoogleClientSecrets.class);
		    	
		    	GoogleTokenResponse tokenResponse =
		    			new GoogleAuthorizationCodeTokenRequest(
					        	  NET_HTTP_TRANSPORT,
					        	  GSON_FACTORY,
					              "https://oauth2.googleapis.com/token",
					              clientSecrets.getDetails().getClientId(),
					              clientSecrets.getDetails().getClientSecret(),
					              e.getIn().getBody(GoogleAuthToken.class).getCode(), 
					              WebServiceRoute.HOSTNAME)  // Specify the same redirect URI that you use with your web
								                             // app. If you don't have a web version of your app, you can
								                             // specify an empty string.
					              .execute();
		    	e.getOut().setBody(tokenResponse);
			})
			.to("log:com.team2.routes?level=INFO");
	}
}
