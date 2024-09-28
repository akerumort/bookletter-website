package com.akerumort.postservice.mappers;

import com.akerumort.postservice.dto.PostCreateDto;
import com.akerumort.postservice.dto.PostResponseDto;
import com.akerumort.postservice.entities.Post;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PostMapper {
    Post toEntity(PostCreateDto dto);

    PostResponseDto toDto(Post entity);
}
