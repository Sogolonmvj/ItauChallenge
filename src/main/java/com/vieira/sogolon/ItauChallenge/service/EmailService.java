package com.vieira.sogolon.ItauChallenge.service;

import com.vieira.sogolon.ItauChallenge.sender.EmailSender;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@AllArgsConstructor
public class EmailService implements EmailSender {

    private final static String EMAIL_CONFIRMATION_MESSAGE = "Confirm your email!";
    private final static String SENDER_EMAIL = "noreply@criticssystem.com";
    private final static String EMAIL_FAILURE_MESSAGE = "Failure while sending email";

    private final static Logger LOGGER = LoggerFactory
            .getLogger(EmailService.class);
    private final JavaMailSender mailSender;

    @Override
    @Async
    public void send(String to, String email) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(email, true);
            helper.setTo(to);
            helper.setSubject(EMAIL_CONFIRMATION_MESSAGE);
            helper.setFrom(SENDER_EMAIL);
            mailSender.send(mimeMessage);
        } catch (MessagingException exception) {
            LOGGER.error(EMAIL_FAILURE_MESSAGE, exception);
            throw new IllegalStateException(EMAIL_FAILURE_MESSAGE);
        }
    }
}
