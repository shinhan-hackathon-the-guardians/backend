package com.shinhan_hackathon.the_family_guardian.domain.chatbot.dto.response;

import com.shinhan_hackathon.the_family_guardian.domain.chatbot.entity.ChatMessage;

import java.time.LocalDateTime;

public record ChatHistoryResponse(
        Long userId,
        String message,
        boolean fromUser,
        LocalDateTime timestamp
) {
    public ChatHistoryResponse(ChatMessage chatMessage) {
        this(
                chatMessage.getUserId(),
                chatMessage.getMessage(),
                chatMessage.isFromUser(),
                chatMessage.getTimestamp()
        );
    }
}
