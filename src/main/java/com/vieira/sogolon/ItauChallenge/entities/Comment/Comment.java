package com.vieira.sogolon.ItauChallenge.entities.Comment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String text;
    private Integer likes;
    private Integer dislikes;
    @ManyToMany(fetch = FetchType.LAZY)
    private List<CommentResponse> responses;

    public Comment(String username, String text) {
        this.username = username;
        this.text = text;
        this.responses = new ArrayList<>();
    }
}
