package com.cognixia.jump.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.cognixia.jump.filter.JwtRequestFilter;

@Configuration
public class SecurityConfiguration {
	
	@Autowired
	UserDetailsService userDetailsService;
	
	@Autowired
	JwtRequestFilter jwtRequestFilter;
	
	@Bean
	protected UserDetailsService userDetailsService() {
		return userDetailsService;
	}
	
	private static final String[] AUTH_WHITE_LIST = {
			"/api/v1/auth/**",
			"/v2/api-docs",
            "/v3/api-docs",
			"/v2/api-docs/**",
			"/v3/api-docs/**",
			"/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/index.html",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html"
    };
	
	@SuppressWarnings({ "removal" })
	@Bean
	protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		http.csrf().disable()
        .authorizeHttpRequests()
        .requestMatchers(AUTH_WHITE_LIST).permitAll()
        .requestMatchers(HttpMethod.POST,"/api/admin/**").hasRole("ADMIN")
        .requestMatchers(HttpMethod.PATCH,"/api/admin/**").hasRole("ADMIN")
        .requestMatchers(HttpMethod.POST,"/api/register/**").permitAll()
        .requestMatchers(HttpMethod.PATCH,"/api/registration/**").permitAll()
        .requestMatchers(HttpMethod.GET,"/api/users/search/**").permitAll()
        .requestMatchers(HttpMethod.GET,"/api/recipes/favorites/users/**").permitAll()
        .requestMatchers(HttpMethod.GET,"/api/recipes/search/**").permitAll()
        .requestMatchers(HttpMethod.GET,"/api/users").permitAll()
        .requestMatchers(HttpMethod.GET,"/api/users/**").permitAll()
        .requestMatchers("/authenticate").permitAll()
        .anyRequest().authenticated()
        .and()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
		
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        
		
		return http.build();
	}
	
	@Bean
	protected PasswordEncoder encoder() {
		
		return new BCryptPasswordEncoder();
	}
	
    @Bean
    protected DaoAuthenticationProvider authenticationProvider() {
        
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder( encoder() );
        
        return authProvider;
    }
    
    @Bean
    protected AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}