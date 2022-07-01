package com.vieira.sogolon.ItauChallenge.entities.Comment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comment_responses")
public class CommentResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String text;
    private Boolean repeated;
    private Integer likes;
    private Integer dislikes;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;

}
