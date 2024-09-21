package com.akerumort.libraryservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Schema(description = "DTO for Author entity")
public class AuthorDTO {

    @Schema(description = "Author ID", example = "1")
    private Long id;

    @NotBlank(message = "First name is required")
    @Size(min = 1, message = "First name must not be empty")
    @Schema(description = "First name of the author", example = "Ivan")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 1, message = "Last name must not be empty")
    @Schema(description = "Last name of the author", example = "Ivanov")
    private String lastName;

    @NotBlank(message = "Country is required")
    @Size(min = 1, message = "Country must not be empty")
    @Schema(description = "Country of the author", example = "Russia")
    private String country;

    @Schema(description = "Set of book IDs associated with the author")
    private Set<Long> bookIds = new HashSet<>();;
}
