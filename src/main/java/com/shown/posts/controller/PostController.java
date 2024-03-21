package com.shown.posts.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.shown.posts.model.Comment;
import com.shown.posts.model.Post;
import com.shown.posts.service.CommentsRepositoryService;
import com.shown.posts.service.PostRepositoryService;

@RestController
public class PostController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private PostRepositoryService postService;

	@Autowired
	private CommentsRepositoryService commentService;

	@GetMapping("/posts")
	public String greeting() {
		return "Hello world";
	}


	@GetMapping("/posts/getPostsById")
	public Post getPostsById(@RequestParam(value = "id") String id) {
		try {
			return postService.findPostById(id);
		} catch(Exception e) {
			logger.error("Error finding the blog", e);
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Blog with author name = %s not found", id));
		}
	}

	@GetMapping("/posts/getPostsFromAuthor")
	public List<Post> getPostsFromAuthor(@RequestParam(value = "authorName") String authorName) {
		List<Post> posts = postService.findPostsByAuthorName(authorName);
		if(posts.isEmpty()) {
			logger.error("Blog with author name : {0} not found", authorName);
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Blog with author name = %s not found", authorName));
		}
		return posts;
	}

	@GetMapping("/posts/getPostsByTitle")
	public Post getPostsByTitle(@RequestParam(value = "title") String title) {
		List<Post> posts = postService.findPostByTitle(title);
		if(posts.isEmpty()) {
			logger.error("Blog with title: {0} not found", title);
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Blog with title = %s not found", title));
		}
		return posts.get(0);
	}
	
	@GetMapping("/posts/getPostsByIds")
	public List<Post> getPostsById(@RequestBody List<String> ids) {
		try {
			return postService.findPostsbyIds(ids);
		} catch(Exception e) {
			logger.error("Error finding the blog", e);
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Popular blog posts could not be found for ids %s", ids));
		}
	}

	@ResponseStatus(HttpStatus.CREATED) // 201
	@PostMapping("/posts/create")
	public Post createPost(@RequestBody Post post) {
		try {
			return postService.createOrUpdatePost(post);
		} catch (DataIntegrityViolationException e) {
			logger.error("Can not create the blog post", e);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@ResponseStatus(HttpStatus.OK) // 201
	@PostMapping("/posts/update")
	public Post updatePost(@RequestBody Post post) {
		post.setUpdateTs(LocalDateTime.now());
		try {
			return postService.createOrUpdatePost(post);
		} catch (DataIntegrityViolationException e) {
			logger.error("Can not update the blog post", e);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@ResponseStatus(HttpStatus.NO_CONTENT) // 204
	@DeleteMapping("/posts/delete")
	public void delete(@RequestParam(value = "id") String id) {
		try {
			commentService.deleteCommentByPostId(id);
			postService.delete(id);
		} catch (Exception e) {
			logger.error("Can not delete the blog post", e);
		}
	}

	@GetMapping("/posts/getCommentsByPostId")
	public List<Comment> getCommentsByPostId(@RequestParam(value = "postId") String postId) {
		return commentService.findCommentsByPostId(postId);
	}

	@ResponseStatus(HttpStatus.CREATED) // 201
	@PostMapping("/posts/createComment")
	public Comment createComment(@RequestBody Comment comment) {
		try {
			return commentService.createComment(comment);
		} catch (DataIntegrityViolationException e) {
			logger.error("Can not create the comment", e);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@ResponseStatus(HttpStatus.OK) // 201
	@PostMapping("/posts/updateComment")
	public Comment updateComment(@RequestParam(value = "userId") String userId, @RequestBody Comment comment) {
		try {
			return commentService.updateComment(comment, userId);
		} catch(AuthorizationServiceException e) {
			logger.error("Can not update someone else's comments", e);
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@ResponseStatus(HttpStatus.NO_CONTENT) // 204
	@DeleteMapping("/posts/deleteComment")
	public void deleteComment(@RequestParam(value = "id") String id, @RequestParam(value = "userId") String userId) {
		try { 
			commentService.deleteComment(id, userId);
		} catch(AuthorizationServiceException e) {
			logger.error("Can not delete someone else's comments", e);
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
		} 
	}}
