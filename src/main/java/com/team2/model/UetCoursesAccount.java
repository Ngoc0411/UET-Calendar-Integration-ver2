package com.team2.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(	name = "uet_courses_account" )

public class UetCoursesAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 50)
    private String username;

    private Long userid;
    
    @Size(max = 120)
    private String token;
    
    @Column(name = "integration_user_id")
    private Long integrationUserId;

    @Size(max = 120)
    @Column(name = "private_token")
    private String privateToken;
    
    public Long getId() {
    	return id;
    }
    
    public void setId(Long id) {
    	this.id = id;
    }
    
    public String getUsername() {
    	return username;
    }
    
    public void setUsername(String username) {
    	this.username = username;
    }
    
    public Long getUserid() {
    	return userid;
    }
    
    public void setUserid(Long userid) {
    	this.userid = userid;
    }
    
    public String getToken() {
    	return token;
    }
    
    public void setToken(String token) {
    	this.token = token;
    }
    
    public String getPrivateToken() {
    	return privateToken;
    }
    
    public void setPrivateToken(String privateToken) {
    	this.privateToken = privateToken;
    }

	public Long getIntegrationUserId() {
		return integrationUserId;
	}

	public void setIntegrationUserId(Long integrationUserId) {
		this.integrationUserId = integrationUserId;
	}   
}
