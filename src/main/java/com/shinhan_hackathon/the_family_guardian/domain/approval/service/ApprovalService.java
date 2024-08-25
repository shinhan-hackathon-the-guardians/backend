package com.shinhan_hackathon.the_family_guardian.domain.approval.service;

import com.shinhan_hackathon.the_family_guardian.domain.approval.repository.ApprovalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ApprovalService {
    private final ApprovalRepository approvalRepository;

    @Transactional
    public void createApproval() {

    }
}
