package com.vieira.sogolon.ItauChallenge.service;

import com.vieira.sogolon.ItauChallenge.dto.UserDTO;
import com.vieira.sogolon.ItauChallenge.entities.UserCritic;
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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final static String USER_NOT_FOUND_MESSAGE = "Usuário %s não encontrado.";
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailSender emailSender;
    private final Environment env;

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

//            if (critic.getEnabled()) {
//                criticDTO.set
//            }

        }

        return null;
    }

    private String buildEmail(String name, String link) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#DFFF00;text-decoration:none;vertical-align:top;display:inline-block\">Confirme seu email!</span>\n" +
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
                "                    <td bgcolor=\"#DFFF00\" width=\"100%\" height=\"10\"></td>\n" +
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
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#008080\">Olá " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#008080\"> Obrigado(a) por se registrar. Por favor, ative sua conta clicando no link abaixo: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#008080\"> <a style=\"color:#008000\" href=\"" + link + "\">Confirme agora.</a> </p></blockquote>\n  <p style=\"color:#008080\">Link expira em 10 minutos.</p> <p style=\"color:#008080\">Até logo!</p>" +
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
