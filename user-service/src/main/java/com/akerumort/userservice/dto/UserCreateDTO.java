package com.akerumort.userservice.dto;

import com.akerumort.userservice.entities.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "DTO for creating a new user")
public class UserCreateDTO {

    @NotNull(message = "Username is required")
    @Schema(description = "Username of the user", example = "ivan_ivanov")
    private String username;

    @NotBlank(message = "First name is required")
    @Schema(description = "First name of the user", example = "Ivan")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Schema(description = "Last name of the user", example = "Ivanov")
    private String lastName;

    @NotNull(message = "Email is required")
    @Schema(description = "Email of the user", example = "ivanov@example.com")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Schema(description = "Password of the user", example = "password123")
    private String password;

    @Size(max = 1024, message = "Bio can't be longer than 1024 characters")
    @Schema(description = "Biography of the user", example = "A short bio about the user")
    private String bio;

    @Size(max = 10, message = "Gender can't be longer than 10 characters")
    @Schema(description = "Gender of the user", example = "Male")
    private String gender;

    @Min(value = 0, message = "Age can't be negative")
    @Max(value = 150, message = "Age can't be more than 150")
    @Schema(description = "Age of the user", example = "20")
    private Integer age;

    @Size(max = 255, message = "City can't be longer than 255 characters")
    @Schema(description = "City where the user lives", example = "Moscow")
    private String city;

    @Size(max = 255, message = "Country can't be longer than 255 characters")
    @Schema(description = "Country where the user lives", example = "Russia")
    private String country;

    @Schema(description = "Role of the user", example = "ROLE_USER")
    private Role role;
}