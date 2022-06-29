package com.vieira.sogolon.ItauChallenge.service;

import com.vieira.sogolon.ItauChallenge.dto.MoviesDTO;
import com.vieira.sogolon.ItauChallenge.entities.Comment.Comment;
import com.vieira.sogolon.ItauChallenge.entities.Comment.CommentResponse;
import com.vieira.sogolon.ItauChallenge.entities.Movies;
import com.vieira.sogolon.ItauChallenge.repository.CommentsRepository;
import com.vieira.sogolon.ItauChallenge.repository.MoviesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MoviesService {

    private final MoviesRepository moviesRepository;
    private final CommentsRepository commentsRepository;
    private final static Double notRated = 0.0;
    private final static Integer notVoted = 0;
    private final static Integer noReactions = 0;
    private static List<Comment> noComments = new ArrayList<>();
    private static List<CommentResponse> noResponses = new ArrayList<>();
//
//    public MoviesDTO getMovie(Movies response) {
//        moviesRepository.save(response);
//        Optional<Movies> movie = moviesRepository.findByTitle(response.getTitle());
//
//    }

    public MoviesDTO getValuesFromExternalAPI(Movies response) {

        MoviesDTO moviesDTO = new MoviesDTO();
        moviesDTO.setImdbID(response.getImdbID());
        moviesDTO.setTitle(response.getTitle());
        moviesDTO.setYear(response.getYear());
        moviesDTO.setGenre(response.getGenre());
        moviesDTO.setImdbRating(response.getImdbRating());
        moviesDTO.setImdbVotes(response.getImdbVotes());
        moviesDTO.setRuntime(response.getRuntime());

        saveMovieInDatabase(response);

        moviesDTO.setRating(response.getRating());
        moviesDTO.setComments(response.getComments());

        return moviesDTO;
    }

    public void saveMovieInDatabase(Movies movie) {
        movie.setComments(noComments);
        movie.setRating(notRated);
        movie.setVotes(notVoted);

        moviesRepository.save(movie);
    }

    public Movies findMovieInDatabase(Long id) {
        Optional<Movies> movie = moviesRepository.findById(id);

        return movie.orElse(null);

    }

    public Movies rateMovie(Long id, Double rating) {
        Movies movie = findMovieInDatabase(id);

        double newRating = calculateRating(movie, rating);

        movie.setRating(newRating);
        movie.setVotes(movie.getVotes()+1);

        moviesRepository.save(movie);

        return movie;
    }

    public double calculateRating(Movies movie, Double rating) {
        double ratingSum = movie.getRating() * movie.getVotes();
        ratingSum += rating;

        return ratingSum/(movie.getVotes()+1);
    }

    public void initiateComment(Comment comment) {
        comment.setResponses(noResponses);
        comment.setLikes(noReactions);
        comment.setDislikes(noReactions);
    }

    public Movies commentMovie(Long id, Comment comment) {
        Movies movie = findMovieInDatabase(id);

        initiateComment(comment);

        saveComment(comment);

        movie.getComments().add(comment);

        moviesRepository.save(movie);

        return movie;
    }

    public void saveComment(Comment comment) {
        commentsRepository.save(comment);
    }

}
