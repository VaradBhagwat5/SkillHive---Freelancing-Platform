package com.example.hackathon.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List; // <-- Ensure this import is present
import java.util.Map;
import java.util.Collections;


import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.hackathon.model.Bid;
import com.example.hackathon.model.BidDTO;
import com.example.hackathon.model.BidDto1;
import com.example.hackathon.model.BidRequest;
import com.example.hackathon.model.BidResponse;
import com.example.hackathon.model.User;
import com.example.hackathon.repositories.UserRepository;
import com.example.hackathon.service.BidService;



@RestController
@RequestMapping("/api/bids")
public class BidController {

    @Autowired
    private BidService bidService;
    
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<BidDTO>> getBidsByProjectId(@PathVariable Long projectId) {
        return ResponseEntity.ok(bidService.getBidsByProjectId(projectId));
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Bid>> getBidsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(bidService.getBidsByUserId(userId));
    }
    
    @GetMapping("/freelancer")
    public ResponseEntity<List<Map<String, Object>>> getBidsGroupedByStatusForFreelancer() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        System.out.println("Fetching bids for freelancer: " + username);

        Map<String, List<BidDto1>> groupedBids = bidService.getBidsGroupedByStatusForFreelancer(username);

        if (groupedBids == null || groupedBids.isEmpty()) {
            System.out.println("No bids found for freelancer: " + username);
            return ResponseEntity.noContent().build();  
        }

        System.out.println("Fetched " + groupedBids.size() + " groups of bids for freelancer: " + username);
        System.out.println(groupedBids);

        // Convert map to a list of objects with status and bids
        List<Map<String, Object>> response = new ArrayList<>();
        for (Map.Entry<String, List<BidDto1>> entry : groupedBids.entrySet()) {
            Map<String, Object> statusGroup = new HashMap<>();
            statusGroup.put("status", entry.getKey());
            statusGroup.put("bids", entry.getValue());
            response.add(statusGroup);
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/place")
    public ResponseEntity<?> placeBid(@RequestBody BidRequest bidRequest) {
        System.out.println("Bid Request: " + bidRequest);

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (bidRequest.getProjectId() == null) {
            return ResponseEntity.badRequest().body("Project ID is required in the request body.");
        }

        try {
            Bid bid = new Bid();
            bid.setAmount(bidRequest.getAmount());
            bid.setProposal(bidRequest.getProposal());
            Long projectId = bidRequest.getProjectId();
            
            Bid placedBid = bidService.placeBid(bid, username, projectId);
            return ResponseEntity.ok(placedBid);
        } catch (Exception e) {
            e.printStackTrace();  // Print stack trace for debugging
            return ResponseEntity.badRequest().body("Failed to place bid: " + e.getMessage());
        }
    }

    @PatchMapping("/update-status/{bidId}")
    public ResponseEntity<Bid> updateBidStatus(@PathVariable Long bidId, @RequestBody Map<String, String> request) {
        String status = request.get("status"); // Extract status from JSON object
        Bid updatedBid = bidService.updateBidStatus(bidId, status);
        return ResponseEntity.ok(updatedBid);
    }

    
    @GetMapping("/project/{projectId}/freelancer")
    public ResponseEntity<List<Bid>> getBidsByProjectAndFreelancer(@PathVariable Long projectId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Bid> bids = bidService.getBidsByProjectAndFreelancer(projectId, username);
        return ResponseEntity.ok(bids);
    }
    
    @GetMapping("/pending")
    public List<BidResponse> getPendingBids(Authentication authentication) {
        // Log entry into the method
        System.out.println("Entered getPendingBids method");

        // Log the current authenticated user's username
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("Authenticated user: " + username);

        // Fetch and return pending bids for the user
        try {
            List<BidResponse> pendingBids = bidService.getPendingBidsByClient(username);
            System.out.println("Fetched pending bids: " + pendingBids);
            return pendingBids;
        } catch (Exception e) {
            // Log the exception if any occurs
            System.err.println("Error fetching pending bids: " + e.getMessage());
            e.printStackTrace();
            return Collections.emptyList(); // Return an empty list in case of an error
        }
    }



}