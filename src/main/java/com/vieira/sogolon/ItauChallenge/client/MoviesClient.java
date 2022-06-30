package com.vieira.sogolon.ItauChallenge.client;

import com.vieira.sogolon.ItauChallenge.entities.Movies;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(url = "http://www.omdbapi.com/", name = "movies")
public interface MoviesClient {

    @GetMapping
    Movies getMovie(@RequestParam(value = "t") String title,
                    @RequestParam(value = "apikey") String key,
                    @RequestParam(value = "r") String type);

}
