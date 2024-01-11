package com.micheal.week8jwt.repository;

import com.micheal.week8jwt.model.Comment;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByPostId(Long PostId);

    @Transactional
    void deleteByPostId(Long Tutorial);

    List<Comment> findByContentIgnoringCaseContaining(String content);
}
