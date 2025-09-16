package com.itravel.backend.security;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.itravel.backend.dto.ApiResponse;
import com.itravel.backend.dto.SignupRequest;
import com.itravel.backend.service.UserService;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    public UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<String>> signup(@RequestBody SignupRequest user) {
        try {
            log.info("Signing up user: {}", user.getEmail());
            String token = userService.signup(user);
            return ResponseEntity.ok(new ApiResponse<>("User signed up successfully", token));
        } catch (Exception e) {
            log.error("Error during signup: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<String>("Signup failed", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@RequestBody Map<String, String> request) {
        try {
            String token = userService.login(request.get("email"), request.get("password"));
            return ResponseEntity.ok(new ApiResponse<>("Login successful", token));
        } catch (Exception e) {
            log.error("Error during login: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<String>("Login failed", e.getMessage()));
        }
    }

}