package com.vieira.sogolon.ItauChallenge.controller;

import com.vieira.sogolon.ItauChallenge.client.MoviesClient;
import com.vieira.sogolon.ItauChallenge.dto.MoviesDTO;

import com.vieira.sogolon.ItauChallenge.entities.Movies;
import com.vieira.sogolon.ItauChallenge.service.MoviesService;
import lombok.AllArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping(path="movies")
@AllArgsConstructor
public class MoviesController {

    private final MoviesClient moviesClient;
    private final MoviesService moviesService;
    private final Environment env;
    private final static String type = "r=json";

    @GetMapping("/{title}")
    public MoviesDTO getMovies(@PathVariable("title") String title) {
        Optional<Movies> movieInDatabase = moviesService.checkInDatabase(title);

        if (movieInDatabase.isPresent()) {
            return moviesService.getValuesFromDatabase(movieInDatabase.get());
        }

        String key = env.getProperty("api.key");
        Movies response = moviesClient.getMovie(title, key, type);
        return moviesService.getValuesFromExternalAPI(response);
    }

}
