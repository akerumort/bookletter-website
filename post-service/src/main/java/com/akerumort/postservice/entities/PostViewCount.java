package com.akerumort.postservice.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "post_view_count")
@Schema(description = "Entity representing the view count of a post")
public class PostViewCount implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID of the view count record", example = "1")
    private Long id;

    @Column(name = "post_id", nullable = false)
    @Schema(description = "ID of the post", example = "1")
    private Long postId;

    @Column(name = "view_count", nullable = false)
    @Schema(description = "Number of views", example = "10")
    private int viewCount = 0;
}
