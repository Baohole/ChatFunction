package com.example.chatfunction.Firebase;

import com.google.firebase.database.*;
import com.example.chatfunction.model.ChatMessageModel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class FirebaseDatabaseService {

    private final DatabaseReference databaseReference;

    public FirebaseDatabaseService() {
        // Initialize the database reference to point to the "messages" path in your Realtime Database
        this.databaseReference = FirebaseDatabase.getInstance().getReference("messages");
    }

    // Method to save a chat message
    public void saveMessage(String messageId, ChatMessageModel chatMessage) {
        databaseReference.child(messageId).setValueAsync(chatMessage);
    }

    // Get all messages
    public CompletableFuture<List<ChatMessageModel>> getAllMessages() {
        CompletableFuture<List<ChatMessageModel>> future = new CompletableFuture<>();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<ChatMessageModel> messages = new ArrayList<>();
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    ChatMessageModel message = messageSnapshot.getValue(ChatMessageModel.class);
                    if (message != null) {
                        messages.add(message);
                    }
                }
                future.complete(messages);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                future.completeExceptionally(databaseError.toException());
            }
        });

        return future;
    }

    // Method to retrieve messages (implement as needed)
}
