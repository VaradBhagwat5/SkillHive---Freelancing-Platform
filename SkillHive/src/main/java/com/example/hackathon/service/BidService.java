package com.example.hackathon.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.hackathon.model.Bid;
import com.example.hackathon.model.BidDTO;
import com.example.hackathon.model.BidDto1;
import com.example.hackathon.model.BidResponse;
import com.example.hackathon.model.Project;
import com.example.hackathon.model.ProjectStatus;
import com.example.hackathon.model.User;
import com.example.hackathon.repositories.BidRepository;
import com.example.hackathon.repositories.ProjectRepository;
import com.example.hackathon.repositories.UserRepository;

@Service
public class BidService {

    @Autowired
    private BidRepository bidRepository;
   
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    public List<Bid> getBidsByUserId(Long userId) {
        // Use the method that retrieves bids by freelancer ID
        return bidRepository.findByFreelancerId(userId);
    }
    
    
    public List<BidDTO> getBidsByProjectId(Long projectId) {
        List<Bid> bids = bidRepository.findByProjectId(projectId);
        return bids.stream().map(bid -> {
            BidDTO dto = new BidDTO();
            dto.setId(bid.getId());
            dto.setAmount(bid.getAmount());
            dto.setProposal(bid.getProposal());
            dto.setStatus(bid.getStatus());
            dto.setFreelancerName(bid.getFreelancer().getUsername()); // Assuming User has a getName() method
            dto.setFreelancerEmail(bid.getFreelancer().getEmail()); 
            dto.setFreelancerId(bid.getFreelancer().getId());
            return dto;
        }).collect(Collectors.toList());
    }

    public Bid placeBid(Bid bid, String username, Long projectId) {
        User freelancer = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("Freelancer not found: " + username));
        System.out.println("Freelancer found: " + freelancer.getUsername());

        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new RuntimeException("Project not found: " + projectId));
        System.out.println("Project found: " + project.getId());

        bid.setFreelancer(freelancer);
        bid.setProject(project);
        bid.setTimestamp(LocalDateTime.now());
        bid.setStatus("PENDING"); // Set initial status

        return bidRepository.save(bid);
    }


    // New method to update bid status
    public Bid updateBidStatus(Long bidId, String status) {
        Bid bid = bidRepository.findById(bidId)
            .orElseThrow(() -> new RuntimeException("Bid not found: " + bidId));

        // Convert status to uppercase to ensure consistency
        bid.setStatus(status.toUpperCase());
        
        // If the bid is accepted, update the project status as well
        if ("ACCEPTED".equalsIgnoreCase(status)) {
            Long projectId = bid.getProject().getId(); // Assuming Bid has a reference to Project
            Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found: " + projectId));
            project.setStatus(ProjectStatus.IN_PROGRESS);
            projectRepository.save(project);
        }
        
        return bidRepository.save(bid);
    }
    
    public List<Bid> getBidsByProjectAndFreelancer(Long projectId, String username) {
        User freelancer = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("Freelancer not found: " + username));
        
        return bidRepository.findByProjectIdAndFreelancerId(projectId, freelancer.getId());
    }
    
    public Map<String, List<BidDto1>> getBidsGroupedByStatusForFreelancer(String username) {
    	// Debugging: Print when fetching bids
        System.out.println("Retrieving bids from the repository for freelancer: " + username);

        // Fetch bids for the freelancer
        List<Bid> bids = bidRepository.findByFreelancerUsername(username);

        // Debugging: Print the number of bids found
        if (bids == null || bids.isEmpty()) {
            System.out.println("No bids found for freelancer: " + username);
        } else {
            System.out.println("Found " + bids.size() + " bids for freelancer: " + username);
        }

        // Group bids by their status
        return bids.stream()
            .map(this::convertToBidDto1)
            .collect(Collectors.groupingBy(BidDto1::getStatus));
    }

    private BidDto1 convertToBidDto1(Bid bid) {
        // Debugging: Print when converting a bid
        System.out.println("Converting bid to BidDto1 for project: " + bid.getProject().getTitle());

        Project project = bid.getProject();
        User client = project.getClient();

        // Debugging: Check if the client is null
        if (client == null) {
            System.out.println("Client is null for project: " + project.getTitle());
        }

        // Use a default client name if client is null
        String clientUsername = (client != null) ? client.getUsername() : "Unknown Client";

        // Create and return BidDto1
        return new BidDto1(
            project.getTitle(),             // Project Title
            clientUsername,                 // Client Name (default if null)
            bid.getAmount(),                // Amount
            bid.getProposal(),              // Proposal
            bid.getStatus()                 // Status
        );
    }
    
    public List<BidResponse> getPendingBidsByClient(String username) {
        // Log entry into the service method
        System.out.println("Entered getPendingBidsByClient method with username: " + username);

        try {
            // Fetch the logged-in user
            User client = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found: " + username));
            System.out.println("Fetched user: " + client);

            // Fetch projects created by the client
            List<Project> clientProjects = projectRepository.findByClient(client);
            System.out.println("Fetched projects for user: " + clientProjects);

            // Fetch pending bids for the client's projects
            List<Bid> pendingBids = bidRepository.findByProjectInAndStatus(clientProjects, "PENDING");
            System.out.println("Fetched pending bids: " + pendingBids);

            // Map bids to BidResponse DTO
            List<BidResponse> response = pendingBids.stream().map(bid -> new BidResponse(
                    bid.getId(),
                    bid.getProject().getTitle(),
                    bid.getFreelancer().getUsername(),
                    bid.getAmount(),
                    bid.getProposal()
            )).collect(Collectors.toList());
            System.out.println("Mapped pending bids to BidResponse: " + response);

            return response;
        } catch (Exception e) {
            // Log the exception if any occurs
            System.err.println("Error in getPendingBidsByClient: " + e.getMessage());
            e.printStackTrace();
            throw e; // Rethrow the exception to propagate it to the caller
        }
    }



}