package com.shinhan_hackathon.the_family_guardian.domain.approval.service;

import com.shinhan_hackathon.the_family_guardian.domain.approval.dto.ApprovalInfoResponse;
import com.shinhan_hackathon.the_family_guardian.domain.approval.entity.AcceptStatus;
import com.shinhan_hackathon.the_family_guardian.domain.approval.entity.Approval;
import com.shinhan_hackathon.the_family_guardian.domain.approval.repository.ApprovalRepository;
import com.shinhan_hackathon.the_family_guardian.domain.family.entity.Family;
import com.shinhan_hackathon.the_family_guardian.domain.user.entity.User;
import com.shinhan_hackathon.the_family_guardian.domain.user.repository.UserRepository;
import com.shinhan_hackathon.the_family_guardian.global.auth.dto.UserPrincipal;
import com.shinhan_hackathon.the_family_guardian.global.auth.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ApprovalService {
    private final ApprovalRepository approvalRepository;
    private final AuthUtil authUtil;
    private final UserRepository userRepository;

    @Transactional
    public Long createApproval(Family family, User user) {
        Optional<Approval> optionalApproval = approvalRepository.findByUser(user);
        if (optionalApproval.isPresent()) {
            Approval approval = optionalApproval.get();

            switch (approval.getAccepted()) {
                case PROGRESS -> throw new RuntimeException("초대가 진행중인 유저입니다.");
                case REFUSE -> throw new RuntimeException("초대를 거절한 유저입니다.");
                case ACCEPT -> throw new RuntimeException("초대를 수락한 유저입니다.");
                default -> throw new RuntimeException("초대에 대한 정보가 있으나 응답 상태가 지정되지 않았습니다.");
            }
        }

        Approval approval = Approval.builder()
                .family(family)
                .user(user)
                .accepted(AcceptStatus.PROGRESS)
                .build();

        approval = approvalRepository.save(approval);
        return approval.getId();
    }

    public ApprovalInfoResponse getApproval() {
        User user = authUtil.getUserPrincipal().user();
        Approval approval = approvalRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("초대가 없습니다."));
        Family family = approval.getFamily();

        return new ApprovalInfoResponse(
                approval.getId(),
                family.getId(),
                family.getName(),
                family.getDescription()
        );
    }
}
