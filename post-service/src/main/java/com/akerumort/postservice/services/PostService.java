package com.akerumort.postservice.services;

import com.akerumort.postservice.dto.PostCreateDto;
import com.akerumort.postservice.dto.PostResponseDto;
import com.akerumort.postservice.entities.Post;
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
    private final PostMapper postMapper;

    public PostResponseDto getPost(Long id) {
        Post post = postRepoService.findById(id);
        return postMapper.toDto(post);
    }

    public List<PostResponseDto> getAllPosts() {
        List<Post> posts = postRepoService.findAll();
        return posts.stream().map(postMapper::toDto).collect(Collectors.toList());
    }


    public PostResponseDto createPost(PostCreateDto postCreateDto) {
        Post post = postMapper.toEntity(postCreateDto);
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());
        return postMapper.toDto(postRepoService.savePost(post));
    }

    public PostResponseDto updatePost(Long id, PostCreateDto postCreateDto) {
        Post post = postRepoService.findById(id);
        post.setTitle(postCreateDto.getTitle());
        post.setContent(postCreateDto.getContent());
        post.setUpdatedAt(LocalDateTime.now());
        return postMapper.toDto(postRepoService.savePost(post));
    }

    public void deletePost(Long id) {
        postRepoService.deleteById(id);
    }
}
