package com.vieira.sogolon.ItauChallenge.service;

import com.vieira.sogolon.ItauChallenge.entities.RegistrationRequest;
import com.vieira.sogolon.ItauChallenge.entities.UserCritic;
import com.vieira.sogolon.ItauChallenge.enums.UserRole;
import com.vieira.sogolon.ItauChallenge.security.token.ConfirmationToken;
import com.vieira.sogolon.ItauChallenge.security.token.service.ConfirmationTokenService;
import com.vieira.sogolon.ItauChallenge.sender.EmailSender;
import lombok.AllArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final UserService userService;
    private final EmailValidatorService emailValidatorService;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailSender emailSender;
    private final Environment env;
    private final EmailTemplateService emailTemplateService;

    private final static String TOKEN_ERROR_MESSAGE = "Token not found!";
    private final static String INVALID_EMAIL_MESSAGE = "Invalid email!";
    private final static String EMAIL_ALREADY_CONFIRMED_MESSAGE = "Email already confirmed!";
    private final static String EXPIRED_TOKEN_MESSAGE = "Token expired!";
    private final static String EMAIL_CONFIRMED_MESSAGE = "Email confirmed successfully!";

    public String register(RegistrationRequest request) {
        boolean isValidEmail = emailValidatorService.
                test(request.getEmail());
        if (!isValidEmail) {
            throw new IllegalStateException(INVALID_EMAIL_MESSAGE);
        }

        String token = userService.signUpUser(
                new UserCritic(
                        request.getFirstName(),
                        request.getLastName(),
                        request.getEmail(),
                        request.getPassword(),
                        UserRole.READER
                )
        );

        String link = env.getProperty("address.token-url") + token;
        emailSender.send(
                request.getEmail(),
                buildEmail(request.getFirstName(), link));

        return token;
    }

    @Transactional
    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(() ->
                        new IllegalStateException(TOKEN_ERROR_MESSAGE));

        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException(EMAIL_ALREADY_CONFIRMED_MESSAGE);
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException(EXPIRED_TOKEN_MESSAGE);
        }

        confirmationTokenService.setConfirmedAt(token);
        userService.enableUser(
                confirmationToken.getUserCritic().getEmail()
        );

        return EMAIL_CONFIRMED_MESSAGE;
    }

    private String buildEmail(String name, String link) {
        return emailTemplateService.buildEmail(name, link);
    }

}
