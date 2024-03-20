package com.shown.posts.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.shown.posts.model.Comment;

public interface CommentsRepository extends MongoRepository<Comment, String> {
	public List<Comment> findCommentsByPostId(String postId);
	public void deleteCommentsByPostId(String postId);
}
