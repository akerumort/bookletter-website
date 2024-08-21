package com.akerumort.userservice.controllers;

import com.akerumort.userservice.dto.UserCreateDTO;
import com.akerumort.userservice.dto.UserDTO;
import com.akerumort.userservice.entities.User;
import com.akerumort.userservice.entities.enums.Role;
import com.akerumort.userservice.exceptions.CustomValidationException;
import com.akerumort.userservice.mappers.UserMapper;
import com.akerumort.userservice.services.UserService;
import com.akerumort.userservice.utils.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@Tag(name = "User Controller", description = "Management of users")
public class UserController {

    private static final Logger logger = LogManager.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Operation(summary = "Get all users", description = "Allows retrieving a list of all users. " +
            "Accessible only to administrators.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of users retrieved"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping
    public List<UserDTO> getAllUsers(Principal principal,
                                     @RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int size) {
        User currentUser = userService.findByUsername(principal.getName());

        if (currentUser.getRole() == Role.ROLE_ADMIN) {
            List<User> users = userService.getAllUsers(page, size);
            return users.stream()
                    .map(userMapper::toDTO)
                    .collect(Collectors.toList());
        } else {
            throw new CustomValidationException("Access denied");
        }
    }

    @Operation(summary = "Get user by ID", description = "Allows retrieving a user by their ID." +
            " Accessible to administrators and the profile owner.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User retrieved"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{id}")
    public UserDTO getUserById(@PathVariable Long id, Principal principal) {
        User currentUser = userService.findByUsername(principal.getName());
        User user = userService.getUserById(id);

        // for ROLE_ADMIN and auth user
        if (currentUser.getRole() == Role.ROLE_ADMIN || currentUser.getId().equals(id)) {
            return userMapper.toDTO(user);
        } else {
            throw new CustomValidationException("Access denied");
        }
    }

    @Operation(summary = "Create a new user", description = "Allows creating a new user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created"),
            @ApiResponse(responseCode = "400", description = "Invalid data")
    })
    @PostMapping
    public UserDTO createUser(@Valid @RequestBody UserCreateDTO userCreateDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new CustomValidationException(bindingResult.getAllErrors().toString());
        }
        User user = userMapper.toEntity(userCreateDTO);
        User savedUser = userService.saveUser(user);
        return userMapper.toDTO(savedUser);
    }

    @Operation(summary = "Update an existing user", description = "Allows updating the details of a user. " +
            "Accessible to administrators and the profile owner.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "400", description = "Invalid data")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id,
                                              @Valid @RequestBody UserCreateDTO userCreateDTO,
                                              BindingResult bindingResult,
                                              Principal principal) {
        if (bindingResult.hasErrors()) {
            throw new CustomValidationException(bindingResult.getAllErrors().toString());
        }

        User currentUser = userService.findByUsername(principal.getName());
        User userToUpdate = userService.getUserById(id);

        if (currentUser.getRole() == Role.ROLE_ADMIN || currentUser.getId().equals(id)) {
            userToUpdate = userMapper.toEntity(userCreateDTO);
            userToUpdate.setId(id);
            User updatedUser = userService.saveUser(userToUpdate);

            String newToken = jwtUtil.generateToken(updatedUser.getUsername());

            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + newToken);

            return ResponseEntity.ok().headers(headers).body(userMapper.toDTO(updatedUser));
        } else {
            throw new CustomValidationException("Access denied");
        }
    }

    @Operation(summary = "Delete a user", description = "Allows deleting a user." +
            " Accessible to administrators and the profile owner.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id, Principal principal) {
        User currentUser = userService.findByUsername(principal.getName());
        User userToDelete = userService.getUserById(id);

        if (currentUser.getRole() == Role.ROLE_ADMIN || currentUser.getId().equals(id)) {
            userService.deleteUser(id);
        } else {
            throw new CustomValidationException("Access denied");
        }
    }

    @Operation(summary = "Register a new user", description = "Allows registering a new user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered"),
            @ApiResponse(responseCode = "400", description = "Invalid data")
    })
    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@Valid @RequestBody UserCreateDTO userCreateDTO,
                                                BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new RuntimeException(bindingResult.getAllErrors().toString());
        }
        User user = userMapper.toEntity(userCreateDTO);
        User savedUser = userService.saveUser(user);
        return ResponseEntity.ok(userMapper.toDTO(savedUser));
    }

    @Operation(summary = "User login", description = "Allows a user to log in.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User logged in"),
            @ApiResponse(responseCode = "401", description = "Invalid login credentials")
    })
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(@Valid @RequestBody UserCreateDTO userCreateDTO,
                                                         BindingResult bindingResult) {
        Map<String, String> response = userService.loginUser(userCreateDTO, bindingResult);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "View profile", description = "Allows retrieving the profile of the current user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile retrieved"),
            @ApiResponse(responseCode = "401", description = "User not authenticated")
    })
    @GetMapping("/profile")
    public ResponseEntity<UserDTO> getUserProfile(@RequestHeader("Authorization") String token) {
        String jwtToken = token.substring(7);
        String username = jwtUtil.extractUsername(jwtToken);
        User user = userService.findByUsername(username);
        return ResponseEntity.ok(userMapper.toDTO(user));
    }

    @Operation(summary = "Update profile", description = "Allows updating the profile of the current user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile updated"),
            @ApiResponse(responseCode = "400", description = "Invalid data")
    })
    @PutMapping("/profile")
    public ResponseEntity<UserDTO> updateProfile(@Valid @RequestBody UserCreateDTO userCreateDTO,
                                                 BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            throw new CustomValidationException(bindingResult.getAllErrors().toString());
        }
        User currentUser = userService.findByUsername(principal.getName());
        User updatedUser = userMapper.toEntity(userCreateDTO);
        updatedUser.setId(currentUser.getId());
        User savedUser = userService.saveUser(updatedUser);

        String newToken = jwtUtil.generateToken(savedUser.getUsername());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + newToken);

        return ResponseEntity.ok().headers(headers).body(userMapper.toDTO(savedUser));
    }

    @Operation(summary = "Delete profile", description = "Allows deleting the profile of the current user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Profile deleted"),
            @ApiResponse(responseCode = "401", description = "User not authenticated")
    })
    @DeleteMapping("/profile")
    public ResponseEntity<Void> deleteProfile(Principal principal) {
        userService.deleteUserByUsername(principal.getName());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Logout", description = "Allows the current user to log out.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User logged out"),
            @ApiResponse(responseCode = "400", description = "Logout error")
    })
    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        Map<String, String> response = new HashMap<>();

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            String username = jwtUtil.extractUsername(token);

            logger.info("User with username {} is logging out", username);
            jwtUtil.invalidateToken(token);
            logger.info("Token for user {} has been invalidated", username);

            response.put("message", "Successfully logged out");
            return ResponseEntity.ok(response);
        } else {
            logger.warn("Logout attempt without token");
            response.put("error", "Logout attempt without token");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}