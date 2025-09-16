package com.itravel.backend.service;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.itravel.backend.dto.SignupRequest;
import com.itravel.backend.models.User;
import com.itravel.backend.repository.UserRepository;
import com.itravel.backend.security.JwtUtil;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.util.Optional;

@Slf4j
@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProfileService profileService;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    PasswordEncoder passwordEncoder;

    public String signup(SignupRequest requestBody) {
        Optional<User> existingUser = userRepository.findByEmail(requestBody.getEmail());
        if (existingUser.isPresent()) {
            throw new RuntimeException("User already exists");
        }

        User user = new User();
        user.setEmail(requestBody.getEmail());
        user.setPassword(passwordEncoder.encode(requestBody.getPassword()));
        userRepository.save(user);
        profileService.createProfileByUser(user);
        return jwtUtil.generateToken(user.getEmail(), user.getId());
    }

    public String login(String email, String password) {

        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        if (passwordEncoder.matches(password, user.getPassword())) {
            return jwtUtil.generateToken(email, user.getId());
        }

        throw new RuntimeException("Invalid credentials");
    }

    public void deleteByEmail(String email) {

        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        userRepository.delete(user);
    }
}
