package com.vieira.sogolon.ItauChallenge.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vieira.sogolon.ItauChallenge.entities.Comment.Comment;
import com.vieira.sogolon.ItauChallenge.entities.Comment.CommentTag;
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

}
