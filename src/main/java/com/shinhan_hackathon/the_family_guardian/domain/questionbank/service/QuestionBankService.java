package com.shinhan_hackathon.the_family_guardian.domain.questionbank.service;

import com.shinhan_hackathon.the_family_guardian.domain.questionbank.entity.QuestionBank;
import com.shinhan_hackathon.the_family_guardian.domain.questionbank.repository.QuestionBankRepository;
import com.shinhan_hackathon.the_family_guardian.domain.questionbank.dto.response.QuestionResponse;
import com.shinhan_hackathon.the_family_guardian.domain.user.entity.Level;
import com.shinhan_hackathon.the_family_guardian.domain.user.entity.User;
import com.shinhan_hackathon.the_family_guardian.domain.user.repository.UserRepository;
import com.shinhan_hackathon.the_family_guardian.global.auth.dto.UserPrincipal;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuestionBankService {

    private final QuestionBankRepository questionBankRepository;
    private final UserRepository userRepository;

    public List<QuestionResponse> getQuestion() {
        List<QuestionBank> questionBanks = questionBankRepository.findQuestion();
        List<QuestionResponse> responses = new ArrayList<>();
        for(QuestionBank questionBank : questionBanks){
            responses.add(new QuestionResponse(questionBank.getAnswer(), questionBank.getQuestion()));
        }
        return responses;
    }

    @Transactional
    public void updateLevel(boolean pass) {
        if(pass) {
            User user = userRepository.findById(getUserId())
                    .orElseThrow(EntityNotFoundException::new);
            user.updateLevel(Level.GUARDIAN);
        }
    }

    private Long getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return userPrincipal.user().getId();
    }
}
