package com.vieira.sogolon.ItauChallenge.repository;

import com.vieira.sogolon.ItauChallenge.entities.Comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentsRepository extends JpaRepository<Comment, Long> {
}
