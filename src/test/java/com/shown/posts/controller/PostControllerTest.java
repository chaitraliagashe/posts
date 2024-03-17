package com.shown.posts.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.shown.posts.PostsApplication;
import com.shown.posts.model.Content;
import com.shown.posts.model.Post;
import com.shown.posts.repository.PostRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = PostsApplication.class)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class PostControllerTest {

	@Autowired
	private MockMvc mvc;
	@Autowired
    private PostRepository postRepository;
	
	Post addedPost;
	
	@BeforeEach
	public void setUp() {
		Content content = new Content("This is a trial text blog", null);
		Post post = new Post(
				"random_user",
				"Random User",
				new ArrayList<Content>(List.of(content)), "Random Blog"
			);
		addedPost = postRepository.save(post);
	}
	
	@Test
	void testGetPostsByTitle() throws Exception {
		mvc.perform(get("/posts/getPostsByTitle").param("title", "Random Blog").contentType(MediaType.APPLICATION_JSON).header("X-API-KEY", "9H3hyCBR"))
			.andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
	}

	@Test
	void testUpdatePost() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
		addedPost.setAuthorName("random user new");
		mvc.perform(post("/posts/update")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(addedPost))
				.header("X-API-KEY", "9H3hyCBR"))
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
	}

	@Test
	void testDeletePost() throws Exception {
		mvc.perform(delete("/posts/deletePost").param("id", addedPost.getId()).contentType(MediaType.APPLICATION_JSON).header("X-API-KEY", "9H3hyCBR"));
		assertEquals(Optional.empty(), postRepository.findById(addedPost.getId()));
	}
	
	@AfterEach
	public void tearDown() {
		postRepository.deleteById(addedPost.getId());
	}

}
