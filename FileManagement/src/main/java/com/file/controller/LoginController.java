package com.file.controller;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.file.dto.AuthResponse;
import com.file.model.Users;
import com.file.repo.LoginRepo;
import com.file.service.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private JwtTokenProvider jwtService;

    @Autowired
    private LoginRepo userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Users loginRequest) {
        logger.info("Login called for username: {}", loginRequest.getUsername());

        Users user = userRepository.findByUsername(loginRequest.getUsername());

        if (user == null) {
            logger.warn("User not found: {}", loginRequest.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponse(false, "Invalid username or password", null, null));
        }

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            logger.warn("Password mismatch for user: {}", user.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponse(false, "Invalid username or password", null, null));
        }

        String accessToken = jwtService.generateAccessToken(user.getUsername());
        String refreshToken = jwtService.generateRefreshToken(user.getUsername());

        logger.info("Generated Access Token: {}", accessToken);
        logger.info("Generated Refresh Token: {}", refreshToken);

        return ResponseEntity.ok(new AuthResponse(true, "Login successful", accessToken, refreshToken));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(@RequestBody Map<String, String> requestBody) {
        String refreshToken = requestBody.get("refreshToken");
        
        if (refreshToken == null || refreshToken.isEmpty()) {
            logger.error("Refresh token is missing in the request");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Refresh token is required");
        }

        try {
            String username = jwtService.extractUserName(refreshToken);
            if (username == null) {
                logger.warn("Invalid or expired refresh token");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired refresh token");
            }

            String newAccessToken = jwtService.generateAccessToken(username);
            logger.info("New Access Token generated for user: {}", username);

            return ResponseEntity.ok(new AuthResponse(true, "Access token refreshed", newAccessToken, refreshToken));

        } catch (Exception e) {
            logger.error("Error refreshing token: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired refresh token");
        }
    }

    @RequestMapping(value = "/login", method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> handleOptions() {
        logger.info("Handling OPTIONS request for /login");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Allow", "GET, POST, OPTIONS");
        return new ResponseEntity<>(headers, HttpStatus.OK);
    }
}
