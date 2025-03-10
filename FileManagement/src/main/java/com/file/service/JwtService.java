package com.file.service;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    @Autowired
    private JwtTokenProvider jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MyUserServiceDetails userDetailsService;

    public Map<String, String> authenticate(String username, String password) {
        Map<String, String> response = new HashMap<>();

        try {
            // Authenticate user
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

            final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            String token = jwtUtil.generateAccessToken(userDetails.getUsername());
            String refreshToken = jwtUtil.generateRefreshToken(userDetails.getUsername());

            // Logging
            System.out.println("Authentication successful for user: " + username);
            System.out.println("Generated Access Token: " + token);
            System.out.println("Generated Refresh Token: " + refreshToken);

            response.put("token", token);
            response.put("refreshToken", refreshToken);
            response.put("message", "Login Successful");

        } catch (DisabledException e) {
            System.out.println("User is disabled: " + username);
            response.put("error", "User is disabled. Contact admin.");
        } catch (BadCredentialsException e) {
            System.out.println("Invalid credentials for user: " + username);
            response.put("error", "Invalid username or password.");
        } catch (Exception e) {
            System.out.println("Authentication error: " + e.getMessage());
            response.put("error", "Authentication failed. Try again.");
        }

        return response;
    }
}
