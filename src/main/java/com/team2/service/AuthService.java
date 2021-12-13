package com.team2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import com.team2.model.ERole;
import com.team2.model.Role;
import com.team2.model.User;
import com.team2.model.LoginRequest;
import com.team2.model.SignupRequest;
import com.team2.security.JwtResponse;
import com.team2.model.MessageResponse;
import com.team2.repository.RoleRepository;
import com.team2.repository.UserRepository;
import com.team2.security.JwtUtils;
import com.team2.security.UserDetailsImpl;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
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
    PasswordEncoder encoder;

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
