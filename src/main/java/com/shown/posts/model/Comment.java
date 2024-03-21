package com.shown.posts.model;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document("comments")
public class Comment {
	@Id
	private String id;
	
	@Field(value = "post_id")
	private String postId;
	
	@Field(value = "author_id")
	private String authorId;
	
	@Field(value = "contents")
	private String contents;
	
	@Field(value = "creation_ts")
	private String creationTs;

	public Comment(String postId, String authorId, String contents) {
		this.postId = postId;
		this.authorId = authorId;
		this.contents = contents;
		this.creationTs = LocalDateTime.now().toString();
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

	public String getAuthorId() {
		return authorId;
	}

	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public String getCreationTs() {
		return creationTs;
	}

	public void setCreationTs(String creationTs) {
		this.creationTs = creationTs;
	}
	
	
}
