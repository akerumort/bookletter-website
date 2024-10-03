package com.akerumort.postservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Schema(description = "DTO for creating a new post")
public class PostCreateDto implements Serializable {

    @NotNull(message = "User ID cannot be null")
    @Schema(description = "ID of the user creating new post",example = "1")
    private Long userId;

    @NotBlank(message = "Title cannot be blank")
    @Schema(description = "Title of the post", example = "My First Post")
    private String title;

    @NotBlank(message = "Content cannot be blank")
    @Schema(description = "Content of the post", example = "This is the content of my first post")
    private String content;
}
