package com.akerumort.postservice.mappers;

import com.akerumort.postservice.dto.PostCreateDto;
import com.akerumort.postservice.dto.PostResponseDto;
import com.akerumort.postservice.entities.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PostMapper {
    Post toEntity(PostCreateDto dto);

    @Mapping(target = "viewCount", source = "entity.viewCount")
    PostResponseDto toDto(Post entity);
}
