package com.team2.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UetAuthToken {

	@JsonProperty("token")
	private String token;
	
	@JsonProperty("privatetoken")
	private String privateToken;

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

	@Override
	public String toString() {
		return "UetAuthToken [token=" + token + ", privateToken=" + privateToken + "]";
	}
	
	
}
