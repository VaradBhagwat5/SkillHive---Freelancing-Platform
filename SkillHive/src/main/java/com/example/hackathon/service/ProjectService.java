package com.example.hackathon.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.hackathon.model.Bid;
import com.example.hackathon.model.Project;
import com.example.hackathon.model.ProjectDTO;
import com.example.hackathon.model.ProjectStatus;
import com.example.hackathon.model.ProjectUpdateRequest;
import com.example.hackathon.model.User;
import com.example.hackathon.repositories.BidRepository;
import com.example.hackathon.repositories.ProjectRepository;
import com.example.hackathon.repositories.UserRepository;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BidRepository bidRepository;

    // Retrieve all projects
    @Transactional(readOnly = true)
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public Project getProjectById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));
    }
    
 // Method to search projects by title or client name
    @Transactional(readOnly = true)
    public List<Project> searchProjectsByTitleOrClient(String searchTerm) {
        return projectRepository.findByTitleContainingOrClientUsernameContaining(searchTerm, searchTerm);
    }

    // Search projects by budget range
    @Transactional(readOnly = true)
    public List<Project> searchProjectsByBudgetRange(double min, double max) {
        return projectRepository.findByBudgetBetween(min, max);
    }



    // Get projects by client ID
    @Transactional(readOnly = true)
    public List<Project> getProjectsByClientId(Long clientId) {
        return projectRepository.findByClientId(clientId);
    }

    // Get projects by freelancer ID
    @Transactional(readOnly = true)
    public List<Project> getProjectsByFreelancerId(Long freelancerId) {
        return projectRepository.findByFreelancerId(freelancerId);
    }

    public Project createProject(ProjectDTO projectDTO) {
    	 // Get authenticated client's username
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User client = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Client not found: " + username));
        Project project = new Project();
        project.setTitle(projectDTO.getTitle());
        project.setDescription(projectDTO.getDescription());
        project.setBudget(projectDTO.getBudget());
        project.setDeadline(projectDTO.getDeadline());
        project.setStatus(projectDTO.getStatus());
        project.setClient(client); 
        return projectRepository.save(project);
    }

    public Project updateProject(Long projectId, ProjectUpdateRequest updateRequest) {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new IllegalArgumentException("Invalid project ID"));
        project.setTitle(updateRequest.getTitle());
        project.setDescription(updateRequest.getDescription());
        project.setBudget(updateRequest.getBudget());
//        project.setStatus(updateRequest.getStatus());
        project.setDeadline(updateRequest.getDeadline());
        return projectRepository.save(project);
    }



    // Accept a bid
    @Transactional
    public Project acceptBid(Long projectId, Long bidId) {
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new RuntimeException("Project not found with id " + projectId));

        Bid bid = bidRepository.findById(bidId)
            .orElseThrow(() -> new RuntimeException("Bid not found with id " + bidId));

        // Update the project with the selected freelancer
        project.setFreelancer(bid.getFreelancer());
        project.setStatus(ProjectStatus.IN_PROGRESS);

        return projectRepository.save(project);
    }
    
    public Long getCurrentClientId() {
        // Get the authenticated user's username
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        
        // Fetch the user by username from the UserRepository
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        
        // Return the user's ID
        return user.getId();  // Assuming the User entity has a getId() method
    }
}
