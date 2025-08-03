package com.example.hackathon.controller;

import com.example.hackathon.model.RoleType;
import com.example.hackathon.service.AuthService;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // Main index route


    // Show login form
    @GetMapping("/login")
    public String showLogin(Model model, @RequestParam(value = "error", required = false) String error) {
        if (error != null) {
            model.addAttribute("error", "Login failed!");
        }
        return "login";  // This will return login.html view
    }

    // Show registration form with CSRF token passed to Thymeleaf
    @GetMapping("/register")
    public String showRegister(CsrfToken csrfToken, Model model) {
        model.addAttribute("_csrf", csrfToken);
        return "register";  // This will return the register.html view
    }

    @PostMapping("/perform_login")
    public String authenticateUser(@RequestParam("username") String username,
                                   @RequestParam("password") String password,
                                   Model model) {

        String response = authService.authenticateUser(username, password);

        if ("User signed in successfully".equals(response)) {
            // Success, display message and redirect to profile
            model.addAttribute("message", "Login successful!");
            return "redirect:/profile";  // Redirect to the profile page on successful login
        } else {
            // Failed login, reload login page with error message
            model.addAttribute("error", response);  // Display login failed message
            return "login";  // Return to login page
        }
    }




    @PostMapping("/perform_register")
    public String registerUser(@RequestParam("username") String username,
                               @RequestParam("email") String email,
                               @RequestParam("password") String password,
                               @RequestParam("roles") String role, // Change to String for single selection
                               Model model) {

        Set<RoleType> roleSet = new HashSet<>();
        if (role != null && !role.isEmpty()) {
            String roleWithPrefix = role.startsWith("ROLE_") ? role : "ROLE_" + role;
            roleSet.add(RoleType.valueOf(roleWithPrefix));
        }

        try {
            ResponseEntity<?> response = authService.registerUser(username, email, password, roleSet);
            if (response.getStatusCode().is2xxSuccessful()) {
                model.addAttribute("message", "User registered successfully!"); // Success message
                return "redirect:/api/auth/login"; // Redirect to login
            } else {
                // Directly set the error message without any additional text or prefix
                model.addAttribute("error", response.getBody()); // Error message from response body
                return "register"; // Return to register page with error
            }
        } catch (Exception e) {
            // Only display the error message, without any "Registration error:" prefix
            model.addAttribute("error", e.getMessage()); // Exception message only
            return "register";
        }
    }


}
