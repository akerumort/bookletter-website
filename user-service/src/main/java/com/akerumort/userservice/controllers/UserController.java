package com.akerumort.userservice.controllers;

import com.akerumort.userservice.dto.UserCreateDTO;
import com.akerumort.userservice.dto.UserDTO;
import com.akerumort.userservice.entities.User;
import com.akerumort.userservice.mappers.UserMapper;
import com.akerumort.userservice.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

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

    @GetMapping("/profile")
    public UserDTO getProfile(Principal principal) {
        User user = userService.findByUsername(principal.getName());
        return userMapper.toDTO(user);
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