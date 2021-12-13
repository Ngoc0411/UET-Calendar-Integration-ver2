package com.team2.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(value = {"warnings"})
public class UetExportToken {
	@JsonProperty("token")
	private String token;
	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public String toString() {
		return "UetExportToken [token=" + token + "]";
	}
	

}
