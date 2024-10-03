package com.akerumort.postservice.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "post")
@Schema(description = "Entity representing a post")
public class Post implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID of the post", example = "1")
    private Long id;

    @Column(name = "user_id", nullable = false)
    @Schema(description = "ID of the user who created the post", example = "1")
    private Long userId;

    @Column(nullable = false)
    @Schema(description = "Title of the post", example = "My First Post")
    private String title;

    @Column(nullable = false)
    @Schema(description = "Content of the post", example = "Some content text here")
    private String content;

    @Column(name = "created_at", nullable = false)
    @Schema(description = "Date and time when the post was created", example = "2024-10-03T12:00:00")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    @Schema(description = "Date and time when the post was last updated", example = "2024-10-03T12:00:00")
    private LocalDateTime updatedAt;
}
