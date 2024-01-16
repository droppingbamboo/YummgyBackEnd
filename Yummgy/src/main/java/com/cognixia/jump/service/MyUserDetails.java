package com.cognixia.jump.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.cognixia.jump.model.User;

public class MyUserDetails implements UserDetails {

	private static final long serialVersionUID = 1L;
	private String username;
	private String password;
	private boolean expired;
	private boolean locked;
	private boolean credentialsBad;
	private boolean enabled;
	private List<GrantedAuthority> authorities;
	
	public MyUserDetails(User user) {
		this.username = user.getYumUsername();
		this.password = user.getYumPassword();
		this.expired = user.isExpired();
		this.locked = user.isLocked();
		this.credentialsBad = user.isCredentialsBad();
		this.enabled = user.isEnabled();
		this.authorities = Arrays.asList(new SimpleGrantedAuthority(user.getRole().name()));
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	} 

	@Override
	public String getUsername() {
		return username;
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return !expired;
	}
	
	@Override
	public boolean isAccountNonLocked() {
		return !locked;
	}
	
	@Override
	public boolean isCredentialsNonExpired() {
		return !credentialsBad;
	}
	
	@Override
	public boolean isEnabled() {
		return enabled;
	}

}