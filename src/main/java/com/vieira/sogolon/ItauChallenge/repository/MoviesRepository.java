package com.vieira.sogolon.ItauChallenge.repository;

import com.vieira.sogolon.ItauChallenge.entities.Movies;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MoviesRepository extends JpaRepository<Movies, Long> {
    Optional<Movies> findByTitle(String title);
}
