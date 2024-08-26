package com.shinhan_hackathon.the_family_guardian.domain.chatbot.service;

import com.shinhan_hackathon.the_family_guardian.domain.chatbot.entity.ChatMessage;
import com.shinhan_hackathon.the_family_guardian.domain.chatbot.repository.ChatMessageRepository;
import com.shinhan_hackathon.the_family_guardian.domain.chatbot.response.ChatHistoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;

    public void saveMessage(Long userId, String message, boolean fromUser) {
        ChatMessage chatMessage = new ChatMessage(userId, message, fromUser);
        chatMessageRepository.insert(chatMessage);

    }

    public List<ChatHistoryResponse> getChatHistory(Long userId) {
        List<ChatHistoryResponse> response = new ArrayList<>();
        for(ChatMessage chatMessage : chatMessageRepository.findByUserIdOrderByTimestampAsc(userId)){
            ChatHistoryResponse chatHistoryResponse = new ChatHistoryResponse(chatMessage);
            response.add(chatHistoryResponse);
        }
        return response;
    }
}
