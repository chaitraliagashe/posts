package com.shown.posts.service;

import java.io.IOException;
import java.util.Optional;

import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.shown.posts.model.Media;
import com.shown.posts.repository.MediaRepository;

@Service
public class MediaRepositoryService {
	@Autowired
	private MediaRepository mediaRepository;
	
	public String addMedia(String postId, String title, MultipartFile file) throws IOException { 
        Media media = new Media(postId, title, new Binary(BsonBinarySubType.BINARY, file.getBytes())); 
        
        Media added = mediaRepository.insert(media); 
        return added.getId(); 
    }
	
	public Media getMedia(String id) throws IOException {   
        Optional<Media> added = mediaRepository.findById(id); 
        return added.get(); 
    }
}
