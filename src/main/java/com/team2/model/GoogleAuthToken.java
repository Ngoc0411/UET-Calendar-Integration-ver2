package com.team2.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GoogleAuthToken {
	@JsonProperty("code")
	private String code;
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
}
