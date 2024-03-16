package com.shown.posts.model;

public class Content {
	private String text;
	private String media;

	public Content() {
		super();
	}

	public Content(String text, String media) {
		super();
		this.text = text;
		this.media = media;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getMedia() {
		return media;
	}

	public void setMedia(String media) {
		this.media = media;
	}	
	
}
