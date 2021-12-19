package com.team2.service;

import java.util.List;

import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.team2.model.EventsEntity;
import com.team2.model.UetCoursesAccount;
import com.team2.repository.EventRepository;
import com.team2.security.UserDetailsImpl;

@Service
public class EventService {

	@Autowired
	EventRepository eventRepository;

	
	public String getEventsFromGGCalendars(ProducerTemplate producerTemplate){
		
        try {
        	//UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        	// retrieve user id
        	producerTemplate.start();
        	String listEvents = (String) producerTemplate.requestBody("direct:google-calendar");
        	producerTemplate.stop();
        	
        	return listEvents;
       
        } catch (Throwable t) {
            return null;
        }
	}
}
