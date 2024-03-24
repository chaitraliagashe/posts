package com.shown.posts.model;

import java.time.LocalDateTime;

import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "media")
public class Media {
	@Id
	private String id;
	
	@Field(value = "post_id")
	private String postId;
	
	@Field(value = "title")
	private String title;
	
	@Field(value = "contents")
	private Binary contents;
	
	@Field(value = "creation_ts")
	private String creationTs;

	public Media(String postId, String title, Binary contents) {
		this.postId = postId;
		this.title = title;
		this.contents = contents;
		this.creationTs = LocalDateTime.now().toString();
	}

	
	public Media() {
		super();
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPostId() {
		return postId;
	}

	public void setPostId(String postId) {
		this.postId = postId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Binary getContents() {
		return contents;
	}

	public void setContents(Binary contents) {
		this.contents = contents;
	}

	public String getCreationTs() {
		return creationTs;
	}

	public void setCreationTs(String creationTs) {
		this.creationTs = creationTs;
	}
}
