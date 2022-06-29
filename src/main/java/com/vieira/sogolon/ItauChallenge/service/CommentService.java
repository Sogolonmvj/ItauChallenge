package com.vieira.sogolon.ItauChallenge.service;

import com.vieira.sogolon.ItauChallenge.entities.Comment.Comment;
import com.vieira.sogolon.ItauChallenge.entities.Comment.CommentResponse;
import com.vieira.sogolon.ItauChallenge.entities.Comment.CommentTag;
import com.vieira.sogolon.ItauChallenge.entities.Movies;
import com.vieira.sogolon.ItauChallenge.repository.CommentResponseRepository;
import com.vieira.sogolon.ItauChallenge.repository.CommentTagRepository;
import com.vieira.sogolon.ItauChallenge.repository.CommentsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CommentService {

    private CommentsRepository commentsRepository;
    private CommentResponseRepository commentResponseRepository;
    private CommentTagRepository commentTagRepository;
    private final static Integer noReactions = 0;

    public Comment findCommentInDatabase(Long id) {
        Optional<Comment> comment = commentsRepository.findById(id);
        return comment.orElse(null);
    }

    public void saveCommentResponse(CommentResponse commentResponse) {
        commentResponseRepository.save(commentResponse);
    }

    public void initiateCommentResponse(CommentResponse commentResponse) {
        commentResponse.setLikes(noReactions);
        commentResponse.setDislikes(noReactions);
    }

    public Comment commentResponse(Long id, CommentResponse commentResponse) {
        Comment comment = findCommentInDatabase(id);

        initiateCommentResponse(commentResponse);

        saveCommentResponse(commentResponse);

        comment.getResponses().add(commentResponse);

        commentsRepository.save(comment);

        return comment;
    }

    public void initiateCommentTag(CommentTag commentTag) {
        commentTag.setLikes(noReactions);
        commentTag.setDislikes(noReactions);
    }

    public CommentTag tagComment(Long id, CommentTag commentTag) {
        Comment comment = findCommentInDatabase(id);

        initiateCommentTag(commentTag);

        commentTag.setTaggedComment(comment);

        commentTagRepository.save(commentTag);

        return commentTag;
    }

}
