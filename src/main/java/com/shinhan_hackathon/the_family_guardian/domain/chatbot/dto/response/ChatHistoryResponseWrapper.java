package com.shinhan_hackathon.the_family_guardian.domain.chatbot.dto.response;

import java.util.List;

public record ChatHistoryResponseWrapper(
        List<ChatHistoryResponse> history
) {
}
