package com.micheal.week8jwt.controller;

import com.micheal.week8jwt.model.Post;
import com.micheal.week8jwt.serviceimpl.PostServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.stream.events.Comment;
import java.util.List;
@RestController
@RequestMapping("/api")
public class PostController {
    private PostServiceImpl postService;

    @Autowired
    public PostController(PostServiceImpl postService) {
        this.postService = postService;
    }

    @PostMapping("/save-post")
    public ResponseEntity<Post> savePost (@RequestBody Post post) {
        return postService.savePost(post);
    }

    public ResponseEntity<Post> createPost(@RequestBody Post newPost){
        return postService.savePost(newPost);
    }

    @GetMapping("/all-post")
    public ResponseEntity<List<Post>> getAllPost(){
        return postService.getAllPost();
    }


    @GetMapping("/search-post/{title}")
    public ResponseEntity<List<Post>> searchByTitle(@PathVariable String title){
        return postService.searchByTitle(title);
    }

    @DeleteMapping("/delete-post/{id}")
    public ResponseEntity<Void> deletePostById(@PathVariable Long id){
        return postService.deletePostById(id);
    }


    @PutMapping("/edit-post/{id}")
    public ResponseEntity<Post> editPostById(@PathVariable Long id, @RequestBody Post postToEdit){
        return postService.editPostById(id, postToEdit);
    }

    @PutMapping("/like/{postId}")
    public ResponseEntity<Post> likePost(@PathVariable Long postId) {
        return postService.likePostById(postId);
    }

    @PutMapping("/unlike/{postId}")
    public ResponseEntity<?> unlikePost(@PathVariable Long postId) {
        return postService.unlikePostById(postId);
    }
}

