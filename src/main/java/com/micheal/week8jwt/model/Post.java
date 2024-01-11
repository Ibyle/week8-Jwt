package com.micheal.week8jwt.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;


import java.time.LocalDate;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "image")
    private String image;

    @Column(name = "published")
    private boolean published;

    @Column(columnDefinition = "DATE")
    @CreationTimestamp
    private LocalDate postDate;

    @Column(name = "likes")
    private Long postLikes;

    public Post(String title, String image, String description, boolean published, Long postLikes) {
        this.title = title;
        this.image = image;
        this.description = description;
        this.published = published;
        this.postLikes = postLikes;

    }

    // Set likes Long value to 0  when creating a new post
    @PrePersist
    public void prePersist() {
        this.postLikes = 0L;

    }
    public void incrementPost(){
        postLikes++;
    }
    public void decrementPost(){
        postLikes--;
    }


}
