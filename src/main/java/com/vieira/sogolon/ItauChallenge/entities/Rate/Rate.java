package com.vieira.sogolon.ItauChallenge.entities.Rate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vieira.sogolon.ItauChallenge.entities.Movies;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "rating")
@Entity
public class Rate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "movies")
    private Movies movies;

    private Double rating;
}
