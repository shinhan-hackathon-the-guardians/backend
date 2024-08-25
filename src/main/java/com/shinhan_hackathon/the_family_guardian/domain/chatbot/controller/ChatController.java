package com.shinhan_hackathon.the_family_guardian.domain.chatbot.controller;

import com.shinhan_hackathon.the_family_guardian.domain.chatbot.entity.ChatMessage;
import com.shinhan_hackathon.the_family_guardian.domain.chatbot.response.ChatHistoryResponse;
import com.shinhan_hackathon.the_family_guardian.domain.chatbot.response.ChatHistoryResponseWrapper;
import com.shinhan_hackathon.the_family_guardian.domain.chatbot.service.ChatService;
import com.shinhan_hackathon.the_family_guardian.global.auth.dto.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.vertexai.gemini.VertexAiGeminiChatModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/chat")
public class ChatController {
    private final VertexAiGeminiChatModel vertexAiGeminiChatModel;
    private final ChatService chatService;

    @PostMapping
    public Map<String, String> chat(@RequestBody String message) {
        Map<String, String> responses = new HashMap<>();

        String vertexAiGeminiResponse = vertexAiGeminiChatModel.call(message);
        responses.put("Guardi의 응답", vertexAiGeminiResponse);

        chatService.saveMessage(getUserId(), message, true);
        chatService.saveMessage(getUserId(), vertexAiGeminiResponse, false);
        return responses;
    }

    @GetMapping("/history/{userId}")
    public ChatHistoryResponseWrapper getChatHistory(@PathVariable Long userId){
        ChatHistoryResponseWrapper responses = new ChatHistoryResponseWrapper(chatService.getChatHistory(userId));
        return responses;
    }

    private Long getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return userPrincipal.user().getId();
    }
}
