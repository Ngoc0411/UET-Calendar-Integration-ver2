package com.team2.routes;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.springframework.stereotype.Component;

import com.team2.model.UetAuthInfo;
import com.team2.model.UetAuthRequest;
import com.team2.model.UetAuthToken;

@Component
public class ExchangeUetTokenRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		onException(Exception.class)
			.handled(true)
			.to("direct:rest-response/failure");
		
		from("direct:uet-exchange-token")
			.process(e -> e.getIn().setHeader("username", e.getIn().getBody(UetAuthRequest.class).getUsername()))
			.process(e -> e.getIn().setHeader("password", e.getIn().getBody(UetAuthRequest.class).getPassword()))
			.to("log:com.team2.routes?level=INFO")
			
			//Get uet auth info:
			.setHeader(Exchange.HTTP_METHOD, constant(org.apache.camel.component.http.HttpMethods.POST))
			.setHeader(Exchange.CONTENT_TYPE, constant("application/x-www-form-urlencoded"))
			.process(e -> e.getIn().setBody("username=" + e.getIn().getHeader("username")
					  + "&password=" + e.getIn().getHeader("password")))
			.to("https://courses.uet.vnu.edu.vn/login/token.php?service=moodle_mobile_app&bridgeEndpoint=true")
			//.unmarshal(new JacksonDataFormat(UetAuthToken.class))
			.to("log:com.team2.routes?level=INFO");

	}
}
