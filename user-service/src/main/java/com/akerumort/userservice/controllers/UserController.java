package com.akerumort.userservice.controllers;

import com.akerumort.userservice.dto.UserCreateDTO;
import com.akerumort.userservice.dto.UserDTO;
import com.akerumort.userservice.entities.User;
import com.akerumort.userservice.entities.enums.Role;
import com.akerumort.userservice.exceptions.CustomValidationException;
import com.akerumort.userservice.mappers.UserMapper;
import com.akerumort.userservice.services.UserService;
import com.akerumort.userservice.utils.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtUtil jwtUtil;

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

    @PostMapping
    public UserDTO createUser(@Valid @RequestBody UserCreateDTO userCreateDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new CustomValidationException(bindingResult.getAllErrors().toString());
        }
        User user = userMapper.toEntity(userCreateDTO);
        User savedUser = userService.saveUser(user);
        return userMapper.toDTO(savedUser);
    }

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

    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@Valid @RequestBody UserCreateDTO userCreateDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new RuntimeException(bindingResult.getAllErrors().toString());
        }
        User user = userMapper.toEntity(userCreateDTO);
        User savedUser = userService.saveUser(user);
        return ResponseEntity.ok(userMapper.toDTO(savedUser));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(@Valid @RequestBody UserCreateDTO userCreateDTO, BindingResult bindingResult) {
        Map<String, String> response = userService.loginUser(userCreateDTO, bindingResult);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/profile")
    public ResponseEntity<UserDTO> getUserProfile(@RequestHeader("Authorization") String token) {
        String jwtToken = token.substring(7);
        String username = jwtUtil.extractUsername(jwtToken);
        User user = userService.findByUsername(username);
        return ResponseEntity.ok(userMapper.toDTO(user));
    }

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

    @DeleteMapping("/profile")
    public ResponseEntity<Void> deleteProfile(Principal principal) {
        userService.deleteUserByUsername(principal.getName());
        return ResponseEntity.noContent().build();
    }
}