package com.micheal.week8jwt.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "comment_generator")
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Post post;

    @Column(name = "commentDate")
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date commentDate;

    private Long commentLikes;

    public Comment(String content, Long commentLikes) {
        this.content = content;
        this.commentLikes = commentLikes;
    }


    // Set likes to 0 when creating a new comment
    @PrePersist
    public void prePersist() {
        this.commentLikes = 0L;
    }

    public void incrementComment(){
        commentLikes++;
    }

    public void decrementComment() {
        commentLikes--;

    }
}

