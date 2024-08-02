package com.akerumort.userservice.controllers;

import com.akerumort.userservice.dto.UserCreateDTO;
import com.akerumort.userservice.dto.UserDTO;
import com.akerumort.userservice.entities.User;
import com.akerumort.userservice.mappers.UserMapper;
import com.akerumort.userservice.services.UserService;
import com.akerumort.userservice.utils.JwtUtil;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger logger = LogManager.getLogger(UserService.class);

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping
    public List<UserDTO> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<User> users = userService.getAllUsers(page, size);
        return users.stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public UserDTO getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return userMapper.toDTO(user);
    }

    @PostMapping
    public UserDTO createUser(@Valid @RequestBody UserCreateDTO userCreateDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new RuntimeException(bindingResult.getAllErrors().toString());
        }
        User user = userMapper.toEntity(userCreateDTO);
        User savedUser = userService.saveUser(user);
        return userMapper.toDTO(savedUser);
    }

    @PutMapping("/{id}")
    public UserDTO updateUser(@PathVariable Long id, @Valid @RequestBody UserCreateDTO userCreateDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new RuntimeException(bindingResult.getAllErrors().toString());
        }
        User user = userMapper.toEntity(userCreateDTO);
        user.setId(id);
        User updatedUser = userService.saveUser(user);
        return userMapper.toDTO(updatedUser);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
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
        if (bindingResult.hasErrors()) {
            throw new RuntimeException(bindingResult.getAllErrors().toString());
        }
        User user = userService.findByUsername(userCreateDTO.getUsername());
        if (user != null) {
            logger.info("User found: " + user.getUsername());
            boolean passwordMatch = userService.checkPassword(userCreateDTO.getPassword(), user.getPassword());
            if (passwordMatch) {
                String token = jwtUtil.generateToken(user.getUsername());
                Map<String, String> response = new HashMap<>();
                response.put("token", token);
                return ResponseEntity.ok(response);
            } else {
                logger.info("Password does not match");
            }
        } else {
            logger.info("User not found");
        }
        return ResponseEntity.status(401).body(null);
    }

    @GetMapping("/profile")
    public ResponseEntity<UserDTO> getUserProfile(@RequestHeader("Authorization") String token) {
        String jwtToken = token.substring(7);
        String username = jwtUtil.extractUsername(jwtToken);
        User user = userService.findByUsername(username);
        return ResponseEntity.ok(userMapper.toDTO(user));
    }

    @PutMapping("/profile")
    public UserDTO updateProfile(@Valid @RequestBody UserCreateDTO userCreateDTO, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            throw new RuntimeException(bindingResult.getAllErrors().toString());
        }
        User currentUser = userService.findByUsername(principal.getName());
        User updatedUser = userMapper.toEntity(userCreateDTO);
        updatedUser.setId(currentUser.getId());
        User savedUser = userService.updateUserProfile(currentUser.getId(), updatedUser);
        return userMapper.toDTO(savedUser);
    }
}