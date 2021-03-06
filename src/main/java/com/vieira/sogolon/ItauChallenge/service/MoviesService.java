package com.vieira.sogolon.ItauChallenge.service;

import com.vieira.sogolon.ItauChallenge.dto.MoviesDTO;
import com.vieira.sogolon.ItauChallenge.entities.Comment.Comment;
import com.vieira.sogolon.ItauChallenge.entities.Comment.CommentResponse;
import com.vieira.sogolon.ItauChallenge.entities.Movies;
import com.vieira.sogolon.ItauChallenge.repository.CommentsRepository;
import com.vieira.sogolon.ItauChallenge.repository.MoviesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final static List<Comment> noComments = new ArrayList<>();
    private final static List<CommentResponse> noResponses = new ArrayList<>();
    private final static Boolean notRepeated = false;

    public Optional<Movies> checkInDatabase(String title) {

        String formattedString = title.substring(0, 1).toUpperCase() + title.substring(1);

        return moviesRepository.findByTitle(formattedString);
    }

    public MoviesDTO getValuesFromDatabase(Movies movieInDatabase) {
        return getMovieDTO(movieInDatabase);
    }

    public MoviesDTO getMovieDTO(Movies movie)  {

        MoviesDTO moviesDTO = new MoviesDTO();
        moviesDTO.setId(movie.getId());
        moviesDTO.setImdbID(movie.getImdbID());
        moviesDTO.setTitle(movie.getTitle());
        moviesDTO.setYear(movie.getYear());
        moviesDTO.setGenre(movie.getGenre());
        moviesDTO.setImdbRating(movie.getImdbRating());
        moviesDTO.setImdbVotes(movie.getImdbVotes());
        moviesDTO.setRuntime(movie.getRuntime());
        moviesDTO.setRating(movie.getRating());
        moviesDTO.setComments(movie.getComments());

        return moviesDTO;
    }

    public MoviesDTO getValuesFromExternalAPI(Movies response) {

        saveMovieInDatabase(response);

        return getMovieDTO(response);
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

    public List<Movies> findAllMoviesInDatabase() {
        return moviesRepository.findAll();
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
        comment.setRepeated(notRepeated);
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

    @Transactional
    public Movies deleteComment(Long movieId, Long commentId) {
        commentsRepository.deleteById(commentId);
        return moviesRepository.findById(movieId).orElse(null);
    }

}
