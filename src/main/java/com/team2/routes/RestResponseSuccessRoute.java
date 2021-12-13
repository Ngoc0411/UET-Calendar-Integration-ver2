package com.team2.routes;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team2.model.UetAuthInfo;
import com.team2.model.UetAuthToken;

public class RestResponseSuccessRoute extends RouteBuilder {
	
	@Override
	public void configure() throws Exception {
		from("direct:rest-response/success")
		.process(new Processor() {

			@Override
			public void process(Exchange exchange) throws Exception {
				exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, 200);
				exchange.getMessage().setHeader(Exchange.CONTENT_TYPE, constant("application/json"));
				String response;
				if (exchange.getIn().getBody() instanceof String || exchange.getIn().getBody() instanceof byte[]) {
					response = "{\"status\": \"Success\", \"data\": " + exchange.getIn().getBody(String.class) + "}";
				} else {
					response = "{\"status\": \"Success\" }";
				}
				exchange.getMessage().setBody(response);
			} 
		  })
		.convertBodyTo(String.class, "UTF-8")
		.to("log:com.team2.routes?level=INFO");
		
		
		from("direct:rest-response-uet-auth-token/success")
		.process(new Processor() {
			@Override
			public void process(Exchange exchange) throws Exception {
				
				exchange.getMessage().setBody(exchange.getIn().getBody(UetAuthToken.class));
				
			} 
		  })
		.marshal(new JacksonDataFormat(UetAuthToken.class))
		.to("log:com.team2.routes?level=INFO");
		
		
		from("direct:rest-response-uet-auth-info/success")
		.process(new Processor() {
			@Override
			public void process(Exchange exchange) throws Exception {
				
				exchange.getMessage().setBody(exchange.getIn().getBody(UetAuthInfo.class));
				
			} 
		  })
		.marshal(new JacksonDataFormat(UetAuthInfo.class))
		.to("log:com.team2.routes?level=INFO");
		
	}

}
