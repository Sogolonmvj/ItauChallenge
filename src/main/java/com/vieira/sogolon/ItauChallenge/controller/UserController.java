package com.vieira.sogolon.ItauChallenge.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.vieira.sogolon.ItauChallenge.dto.UserDTO;
import com.vieira.sogolon.ItauChallenge.entities.Comment.Comment;
import com.vieira.sogolon.ItauChallenge.entities.Comment.CommentResponse;
import com.vieira.sogolon.ItauChallenge.entities.Comment.CommentTag;
import com.vieira.sogolon.ItauChallenge.entities.Movies;
import com.vieira.sogolon.ItauChallenge.enums.UserRole;
import com.vieira.sogolon.ItauChallenge.parser.Json;
import com.vieira.sogolon.ItauChallenge.service.CommentService;
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
    private final CommentService commentService;

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

    @PostMapping("/critic/rate") // rate a movie
    public ResponseEntity<?> rateMovie(@RequestParam(value = "id", defaultValue = "") Long id,
                                       @RequestParam(value = "rating", defaultValue = "") Double rating) {
        Movies movie = moviesService.rateMovie(id, rating);
//        , @RequestParam(value = "token", defaultValue = "") UUID token
//        Optional<UserDTO> critic = userService.getUserCritic(token.toString());
//        critic.ifPresent(user -> user.setPoints(user.getPoints()+1));
        return ResponseEntity.ok().body(movie);
    }

    @PostMapping("/critic/comment") // comment a movie
    public ResponseEntity<?> commentMovie(@RequestParam(value = "id", defaultValue = "") Long id,
                                       @RequestBody Comment comment) {
        Movies movie = moviesService.commentMovie(id, comment);
//        , @RequestParam(value = "token", defaultValue = "") UUID token
//        Optional<UserDTO> critic = userService.getUserCritic(token.toString());
//        critic.ifPresent(user -> user.setPoints(user.getPoints()+1));
        return ResponseEntity.ok().body(movie);
    }

    @PostMapping("/critic/comment/response") // comment a movie
    public ResponseEntity<?> commentResponse(@RequestParam(value = "id", defaultValue = "") Long id,
                                          @RequestBody CommentResponse commentResponse) {
        Comment comment = commentService.commentResponse(id, commentResponse);
//        , @RequestParam(value = "token", defaultValue = "") UUID token
//        Optional<UserDTO> critic = userService.getUserCritic(token.toString());
//        critic.ifPresent(user -> user.setPoints(user.getPoints()+1));
        return ResponseEntity.ok().body(comment);
    }

    @PostMapping("/critic/comment/tag") // comment a movie
    public ResponseEntity<?> tagComment(@RequestParam(value = "id", defaultValue = "") Long id,
                                             @RequestBody CommentTag commentTag) {
        CommentTag newComment = commentService.tagComment(id, commentTag);
//        , @RequestParam(value = "token", defaultValue = "") UUID token
//        Optional<UserDTO> critic = userService.getUserCritic(token.toString());
//        critic.ifPresent(user -> user.setPoints(user.getPoints()+1));
        return ResponseEntity.ok().body(newComment);
    }

    @PostMapping("/critic/comment/like") // rate a movie
    public ResponseEntity<?> likeComment(@RequestParam(value = "id", defaultValue = "") Long id) {
        Comment comment = commentService.likeComment(id);
//        Movies movie = moviesService.rateMovie(id, rating);
//        , @RequestParam(value = "token", defaultValue = "") UUID token
//        Optional<UserDTO> critic = userService.getUserCritic(token.toString());
//        critic.ifPresent(user -> user.setPoints(user.getPoints()+1));
        return ResponseEntity.ok().body(comment);
    }

    @PostMapping("/critic/comment/response/like") // rate a movie
    public ResponseEntity<?> likeCommentResponse(@RequestParam(value = "id", defaultValue = "") Long id) {
        CommentResponse commentResponse = commentService.likeCommentResponse(id);
//        Movies movie = moviesService.rateMovie(id, rating);
//        , @RequestParam(value = "token", defaultValue = "") UUID token
//        Optional<UserDTO> critic = userService.getUserCritic(token.toString());
//        critic.ifPresent(user -> user.setPoints(user.getPoints()+1));
        return ResponseEntity.ok().body(commentResponse);
    }

    @PostMapping("/critic/comment/tag/like") // rate a movie
    public ResponseEntity<?> likeCommentTag(@RequestParam(value = "id", defaultValue = "") Long id) {
        CommentTag commentTag = commentService.likeCommentTag(id);
//        Movies movie = moviesService.rateMovie(id, rating);
//        , @RequestParam(value = "token", defaultValue = "") UUID token
//        Optional<UserDTO> critic = userService.getUserCritic(token.toString());
//        critic.ifPresent(user -> user.setPoints(user.getPoints()+1));
        return ResponseEntity.ok().body(commentTag);
    }

    @PostMapping("/critic/comment/dislike") // rate a movie
    public ResponseEntity<?> dislikeComment(@RequestParam(value = "id", defaultValue = "") Long id) {
        Comment comment = commentService.dislikeComment(id);
//        Movies movie = moviesService.rateMovie(id, rating);
//        , @RequestParam(value = "token", defaultValue = "") UUID token
//        Optional<UserDTO> critic = userService.getUserCritic(token.toString());
//        critic.ifPresent(user -> user.setPoints(user.getPoints()+1));
        return ResponseEntity.ok().body(comment);
    }

    @PostMapping("/critic/comment/response/dislike") // rate a movie
    public ResponseEntity<?> dislikeCommentResponse(@RequestParam(value = "id", defaultValue = "") Long id) {
        CommentResponse commentResponse = commentService.dislikeCommentResponse(id);
//        Movies movie = moviesService.rateMovie(id, rating);
//        , @RequestParam(value = "token", defaultValue = "") UUID token
//        Optional<UserDTO> critic = userService.getUserCritic(token.toString());
//        critic.ifPresent(user -> user.setPoints(user.getPoints()+1));
        return ResponseEntity.ok().body(commentResponse);
    }

    @PostMapping("/critic/comment/tag/dislike") // rate a movie
    public ResponseEntity<?> dislikeCommentTag(@RequestParam(value = "id", defaultValue = "") Long id) {
        CommentTag commentTag = commentService.dislikeCommentTag(id);
//        Movies movie = moviesService.rateMovie(id, rating);
//        , @RequestParam(value = "token", defaultValue = "") UUID token
//        Optional<UserDTO> critic = userService.getUserCritic(token.toString());
//        critic.ifPresent(user -> user.setPoints(user.getPoints()+1));
        return ResponseEntity.ok().body(commentTag);
    }

    @DeleteMapping("/moderator/comment") // comment a movie
    public ResponseEntity<?> deleteComment(@RequestParam(value = "id", defaultValue = "") Long movieId, @RequestParam(value = "comment_id", defaultValue = "") Long commentId) {
        Movies movie = moviesService.deleteComment(movieId, commentId);
//        , @RequestParam(value = "token", defaultValue = "") UUID token
//        Optional<UserDTO> critic = userService.getUserCritic(token.toString());
//        critic.ifPresent(user -> user.setPoints(user.getPoints()+1));
        return ResponseEntity.ok().body(movie);
    }

    @DeleteMapping("/moderator/comment/response") // comment a movie
    public ResponseEntity<?> deleteCommentResponse(@RequestParam(value = "id", defaultValue = "") Long commentId, @RequestParam(value = "comment_response_id", defaultValue = "") Long commentResponseId) {
        Comment comment = commentService.deleteCommentResponse(commentId, commentResponseId);
//        , @RequestParam(value = "token", defaultValue = "") UUID token
//        Optional<UserDTO> critic = userService.getUserCritic(token.toString());
//        critic.ifPresent(user -> user.setPoints(user.getPoints()+1));
        return ResponseEntity.ok().body(comment);
    }

    @DeleteMapping("/moderator/comment/tag") // comment a movie
    public ResponseEntity<?> deleteCommentTag(@RequestParam(value = "comment_tag_id", defaultValue = "") Long commentTagId) {
        commentService.deleteCommentTag(commentTagId);
//        , @RequestParam(value = "token", defaultValue = "") UUID token
//        Optional<UserDTO> critic = userService.getUserCritic(token.toString());
//        critic.ifPresent(user -> user.setPoints(user.getPoints()+1));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/moderator/comment/repeated")
    public ResponseEntity<?> markCommentAsRepeated(@RequestParam(value = "repeated_comment_id", defaultValue = "") Long repeatedCommentId) {
        Comment comment = commentService.markCommentAsRepeated(repeatedCommentId);
        return ResponseEntity.ok().body(comment);
    }

    @PostMapping("/moderator/comment/response/repeated")
    public ResponseEntity<?> markCommentResponseAsRepeated(@RequestParam(value = "repeated_comment_response_id", defaultValue = "") Long repeatedCommentResponseId) {
        CommentResponse commentResponse = commentService.markCommentResponseAsRepeated(repeatedCommentResponseId);
        return ResponseEntity.ok().body(commentResponse);
    }

    @PostMapping("/moderator/comment/tag/repeated")
    public ResponseEntity<?> markCommentTagAsRepeated(@RequestParam(value = "repeated_comment_tag_id", defaultValue = "") Long repeatedCommentTagId) {
        CommentTag commentTag = commentService.markCommentTagAsRepeated(repeatedCommentTagId);
        return ResponseEntity.ok().body(commentTag);
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
