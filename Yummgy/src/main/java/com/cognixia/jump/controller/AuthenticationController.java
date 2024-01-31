package com.cognixia.jump.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cognixia.jump.model.AuthenticationRequest;
import com.cognixia.jump.model.AuthenticationResponse;
import com.cognixia.jump.util.JwtUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Authentication", description = "Handles authentication, and only authentication.")
public class AuthenticationController {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserDetailsService userDetailsService;

	@Autowired
	JwtUtil jwtUtil;
	
	
	@Operation(summary = "Authenticate a user",
			description = "Takes in a user's username and unencrypted password and "
					+ "checks if that user and their encrypted password is in the user database, and if so allows them security access"
					+ " by returning a valid bearer JWT.")
	@ApiResponses({
			@ApiResponse(responseCode="201",
			description="Authentication Approved"),
			@ApiResponse(responseCode="400",
			description="Incorrect username or password")
		}
	)
	@PostMapping("/authenticate")
	public ResponseEntity<?> createJwtToken(@RequestBody AuthenticationRequest request) throws Exception {
		
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

		} catch (BadCredentialsException e) {
			throw new BadCredentialsException("Incorrect username or password");
		} catch (DisabledException e) {
			throw new DisabledException("Account is disabled");
		} catch (LockedException e) {
			throw new LockedException("Account is locked");
		}


		final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());

		final String jwt = jwtUtil.generateTokens(userDetails);

		return ResponseEntity.status(201).body( new AuthenticationResponse(jwt) );

	}
	
}