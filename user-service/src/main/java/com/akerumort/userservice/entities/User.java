package com.akerumort.userservice.entities;

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
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
}
