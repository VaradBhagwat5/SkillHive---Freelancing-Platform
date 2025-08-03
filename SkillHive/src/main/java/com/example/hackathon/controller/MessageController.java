package com.example.hackathon.controller;

import java.security.Principal;
import java.util.Set;

import com.example.hackathon.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.hackathon.model.Message;
import com.example.hackathon.model.User;
import com.example.hackathon.service.MessageService;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping("/{conversationId}")
    public ResponseEntity<?> sendMessage(@PathVariable Long conversationId,
                                         @RequestBody Message message,
                                         Principal principal) {
        User sender = ((UserDetailsImpl) ((UsernamePasswordAuthenticationToken) principal).getPrincipal()).getUser();
        return ResponseEntity.ok(messageService.sendMessage(conversationId, message, sender));
    }

    @GetMapping("/{conversationId}")
    public ResponseEntity<?> getMessages(@PathVariable Long conversationId) {
        return ResponseEntity.ok(messageService.getMessages(conversationId));
    }

    @PostMapping("/start")
    public ResponseEntity<?> startConversation(@RequestBody Set<User> participants) {
        return ResponseEntity.ok(messageService.startConversation(participants));
    }

    @GetMapping("/conversations")
    public ResponseEntity<?> getConversations(Principal principal) {
        User user = ((UserDetailsImpl) ((UsernamePasswordAuthenticationToken) principal).getPrincipal()).getUser();
        return ResponseEntity.ok(messageService.getConversations(user));
    }
}
