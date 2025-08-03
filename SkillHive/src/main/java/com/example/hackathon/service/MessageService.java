package com.example.hackathon.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.hackathon.model.Conversation;
import com.example.hackathon.model.Message;
import com.example.hackathon.model.User;
import com.example.hackathon.repositories.ConversationRepository;
import com.example.hackathon.repositories.MessageRepository;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ConversationRepository conversationRepository;

    public Message sendMessage(Long conversationId, Message message, User sender) {
        Conversation conversation = conversationRepository.findById(conversationId)
            .orElseThrow(() -> new RuntimeException("Conversation not found"));

        message.setConversation(conversation);
        message.setSender(sender);
        message.setTimestamp(LocalDateTime.now());

        return messageRepository.save(message);
    }

    public List<Message> getMessages(Long conversationId) {
        return messageRepository.findByConversationId(conversationId);
    }

    public Conversation startConversation(Set<User> participants) {
        Conversation conversation = new Conversation();
        conversation.setParticipants(participants);

        return conversationRepository.save(conversation);
    }

    public List<Conversation> getConversations(User user) {
        return conversationRepository.findByParticipantsContaining(user);
    }
}

