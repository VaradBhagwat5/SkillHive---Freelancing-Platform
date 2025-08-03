package com.example.hackathon.repositories;

import java.util.List;

import com.example.hackathon.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.hackathon.model.Bid;
import com.example.hackathon.model.Project;

public interface BidRepository extends JpaRepository<Bid, Long> {
    List<Bid> findByProjectId(Long projectId);
    List<Bid> findByFreelancerId(Long userId);
    List<Bid> findByProjectIdAndFreelancerId(Long projectId, Long freelancerId);
    
    List<Bid> findByProjectInAndStatus(List<Project> projects, String status);
    
    @Query("SELECT b FROM Bid b JOIN b.freelancer f WHERE f.username = :username")
    List<Bid> findByFreelancerUsername(@Param("username") String username);

}