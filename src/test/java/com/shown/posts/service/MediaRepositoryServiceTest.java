package com.shown.posts.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.FileInputStream;
import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import com.shown.posts.model.Media;
import com.shown.posts.repository.MediaRepository;

@SpringBootTest
class MediaRepositoryServiceTest {
	@Autowired
	private MediaRepository mediaRepository;
	@Autowired
	private MediaRepositoryService service;
	
	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	void testAddMedia() throws IOException {
		FileInputStream inputFile = new FileInputStream(getClass().getClassLoader().getResource("coffee.jpg").getFile());  
		MockMultipartFile file = new MockMultipartFile("Coffee", "coffee", "multipart/form-data", inputFile);
		String mediaId = service.addMedia("65fc72d9d7269a7625591c2b", "There is nothing better than a hot cup of coffee", file);
		Media media = service.getMedia(mediaId);
		assertEquals(mediaId, media.getId());
		mediaRepository.delete(media);
	}

}
