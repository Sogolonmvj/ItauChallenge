package com.vieira.sogolon.ItauChallenge.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.vieira.sogolon.ItauChallenge.dto.UserDTO;
import com.vieira.sogolon.ItauChallenge.entities.UserCritic;
import com.vieira.sogolon.ItauChallenge.enums.UserRole;
import com.vieira.sogolon.ItauChallenge.repository.UserRepository;
import com.vieira.sogolon.ItauChallenge.security.token.ConfirmationToken;
import com.vieira.sogolon.ItauChallenge.security.token.service.ConfirmationTokenService;
import com.vieira.sogolon.ItauChallenge.sender.EmailSender;
import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class UserService implements UserDetailsService {

    private final static String USER_NOT_FOUND_MESSAGE = "User not found in the database!";
    private final static String USER_FOUND_MESSAGE = "User found in the database: {}";
    private final static String NOT_ACTIVATED_MESSAGE = "Email not activated! Please, activate your email to keep using our services!";
    private final static String USER_REGISTERED_MESSAGE = "User already registered!";
    private final static String MISSING_TOKEN_MESSAGE = "Token is missing!";
    private final static String INVALID_TOKEN_MESSAGE = "Token not valid!";
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailSender emailSender;
    private final Environment env;
    private final EmailTemplateService emailTemplateService;
    private final static String TOKEN_STARTER = "Bearer ";
    private final static int BASIC_POINTS = 20;
    private final static int ADVANCED_POINTS = 100;
    private final static int MODERATOR_POINTS = 1000;

    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        Optional<UserCritic> critic = userRepository.findByEmail(email);

        if (critic.isPresent()) {
            log.info(USER_FOUND_MESSAGE, email);
            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(critic.get().getUserRole().toString()));
            return new User(critic.get().getUsername(), critic.get().getPassword(), authorities);
        }

        log.error(USER_NOT_FOUND_MESSAGE);
        throw new UsernameNotFoundException(USER_NOT_FOUND_MESSAGE);
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

                throw new IllegalStateException(NOT_ACTIVATED_MESSAGE);

            }

            throw new IllegalStateException(USER_REGISTERED_MESSAGE);

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

    public UserCritic findUserCritic(String email) {
        Optional<UserCritic> critic = userRepository.findByEmail(email);

        return critic.orElse(null);
    }

    public UserCritic getUserByToken(String token) {
        if (token != null && token.startsWith(TOKEN_STARTER)) {
            try {
                String tokenWithoutBearer = token.substring(TOKEN_STARTER.length());
                String secret = env.getProperty("key.secret");
                Algorithm algorithm = Algorithm.HMAC256(secret.getBytes(StandardCharsets.UTF_8));
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(tokenWithoutBearer);
                String username = decodedJWT.getSubject();
                return findUserCritic(username);
            } catch (Exception exception) {
                log.info(INVALID_TOKEN_MESSAGE, exception);
                throw new UsernameNotFoundException(USER_NOT_FOUND_MESSAGE);
            }
        } else {
            throw new RuntimeException(MISSING_TOKEN_MESSAGE);
        }
    }

    public void getPoint(String token) {
        UserCritic critic = getUserByToken(token);
        critic.setPoints(critic.getPoints() + 1);
        userRepository.save(checkPoints(critic));
    }

    public UserCritic checkPoints(UserCritic critic) {
        if (critic.getPoints() >= BASIC_POINTS && critic.getPoints() < ADVANCED_POINTS) {
            critic.setUserRole(UserRole.BASIC);
        }

        if (critic.getPoints() >= ADVANCED_POINTS && critic.getPoints() < MODERATOR_POINTS) {
            critic.setUserRole(UserRole.ADVANCED);
        }

        if (critic.getPoints() >= MODERATOR_POINTS) {
            critic.setUserRole(UserRole.MODERATOR);
        }
        return critic;
    }

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
