package com.akerumort.libraryservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Schema(description = "DTO for Book entity")
public class BookDTO {

    @Schema(description = "Book ID", example = "1")
    private Long id;

    @NotBlank(message = "Title is required")
    @Size(min = 1, message = "Title must not be empty")
    @Schema(description = "Title of the book", example = "Java. The Complete Reference")
    private String title;

    @NotBlank(message = "Genre is required")
    @Size(min = 1, message = "Genre must not be empty")
    @Schema(description = "Genre of the book", example = "Programming")
    private String genre;

    @Positive(message = "Publication year must be positive")
    @NotNull(message = "Publication year cannot be null")
    @Schema(description = "Publication year of the book", example = "2023")
    private Integer publicationYear;

    @NotNull(message = "Author IDs cannot be null")
    @Schema(description = "Set of author IDs associated with the book")
    private Set<Long> authorIds;
}
