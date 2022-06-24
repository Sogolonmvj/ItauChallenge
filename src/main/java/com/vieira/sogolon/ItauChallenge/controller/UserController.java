package com.vieira.sogolon.ItauChallenge.controller;

import com.vieira.sogolon.ItauChallenge.dto.UserDTO;
import com.vieira.sogolon.ItauChallenge.entities.UserCritic;
import com.vieira.sogolon.ItauChallenge.enums.UserRole;
import com.vieira.sogolon.ItauChallenge.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(path="api")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping(path="/users")
    public ResponseEntity<List<UserDTO>> fetchAllCritics() {
        List<UserDTO> criticDTOS = userService.getAllCritics();

        if (criticDTOS.isEmpty()) {
            return ResponseEntity.noContent().build(); // change to try/catch
        }
        return ResponseEntity.ok(criticDTOS);
    }

    @GetMapping(path="/user/{email}")
    public ResponseEntity<?> fetchCritic(
            @PathVariable("email") String email) { // change email to id
        Optional<UserDTO> criticDTO = userService.getUserCritic(email);

        if (criticDTO.isEmpty()) {
            return ResponseEntity.notFound().build(); // change to try/catch
        }
        return ResponseEntity.ok(criticDTO);
    }

    @PatchMapping(path="/moderator/user/{email}")
    public ResponseEntity<?> becomeModerator(
            @PathVariable("email") String email,
            @Validated @RequestBody UserRole userRole) { // change email to id
        Optional<UserCritic> criticUpdated = userService.becomeModerator(email, userRole);

        if (criticUpdated.isEmpty()) {
            return ResponseEntity.badRequest().build(); // change to try/catch
        }

        return ResponseEntity.ok(criticUpdated);
    }
}
