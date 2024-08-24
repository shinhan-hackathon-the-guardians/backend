package com.shinhan_hackathon.the_family_guardian.domain.family.service;

import com.shinhan_hackathon.the_family_guardian.domain.family.dto.CreateFamilyRequest;
import com.shinhan_hackathon.the_family_guardian.domain.family.dto.CreateFamilyResponse;
import com.shinhan_hackathon.the_family_guardian.domain.family.entity.Family;
import com.shinhan_hackathon.the_family_guardian.domain.family.repository.FamilyRepository;
import com.shinhan_hackathon.the_family_guardian.domain.user.entity.User;
import com.shinhan_hackathon.the_family_guardian.domain.user.repository.UserRepository;
import com.shinhan_hackathon.the_family_guardian.domain.user.service.UserService;
import com.shinhan_hackathon.the_family_guardian.global.auth.dto.UserPrincipal;
import com.shinhan_hackathon.the_family_guardian.global.auth.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FamilyService {

    private final FamilyRepository familyRepository;
    private final UserRepository userRepository;
    private final AuthUtil authUtil;

    @Transactional
    public CreateFamilyResponse registerFamily(CreateFamilyRequest createFamilyRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

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
                .build();

        Family savedFamily = familyRepository.save(family);
        user.updateFamily(savedFamily);
        authUtil.updateAuthentication(user);

        return new CreateFamilyResponse(
                savedFamily.getId(),
                savedFamily.getName(),
                savedFamily.getDescription(),
                savedFamily.getApprovalRequirement()
        );
    }
}
