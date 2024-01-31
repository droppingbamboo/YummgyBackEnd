package com.cognixia.jump.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cognixia.jump.exception.AlreadyInUseException;
import com.cognixia.jump.exception.InvalidResourceFormatException;
import com.cognixia.jump.exception.ResourceNotFoundException;
import com.cognixia.jump.exception.TokenExpiredException;
import com.cognixia.jump.model.ConfirmationToken;
import com.cognixia.jump.model.RegistrationRequest;
import com.cognixia.jump.model.User;
import com.cognixia.jump.repository.UserRepository;
import com.cognixia.jump.service.EmailService;
import com.cognixia.jump.service.EmailValidation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RequestMapping("/api")
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
	
	
	
	@Operation(summary = "Registers a user and sends an email out to the user to confirm registration.",
			description = "Adds a user to the user table in the database based off a username and password."
					+ "The password is encrypted before it is stored to ensure that passwords are secure."
					+ " The user is stored in the database with enabled set to false and the USER role."
					+ " Once they verify by clicking on the email link.")
	@ApiResponses( value = {
			@ApiResponse(responseCode="201",
			description="User has been created"),
			@ApiResponse(responseCode="400",
			description="User data or email not formatted properly, or user already enabled")}
	)
	@CrossOrigin
	@PostMapping("/register/user")
	public ResponseEntity<?> register(@RequestBody RegistrationRequest request) throws AlreadyInUseException, InvalidResourceFormatException {
        boolean isValidEmail = emailValidator.
                testEmail(request.getEmail());

        if (!isValidEmail) {
            throw new InvalidResourceFormatException("Email", request.getEmail());
        }
        
        

        String token = userController.addUser(
                	new User(
                		request.getYumUsername(),
                		request.getYumPassword(),
                		User.Role.ROLE_USER,
                        request.getEmail()
                )
        );

        String link = "http://localhost:3000/verification?token=" + token;
        emailSender.send(
                request.getEmail(),
                buildEmail(request.getYumUsername(), link));

        return ResponseEntity.status(201).body(token);
    }
	
	
	@Operation(summary = "Sets a user to enabled in the database based off the provided token.",
			description = "Updates a user's enabled value to true in the database, this is based off the provided"
					+ " token and if its matches a token thjat exists within the database.")
	@ApiResponses( value = {
			@ApiResponse(responseCode="200",
			description="User has been enabled"),
			@ApiResponse(responseCode="400",
			description="Token is already in use or token is expired"),
			@ApiResponse(responseCode="404",
			description="Token specified was not found")}
	)
	@CrossOrigin
	@Transactional
	@PatchMapping("/registration/confirm")
    public ResponseEntity<?> confirmToken(@RequestParam String token) throws ResourceNotFoundException, AlreadyInUseException, TokenExpiredException {
        ConfirmationToken confirmationToken = confirmationTokenController.getToken(token)
                .orElseThrow(() ->
                        new ResourceNotFoundException("token"));

        if (confirmationToken.getConfirmedAt() != null) {
            throw new AlreadyInUseException("Token", confirmationToken.getToken(), "Token already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException(expiredAt.toString());
        }

        confirmationTokenController.setConfirmedAt(token);
        userController.setEnabled(confirmationToken.getUser().getEmail());
        return ResponseEntity.status(200).body("User has been enabled");
    }
	
	
	private String buildEmail(String name, String link) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#dae1b6\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#1c1b1a;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#e7b790\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#e7b790\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#1c1b1a;text-decoration:none;vertical-align:top;display:inline-block\">Confirm your email with Yummgy</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#abceab\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Thank you for registering. Please click on the below link to activate your account: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Activate Now</a> </p></blockquote>\n Link will expire in 15 minutes. <p>See you soon</p>" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";
    }

}
