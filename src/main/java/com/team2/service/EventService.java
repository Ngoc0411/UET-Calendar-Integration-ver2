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

	
	public List<EventsEntity> getEventsFromGGCalendars(ProducerTemplate producerTemplate){
		
        try {
        	UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        	// retrieve user id
        	producerTemplate.start();
        	List<EventsEntity> listEvents = producerTemplate.requestBodyAndHeader("direct:google-calendar", "", 
        															"integration_user_id", userDetails.getId().intValue(), 
        															List.class);
        	producerTemplate.stop();
        	
        	return listEvents;
       
        } catch (Throwable t) {
            return null;
        }
	}
}
