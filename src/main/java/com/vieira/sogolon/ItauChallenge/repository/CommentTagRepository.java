package com.vieira.sogolon.ItauChallenge.repository;

import com.vieira.sogolon.ItauChallenge.entities.Comment.CommentTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentTagRepository extends JpaRepository<CommentTag, Long> {
}
