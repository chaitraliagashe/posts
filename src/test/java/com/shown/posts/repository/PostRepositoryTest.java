package com.shown.posts.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
				"testUser",
				"Test User",
				new ArrayList<Content>(List.of(content)), "Test Blog"
			);
		Post addedPost = repository.save(post);
		Post fetchedPost = repository.findByTitle("Test Blog").get(0);
		assertEquals(addedPost.getId(), fetchedPost.getId());
		List<Content> currentContents = addedPost.getContents();
		currentContents.add(new Content("This does not content more informatio", null));
		addedPost.setContents(currentContents);
		Post updatedPost = repository.save(addedPost);
		List<Post> posts = repository.findByAuthorName("Test User");
		assertEquals(1, posts.size());
		assertEquals(addedPost.getContents().size(), posts.get(0).getContents().size());
		repository.delete(fetchedPost);
		Optional<Post> deletedPost = repository.findById(addedPost.getId());
		assertEquals(Optional.empty(), deletedPost);
	}

}
