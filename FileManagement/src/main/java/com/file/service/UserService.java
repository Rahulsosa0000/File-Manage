package com.file.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.file.model.UserPrincipal;
import com.file.model.Users;
import com.file.repo.LoginRepo;

@Service
public class UserService  implements UserDetailsService{

    @Autowired
    private LoginRepo userRepository;

    // Method to find a user by their username
    public Users findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Method to get role-specific data, if required
    public void getRoleSpecificData(String role) {
        if ("ADMIN".equals(role)) {
            // Fetch Admin-specific data logic here
            System.out.println("Fetching admin data...");
        } else if ("EMPLOYEE".equals(role)) {
            // Fetch Employee-specific data logic here
            System.out.println("Fetching employee data...");
        } else if ("MANAGER".equals(role)) {
            // Fetch Manager-specific data logic here
            System.out.println("Fetching manager data...");
        }
    }
    
    
    

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Users user = userRepository.findByUsername(username);
		if(user == null) {
			System.out.println("User Not Found...");
			throw new UsernameNotFoundException("User Not Found...");
		}
		
		return new UserPrincipal(user);
	}
    
    
}
