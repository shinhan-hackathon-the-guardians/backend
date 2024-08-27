package com.shinhan_hackathon.the_family_guardian.domain.questionbank.dto.response;

import java.util.List;

public record QuestionListResponse(
        List<QuestionResponse> questions
) {
}
