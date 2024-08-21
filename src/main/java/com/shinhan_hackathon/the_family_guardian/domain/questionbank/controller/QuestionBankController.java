package com.shinhan_hackathon.the_family_guardian.domain.questionbank.controller;

import com.shinhan_hackathon.the_family_guardian.domain.questionbank.entity.QuestionBank;
import com.shinhan_hackathon.the_family_guardian.domain.questionbank.response.QuestionListResponse;
import com.shinhan_hackathon.the_family_guardian.domain.questionbank.response.QuestionResponse;
import com.shinhan_hackathon.the_family_guardian.domain.questionbank.service.QuestionBankService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/question")
public class QuestionBankController {

    private final QuestionBankService questionBankService;

    @GetMapping
    public ResponseEntity<QuestionListResponse> getQuestion() {
        QuestionListResponse questions = new QuestionListResponse(questionBankService.getQuestion());
        return ResponseEntity.ok().body(questions);
    }
}
