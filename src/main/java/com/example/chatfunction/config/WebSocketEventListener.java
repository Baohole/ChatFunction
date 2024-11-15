package com.example.chatfunction.config;

import com.example.chatfunction.model.ChatMessageModel;
import com.example.chatfunction.model.MessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {

    private final SimpMessageSendingOperations messageTemplate;
    @EventListener
    public void HandleDisconnectListener (SessionDisconnectEvent event) {
        // TO DO
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String)headerAccessor.getSessionAttributes().get("username");
        if (username != null) {
//            log.info("Disconnected from " + username);
            var chatMessage = ChatMessageModel.builder()
                    .type(MessageType.LEAVE)
                    .sender(username)
                    .build();

            messageTemplate.convertAndSend("/topic/public", chatMessage);
        }
    }
}
