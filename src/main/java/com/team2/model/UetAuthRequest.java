package com.team2.model;

import javax.validation.constraints.NotBlank;

public class UetAuthRequest {

    @NotBlank
    private String username;

    @NotBlank
    private String password;
    
    public String getUsername() {
    	return username;
    }

    public String getPassword() {
    	return password;
    }
    
    public void setUsername(String username) {
    	this.username = username;
    }
    
    public void setPassword(String password) {
    	this.password = password;
    }
}
