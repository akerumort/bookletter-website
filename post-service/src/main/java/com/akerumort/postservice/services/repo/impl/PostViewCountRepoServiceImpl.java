package com.akerumort.postservice.services.repo.impl;

import com.akerumort.postservice.entities.PostViewCount;
import com.akerumort.postservice.repos.PostViewCountRepository;
import com.akerumort.postservice.services.repo.PostViewCountRepoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Log4j2
public class PostViewCountRepoServiceImpl implements PostViewCountRepoService {
    private final PostViewCountRepository postViewCountRepository;

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "viewCounts", key = "#postId")
    public PostViewCount findViewCountById(Long postId) {
        return postViewCountRepository.findById(postId)
                .orElseGet(() -> {
                    PostViewCount postViewCount = new PostViewCount();
                    postViewCount.setPostId(postId);
                    postViewCount.setViewCount(0);
                    return postViewCount;
                });
    }

    @Override
    @Transactional
    @CachePut(value = "viewCounts", key = "#postViewCount.postId")
    public PostViewCount saveViewCount(PostViewCount postViewCount) {
        return postViewCountRepository.save(postViewCount);
    }
}
