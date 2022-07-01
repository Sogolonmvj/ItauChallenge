package com.vieira.sogolon.ItauChallenge.entities.Comment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vieira.sogolon.ItauChallenge.entities.Movies;
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
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String text;
    private Integer likes;
    private Integer dislikes;
    private Boolean repeated;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "movie_id")
    private Movies movie;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<CommentResponse> responses;

    public Comment(String username, String text) {
        this.username = username;
        this.text = text;
        this.repeated = false;
        this.responses = new ArrayList<>();
    }

}
