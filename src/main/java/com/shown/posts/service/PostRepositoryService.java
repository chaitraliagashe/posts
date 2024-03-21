package com.shown.posts.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
	
	public void delete(String id) {
		repository.deleteById(id);
	}

	public Post findPostById(String id) {
		return repository.findById(id).orElseThrow();
	}
	
	public List<Post> findPostsbyIds(List<String> postIds) {
		return repository.findAllById(postIds);
	}
}
