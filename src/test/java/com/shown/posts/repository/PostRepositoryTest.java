package com.shown.posts.repository;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.shown.posts.model.Content;
import com.shown.posts.model.Post;

@SpringBootTest
class PostRepositoryTest {

	@Autowired
	PostRepository repository;
	
	@Test
	void test() {
		Content content = new Content("This is a trial text blog", null);
		Post post = new Post(
				UUID.randomUUID().toString(),
				"testUser",
				"Test User",
				List.of(content), "Test Blog"
			);
		repository.save(post);
	}

}
