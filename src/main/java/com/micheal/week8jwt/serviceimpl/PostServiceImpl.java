package com.micheal.week8jwt.serviceimpl;

import com.micheal.week8jwt.model.Post;
import com.micheal.week8jwt.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class PostServiceImpl {

    private final PostRepository postRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public ResponseEntity<Post> savePost(Post newPost) {
        Post post = new Post(newPost.getTitle(), newPost.getImage(), newPost.getDescription(), newPost.isPublished(), newPost.getPostLikes());
        postRepository.save(post);
        return new ResponseEntity<>(post, HttpStatus.CREATED);

    }

    public ResponseEntity<List<Post>> getAllPost() {
        List<Post> allPost = postRepository.findAll();
        return new ResponseEntity<>(allPost, HttpStatus.FOUND);
    }

    public ResponseEntity<List<Post>> searchByTitle(String title) {
        List<Post> allSearchByTitle = postRepository.findByTitleIgnoreCaseStartingWith(title);
        if (allSearchByTitle.isEmpty()) {
            return new ResponseEntity<>(allSearchByTitle, HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(allSearchByTitle, HttpStatus.FOUND);

        }
    }

    public ResponseEntity<Void> deletePostById(Long id) {
        postRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<Post> editPostById(Long id, Post postToEdit) {
        Optional<Post> post = postRepository.findById(id);
        if (post.isPresent()) {
            Post post1 = post.get();
            post1.setTitle(postToEdit.getTitle());
            post1.setImage(postToEdit.getImage());
            post1.setDescription(postToEdit.getDescription());
            post1.setPublished(postToEdit.isPublished());
            postRepository.save(post1);
            return new ResponseEntity<>(post1, HttpStatus.OK);

        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }
    }

    public ResponseEntity<Post> likePostById(Long postId) {
        Post post = postRepository.findById(postId).orElse(null);

        if (post != null){
            post.incrementPost();
            postRepository.save(post);
            return new ResponseEntity<>(post, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    public ResponseEntity<?> unlikePostById(Long postId) {
        Post post = postRepository.findById(postId).orElse(null);

        if (post!=null && post.getPostLikes() > 0){
            post.decrementPost();
            postRepository.save(post);
            return new ResponseEntity<>(post, HttpStatus.OK);
        }else{
            Post zeroLikesPost = new Post(post.getTitle(), post.getImage(), post.getDescription(), post.isPublished(), 0L);
            zeroLikesPost.setId(post.getId());
            zeroLikesPost.setPostDate(post.getPostDate());
            return new ResponseEntity<>(zeroLikesPost, HttpStatus.OK);
        }
    }
}
