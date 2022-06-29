package com.vieira.sogolon.ItauChallenge.service;

import com.vieira.sogolon.ItauChallenge.dto.UserDTO;
import com.vieira.sogolon.ItauChallenge.entities.UserCritic;
import com.vieira.sogolon.ItauChallenge.enums.UserRole;
import com.vieira.sogolon.ItauChallenge.repository.UserRepository;
import com.vieira.sogolon.ItauChallenge.security.token.ConfirmationToken;
import com.vieira.sogolon.ItauChallenge.security.token.service.ConfirmationTokenService;
import com.vieira.sogolon.ItauChallenge.sender.EmailSender;
import lombok.AllArgsConstructor;

import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final static String USER_NOT_FOUND_MESSAGE = "Usuário %s não encontrado.";
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailSender emailSender;
    private final Environment env;
    private final EmailTemplateService emailTemplateService;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException(
                        String.format(USER_NOT_FOUND_MESSAGE, email)));
    }

    public String signUpUser(UserCritic userCritic) {
        boolean userExists = userRepository.findByEmail(userCritic.getEmail())
                .isPresent();
        if (userExists) {

            if (!userCritic.getEnabled()) {

                String newToken = UUID.randomUUID().toString();

                String link = env.getProperty("address.token-url") + newToken;
                emailSender.send(
                        userCritic.getEmail(),
                        buildEmail(userCritic.getFirstName(), link));

                ConfirmationToken confirmationToken = new ConfirmationToken(
                        newToken,
                        LocalDateTime.now(),
                        LocalDateTime.now().plusMinutes(10),
                        userRepository.findByEmail(userCritic.getEmail()).get()
                );

                confirmationTokenService.saveConfirmationToken(
                        confirmationToken
                );

                throw new IllegalStateException(
                        "Email não ativado! " +
                                "Por favor, " +
                                "ative seu email para utilizar nossos serviços!"
                );

            }

            throw new IllegalStateException("Usuário já registrado!");

        }

        String encodedPassword = bCryptPasswordEncoder.encode(
                userCritic.getPassword()
        );

        userCritic.setPassword(encodedPassword);

        userRepository.save(userCritic);

        String token = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(10),
                userCritic
        );

        confirmationTokenService.saveConfirmationToken(confirmationToken);

        return token;
    }

    public int enableUser(String email) {
        return userRepository.enableUser(email);
    }

    public List<UserDTO> getAllCritics() {
        List<UserCritic> critics = userRepository.findAll();

        List<UserDTO> criticDTOS = new ArrayList<>();

        for (UserCritic critic: critics) {

            UserDTO criticDTO = new UserDTO();

            if (critic.getEnabled()) {
                criticDTO.setId(critic.getId());
                criticDTO.setFirstName(critic.getFirstName());
                criticDTO.setLastName(critic.getLastName());
                criticDTO.setEmail(critic.getEmail());
                criticDTO.setUserRole(critic.getUserRole());

                criticDTOS.add(criticDTO);
            }

        }
        return criticDTOS;
    }

    public Optional<UserDTO> getUserCritic(String email) {
        Optional<UserCritic> critic = userRepository.findByEmail(email);

        return getUserDTO(critic);
    }

//    public Optional<UserDTO> getUserByToken(UUID token) {
//        Optional<UserCritic> critic = userRepository.findByToken(token);
//
//    }

    public Optional<UserDTO> becomeModerator(String email, UserRole userRole) {
        Optional<UserCritic> critic = userRepository.findByEmail(email);

        if (critic.isPresent()) {
            critic.get().setUserRole(userRole);
            userRepository.save(critic.get());
        }

        return getUserDTO(critic);
    }

    private Optional<UserDTO> getUserDTO(Optional<UserCritic> critic) {
        Optional<UserDTO> criticDTO = Optional.of(new UserDTO());

        if (critic.isPresent() && critic.get().getEnabled()) {
            criticDTO.get().setId(critic.get().getId());
            criticDTO.get().setFirstName(critic.get().getFirstName());
            criticDTO.get().setLastName(critic.get().getLastName());
            criticDTO.get().setEmail(critic.get().getEmail());
            criticDTO.get().setUserRole(critic.get().getUserRole());
        }

        return criticDTO;
    }

    private String buildEmail(String name, String link) {
        return emailTemplateService.buildEmail(name, link);
    }

}
