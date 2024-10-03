package com.akerumort.postservice.services;

import com.akerumort.postservice.dto.PostCreateDto;
import com.akerumort.postservice.dto.PostResponseDto;
import com.akerumort.postservice.entities.Post;
import com.akerumort.postservice.entities.PostViewCount;
import com.akerumort.postservice.mappers.PostMapper;
import com.akerumort.postservice.services.repo.PostRepoService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

    @Mock
    private PostRepoService postRepoService;

    @Mock
    private PostViewCountService postViewCountService;

    @Mock
    private PostMapper postMapper;

    @InjectMocks
    private PostService postService;

    private Post post;
    private PostResponseDto postResponseDto;
    private PostCreateDto postCreateDto;
    private PostViewCount postViewCount;

    @BeforeEach
    void setUp() {
        post = new Post();
        post.setId(1L);
        post.setTitle("Test Post");
        post.setContent("This is a test post.");
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());

        postViewCount = new PostViewCount();
        postViewCount.setPostId(1L);
        postViewCount.setViewCount(10);

        postCreateDto = new PostCreateDto();
        postCreateDto.setTitle("Test Post");
        postCreateDto.setContent("This is a test post.");

        postResponseDto = new PostResponseDto();
        postResponseDto.setId(1L);
        postResponseDto.setTitle("Test Post");
        postResponseDto.setContent("This is a test post.");
        postResponseDto.setViewCount(10);
    }

    @Test
    void testGetPost() {
        when(postRepoService.findById(anyLong())).thenReturn(post);
        when(postViewCountService.incrementViewCount(anyLong())).thenReturn(postViewCount);
        when(postMapper.toDto(any(Post.class))).thenReturn(postResponseDto);

        PostResponseDto result = postService.getPost(1L);

        assertNotNull(result);
        assertEquals(10, result.getViewCount());
        verify(postRepoService, times(1)).findById(1L);
        verify(postViewCountService, times(1)).incrementViewCount(1L);
    }

    @Test
    void testGetAllPosts() {
        List<Post> posts = Collections.singletonList(post);
        when(postRepoService.findAll()).thenReturn(posts);
        when(postMapper.toDto(any(Post.class))).thenReturn(postResponseDto);
        when(postViewCountService.getViewCount(anyLong())).thenReturn(10);

        List<PostResponseDto> result = postService.getAllPosts();

        assertEquals(1, result.size());
        assertEquals(10, result.get(0).getViewCount());
        verify(postRepoService, times(1)).findAll();
    }

    @Test
    void testCreatePost() {
        when(postMapper.toEntity(any(PostCreateDto.class))).thenReturn(post);
        when(postRepoService.savePost(any(Post.class))).thenReturn(post);
        when(postMapper.toDto(any(Post.class))).thenReturn(postResponseDto);

        PostResponseDto result = postService.createPost(postCreateDto);

        assertNotNull(result);
        verify(postRepoService, times(1)).savePost(any(Post.class));
    }

    @Test
    void testUpdatePost() {
        when(postMapper.toEntity(any(PostCreateDto.class))).thenReturn(post);
        when(postRepoService.updatePost(anyLong(), any(Post.class))).thenReturn(post);
        when(postMapper.toDto(any(Post.class))).thenReturn(postResponseDto);

        PostResponseDto result = postService.updatePost(1L, postCreateDto);

        assertNotNull(result);
        verify(postRepoService, times(1)).updatePost(1L, post);
    }

    @Test
    void testDeletePost() {
        doNothing().when(postRepoService).deleteById(anyLong());

        postService.deletePost(1L);

        verify(postRepoService, times(1)).deleteById(1L);
    }
}
