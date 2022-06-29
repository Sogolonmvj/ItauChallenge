package com.vieira.sogolon.ItauChallenge.service;

import com.vieira.sogolon.ItauChallenge.dto.MoviesDTO;
import com.vieira.sogolon.ItauChallenge.entities.Movies;
import com.vieira.sogolon.ItauChallenge.entities.Rate.Rate;
import com.vieira.sogolon.ItauChallenge.repository.MoviesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MoviesService {

    private final MoviesRepository moviesRepository;
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

        moviesRepository.save(response);

        return moviesDTO;
    }

    public Movies rateMovie(Rate rate) {
        Optional<Movies> movie = moviesRepository.findByTitle(rate.getMovies().getTitle());

        return movie.get();

//        if (movie.isPresent()) {
//            movie.get().setRating(rate.getRating());
//            movie.get().setVotes(movie.get().getVotes() + 1);
//            moviesRepository.save(movie.get());
//            return movie.get();
//        }
//
//        return null;
    }

}
