package com.micheal.week8jwt.controller;

import com.micheal.week8jwt.model.Comment;
import com.micheal.week8jwt.serviceimpl.CommentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/comment")
public class CommentController {
    private CommentServiceImpl commentService;
    @Autowired
    public CommentController(CommentServiceImpl commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/create-comment/{postId}")
    public ResponseEntity<Comment> createComment
            (@PathVariable Long postId, @RequestBody Comment newComment){
        return commentService.saveComment(postId, newComment);

    }

    @PutMapping("/edit-comment/{commentId}")
    public ResponseEntity<Comment> editCommentById
            (@PathVariable Long commentId, @RequestBody Comment commentToEdit){
        return commentService.editCommentById(commentId,commentToEdit);
    }

    @GetMapping("/find-comment-postId/{postId}")
    public ResponseEntity<List<Comment>>  findAllCommentByPostId(@PathVariable Long postId){
        return commentService.findAllCommentById(postId);
    }

    @GetMapping("/all-comment")
    public ResponseEntity<List<Comment>> getAllComment() {
        return commentService.getAllComment();
    }

    @GetMapping("/get-comment/{content}")
    public ResponseEntity<List<Comment>> searchCommentByContent(@PathVariable String content){
        return commentService.findCommentByContent(content);
    }


    @DeleteMapping("delete-comment/{id}")
    public  ResponseEntity<Void> deleteCommentById(@PathVariable Long id){
        return commentService.deleteById(id);
    }

    @PutMapping("/like-comment/{id}")
    public ResponseEntity<Comment> likeCommentById(@PathVariable Long id){
        return commentService.likeCommentById(id);
    }

    @PutMapping("/unlike-comment/{id}")
    public ResponseEntity<Comment> unlikeCommentById(@PathVariable Long id){
        return commentService.unlikeCommentById(id);
    }


}

