package com.example.hackathon.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.hackathon.model.User;
import com.example.hackathon.model.UserProfile;
import com.example.hackathon.repositories.UserProfileRepository;
import com.example.hackathon.repositories.UserRepository;


@Service
public class UserProfileService {

    @Autowired
    private UserProfileRepository userProfileRepository;
    
    @Autowired
    private UserRepository userRepository;

    public Optional<UserProfile> getUserProfile(Long userId) {
        return userProfileRepository.findById(userId);
    }

    public UserProfile createUserProfile(Long userId, UserProfile userProfile) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));
        
        userProfile.setUser(user);  // Set the user for the profile
        return userProfileRepository.save(userProfile);
    }

    public UserProfile updateUserProfile(Long userId, UserProfile updatedProfile) {
        UserProfile existingProfile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found for user: " + userId));
        
        // Update the profile fields
        existingProfile.setFirstName(updatedProfile.getFirstName());
        existingProfile.setLastName(updatedProfile.getLastName());
        existingProfile.setBio(updatedProfile.getBio());

        return userProfileRepository.save(existingProfile);
    }

}
