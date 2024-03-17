package com.shown.posts.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import com.shown.posts.model.Post;
import com.shown.posts.repository.PostRepository;

@RestController
public class PostController {

	@Autowired
	private PostRepository repository;
	
	@GetMapping("/posts")
	public String greeting() {
		return "Hello world";
	}
	
	@GetMapping("/posts/getPostsFromAuthor")
	public List<Post> getPostsFromAuthor(String authorName) {
		return repository.findByAuthorName(authorName);
	}
	
	@ResponseStatus(HttpStatus.CREATED) // 201
	@PostMapping("/posts/create")
	public Post createPost(@RequestBody Post post) {
		return repository.save(post);
	}
	
	@ResponseStatus(HttpStatus.OK) // 201
	@PostMapping("/posts/update")
	public Post updatePost(@RequestBody Post post) {
		post.setUpdateTs(LocalDateTime.now());
		return repository.save(post);
	}
	
	@ResponseStatus(HttpStatus.NO_CONTENT) // 204
	@DeleteMapping("/posts/delete")
	public void deletePost(@RequestParam(value = "id") String id) {
		repository.deleteById(id);
	}
}
