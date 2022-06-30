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
import com.vieira.sogolon.ItauChallenge.service.TokenService;
import com.vieira.sogolon.ItauChallenge.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path="api")
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final MoviesService moviesService;
    private final CommentService commentService;
    private final TokenService tokenService;

    @GetMapping(path="/users")
    public ResponseEntity<List<UserDTO>> fetchAllCritics() {
        List<UserDTO> criticDTOS = userService.getAllCritics();

        if (criticDTOS.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(criticDTOS);
    }

    @GetMapping(path="/user/{email}")
    public ResponseEntity<?> fetchCritic(
            @PathVariable("email") String email) {
        Optional<UserDTO> criticDTO = userService.getUserCritic(email);

        if (criticDTO.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(criticDTO);
    }

    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) {
        tokenService.getRefreshToken(request, response);
    }

    @PatchMapping(path="/moderator/user/{email}")
    public ResponseEntity<?> becomeModerator(
            @PathVariable("email") String email,
            @Validated @RequestBody String userRole) {

        try {
            JsonNode node = Json.parse(userRole);

            if (node.get("userRole").asText().equalsIgnoreCase(UserRole.MODERATOR.name())) {
                Optional<UserDTO> criticUpdated = userService.becomeModerator(email, UserRole.MODERATOR);

                if (criticUpdated.isEmpty()) {
                    return ResponseEntity.badRequest().build();
                }

                return ResponseEntity.ok(criticUpdated);
            }

        } catch (JsonProcessingException exception) {
            exception.printStackTrace();
        }

        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/critic/rate")
    public ResponseEntity<?> rateMovie(
            @RequestParam(value = "id", defaultValue = "") Long id,
            @RequestParam(value = "rating", defaultValue = "") Double rating, @RequestHeader(value = "Authorization") String token) {
        Movies movie = moviesService.rateMovie(id, rating);
        userService.getPoint(token);
        return ResponseEntity.ok().body(movie);
    }

    @PostMapping("/critic/comment")
    public ResponseEntity<?> commentMovie(
            @RequestParam(value = "id", defaultValue = "") Long id,
            @RequestBody Comment comment, @RequestHeader(value = "Authorization") String token) {
        Movies movie = moviesService.commentMovie(id, comment);
        userService.getPoint(token);
        return ResponseEntity.ok().body(movie);
    }

    @PostMapping("/critic/comment/response")
    public ResponseEntity<?> commentResponse(
            @RequestParam(value = "id", defaultValue = "") Long id,
            @RequestBody CommentResponse commentResponse, @RequestHeader(value = "Authorization") String token) {
        Comment comment = commentService.commentResponse(id, commentResponse);
        userService.getPoint(token);
        return ResponseEntity.ok().body(comment);
    }

    @PostMapping("/critic/comment/tag")
    public ResponseEntity<?> tagComment(
            @RequestParam(value = "id", defaultValue = "") Long id,
            @RequestBody CommentTag commentTag, @RequestHeader(value = "Authorization") String token) {
        CommentTag newComment = commentService.tagComment(id, commentTag);
        userService.getPoint(token);
        return ResponseEntity.ok().body(newComment);
    }

    @PostMapping("/critic/comment/like")
    public ResponseEntity<?> likeComment(
            @RequestParam(value = "id", defaultValue = "") Long id) {
        Comment comment = commentService.likeComment(id);
        return ResponseEntity.ok().body(comment);
    }

    @PostMapping("/critic/comment/response/like")
    public ResponseEntity<?> likeCommentResponse(
            @RequestParam(value = "id", defaultValue = "") Long id) {
        CommentResponse commentResponse = commentService.likeCommentResponse(id);
        return ResponseEntity.ok().body(commentResponse);
    }

    @PostMapping("/critic/comment/dislike")
    public ResponseEntity<?> dislikeComment(
            @RequestParam(value = "id", defaultValue = "") Long id) {
        Comment comment = commentService.dislikeComment(id);
        return ResponseEntity.ok().body(comment);
    }

    @PostMapping("/critic/comment/response/dislike")
    public ResponseEntity<?> dislikeCommentResponse(
            @RequestParam(value = "id", defaultValue = "") Long id) {
        CommentResponse commentResponse = commentService.dislikeCommentResponse(id);
        return ResponseEntity.ok().body(commentResponse);
    }

    @DeleteMapping("/moderator/comment")
    public ResponseEntity<?> deleteComment(
            @RequestParam(value = "id", defaultValue = "") Long movieId,
            @RequestParam(value = "comment_id", defaultValue = "") Long commentId) {
        Movies movie = moviesService.deleteComment(movieId, commentId);
        return ResponseEntity.ok().body(movie);
    }

    @DeleteMapping("/moderator/comment/response")
    public ResponseEntity<?> deleteCommentResponse(
            @RequestParam(value = "id", defaultValue = "") Long commentId,
            @RequestParam(value = "comment_response_id", defaultValue = "") Long commentResponseId) {
        Comment comment = commentService.deleteCommentResponse(commentId, commentResponseId);
        return ResponseEntity.ok().body(comment);
    }

    @DeleteMapping("/moderator/comment/tag")
    public ResponseEntity<?> deleteCommentTag(
            @RequestParam(value = "comment_tag_id", defaultValue = "") Long commentTagId) {
        commentService.deleteCommentTag(commentTagId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/moderator/comment/repeated")
    public ResponseEntity<?> markCommentAsRepeated(
            @RequestParam(value = "repeated_comment_id", defaultValue = "") Long repeatedCommentId) {
        Comment comment = commentService.markCommentAsRepeated(repeatedCommentId);
        return ResponseEntity.ok().body(comment);
    }

    @PostMapping("/moderator/comment/response/repeated")
    public ResponseEntity<?> markCommentResponseAsRepeated(
            @RequestParam(value = "repeated_comment_response_id", defaultValue = "") Long repeatedCommentResponseId) {
        CommentResponse commentResponse = commentService.markCommentResponseAsRepeated(repeatedCommentResponseId);
        return ResponseEntity.ok().body(commentResponse);
    }


}
