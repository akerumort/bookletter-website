package com.akerumort.userservice.services;

import com.akerumort.userservice.entities.User;
import com.akerumort.userservice.repos.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private static final Logger logger = LogManager.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers(int page, int size) {
        logger.info("Fetching users with pagination");
        Pageable pageable = PageRequest.of(page, size);
        Page<User> userPage = userRepository.findAll(pageable);
        return userPage.getContent();
    }

    public User getUserById(Long id) {
        logger.info("Fetched user by ID: " + id);
        return userRepository.findById(id).orElse(null);
    }

    public User saveUser(User user) {
        logger.info("Saved user: " + user.getUsername());
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
        logger.info("User with ID " + id + " deleted successfully");
    }
}