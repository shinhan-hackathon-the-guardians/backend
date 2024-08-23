package com.shinhan_hackathon.the_family_guardian.domain.chatbot.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.vertexai.gemini.VertexAiGeminiChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/chat")
public class ChatController {
    private final VertexAiGeminiChatModel vertexAiGeminiChatModel;

    @GetMapping
    public Map<String, String> chat(@RequestBody String message) {
        Map<String, String> responses = new HashMap<>();

        String vertexAiGeminiResponse = vertexAiGeminiChatModel.call(message);
        responses.put("Guardi의 응답", vertexAiGeminiResponse);
        return responses;
    }

}
