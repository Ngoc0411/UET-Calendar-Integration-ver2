package com.team2.routes;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

public class RestResponseFailureRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		from("direct:rest-response/failure")
		.process(new Processor() {

			@Override
			public void process(Exchange exchange) throws Exception {
				exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, 406);
				exchange.getMessage().setHeader(Exchange.CONTENT_TYPE, constant("application/json"));
				exchange.getMessage().setBody("{\"status\": \"Failure\"}");	
			} 
		  })
		.convertBodyTo(String.class, "UTF-8")
		.to("log:com.team2.routes?level=INFO");
	}

}
