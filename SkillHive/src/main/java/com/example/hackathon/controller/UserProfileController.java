package com.example.hackathon.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.hackathon.model.User;
import com.example.hackathon.model.UserProfile;
import com.example.hackathon.repositories.UserProfileRepository;
import com.example.hackathon.repositories.UserRepository;
import com.example.hackathon.service.UserProfileService;
import org.springframework.security.core.context.SecurityContextHolder;

@Controller
@RequestMapping("/profile")
public class UserProfileController {

    @Autowired
    private UserProfileService userProfileService;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserProfileRepository userProfileRepository;

    @GetMapping
    public String getUserProfile(Model model) {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found: " + username));

            Optional<UserProfile> existingProfile = userProfileRepository.findByUserId(user.getId());

            if (existingProfile.isPresent()) {
                model.addAttribute("userProfile", existingProfile.get());
                model.addAttribute("isProfilePresent", true); // Profile exists, show update button
            } else {
                model.addAttribute("userProfile", new UserProfile());
                model.addAttribute("isProfilePresent", false); // Profile doesn't exist, show create button
            }

            return "UserProfile"; // The Thymeleaf template for user profile
        } catch (Exception e) {
            model.addAttribute("error", "Failed to load profile: " + e.getMessage());
            return "UserProfile"; // Show the same form with error
        }
    }

    @PostMapping("/update")
    public String updateUserProfile(@ModelAttribute UserProfile updatedProfile, Model model) {
        try {
        	String username = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found: " + username));
            // Check if the profile for this user exists before attempting to update
            Optional<UserProfile> existingProfile = userProfileRepository.findByUserId(user.getId());

            if (existingProfile.isPresent()) {
                UserProfile profile = existingProfile.get();
                profile.setFirstName(updatedProfile.getFirstName());
                profile.setLastName(updatedProfile.getLastName());
                profile.setBio(updatedProfile.getBio());

                UserProfile updatedUserProfile = userProfileService.updateUserProfile(user.getId(), profile);
                model.addAttribute("userProfile", updatedUserProfile);
                return "redirect:/profile"; // Redirect to profile page after update
            } else {
                throw new RuntimeException("Profile not found for user: " + username);
            }
        } catch (Exception e) {
            model.addAttribute("error", "Profile update failed: " + e.getMessage());
            return "UserProfile"; // Show the same form with error
        }
    }


    @PostMapping("/create")
    public String createUserProfile(@ModelAttribute UserProfile newProfile, Model model) {
        try {
        	String username = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found: " + username));
            // Check if profile already exists
            Optional<UserProfile> existingProfile = userProfileRepository.findByUserId(user.getId());

            if (existingProfile.isPresent()) {
                throw new RuntimeException("Profile already exists for user: " + username);
            }

            newProfile.setUser(user);
            UserProfile createdProfile = userProfileService.createUserProfile(user.getId(), newProfile);
            return "redirect:/profile"; // Redirect to profile page after creation
        } catch (Exception e) {
            model.addAttribute("error", "Profile creation failed: " + e.getMessage());
            return "UserProfile"; // Show the same form with error
        }
    }



}
