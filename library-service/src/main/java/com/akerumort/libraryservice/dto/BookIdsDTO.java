package com.akerumort.libraryservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Schema(description = "DTO for passing a set of book IDs")
public class BookIdsDTO {

    @NotEmpty(message = "Book IDs cannot be null")
    @Schema(description = "Set of book IDs", example = "[1, 2, 3]")
    private Set<Long> bookIds;
}