package com.example.hackathon.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.hackathon.model.Conversation;
import com.example.hackathon.model.User;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    List<Conversation> findByParticipantsContaining(User user);
}

