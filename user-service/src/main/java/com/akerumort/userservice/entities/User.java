package com.akerumort.userservice.entities;

import com.akerumort.userservice.entities.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "\"user\"")
@Schema(description = "User entity representing the user data")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Username is required")
    @Size(max = 255, message = "Username can't be longer than 255 characters")
    @Schema(description = "ID of the user", example = "1")
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Schema(description = "Role of the user", example = "ROLE_USER")
    private Role role = Role.ROLE_USER;
}
