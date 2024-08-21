package com.shinhan_hackathon.the_family_guardian.domain.questionbank.service;

import com.shinhan_hackathon.the_family_guardian.domain.questionbank.entity.QuestionBank;
import com.shinhan_hackathon.the_family_guardian.domain.questionbank.repository.QuestionBankRepository;
import com.shinhan_hackathon.the_family_guardian.domain.questionbank.response.QuestionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionBankService {

    private final QuestionBankRepository questionBankRepository;

    public List<QuestionResponse> getQuestion() {
        List<QuestionBank> questionBanks = questionBankRepository.findQuestion();
        List<QuestionResponse> responses = new ArrayList<>();
        for(QuestionBank questionBank : questionBanks){
            responses.add(new QuestionResponse(questionBank.getAnswer(), questionBank.getQuestion()));
        }
        return responses;
    }
}
