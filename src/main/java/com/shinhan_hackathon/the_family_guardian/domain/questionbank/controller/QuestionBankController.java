package com.shinhan_hackathon.the_family_guardian.domain.questionbank.controller;

import com.shinhan_hackathon.the_family_guardian.domain.questionbank.dto.request.QuestionCompleteRequest;
import com.shinhan_hackathon.the_family_guardian.domain.questionbank.dto.response.QuestionListResponse;
import com.shinhan_hackathon.the_family_guardian.domain.questionbank.service.QuestionBankService;
import com.shinhan_hackathon.the_family_guardian.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/question")
public class QuestionBankController {

    private final QuestionBankService questionBankService;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<QuestionListResponse> getQuestion() {
        QuestionListResponse questions = new QuestionListResponse(questionBankService.getQuestion());
        return ResponseEntity.ok().body(questions);
    }

    @PostMapping("/complete")
    public ResponseEntity<String> questionComplete(@RequestBody QuestionCompleteRequest questionCompleteRequest) {
        questionBankService.updateLevel(questionCompleteRequest.pass());
        return ResponseEntity.ok("완료되었습니다");
    }

}
