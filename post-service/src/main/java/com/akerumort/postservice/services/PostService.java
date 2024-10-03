package com.akerumort.postservice.services;

import com.akerumort.postservice.dto.PostCreateDto;
import com.akerumort.postservice.dto.PostResponseDto;
import com.akerumort.postservice.entities.Post;
import com.akerumort.postservice.entities.PostViewCount;
import com.akerumort.postservice.mappers.PostMapper;
import com.akerumort.postservice.services.repo.PostRepoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class PostService {
    private final PostRepoService postRepoService;
    private final PostViewCountService postViewCountService;
    private final PostMapper postMapper;

    public PostResponseDto getPost(Long id) {
        log.info("Fetching post with id {}", id);
        Post post = postRepoService.findById(id);
        PostViewCount postViewCount = postViewCountService.incrementViewCount(id);
        PostResponseDto postResponseDto = postMapper.toDto(post);
        postResponseDto.setViewCount(postViewCount.getViewCount());
        return postResponseDto;
    }

    public List<PostResponseDto> getAllPosts() {
        log.info("Fetching all posts");
        List<Post> posts = postRepoService.findAll();
        return posts.stream()
                .map(post -> {
                    PostResponseDto dto = postMapper.toDto(post);
                    dto.setViewCount(postViewCountService.getViewCount(post.getId()));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public PostResponseDto createPost(PostCreateDto postCreateDto) {
        log.info("Creating a new post with title {}", postCreateDto.getTitle());
        Post post = postMapper.toEntity(postCreateDto);
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());
        return postMapper.toDto(postRepoService.savePost(post));
    }

    public PostResponseDto updatePost(Long id, PostCreateDto postCreateDto) {
        log.info("Updating post with id {}", id);
        Post post = postMapper.toEntity(postCreateDto);
        return postMapper.toDto(postRepoService.updatePost(id, post));
    }

    public void deletePost(Long id) {
        log.info("Deleting post with id {}", id);
        postRepoService.deleteById(id);
    }
}
