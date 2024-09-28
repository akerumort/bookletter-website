package com.akerumort.postservice.controllers;

import com.akerumort.postservice.dto.PostCreateDto;
import com.akerumort.postservice.dto.PostResponseDto;
import com.akerumort.postservice.services.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    @GetMapping
    public ResponseEntity<List<PostResponseDto>> getPosts() {
        return new ResponseEntity<>(postService.getAllPosts(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDto> getPost(@PathVariable Long id) {
        PostResponseDto postResponseDto = postService.getPost(id);
        return new ResponseEntity<>(postResponseDto, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<PostResponseDto> createPost(@RequestBody PostCreateDto postCreateDto) {
        return new ResponseEntity<>(postService.createPost(postCreateDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostResponseDto> updatePost(@PathVariable Long id, @RequestBody PostCreateDto postCreateDto) {
        PostResponseDto post = postService.updatePost(id, postCreateDto);
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
