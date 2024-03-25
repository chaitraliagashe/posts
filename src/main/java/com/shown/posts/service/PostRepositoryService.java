package com.shown.posts.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.stereotype.Service;

import com.shown.posts.model.Post;
import com.shown.posts.repository.PostRepository;

@Service
public class PostRepositoryService {
	@Autowired
	PostRepository repository;

	public List<Post> findPostsByAuthorName(String authorName){
		return repository.findByAuthorName(authorName);
	}

	public List<Post> findPostByTitle(String title){
		return repository.findByTitle(title);
	}

	public Post createOrUpdatePost(Post post) {
		return repository.save(post);
	}

	public void delete(String userId, String id) {
		Optional<Post> post = repository.findById(id);
		if(post.isPresent()) {
			if(!userId.equals(post.get().getAuthorId())) {
				throw new AuthorizationServiceException("deleting a post by another user is not permitted");
			}
		}
		repository.deleteById(id);
	}

	public Post findPostById(String id) {
		return repository.findById(id).orElseThrow();
	}

	public List<Post> findPostsbyIds(List<String> postIds) {
		return repository.findAllById(postIds);
	}
}
