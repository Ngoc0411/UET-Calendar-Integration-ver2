package com.team2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.apache.camel.ProducerTemplate;

import org.springframework.web.bind.annotation.RequestBody;

import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.json.gson.GsonFactory;
import com.team2.model.ERole;
import com.team2.model.GoogleAuthToken;
import com.team2.model.Role;
import com.team2.model.User;
import com.team2.model.LoginRequest;
import com.team2.model.SignupRequest;
import com.team2.model.UetAuthToken;
import com.team2.routes.WebServiceRoute;
import com.team2.model.UetCoursesAccount;
import com.team2.model.GoogleAccount;
import com.team2.security.JwtResponse;
import com.team2.model.MessageResponse;
import com.team2.repository.RoleRepository;
import com.team2.repository.UserRepository;
import com.team2.repository.GoogleAccountRespository;
import com.team2.repository.UetCoursesRepository;
import com.team2.security.JwtUtils;
import com.team2.security.UserDetailsImpl;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;

import javax.validation.Valid;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class AuthService {
	    
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;
    
    @Autowired
    UetCoursesRepository uetCoursesRepository;
    
    @Autowired
    GoogleAccountRespository googleAccountRepository;

    @Autowired
    PasswordEncoder encoder;
    
    public ServiceResult saveUetCoursesAccount(ProducerTemplate producerTemplate, UetAuthToken uet) {
        ServiceResult result = new ServiceResult();

        try {
        	UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        	// retrieve user id
        	producerTemplate.start();
        	String userid = producerTemplate.requestBody("direct:uet-auth", uet, String.class);
        	producerTemplate.stop();
        	
        	String privateToken = uet.getPrivateToken();
        	String token = uet.getToken();
        	
            // Create new UetCoursesAccount account
        	UetCoursesAccount account = new UetCoursesAccount();
        	account.setToken(token);
        	account.setPrivateToken(privateToken);
        	account.setUserid(Long.parseLong(userid));
        	account.setUsername(userDetails.getUsername());
        	
        	account = uetCoursesRepository.save(account);
        	
        	Optional<User> user = userRepository.findById(userDetails.getId());
        	
        	user.get().setUet_courses_id(account.getId());
        	userRepository.save(user.get());
 
        	result.setData(account);
        } catch (Throwable t) {
//          e.printStackTrace();
            result.setData(null);
            result.setMessage("Error: Username or password incorrect! Try again");
            result.setStatus(ServiceResult.Status.FAILED);
        }

    	return result;
    }
    
    public ServiceResult saveGoogleAccount(ProducerTemplate producerTemplate, GoogleAuthToken gat) {
        ServiceResult result = new ServiceResult();

        try {
	    	UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	
	    	
	    	// retrieve Google access token & refresh token
	    	producerTemplate.start();
	    	GoogleTokenResponse tokenResponse = producerTemplate.requestBody("direct:google-auth", 
	    			gat, GoogleTokenResponse.class);
	    	producerTemplate.stop();
	    	
	    	String code = gat.getCode();
	    	
	    	GoogleAccount account = new GoogleAccount(null,
	    			tokenResponse.getIdToken(),
	    			tokenResponse.getAccessToken(),
	    			tokenResponse.getRefreshToken(),
	    			tokenResponse.getExpiresInSeconds(),
	    			tokenResponse.getTokenType(),
	    			tokenResponse.getScope(),
	    			code);
	
	    	account = googleAccountRepository.save(account);
	    	Optional<User> user = userRepository.findById(userDetails.getId());

    		user.get().setGoogle_id(account.getId());
    		userRepository.save(user.get());

	    	result.setData(account);
	    } catch (Throwable t) {
	//      e.printStackTrace();
	        result.setData(null);
	        result.setMessage("Error: Username or password incorrect! Try again");
	        result.setStatus(ServiceResult.Status.FAILED);
	    }
	    
    	return result;
    }

    public ServiceResult authenticateUser(LoginRequest loginRequest){
        ServiceResult result = new ServiceResult();

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(item -> item.getAuthority())
                    .collect(Collectors.toList());

            result.setData(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                roles));

        } catch (Exception e) {
//            e.printStackTrace();
            result.setData(null);
            result.setMessage("Error: Username or password incorrect! Try again");
            result.setStatus(ServiceResult.Status.FAILED);
        }

        return result;
    }

    public ServiceResult registerUser(SignupRequest signUpRequest) {

        ServiceResult result = new ServiceResult();

        try {
            if (userRepository.existsByUsername(signUpRequest.getUsername())) {
                result.setData(null);
                result.setMessage("Error: Username is already taken!");
                result.setStatus(ServiceResult.Status.FAILED);
                return result;
            }

//            if (userRepository.existsByEmail(signUpRequest.getEmail())) {
//                result.setData(null);
//                result.setMessage("Error: Email is already in use!");
//                result.setStatus(ServiceResult.Status.FAILED);
//                return result;
//            }

            // Create new user's account
            User user = new User(signUpRequest.getUsername(),
                    encoder.encode(signUpRequest.getPassword()),
                    null,
                    null);

            Set<String> strRoles = signUpRequest.getRole();
            Set<Role> roles = new HashSet<>();

            if (strRoles == null) {
                Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                roles.add(userRole);
            } else {
                strRoles.forEach(role -> {
                    switch (role) {
                        case "admin":
                            Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                            roles.add(adminRole);

                            break;
                        case "mod":
                            Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                            roles.add(modRole);

                            break;
                        default:
                            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                            roles.add(userRole);
                    }
                });
            }

            user.setRoles(roles);
            userRepository.save(user);
            result.setMessage("User registered successfully!");

        } catch (Exception e) {
            e.printStackTrace();
            result.setData(null);
            result.setMessage(e.getMessage());
            result.setStatus(ServiceResult.Status.FAILED);
        }

        return result;
    }
}
