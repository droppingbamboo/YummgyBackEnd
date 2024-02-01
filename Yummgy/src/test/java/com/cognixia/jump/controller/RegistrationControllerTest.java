package com.cognixia.jump.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.http.MediaType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.cognixia.jump.model.ConfirmationToken;
import com.cognixia.jump.model.RegistrationRequest;
import com.cognixia.jump.model.User;
import com.cognixia.jump.repository.UserRepository;
import com.cognixia.jump.service.EmailService;
import com.cognixia.jump.service.EmailValidation;
import com.cognixia.jump.service.MyUserDetailsService;
import com.cognixia.jump.util.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(RegistrationController.class)
public class RegistrationControllerTest {

	private static final String STARTING_URI = "http://localhost:8080/api";

	@Autowired
	private MockMvc mvc;

	@MockBean
	private UserRepository userRepo;

	@MockBean
	private UserController userController;

	@MockBean
	private ConfirmationTokenController confirmationTokenController;

	@MockBean
	private EmailValidation emailValidator;

	@MockBean
	private EmailService emailSender;

	@MockBean
	private MyUserDetailsService userDetailsService;

	@MockBean
	private JwtUtil jwtUtil;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@BeforeEach
	public void setup(TestInfo testInfo) {
		// Print the name of the test being run
		System.out.println("-------------RUNNING TEST--------- : " + testInfo.getTestMethod().get().getName());
		// Init MockMvc Object and build
		mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	@Test
	public void testRegisterUser_Success() throws Exception {
		String uri = STARTING_URI + "/register/user";
		// Mock data
		RegistrationRequest registrationRequest = new RegistrationRequest("testUser", "testPassword",
				"test@example.com");

		// Mock email validation result
		when(emailValidator.testEmail(anyString())).thenReturn(true);

		// Mock userController response
		when(userController.addUser(any(User.class))).thenReturn("token123");

		// Perform the POST request
		mvc.perform(post(uri).contentType(MediaType.APPLICATION_JSON).content(asJsonString(registrationRequest)))
				.andExpect(status().isOk()).andExpect(content().string("token123"));

		// Verify interactions
		verify(emailValidator, times(1)).testEmail("test@example.com");
		verify(userController, times(1)).addUser(any(User.class));
		verify(emailSender, times(1)).send(anyString(), anyString());
		verifyNoMoreInteractions(emailValidator, userController, emailSender);
	}

	@Test
	public void testRegisterUser_InvalidEmail() throws Exception {
		String uri = STARTING_URI + "/register/user";
		// Mock data with invalid email
		RegistrationRequest registrationRequest = new RegistrationRequest("testUser", "testPassword", "invalidemail");

		// Mock email validation result
		when(emailValidator.testEmail(anyString())).thenReturn(false);

		// Perform the POST request
		mvc.perform(post(uri).contentType(MediaType.APPLICATION_JSON).content(asJsonString(registrationRequest)))
				.andExpect(status().isBadRequest());

		// Verify interactions
		verify(emailValidator, times(1)).testEmail("invalidemail");
		verifyNoMoreInteractions(emailValidator, userController, emailSender);
	}

	@Test
	public void testConfirmToken_Success() throws Exception {
		String uri = STARTING_URI + "/registration/confirm";
		// Mock data
		String token = "token123";
		ConfirmationToken confirmationToken = new ConfirmationToken();
		confirmationToken.setToken(token);
		User user = new User();
		user.setEmail("test@example.com");
		confirmationToken.setUser(user);
		LocalDateTime expirationTime = LocalDateTime.now().plusHours(1);
		confirmationToken.setExpiresAt(expirationTime);

		// Mock confirmationTokenController response
		when(confirmationTokenController.getToken(token)).thenReturn(Optional.of(confirmationToken));

		// Perform the PATCH request
		mvc.perform(patch(uri).param("token", token)).andExpect(status().isOk());

		// Verify interactions
		verify(confirmationTokenController, times(1)).getToken(token);
		verify(confirmationTokenController, times(1)).setConfirmedAt(token);
		verify(userController, times(1)).setEnabled("test@example.com");
		verifyNoMoreInteractions(confirmationTokenController, userController);
	}

	@Test
	public void testConfirmToken_TokenNotFound() throws Exception {
		String uri = STARTING_URI + "/registration/confirm";
		// Mock data
		String token = "nonexistentToken";

		// Mock confirmationTokenController response
		when(confirmationTokenController.getToken(token)).thenReturn(Optional.empty());

		// Perform the PATCH request
		mvc.perform(patch(uri).param("token", token)).andExpect(status().isNotFound());

		// Verify interactions
		verify(confirmationTokenController, times(1)).getToken(token);
		verifyNoMoreInteractions(confirmationTokenController, userController);
	}

	public static String asJsonString(final Object obj) {

		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}

	}
}
