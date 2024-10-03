package com.akerumort.userservice.dto;

import com.akerumort.userservice.entities.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "DTO for user information")
public class UserResponseDTO {

    private Long id;

    @NotBlank(message = "Username is required")
    @Size(max = 255, message = "Username can't be longer than 255 characters")
    @Schema(description = "Username of the user", example = "ivan_ivanov")
    private String username;

    @NotBlank(message = "First name is required")
    @Size(max = 255, message = "First name can't be longer than 255 characters")
    @Schema(description = "First name of the user", example = "Ivan")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 255, message = "Last name can't be longer than 255 characters")
    @Schema(description = "Last name of the user", example = "Ivanov")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Size(max = 255, message = "Email can't be longer than 255 characters")
    @Schema(description = "Email of the user", example = "ivanov@example.com")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Schema(description = "Password of the user", example = "password123")
    private String password;

    @Schema(description = "Role of the user", example = "ROLE_USER")
    private Role role;
}
