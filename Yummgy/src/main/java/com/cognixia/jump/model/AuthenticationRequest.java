package com.cognixia.jump.model;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;

public class AuthenticationRequest implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Schema(description="Username", example="bomono3")
	private String username;
	@Schema(description="Password", example="Hunter2")
	private String password;
	
	public AuthenticationRequest() {
		
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
}