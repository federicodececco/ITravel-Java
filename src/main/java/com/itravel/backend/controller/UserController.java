package com.itravel.backend.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.itravel.backend.dto.ApiResponse;
import com.itravel.backend.security.JwtUtil;
import com.itravel.backend.service.UserService;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    public UserService userService;
    @Autowired
    private JwtUtil jwtUtil;

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse<String>> deleteUser(@RequestHeader("Authorization") String token) {
        try {
            log.info("Deleting user with token: {}", token);
            String email = jwtUtil.extractEmail(token.replace("Bearer ", ""));
            userService.deleteByEmail(email);
            return ResponseEntity.ok(new ApiResponse<>("User deleted successfully", null));
        } catch (Exception e) {
            log.error("Error during user deletion: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<String>("User deletion failed", e.getMessage()));
        }
    }
}
