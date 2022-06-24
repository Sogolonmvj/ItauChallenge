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

    public String register(RegistrationRequest request) {
        boolean isValidEmail = emailValidatorService.
                test(request.getEmail());
        if (!isValidEmail) {
            throw new IllegalStateException("Email inválido!");
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
                        new IllegalStateException("Token não encontrado!"));

        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("Email já confirmado!");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Token expirado!");
        }

        confirmationTokenService.setConfirmedAt(token);
        userService.enableUser(
                confirmationToken.getUserCritic().getEmail()
        );

        return "Email confirmado com sucesso!";
    }

    private String buildEmail(String name, String link) {
        return emailTemplateService.buildEmail(name, link);
    }

}
