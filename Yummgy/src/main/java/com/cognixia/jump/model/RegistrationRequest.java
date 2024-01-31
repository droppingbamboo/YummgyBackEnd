package com.cognixia.jump.model;

import java.util.Objects;

public class RegistrationRequest {
	private String yumUsername;
	private final String yumPassword;
	private final String email;
	
	public RegistrationRequest(String yumUsername, String yumPassword, String email) {
		super();
		this.yumUsername = yumUsername;
		this.yumPassword = yumPassword;
		this.email = email;
	}
	
	public String getYumUsername() {
		return yumUsername;
	}
	public void setYumUsername(String yumUsername) {
		this.yumUsername = yumUsername;
	}
	public String getYumPassword() {
		return yumPassword;
	}
	public String getEmail() {
		return email;
	}

	@Override
	public int hashCode() {
		return Objects.hash(email, yumPassword, yumUsername);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RegistrationRequest other = (RegistrationRequest) obj;
		return Objects.equals(email, other.email) && Objects.equals(yumPassword, other.yumPassword)
				&& Objects.equals(yumUsername, other.yumUsername);
	}

	@Override
	public String toString() {
		return "RegistrationRequest [yumUsername=" + yumUsername + ", yumPassword=" + yumPassword + ", email=" + email
				+ "]";
	}
	
	
	
}
