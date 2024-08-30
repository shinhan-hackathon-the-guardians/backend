package com.shinhan_hackathon.the_family_guardian.domain.family.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shinhan_hackathon.the_family_guardian.domain.approval.service.ApprovalService;
import com.shinhan_hackathon.the_family_guardian.domain.family.dto.*;
import com.shinhan_hackathon.the_family_guardian.domain.family.entity.Family;
import com.shinhan_hackathon.the_family_guardian.domain.family.repository.FamilyRepository;
import com.shinhan_hackathon.the_family_guardian.domain.user.entity.Role;
import com.shinhan_hackathon.the_family_guardian.domain.user.entity.User;
import com.shinhan_hackathon.the_family_guardian.domain.user.repository.UserRepository;
import com.shinhan_hackathon.the_family_guardian.domain.user.service.UserService;
import com.shinhan_hackathon.the_family_guardian.global.auth.dto.UserPrincipal;
import com.shinhan_hackathon.the_family_guardian.global.auth.util.AuthUtil;
import com.shinhan_hackathon.the_family_guardian.global.fcm.FcmSender;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FamilyService {

    private final FamilyRepository familyRepository;
    private final UserRepository userRepository;
    private final AuthUtil authUtil;
    private final ApprovalService approvalService;
    private final UserService userService;
    private final FcmSender fcmSender;
    private final ObjectMapper jacksonObjectMapper;

    @Transactional
    public CreateFamilyResponse registerFamily(CreateFamilyRequest createFamilyRequest) {
        UserPrincipal principal = authUtil.getUserPrincipal();

        if (principal.getFamily() != null) {
            throw new RuntimeException("이미 소속된 가족이 있습니다.");
        }

        Long userId = Long.valueOf(principal.getUsername());
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("유저가 없습니다."));

        Family family = Family.builder()
                .name(createFamilyRequest.name())
                .description(createFamilyRequest.description())
                .approvalRequirement(createFamilyRequest.approvalRequirement())
                .users(new ArrayList<>(List.of(user)))
                .totalManagerCount(1)
                .createdAt(LocalDate.now(ZoneId.systemDefault()))
                .build();

        Family savedFamily = familyRepository.save(family);
        user.updateFamily(savedFamily);
        user.updateRole(Role.OWNER);
        authUtil.updateAuthentication(user);

        return new CreateFamilyResponse(
                savedFamily.getId(),
                savedFamily.getName(),
                savedFamily.getDescription(),
                savedFamily.getApprovalRequirement()
        );
    }

    public FamilyInfoResponse findFamilyInfo(Long familyId) {
        isValidFamilyUser(familyId);

        Family family = getFamilyFromDatabase(familyId);
        return new FamilyInfoResponse(
                family.getName(),
                family.getDescription(),
                family.getApprovalRequirement(),
                family.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))
        );
    }

    private void isValidFamilyUser(Long familyId) {
        UserPrincipal userPrincipal = authUtil.getUserPrincipal();
        Family userFamily = userPrincipal.getFamily();

        if (userFamily == null) {
            throw new RuntimeException("소속된 가족 정보가 없습니다.");
        }

        Long userFamilyId = userFamily.getId();
        if (!userFamilyId.equals(familyId)) {
            throw new AccessDeniedException("소속된 가족이 아닙니다..");
        }
    }

    private Family getFamilyFromDatabase(Long familyId) {
        return familyRepository.findById(familyId).orElseThrow(() -> new RuntimeException("가족이 존재하지 않습니다."));
    }


    @Transactional
    public UpdateFamilyResponse updateFamily(Long familyId, UpdateFamilyRequest updateFamilyRequest) {
        UserPrincipal userPrincipal = authUtil.getUserPrincipal();
        Long userFamilyId = userPrincipal.getFamily().getId();
        if (!userFamilyId.equals(familyId)) {
            throw new AccessDeniedException("소속된 가족이 아닙니다.");
        }

        Family family = getFamilyFromDatabase(familyId);

        if (!family.getName().equals(updateFamilyRequest.name())) {
            family.updateName(updateFamilyRequest.name());
        }
        if (!family.getDescription().equals(updateFamilyRequest.description())) {
            family.updateDescription(updateFamilyRequest.description());
        }
        if (!family.getApprovalRequirement().equals(updateFamilyRequest.approvalRequirement())) {
            family.updateApprovalRequirement(updateFamilyRequest.approvalRequirement());
        }

        return new UpdateFamilyResponse(
                family.getId(),
                family.getName(),
                family.getDescription(),
                family.getApprovalRequirement()
        );
    }

    @Transactional
    public AddFamilyMemberResponse registerMember(Long familyId, AddFamilyMemberRequest addFamilyMemberRequest) throws JsonProcessingException {
        Family family = authUtil.getUserPrincipal().getFamily();
        if (!family.getId().equals(familyId)) {
            throw new AccessDeniedException("소속된 가족이 아닙니다.");
        }

        User user = userRepository.findByPhone(addFamilyMemberRequest.phoneNumber()).orElseThrow(() -> new RuntimeException("유저가 없습니다."));
        if (user.getFamily() != null) {
            throw new RuntimeException("소속된 가족이 있는 사용자입니다.");
        }

        Long ownerId = Long.valueOf(authUtil.getUserPrincipal().getUsername());
        User owner = userRepository.findById(ownerId).orElseThrow(() -> new RuntimeException("유저가 없습니다."));

        FamilyInviteNotification familyInviteNotification = new FamilyInviteNotification(user.getId(), user.getName(), family.getDescription(), ownerId, owner.getName());
        String notificationBody = jacksonObjectMapper.writeValueAsString(familyInviteNotification);
        fcmSender.sendMessage(
                user.getDeviceToken(),
                "가족 초대 알림",
                notificationBody
        );

        Long approvalId = approvalService.createApproval(family, user, addFamilyMemberRequest.relationship());
        return new AddFamilyMemberResponse(
                user.getId(),
                user.getName(),
                user.getPhone(),
                approvalId
        );
    }

    @Transactional
    public UpdateUserRoleResponse manageFamilyUserRole(Long familyId, Long targetUserId, Role newRole) {
        Long ownerId = Long.valueOf(authUtil.getUserPrincipal().getUsername());
        User owner = userService.getUser(ownerId);
        Family ownerFamily = owner.getFamily();
        if (!ownerFamily.getId().equals(familyId)) {
            throw new RuntimeException("소속된 가족이 아닙니다.");
        }

        User target = userService.getUser(targetUserId);
        if (target.getFamily() == null ||
                !target.getFamily().getId().equals(familyId)) {
            throw new RuntimeException("같은 가족이 아닌 유저입니다.");
        }

        Role oldRole = target.getRole();
        if (oldRole.equals(newRole)) {
            throw new RuntimeException("기존 역할과 동일합니다.");
        }

        target.updateRole(newRole);

        Family familyForUpdate = getFamilyForUpdate(familyId);
        if (newRole.equals(Role.MEMBER)) {
            familyForUpdate.decreaseTotalManagerCount();
        } else {
            familyForUpdate.increaseTotalManagerCount();
        }

        return new UpdateUserRoleResponse(targetUserId, newRole);
    }

    public Family getFamilyForUpdate(Long id) {
        return familyRepository.findByIdForUpdate(id)
                .orElseThrow(() -> new RuntimeException("해당 가족이 없습니다."));
    }

    public Family findByGroupId(Long groupId) {
        Family family = familyRepository.findById(groupId)
                .orElseThrow(EntityNotFoundException::new);
        return family;
    }


    public FamilyUserResponse findFamilyUsers(Long familyId) {
        isValidFamilyUser(familyId);

        Family family = getFamilyFromDatabase(familyId);
        List<FamilyUserResponse.FamilyUser> familyUserList = family.getUsers().stream().map(user -> new FamilyUserResponse.FamilyUser(
                user.getId(),
                user.getName(),
                user.getBirthDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")),
                user.getLevel(),
                user.getRole(),
                user.getRelationship()
        )).toList();

        return new FamilyUserResponse(familyUserList);

    }
}
