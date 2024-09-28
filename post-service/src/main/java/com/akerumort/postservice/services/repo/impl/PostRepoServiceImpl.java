package com.akerumort.postservice.services.repo.impl;

import com.akerumort.postservice.entities.Post;
import com.akerumort.postservice.repos.PostRepository;
import com.akerumort.postservice.services.repo.PostRepoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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
        return postRepository.save(post);
    }

    @Override
    public Post findById(Long id) {
        Optional<Post> post = postRepository.findById(id);
        return post.orElse(null); // добавить сюда исключение позже
    }

    @Override
    public List<Post> findAll() {
        return postRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        postRepository.deleteById(id);
    }
}
