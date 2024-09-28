package com.akerumort.postservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostCreateDto {
    private Long userId;
    private String title;
    private String content;
}