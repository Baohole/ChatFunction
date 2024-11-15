package com.example.chatfunction.model;

import lombok.*;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessageModel {

    private  String id;
    private String message;
    private String sender;
    private MessageType type;

    public String getUserName() {
        return sender;
    }
    private Timestamp send_time;
    private String room;
//    private Arrays files;

    @Override
    public String toString() {
        return "ChatMessage{" +
                "messageId='" + type + '\'' +
                ", senderId='" + sender + '\'' +
                ", content='" + message + '\'' +
                '}';
    }

}
