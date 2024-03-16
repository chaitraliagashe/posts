package com.shown.posts.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.shown.posts.model.Post;

public interface PostRepository extends MongoRepository<Post, String>{
	public Post findByTitle(String title);
	public List<Post> findByAuthorName(String authorName);
}
