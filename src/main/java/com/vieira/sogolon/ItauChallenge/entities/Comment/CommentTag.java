package com.vieira.sogolon.ItauChallenge.entities.Comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CommentTag {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String text;
    private Boolean repeated;
    @OneToOne(fetch = FetchType.LAZY)
    private Comment taggedComment;
    private Integer likes;
    private Integer dislikes;
}
