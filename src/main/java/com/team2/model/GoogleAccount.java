package com.team2.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(	name = "google_account" )

public class GoogleAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 1500)
    @Column(name = "google_account")
    private String googleAccount;
    
    @Size(max = 1550)
    @Column(name = "token")
    private String token;

    @Column(name = "expires_in")
    private Long expiresIn;

    @Column(name = "integration_user_id")
    private Long integrationUserId;
    
    @Size(max = 50)
    @Column(name = "token_type")
    private String tokenType;
    
    @Size(max = 1500)
    @Column(name = "scope")
    private String scope;

    @Size(max = 1520)
    @Column(name = "refresh_token")
    private String refreshToken;

    @Size(max = 1520)
    @Column(name = "code")
    private String code;
    
    public GoogleAccount() {
    	
    }

	public GoogleAccount(Long id, Long integrationUserId, @Size(max = 50) String googleAccount, @Size(max = 250) String token, @Size(max = 120) String refreshToken, Long expiresIn,
			@Size(max = 50) String tokenType, @Size(max = 200) String scope, @Size(max = 120) String code) {
		super();
		this.id = id;
		this.integrationUserId = integrationUserId;
		this.googleAccount = googleAccount;
		this.token = token;
		this.refreshToken = refreshToken;
		this.expiresIn = expiresIn;
		this.tokenType = tokenType;
		this.scope = scope;
		this.code = code;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getGoogleAccount() {
		return googleAccount;
	}

	public void setGoogleAccount(String googleAccount) {
		this.googleAccount = googleAccount;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Long getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(Long expiresIn) {
		this.expiresIn = expiresIn;
	}

	public String getTokenType() {
		return tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Long getIntegrationUserId() {
		return integrationUserId;
	}

	public void setIntegrationUserId(Long integrationUserId) {
		this.integrationUserId = integrationUserId;
	}
}
