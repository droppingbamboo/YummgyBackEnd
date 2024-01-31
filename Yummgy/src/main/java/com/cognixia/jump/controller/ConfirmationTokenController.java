package com.cognixia.jump.controller;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.cognixia.jump.model.ConfirmationToken;
import com.cognixia.jump.repository.ConfirmationTokenRepository;

@RestController
public class ConfirmationTokenController {
	
	@Autowired
	private ConfirmationTokenRepository confirmationTokenRepo;
	
	public void saveConfirmationToken(ConfirmationToken token) {
        confirmationTokenRepo.save(token);
    }

    public Optional<ConfirmationToken> getToken(String token) {
        return confirmationTokenRepo.findByToken(token);
    }

    public int setConfirmedAt(String token) {
        return confirmationTokenRepo.updateConfirmedAt(
                token, LocalDateTime.now());
    }
}
