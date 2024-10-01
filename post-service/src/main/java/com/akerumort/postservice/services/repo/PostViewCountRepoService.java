package com.akerumort.postservice.services.repo;

import com.akerumort.postservice.entities.PostViewCount;
import org.springframework.stereotype.Service;

@Service
public interface PostViewCountRepoService {
    PostViewCount findViewCountById(Long postId);
    PostViewCount saveViewCount(PostViewCount postViewCount);
}
