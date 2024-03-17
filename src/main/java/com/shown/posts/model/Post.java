package com.shown.posts.model;

import java.time.LocalDateTime;
import java.util.List;

import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.BSONTimestamp;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document("posts")
public class Post {
	@Id
	private String id;
	
	@Field(value = "author_id")
	private String authorId;
	
	@Field(value = "author_name")
	private String authorName;
	
	@Field(value = "contents")
	private List<Content> contents;
	
	@Field(value = "creation_ts")
	private String creationTs;
	
	@Field(value = "update_ts")
	private String updateTs;
	
	@Field(value = "title")
	private String title;
	
	
	public Post(String authorId, String authorName, List<Content> contents, String title) {
		this.authorId = authorId;
		this.authorName = authorName;
		this.contents = contents;
		this.creationTs = LocalDateTime.now().toString();
		this.updateTs = LocalDateTime.now().toString();
		this.title = title;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getAuthorId() {
		return authorId;
	}


	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}


	public String getAuthorName() {
		return authorName;
	}


	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}


	public List<Content> getContents() {
		return contents;
	}


	public void setContents(List<Content> contents) {
		this.contents = contents;
	}


	public LocalDateTime getCreationTs() {
		return LocalDateTime.parse(creationTs);
	}


	public void setCreationTs(LocalDateTime creationTs) {
		this.creationTs = creationTs.toString();
	}


	public LocalDateTime getUpdateTs() {
		return LocalDateTime.parse(updateTs);
	}


	public void setUpdateTs(LocalDateTime updateTs) {
		this.updateTs = updateTs.toString();
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}
	
}
