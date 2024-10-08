package com.akerumort.userservice.services;

import com.akerumort.userservice.dto.UserCreateDTO;
import com.akerumort.userservice.entities.User;
import com.akerumort.userservice.entities.enums.Role;
import com.akerumort.userservice.exceptions.CustomValidationException;
import com.akerumort.userservice.repos.UserRepository;
import com.akerumort.userservice.utils.JwtUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    private static final Logger logger = LogManager.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public List<User> getAllUsers(int page, int size) {
        logger.info("Fetching users with pagination");
        Pageable pageable = PageRequest.of(page, size);
        Page<User> userPage = userRepository.findAll(pageable);
        return userPage.getContent();
    }

    public User getUserById(Long id) {
        logger.info("Fetching user by id: " + id);
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User saveUser(User user) {
        if (user.getId() == null) {
            if (userRepository.findByUsername(user.getUsername()) != null) {
                throw new CustomValidationException("Username already exists.");
            }
            if (userRepository.findByEmail(user.getEmail()).isPresent()) {
                throw new CustomValidationException("Email already exists.");
            }
            if (user.getRole() == null) {
                user.setRole(Role.ROLE_USER);
            }
        } else {
            User existingUser = userRepository.findById(user.getId()).orElseThrow(() -> new RuntimeException("User not found"));
            if (!existingUser.getUsername().equals(user.getUsername()) && userRepository.findByUsername(user.getUsername()) != null) {
                throw new CustomValidationException("Username already exists.");
            }
            if (!existingUser.getEmail().equals(user.getEmail()) && userRepository.findByEmail(user.getEmail()).isPresent()) {
                throw new CustomValidationException("Email already exists.");
            }
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        logger.info("Saving user with encoded password: " + user.getPassword());
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        logger.info("Deleting user by id: " + id);
        userRepository.deleteById(id);
    }

    public User updateUserProfile(Long id, User updatedUser) {
        User user = getUserById(id);
        if (user != null) {
            user.setFirstName(updatedUser.getFirstName());
            user.setLastName(updatedUser.getLastName());
            user.setEmail(updatedUser.getEmail());
            user.setBio(updatedUser.getBio());
            user.setGender(updatedUser.getGender());
            user.setAge(updatedUser.getAge());
            user.setCity(updatedUser.getCity());
            user.setCountry(updatedUser.getCountry());
            if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
            }
            return userRepository.save(user);
        }
        return null;
    }

    public User findByUsername(String username) {
        logger.info("Finding user by username: " + username);
        return userRepository.findByUsername(username);
    }

    public boolean checkPassword(String rawPassword, String encodedPassword) {
        boolean matches = passwordEncoder.matches(rawPassword, encodedPassword);
        logger.info("Password match result: " + matches);
        return matches;
    }

    public Map<String, String> loginUser(UserCreateDTO userCreateDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getAllErrors().toString();
            logger.error("Validation errors: {}", errorMessage);
            throw new CustomValidationException(errorMessage);
        }

        User user = findByUsername(userCreateDTO.getUsername());
        if (user != null) {
            logger.info("User found: " + user.getUsername());
            if (checkPassword(userCreateDTO.getPassword(), user.getPassword())) {
                String token = jwtUtil.generateToken(user.getUsername());
                Map<String, String> response = new HashMap<>();
                response.put("token", token);
                return response;
            } else {
                logger.info("Password does not match");
            }
        } else {
            logger.info("User not found");
        }
        throw new CustomValidationException("Invalid username or password");
    }

    public void deleteUserByUsername(String username) {
        User user = findByUsername(username);
        if (user != null) {
            logger.info("Deleting user by username: " + username);
            userRepository.delete(user);
        } else {
            throw new RuntimeException("User not found");
        }
    }
}