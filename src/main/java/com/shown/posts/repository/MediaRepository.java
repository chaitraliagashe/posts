package com.shown.posts.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.shown.posts.model.Media;

public interface MediaRepository extends MongoRepository<Media, String> {

}
