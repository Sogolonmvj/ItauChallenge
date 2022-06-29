package com.vieira.sogolon.ItauChallenge.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vieira.sogolon.ItauChallenge.entities.Comment.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name="movies")
public class Movies {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String imdbID;
    @JsonProperty(value = "Title")
    private String title;
    @JsonProperty(value = "Year")
    private String year;
    @JsonProperty(value = "Runtime")
    private String runtime;
    @JsonProperty(value = "Genre")
    private String genre;
    private String imdbRating;
    private String imdbVotes;
    private Double rating;
    private Integer votes;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Comment> comments;

    public Movies(String imdbID,
                  String title,
                  String year,
                  String runtime,
                  String genre,
                  String imdbRating,
                  String imdbVotes) {
        this.imdbID = imdbID;
        this.title = title;
        this.year = year;
        this.runtime = runtime;
        this.genre = genre;
        this.imdbRating = imdbRating;
        this.imdbVotes = imdbVotes;
        this.rating = 0.0;
        this.votes = 0;
        this.comments = new ArrayList<>();
    }

}
