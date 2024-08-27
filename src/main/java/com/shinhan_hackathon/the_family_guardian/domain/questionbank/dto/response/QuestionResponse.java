package com.shinhan_hackathon.the_family_guardian.domain.questionbank.dto.response;

import com.shinhan_hackathon.the_family_guardian.domain.questionbank.entity.QuestionBank;

public record QuestionResponse(
        Long id,
        String answer,
        String question,
        String explanation
) {

}
