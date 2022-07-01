package com.vieira.sogolon.ItauChallenge.entities.Comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comment_tags")
public class CommentTag {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String text;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Comment taggedComment;

}
