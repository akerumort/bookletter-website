package com.akerumort.postservice.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Schema(description = "Data Transfer Object for retrieving a post")
public class PostResponseDto implements Serializable {

    @Schema(description = "ID of the post", example = "1")
    private Long id;

    @Schema(description = "ID of the user who created the post", example = "1")
    private Long userId;

    @Schema(description = "Title of the post", example = "My First Post")
    private String title;

    @Schema(description = "Content of the post", example = "Some content text here")
    private String content;

    @Schema(description = "Date and time when the post was created", example = "2024-10-03T12:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "Date and time when the post was last updated", example = "2024-10-03T12:00:00")
    private LocalDateTime updatedAt;

    @Schema(description = "Number of views of the post", example = "10")
    private int viewCount;
}
