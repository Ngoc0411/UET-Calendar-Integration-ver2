package com.team2.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.team2.model.LoginRequest;
import com.team2.model.SignupRequest;
import com.team2.service.AuthService;
import com.team2.service.ServiceResult;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping("/signin")
    public ResponseEntity<ServiceResult> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        ServiceResult sr = authService.authenticateUser(loginRequest);
        return sr.getStatus() == ServiceResult.Status.FAILED ? new ResponseEntity<ServiceResult>(sr, HttpStatus.UNAUTHORIZED):
                new ResponseEntity<ServiceResult>(sr, HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        ServiceResult sr = authService.registerUser(signUpRequest);
        return sr.getStatus() == ServiceResult.Status.FAILED ? new ResponseEntity<ServiceResult>(sr, HttpStatus.BAD_REQUEST):
                new ResponseEntity<ServiceResult>(sr, HttpStatus.OK);
    }
}
