package com.shinhan_hackathon.the_family_guardian.domain.questionbank.response;

import com.shinhan_hackathon.the_family_guardian.domain.questionbank.entity.QuestionBank;

public record QuestionResponse(
        String answer,
        String question
) {

}
