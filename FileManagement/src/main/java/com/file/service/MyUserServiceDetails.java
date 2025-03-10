package com.file.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.file.model.UserPrincipal;
import com.file.model.Users;
import com.file.repo.LoginRepo;

@Service
@Primary
public class MyUserServiceDetails implements UserDetailsService {

    @Autowired
    private LoginRepo repo; 

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Users> optionalUser = Optional.ofNullable(repo.findByUsername(username));

        Users user = optionalUser.orElseThrow(() -> {
            System.out.println("User not found: " + username);
            return new UsernameNotFoundException("User Not Found...");
        });

        System.out.println("User Found: " + user.getUsername() + " | Role: " + user.getRole().getRoleName());

        return new UserPrincipal(user);
    }
}
