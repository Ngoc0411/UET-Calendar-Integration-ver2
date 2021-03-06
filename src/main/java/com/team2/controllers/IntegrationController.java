package com.team2.controllers;

import java.io.IOException;
import java.util.List;

import javax.validation.Valid;

import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.team2.model.EventsEntity;
import com.team2.model.GoogleAccount;
import com.team2.model.GoogleAuthToken;
import com.team2.model.LoginRequest;
import com.team2.model.SignupRequest;
import com.team2.model.UetAuthRequest;
import com.team2.model.UetAuthToken;
import com.team2.model.UetCoursesAccount;
import com.team2.service.AuthService;
import com.team2.service.EventService;
import com.team2.service.ServiceResult;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/integration")
public class IntegrationController {

    @Autowired
    AuthService authService;
    
    @Autowired
    EventService eventService;
    
    @Autowired
    ProducerTemplate producerTemplate;

    @PostMapping("/uet-auth")
    @ResponseBody
    public ResponseEntity<ServiceResult> updateUetAuthToken(@Valid @RequestBody UetAuthToken uetAuthToken) {    
    	// save to database
    	ServiceResult sr = authService.saveUetCoursesAccount(producerTemplate, uetAuthToken);
    	
        return sr.getStatus() == ServiceResult.Status.FAILED ? new ResponseEntity<ServiceResult>(sr, HttpStatus.UNAUTHORIZED):
            new ResponseEntity<ServiceResult>(sr, HttpStatus.OK);

    }

    @PostMapping("/google-auth")
    public ResponseEntity<ServiceResult> updateGoogleAuthToken(@Valid @RequestBody GoogleAuthToken googleAuthToken) {    	
    	// save to database
    	ServiceResult sr = authService.saveGoogleAccount(producerTemplate, googleAuthToken);

        return sr.getStatus() == ServiceResult.Status.FAILED ? new ResponseEntity<ServiceResult>(sr, HttpStatus.UNAUTHORIZED):
            new ResponseEntity<ServiceResult>(sr, HttpStatus.OK);
    }

    @PostMapping("/get-calendars-from-google-calendars")
    @ResponseBody
    public ResponseEntity<ServiceResult> getEventsFromGGCalendars() {
    	List<EventsEntity> listEvents = eventService.getEventsFromGGCalendars(producerTemplate);
    	ServiceResult sr = new ServiceResult();
    	sr.setData(listEvents);
    	
        return sr.getStatus() == ServiceResult.Status.FAILED ? new ResponseEntity<ServiceResult>(sr, HttpStatus.UNAUTHORIZED):
            new ResponseEntity<ServiceResult>(sr, HttpStatus.OK);
    }
    
    @PostMapping("/exchange-uet-token")
    @ResponseBody
    public String exchangeUetToken(@Valid @RequestBody UetAuthRequest uetAuthRequest) throws Exception {
    	producerTemplate.start();
    	String response = producerTemplate.requestBody("direct:uet-exchange-token", uetAuthRequest, String.class);
    	producerTemplate.stop();

    	return response;
    }
    
    @PostMapping("/synchronize-calendars")
    @ResponseBody
    public String synchronizeCalendars() throws Exception {
    	producerTemplate.start();
    	producerTemplate.requestBody("direct:service-scheduler", "");
    	producerTemplate.stop();

    	return "";
    }
}
