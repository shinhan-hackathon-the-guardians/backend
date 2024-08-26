package com.shinhan_hackathon.the_family_guardian.domain.chatbot.entity;

import lombok.Getter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Getter
@Document(collection = "chat_messages")
public class ChatMessage {

    @Id
    @Field("id")
    private ObjectId id;

    @Field("userId")
    private Long userId;

    @Field("message")
    private String message;

    @Field("fromUser")
    private boolean fromUser;  // true: 사용자 메시지, false: 봇 메시지

    @Field("timestamp")
    private LocalDateTime timestamp;

    public ChatMessage() {}

    public ChatMessage(Long userId, String message, boolean fromUser) {
        this.userId = userId;
        this.message = message;
        this.fromUser = fromUser;
        this.timestamp = LocalDateTime.now();
    }

}