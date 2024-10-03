package com.akerumort.postservice.controllers;

import com.akerumort.postservice.dto.PostCreateDto;
import com.akerumort.postservice.dto.PostResponseDto;
import com.akerumort.postservice.services.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
@Tag(name = "Post Controller", description = "Controller for managing posts")
public class PostController {
    private final PostService postService;

    @Operation(summary = "Get all posts", description = "Returns a list of all posts")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Successful operation",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = PostResponseDto.class)))),
            @ApiResponse(responseCode = "404",
                    description = "Posts not found",
                    content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<PostResponseDto>> getPosts() {
        return new ResponseEntity<>(postService.getAllPosts(), HttpStatus.OK);
    }

    @Operation(summary = "Get post by ID", description = "Returns a single post by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = PostResponseDto.class))),
            @ApiResponse(responseCode = "404",
                    description = "Post not found",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDto> getPost(@PathVariable Long id) {
        PostResponseDto postResponseDto = postService.getPost(id);
        return new ResponseEntity<>(postResponseDto, HttpStatus.OK);
    }

    @Operation(summary = "Create a new post", description = "Creates a new post")
    @ApiResponses({
            @ApiResponse(responseCode = "201",
                    description = "Post created",
                    content = @Content(schema = @Schema(implementation = PostResponseDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "Invalid input",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<PostResponseDto> createPost(@Valid @RequestBody PostCreateDto postCreateDto) {
        return new ResponseEntity<>(postService.createPost(postCreateDto), HttpStatus.CREATED);
    }

    @Operation(summary = "Update an existing post", description = "Updates an existing post by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = PostResponseDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "Invalid input",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "Post not found",
                    content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<PostResponseDto> updatePost(@PathVariable Long id,
                                                      @Valid @RequestBody PostCreateDto postCreateDto) {
        PostResponseDto post = postService.updatePost(id, postCreateDto);
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    @Operation(summary = "Delete a post", description = "Deletes a post by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204",
                    description = "Post deleted",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "Post not found",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
