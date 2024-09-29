package com.akerumort.postservice.services.repo.impl;

import com.akerumort.postservice.entities.Post;
import com.akerumort.postservice.exceptions.DatabaseAccessException;
import com.akerumort.postservice.exceptions.InvalidPostException;
import com.akerumort.postservice.exceptions.PostNotFoundException;
import com.akerumort.postservice.repos.PostRepository;
import com.akerumort.postservice.services.repo.PostRepoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class PostRepoServiceImpl implements PostRepoService {
    private final PostRepository postRepository;

    @Override
    @Transactional(readOnly = true)
    public Post findById(Long id) {
        Optional<Post> post = postRepository.findById(id);
        return post.orElseThrow(() -> new PostNotFoundException("Post with id " + id + " not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Post> findAll() {
        try {
            return postRepository.findAll();
        } catch (DataAccessException e) {
            log.error("Error retrieving posts: {}", e.getMessage());
            throw new DatabaseAccessException("Failed to retrieve posts");
        }
    }

    @Override
    @Transactional
    public Post savePost(Post post) {
        if (post.getTitle() == null || post.getContent() == null) {
            throw new InvalidPostException("Post title and content must not be null");
        }
        try {
            return postRepository.save(post);
        } catch (DataAccessException e) {
            log.error("Error saving post: " + e.getMessage());
            throw new DatabaseAccessException("Failed to save post");
        }

    }

    @Override
    @Transactional
    public Post updatePost(Long id, Post post) {
        if (post.getTitle() == null || post.getContent() == null) {
            throw new InvalidPostException("Post title and content must not be null");
        }
        try {
            Post existingPost = findById(id);
            existingPost.setTitle(post.getTitle());
            existingPost.setContent(post.getContent());
            existingPost.setUpdatedAt(LocalDateTime.now());
            return postRepository.saveAndFlush(existingPost);
        } catch (DataAccessException e) {
            log.error("Error updating post: " + e.getMessage());
            throw new DatabaseAccessException("Failed to update post");
        }
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        try {
            postRepository.deleteById(id);
        } catch (DataAccessException e) {
            log.error("Error deleting post: {}", e.getMessage());
            throw new DatabaseAccessException("Failed to delete post");
        }
    }
}
