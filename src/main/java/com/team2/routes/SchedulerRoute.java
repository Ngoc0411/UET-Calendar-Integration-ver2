package com.team2.routes;

import java.util.Optional;

import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.team2.model.GoogleAccount;
import com.team2.model.UetCoursesAccount;
import com.team2.model.User;
import com.team2.repository.GoogleAccountRepository;
import com.team2.repository.UetCoursesRepository;
import com.team2.repository.UserRepository;

@Component
public class SchedulerRoute extends RouteBuilder {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	GoogleAccountRepository gaRepository;
	
	@Autowired
	UetCoursesRepository uetRepository;

	@Autowired
	ProducerTemplate producerTemplate;
	
	@Override
	// TODO: This will need to change when we have more than one user
	public void configure() throws Exception {
		from("scheduler://schedulerjob?delay=846000&initialDelay=5&timeUnit=SECONDS")
			.to("direct:service-scheduler");
		
		from("direct:service-scheduler")
			.process(e -> {	
				for (User user : userRepository.findAll()) {
					if (user.getUet_courses_id() != null) {
						Optional<UetCoursesAccount> uet = uetRepository.findById(user.getUet_courses_id());
						if (uet.isPresent()) {
							producerTemplate.start();
							producerTemplate.sendBody("direct:uet-courses-calendar", uet.get());
							producerTemplate.stop();
						}
					}
					
//					if (user.getGoogle_id() != null) {
//						Optional<GoogleAccount> ga = gaRepository.findById(user.getGoogle_id());
//						if (ga.isPresent()) {
//							producerTemplate.start();
//							producerTemplate.sendBody("direct:google-gmail", user.getGoogle_id());
//							producerTemplate.stop();
//						}
//					}
				}
			});
	}
}
