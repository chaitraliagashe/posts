package com.shown.posts.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.stereotype.Service;

import com.shown.posts.model.Comment;
import com.shown.posts.repository.CommentsRepository;

@Service
public class CommentsRepositoryService {
	@Autowired
	private CommentsRepository repository;

	public List<Comment> findCommentsByPostId(String postId) {
		return repository.findCommentsByPostId(postId);
	}

	public Comment createComment (Comment comment) {
		return repository.save(comment);
	}

	public Comment updateComment(Comment comment, String userId) {
		Optional<Comment> commentFromDb = repository.findById(comment.getId());
		if(!comment.getAuthorId().equals(userId)) {
			throw new AuthorizationServiceException("updating a comment by another user is not permitted");
		}
		if(commentFromDb.isPresent()) {
			LocalDateTime dateFromDb = LocalDateTime.parse(commentFromDb.get().getCreationTs());
			Long hours = dateFromDb.until(LocalDateTime.now(), ChronoUnit.HOURS);
			if(hours < 1) {
				return repository.save(comment);
			} else {
				throw new IllegalArgumentException("Comment can no longer be updated");
			}
		} else {
			return repository.save(comment);
		}
	}

	public void deleteComment(String commentId, String userId) {
		Optional<Comment> commentFromDb = repository.findById(commentId);
		if(commentFromDb.isPresent()) {
			if(!commentFromDb.get().getAuthorId().equals(userId)) {
				throw new AuthorizationServiceException("updating a comment by another user is not permitted");
			}
			repository.deleteById(commentId);
		}
	}

	public void deleteCommentByPostId(String postId) {
		repository.deleteCommentsByPostId(postId);
	}
}
