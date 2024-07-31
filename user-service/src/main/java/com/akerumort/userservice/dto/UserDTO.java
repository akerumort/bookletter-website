package com.akerumort.userservice.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {

    private Long id;

    @NotBlank(message = "Username is required")
    @Size(max = 255, message = "Username can't be longer than 255 characters")
    private String username;

    @NotBlank(message = "First name is required")
    @Size(max = 255, message = "First name can't be longer than 255 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 255, message = "Last name can't be longer than 255 characters")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Size(max = 255, message = "Email can't be longer than 255 characters")
    private String email;
}
