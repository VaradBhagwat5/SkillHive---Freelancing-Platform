package com.example.hackathon.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.hackathon.model.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByConversationId(Long conversationId);
}

