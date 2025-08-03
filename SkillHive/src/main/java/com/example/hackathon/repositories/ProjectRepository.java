package com.example.hackathon.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.hackathon.model.Project;
import com.example.hackathon.model.User;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByClientId(Long clientId); // Query by client
    List<Project> findByFreelancerId(Long freelancerId); // Query by freelancer
    // Custom query to search by project title or client name (case insensitive)
    List<Project> findByTitleContainingOrClientUsernameContaining(String title, String clientName);

    // Search by budget range
    List<Project> findByBudgetBetween(double min, double max);
    List<Project> findByClient(User client);
}