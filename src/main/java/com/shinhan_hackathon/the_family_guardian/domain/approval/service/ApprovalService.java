package com.shinhan_hackathon.the_family_guardian.domain.approval.service;

import com.shinhan_hackathon.the_family_guardian.domain.approval.dto.ApprovalInfoResponse;
import com.shinhan_hackathon.the_family_guardian.domain.approval.dto.ApprovalReplyResponse;
import com.shinhan_hackathon.the_family_guardian.domain.approval.entity.AcceptStatus;
import com.shinhan_hackathon.the_family_guardian.domain.approval.entity.Approval;
import com.shinhan_hackathon.the_family_guardian.domain.approval.repository.ApprovalRepository;
import com.shinhan_hackathon.the_family_guardian.domain.family.entity.Family;
import com.shinhan_hackathon.the_family_guardian.domain.family.repository.FamilyRepository;
import com.shinhan_hackathon.the_family_guardian.domain.user.entity.Role;
import com.shinhan_hackathon.the_family_guardian.domain.user.entity.User;
import com.shinhan_hackathon.the_family_guardian.domain.user.repository.UserRepository;
import com.shinhan_hackathon.the_family_guardian.global.auth.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ApprovalService {
    private final ApprovalRepository approvalRepository;
    private final AuthUtil authUtil;
    private final UserRepository userRepository;
    private final FamilyRepository familyRepository;

    @Transactional
    public Long createApproval(Family family, User user, String relationship) {
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
                .relationship(relationship)
                .build();

        approval = approvalRepository.save(approval);
        return approval.getId();
    }

    public List<ApprovalInfoResponse> getApproval() {
        User user = authUtil.getUserPrincipal().user();

        List<Approval> approvalList = approvalRepository.findAllByUserAndAccepted(user, AcceptStatus.PROGRESS);

        List<ApprovalInfoResponse> approvalInfoResponseList = approvalList.stream().map(approval -> {
            Family family = approval.getFamily();
            return new ApprovalInfoResponse(
                    approval.getId(),
                    family.getId(),
                    family.getName(),
                    family.getDescription()
            );
        }).toList();

        return approvalInfoResponseList;
    }

    @Transactional
    public ApprovalReplyResponse acceptApproval(Long approvalId, Boolean approvalStatus) {
        Approval approval = approvalRepository.findById(approvalId).orElseThrow(() -> new RuntimeException("요청이 없습니다."));
        Long userId = Long.valueOf(authUtil.getUserPrincipal().getUsername());
        User user = userRepository.getReferenceById(userId);

        if (!approval.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("요청 수락 대상 사용자가 아닙니다.");
        }

        switch (approval.getAccepted()) {
            case ACCEPT -> throw new RuntimeException("이미 수락된 요청입니다");
            case REFUSE -> throw new RuntimeException("이미 거절된 요청입니다.");
        }

        approval.updateAccepted(approvalStatus);
        ApprovalReplyResponse.FamilyInfo familyInfo = null;
        if (approvalStatus) {
            Long familyId = approval.getFamily().getId();
            Family family = familyRepository.findById(familyId).orElseThrow(() -> new RuntimeException("존재하지 않는 가족입니다."));
            familyInfo = new ApprovalReplyResponse.FamilyInfo(
                    family.getId(),
                    family.getName(),
                    family.getDescription()
            );

            family.addUser(user);
            user.updateFamily(family);
            user.updateRole(Role.MEMBER);
            user.updateRelationship(approval.getRelationship());

            authUtil.updateAuthentication(user);
        }

        return new ApprovalReplyResponse(
                approval.getAccepted(),
                familyInfo
        );
    }
}
