package com.shown.posts.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.shown.posts.PostsApplication;
import com.shown.posts.model.Comment;
import com.shown.posts.model.Content;
import com.shown.posts.model.Post;
import com.shown.posts.repository.CommentsRepository;
import com.shown.posts.repository.MediaRepository;
import com.shown.posts.repository.PostRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = PostsApplication.class)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class PostControllerTest {

	@Autowired
	private MockMvc mvc;
	@Autowired
    private PostRepository postRepository;
	@Autowired
	private CommentsRepository commentsRepository;
	@Autowired
	private MediaRepository mediaRepository;
	
	Post addedPost;
	Comment addedComment;
	
	@BeforeEach
	public void setUp() {
		Content content = new Content("This is a trial text blog", null);
		Post post = new Post(
				"random_user",
				"Random User",
				new ArrayList<Content>(List.of(content)), "Random Blog"
			);
		addedPost = postRepository.save(post);
		Comment comment = new Comment(addedPost.getId(), "author123", "This is a sample comment");
		addedComment = commentsRepository.save(comment);
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
				.param("userId", addedPost.getAuthorId())
				.content(objectMapper.writeValueAsBytes(addedPost))
				.header("X-API-KEY", "9H3hyCBR"))
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
	}

	@Test
	void testDeletePost() throws Exception {
		mvc.perform(delete("/posts/delete")
				.param("id", addedPost.getId())
				.param("userId", addedPost.getAuthorId())
				.contentType(MediaType.APPLICATION_JSON).header("X-API-KEY", "9H3hyCBR"));
		assertEquals(Optional.empty(), postRepository.findById(addedPost.getId()));
	}
	
	@Test
	void testCreateAndDeleteComment() throws Exception {
		Comment comment = new Comment(addedPost.getId(), "authorABS", "This is a sample comment by another user");
		ObjectMapper objectMapper = new ObjectMapper();
		mvc.perform(post("/posts/createComment")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(comment))
				.header("X-API-KEY", "9H3hyCBR"))
			.andExpect(status().isCreated())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
		List<Comment> commentList = commentsRepository.findCommentsByPostId(addedPost.getId());
		List<Comment> singleComment = commentList.stream().filter(c -> c.getAuthorId().equals("authorABS")).collect(Collectors.toList());
		mvc.perform(delete("/posts/deleteComment")
				.contentType(MediaType.APPLICATION_JSON)
				.param("id", singleComment.get(0).getId())
				.param("userId", singleComment.get(0).getAuthorId())
				.header("X-API-KEY", "9H3hyCBR"));
		commentList = commentsRepository.findCommentsByPostId(addedPost.getId());
		assertEquals(1, commentList.size());
	}
	
	@Test
	void testUpdateComment() throws Exception {
		addedComment.setContents("I am modifying the comment");
		ObjectMapper objectMapper = new ObjectMapper();
		mvc.perform(post("/posts/updateComment")
				.contentType(MediaType.APPLICATION_JSON)
				.param("userId", "author123")
				.content(objectMapper.writeValueAsBytes(addedComment))
				.header("X-API-KEY", "9H3hyCBR"))
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
	}
	
	@Test
	void testUpdateCommentWrongUser() throws Exception {
		addedComment.setContents("I am modifying the comment");
		ObjectMapper objectMapper = new ObjectMapper();
		mvc.perform(post("/posts/updateComment")
				.contentType(MediaType.APPLICATION_JSON)
				.param("userId", "a123")
				.content(objectMapper.writeValueAsBytes(addedComment))
				.header("X-API-KEY", "9H3hyCBR"))
			.andExpect(status().is4xxClientError());
	}
	
	@Test
	void testDeleteCommentWrongUser() throws Exception {
		addedComment.setContents("I am modifying the comment");
		ObjectMapper objectMapper = new ObjectMapper();
		mvc.perform(post("/posts/deleteComment")
				.contentType(MediaType.APPLICATION_JSON)
				.param("userId", "a123")
				.content(objectMapper.writeValueAsBytes(addedComment))
				.header("X-API-KEY", "9H3hyCBR"))
			.andExpect(status().is4xxClientError());
	}
	
	@Test
	void testUploadMediaByPostIdAndGetMediaById() throws Exception {
		FileInputStream inputFile = new FileInputStream(getClass().getClassLoader().getResource("coffee.jpg").getFile());  
		MockMultipartFile file = new MockMultipartFile("content", "coffee", "multipart/form-data", inputFile);
		MvcResult result = mvc.perform(multipart(HttpMethod.POST, "/posts/uploadMediaByPostId")
				.file(file)
				.param("postId", "a123")
				.param("title", "Coffe time!!")
				.header("X-API-KEY", "9H3hyCBR"))
			.andExpect(status().isCreated())
			.andReturn();
		String json = result.getResponse().getContentAsString();
		String[] resultPaths = json.split(":");
		mvc.perform(get("/posts/getMediaById")
										.param("id", resultPaths[1])
										.contentType(MediaType.APPLICATION_JSON)
										.header("X-API-KEY", "9H3hyCBR"))
								.andExpect(status().isOk())
								.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
								.andReturn();
		mediaRepository.deleteById(resultPaths[1]);
	}
	
	@AfterEach
	public void tearDown() {
		commentsRepository.deleteCommentsByPostId(addedPost.getId());
		postRepository.deleteById(addedPost.getId());
	}

}
