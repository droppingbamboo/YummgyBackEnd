package com.cognixia.jump.filter;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.cognixia.jump.service.MyUserDetailsService;
import com.cognixia.jump.util.JwtUtil;

// filters in spring are used to filter through requests/responses
// perform some check for security before a request is completed or a response is sent

// this filter will intercept every request coming in and examine the header for tokens

// will label this as a component so spring can recognize it and be able to autowire some of its properties
// and call it to filter requests before sending any responses
@Component
public class JwtRequestFilter extends OncePerRequestFilter { // abstract class that makes sure an action performed once when filter is called

	@Autowired
	private MyUserDetailsService userDetailsService;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		final String authorizationHeader = request.getHeader("Authorization");
		
		String jwt = null;
		String username = null;
		

		if( authorizationHeader != null && authorizationHeader.startsWith("Bearer ") ) {
			
			jwt = authorizationHeader.substring(7);
			
			username = jwtUtil.extractUsername(jwt);
			
		}
		
		if( username != null && SecurityContextHolder.getContext().getAuthentication() == null ) {
			
			
			UserDetails userDetails = userDetailsService.loadUserByUsername(username);
			
			if( jwtUtil.validateToken(jwt, userDetails) ) {
				
				
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
						new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
				
				usernamePasswordAuthenticationToken.setDetails(
						new WebAuthenticationDetailsSource().buildDetails(request) );
				
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
						
		}
		
		filterChain.doFilter(request, response);
	}

}