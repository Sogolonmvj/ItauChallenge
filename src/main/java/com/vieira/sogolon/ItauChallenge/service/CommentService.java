package com.vieira.sogolon.ItauChallenge.service;

import com.vieira.sogolon.ItauChallenge.entities.Comment.Comment;
import com.vieira.sogolon.ItauChallenge.entities.Comment.CommentResponse;
import com.vieira.sogolon.ItauChallenge.entities.Comment.CommentTag;
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

    public CommentResponse findCommentResponseInDatabase(Long id) {
        Optional<CommentResponse> commentResponse = commentResponseRepository.findById(id);
        return commentResponse.orElse(null);
    }

    public CommentTag findCommentTagInDatabase(Long id) {
        Optional<CommentTag> commentTag = commentTagRepository.findById(id);
        return commentTag.orElse(null);
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

    public Comment likeComment(Long id) {
        Comment comment = findCommentInDatabase(id);

        comment.setLikes(comment.getLikes() + 1);

        commentsRepository.save(comment);

        return comment;
    }

    public CommentResponse likeCommentResponse(Long id) {
        CommentResponse commentResponse = findCommentResponseInDatabase(id);

        commentResponse.setLikes(commentResponse.getLikes() + 1);

        commentResponseRepository.save(commentResponse);

        return commentResponse;
    }

    public CommentTag likeCommentTag(Long id) {
        CommentTag commentTag = findCommentTagInDatabase(id);

        commentTag.setLikes(commentTag.getLikes() + 1);

        commentTagRepository.save(commentTag);

        return commentTag;
    }

    public Comment dislikeComment(Long id) {
        Comment comment = findCommentInDatabase(id);

        comment.setDislikes(comment.getDislikes()  + 1);

        commentsRepository.save(comment);

        return comment;
    }

    public CommentResponse dislikeCommentResponse(Long id) {
        CommentResponse commentResponse = findCommentResponseInDatabase(id);

        commentResponse.setDislikes(commentResponse.getDislikes() + 1);

        commentResponseRepository.save(commentResponse);

        return commentResponse;
    }

    public CommentTag dislikeCommentTag(Long id) {
        CommentTag commentTag = findCommentTagInDatabase(id);

        commentTag.setDislikes(commentTag.getDislikes() + 1);

        commentTagRepository.save(commentTag);

        return commentTag;
    }

}