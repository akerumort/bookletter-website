package com.akerumort.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {

    private Long id;

    @NotBlank(message = "Username is required")
    @Size(max = 255, message = "Username can't be longer than 255 characters")
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Size(max = 255, message = "Email can't be longer than 255 characters")
    private String email;

    @Size(max = 1024, message = "Bio can't be longer than 1024 characters")
    private String bio;
}
