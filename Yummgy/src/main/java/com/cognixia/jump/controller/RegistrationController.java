package com.cognixia.jump.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cognixia.jump.repository.UserRepository;
import com.cognixia.jump.service.EmailService;
import com.cognixia.jump.service.EmailValidation;

@RequestMapping("/register")
@RestController
public class RegistrationController {
	
	@Autowired
	private UserController userController;
	
	@Autowired
	private EmailValidation emailValidator;
	
	@Autowired
	private ConfirmationTokenController confirmationTokenController;
	
	@Autowired
	private EmailService emailSender;
	
	@PostMapping
	public ResponseEntity<?> testEmail() {
		emailSender.send("hello@gmail.com", "email");
		return ResponseEntity.status(201).body("email sent");
	}
}
