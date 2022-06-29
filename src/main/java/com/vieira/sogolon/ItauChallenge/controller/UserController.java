package com.vieira.sogolon.ItauChallenge.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.vieira.sogolon.ItauChallenge.dto.UserDTO;
import com.vieira.sogolon.ItauChallenge.entities.Movies;
import com.vieira.sogolon.ItauChallenge.entities.Rate.Rate;
import com.vieira.sogolon.ItauChallenge.entities.UserRequest.MovieRequest;
import com.vieira.sogolon.ItauChallenge.enums.UserRole;
import com.vieira.sogolon.ItauChallenge.parser.Json;
import com.vieira.sogolon.ItauChallenge.service.MoviesService;
import com.vieira.sogolon.ItauChallenge.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path="api")
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final MoviesService moviesService;

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
            @Validated @RequestBody String userRole) { // change email to id

        try {
            JsonNode node = Json.parse(userRole);

            if (node.get("userRole").asText().equalsIgnoreCase(UserRole.MODERATOR.name())) {
                Optional<UserDTO> criticUpdated = userService.becomeModerator(email, UserRole.MODERATOR);

                if (criticUpdated.isEmpty()) {
                    return ResponseEntity.badRequest().build(); // change to try/catch
                }

                return ResponseEntity.ok(criticUpdated);
            }

        } catch (JsonProcessingException exception) {
            exception.printStackTrace();
        }

        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/critic/rate")
    public ResponseEntity<?> rateMovie(@RequestBody Rate rate) {
        Movies movie = moviesService.rateMovie(rate);
        return ResponseEntity.ok().body(movie);
    }

//
//    @PostMapping("/titles")
//    ResponseEntity<Movies> seeMovie(@RequestBody MovieRequest request){
//
//        return ResponseEntity.ok(movie);
//    }
//
//    @PostMapping(path="/critic") // reader
//    public ResponseEntity<?> postComment (@Validated @RequestBody String movieTitle, String comment) {
//
//
//        return userService.postComment(movieTitle, comment);
//    }


}
