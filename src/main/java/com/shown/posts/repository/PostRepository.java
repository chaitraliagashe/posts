package com.shown.posts.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.shown.posts.model.Post;

public interface PostRepository extends MongoRepository<Post, String>{

}
