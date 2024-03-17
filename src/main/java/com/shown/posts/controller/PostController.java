package com.shown.posts.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import com.mongodb.MongoWriteException;
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
	public List<Post> getPostsFromAuthor(@RequestParam(value = "authorName") String authorName) {
		List<Post> posts = repository.findByAuthorName(authorName);
		if(posts.isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Blog with author name = %s not found", authorName));
		return posts;
	}
	
	@GetMapping("/posts/getPostsByTitle")
	public Post getPostsByTitle(@RequestParam(value = "title") String title) {
		List<Post> posts = repository.findByTitle(title);
		if(posts.isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Blog with title = %s not found", title));
		return posts.get(0);
	}
	
	@ResponseStatus(HttpStatus.CREATED) // 201
	@PostMapping("/posts/create")
	public Post createPost(@RequestBody Post post) {
		try {
			 return repository.save(post);
		} catch (DataIntegrityViolationException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	
	@ResponseStatus(HttpStatus.OK) // 201
	@PostMapping("/posts/update")
	public Post updatePost(@RequestBody Post post) {
		post.setUpdateTs(LocalDateTime.now());
		try {
			 return repository.save(post);
		} catch (DataIntegrityViolationException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	
	@ResponseStatus(HttpStatus.NO_CONTENT) // 204
	@DeleteMapping("/posts/delete")
	public void deletePost(@RequestParam(value = "id") String id) {
		repository.deleteById(id);
	}
}
