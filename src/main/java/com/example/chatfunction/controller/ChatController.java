package com.example.chatfunction.controller;

import com.example.chatfunction.model.ChatMessageModel;
import com.example.chatfunction.Firebase.FirebaseDatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/messages")
public class ChatController {

    @Autowired
    private FirebaseDatabaseService firebaseDatabaseService;

    // WebSocket handler to add a user
    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessageModel addUser(
            @Payload ChatMessageModel chatMessage,
            SimpMessageHeaderAccessor headerAccessor
    ){
        // Add username in WebSocket session
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }

    // WebSocket handler to send a message
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessageModel sendMessage(@Payload ChatMessageModel chatMessage) {

        firebaseDatabaseService.saveMessage(chatMessage.getSender(), chatMessage);  // Assumes saveMessage(sender, message)
        return chatMessage;
    }

    // REST endpoint to retrieve all messages
    @GetMapping("/")
    public CompletableFuture<List<ChatMessageModel>> retrieveMessages() {
        return firebaseDatabaseService.getAllMessages()
                .thenApply(messages -> {
                    if (messages != null) {
                        return messages;  // Returns all messages as JSON
                    } else {
                        return List.of();  // Returns an empty list if no messages
                    }
                });
    }
}
