package com.team2.routes;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class SchedulerRoute extends RouteBuilder {
	@Override
	// TODO: This will need to change when we have more than one user
	public void configure() throws Exception {
		from("scheduler://schedulerjob?delay=846000&initialDelay=5&timeUnit=SECONDS")
		.to("direct:google-gmail", "direct:uet-courses-calendar");
	}
}
