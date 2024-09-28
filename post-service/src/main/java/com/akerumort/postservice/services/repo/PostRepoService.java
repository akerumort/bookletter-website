package com.akerumort.postservice.services.repo;

import com.akerumort.postservice.entities.Post;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PostRepoService {
    Post savePost(Post post);
    Post findById(Long id);
    List<Post> findAll();
    void deleteById(Long id);
}
