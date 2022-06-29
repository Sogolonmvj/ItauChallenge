package com.vieira.sogolon.ItauChallenge.repository;

import com.vieira.sogolon.ItauChallenge.entities.Comment.CommentResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentResponseRepository extends JpaRepository<CommentResponse, Long> {
}
