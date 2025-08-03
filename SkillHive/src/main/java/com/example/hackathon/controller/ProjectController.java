package com.example.hackathon.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.hackathon.model.Project;
import com.example.hackathon.model.ProjectDTO;
import com.example.hackathon.model.ProjectStatus;
import com.example.hackathon.model.ProjectUpdateRequest;
import com.example.hackathon.repositories.ProjectRepository;
import com.example.hackathon.service.ProjectService;

@Controller // For serving the Freelancer Dashboard page
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProjectRepository projectRepository;
    // This method retrieves all projects and populates them into the Thymeleaf model
    @GetMapping
    public String getAllProjects(Model model) {
        List<Project> projects = projectService.getAllProjects();
        System.out.println(projects);
        model.addAttribute("projects", projects); // Add projects to the model
        return "freelancer_dashboard"; // Return the Thymeleaf template
    }

    // Fetch projects by client ID
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<Project>> getProjectsByClientId(@PathVariable Long clientId) {
        return ResponseEntity.ok(projectService.getProjectsByClientId(clientId));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable Long id) {
        Project project = projectService.getProjectById(id); // Fetch the project directly
        return ResponseEntity.ok(project); // Return the full Project entity as JSON
    }
    
    
 // Add a new method to search by budget range
    @GetMapping("/searchByBudget")
    public ResponseEntity<List<Project>> searchProjectsByBudget(@RequestParam double min, @RequestParam double max) {
        return ResponseEntity.ok(projectService.searchProjectsByBudgetRange(min, max));
    }

    // Updated service method for search by title or client
    @GetMapping("/search")
    public ResponseEntity<List<Project>> searchProjects(@RequestParam String searchTerm) {
        List<Project> projects = projectService.searchProjectsByTitleOrClient(searchTerm);
        return ResponseEntity.ok(projects);
    }



    // Fetch projects by freelancer ID
    @GetMapping("/freelancer/{freelancerId}")
    public ResponseEntity<List<Project>> getProjectsByFreelancerId(@PathVariable Long freelancerId) {
        return ResponseEntity.ok(projectService.getProjectsByFreelancerId(freelancerId));
    }

    @GetMapping("/dashboard")
    public String getClientDashboard(Model model) {
        Long clientId = projectService.getCurrentClientId();  // Assume you have logic to get authenticated client ID
        List<Project> projects = projectService.getProjectsByClientId(clientId);
        model.addAttribute("projects", projects);
        return "client_dashboard"; // Returns the Thymeleaf dashboard view
    }

    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<Project> createProject(@RequestBody ProjectDTO projectDTO) {
        // No need to convert the status to uppercase or call valueOf
        Project project = projectService.createProject(projectDTO);
        return ResponseEntity.ok(project);
    }

    @PutMapping("/update/{projectId}")
    public ResponseEntity<Project> updateProject(
            @PathVariable Long projectId,
            @RequestBody ProjectUpdateRequest updateRequest) {

        // No need to convert the status to uppercase or call valueOf
        Project updatedProject = projectService.updateProject(projectId, updateRequest);
        return ResponseEntity.ok(updatedProject);
    }
    
    @PatchMapping("/update-status/{id}")
    public ResponseEntity<Project> updateProjectStatus(@PathVariable Long id, @RequestBody Map<String, String> statusMap) {
        Optional<Project> projectOptional = projectRepository.findById(id);
        if (!projectOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        
        Project project = projectOptional.get();
        String statusString = statusMap.get("status");

        try {
            // Convert String to ProjectStatus enum
            ProjectStatus status = ProjectStatus.valueOf(statusString.toUpperCase());
            project.setStatus(status); // Update the status with the enum
            projectRepository.save(project);
            return ResponseEntity.ok(project);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null); // Return bad request if the status is invalid
        }
    }


  
    // Accept a bid on a project
    @PutMapping("/{projectId}/acceptBid/{bidId}")
    public ResponseEntity<Project> acceptBid(@PathVariable Long projectId, @PathVariable Long bidId) {
        Project updatedProject = projectService.acceptBid(projectId, bidId);
        return ResponseEntity.ok(updatedProject);
    }
}
