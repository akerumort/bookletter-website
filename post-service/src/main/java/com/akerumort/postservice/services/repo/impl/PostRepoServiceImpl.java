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

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class PostRepoServiceImpl implements PostRepoService {
    private final PostRepository postRepository;

    @Override
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
    public Post findById(Long id) {
        Optional<Post> post = postRepository.findById(id);
        return post.orElseThrow(() -> new PostNotFoundException("Post with id " + id + " not found"));
    }

    @Override
    public List<Post> findAll() {
        try {
            return postRepository.findAll();
        } catch (DataAccessException e) {
            log.error("Error retrieving posts: {}", e.getMessage());
            throw new DatabaseAccessException("Failed to retrieve posts");
        }
    }

    @Override
    public void deleteById(Long id) {
        try {
            postRepository.deleteById(id);
        } catch (DataAccessException e) {
            log.error("Error deleting post: {}", e.getMessage());
            throw new DatabaseAccessException("Failed to delete post");
        }
    }
}
