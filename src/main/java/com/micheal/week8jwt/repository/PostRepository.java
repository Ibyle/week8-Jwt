package com.micheal.week8jwt.repository;

import com.micheal.week8jwt.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByPublished (boolean published);

    List<Post> findByTitleIgnoreCaseStartingWith(String title);
}
