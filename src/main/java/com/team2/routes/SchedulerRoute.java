package com.team2.routes;

import org.apache.camel.builder.RouteBuilder;

public class SchedulerRoute extends RouteBuilder {
	@Override
	// TODO: This will need to change when we have more than one user
	public void configure() throws Exception {
		from("scheduler://schedulerjob?delay=846000&initialDelay=30&timeUnit=SECONDS")
		.to("direct:google-gmail", "direct:uet-courses-calendar");
	}
}
