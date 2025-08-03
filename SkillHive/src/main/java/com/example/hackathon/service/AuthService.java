package com.example.hackathon.service;

import com.example.hackathon.model.Role;
import com.example.hackathon.model.RoleType;
import com.example.hackathon.model.User;
import com.example.hackathon.repositories.RoleRepository;
import com.example.hackathon.repositories.UserRepository;
import com.example.hackathon.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    public String authenticateUser(String username, String password) {
        try {
            // Attempt authentication
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));

            // If successful, set the authentication in the context
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return "User signed in successfully";  // Success message
        } catch (BadCredentialsException e) {
            // Handle invalid credentials
            return "Invalid credentials";  // Return a user-friendly error message
        } catch (Exception e) {
            // Handle any other exceptions
            return "An error occurred: " + e.getMessage();  // Return a generic error message
        }
    }


    public ResponseEntity<?> registerUser(String username, String email, String password, Set<RoleType> roles) {
        // Check if username or email already exists
        if (userRepository.existsByUsername(username)) {
            return ResponseEntity.badRequest().body("Error: Username is already taken!");
        }

        if (userRepository.existsByEmail(email)) {
            return ResponseEntity.badRequest().body("Error: Email is already in use!");
        }

        // Create a new user object
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(encoder.encode(password)); // Encrypt the password

        // Fetch and set roles
        Set<Role> userRoles = new HashSet<>();
        for (RoleType roleType : roles) {
            Role role = roleRepository.findByName(roleType)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            userRoles.add(role);
        }
        user.setRoles(userRoles);

        // Save the user in the database
        userRepository.save(user);

        return ResponseEntity.ok("User registered successfully!");
    }

}
