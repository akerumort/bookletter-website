package com.akerumort.userservice.dto;

import com.akerumort.userservice.entities.enums.Role;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateDTO {

    @NotNull(message = "Username is required")
    private String username;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotNull(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    @Size(max = 1024, message = "Bio can't be longer than 1024 characters")
    private String bio;

    @Size(max = 10, message = "Gender can't be longer than 10 characters")
    private String gender;

    @Min(value = 0, message = "Age can't be negative")
    @Max(value = 150, message = "Age can't be more than 150")
    private Integer age;

    @Size(max = 255, message = "City can't be longer than 255 characters")
    private String city;

    @Size(max = 255, message = "Country can't be longer than 255 characters")
    private String country;

    private Role role;
}