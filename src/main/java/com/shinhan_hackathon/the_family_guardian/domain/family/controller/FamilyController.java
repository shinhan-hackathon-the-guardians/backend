package com.shinhan_hackathon.the_family_guardian.domain.family.controller;

import com.shinhan_hackathon.the_family_guardian.domain.family.dto.*;
import com.shinhan_hackathon.the_family_guardian.domain.family.service.FamilyService;
import com.shinhan_hackathon.the_family_guardian.domain.user.entity.Level;
import com.shinhan_hackathon.the_family_guardian.domain.user.entity.Role;
import com.shinhan_hackathon.the_family_guardian.global.auth.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/family")
@RequiredArgsConstructor
public class FamilyController {

    private final FamilyService familyService;
    private final AuthUtil authUtil;

    @GetMapping("/{family_id}")
    public ResponseEntity<FamilyInfoResponse> getFamilyInfoById(@PathVariable(value = "family_id") Long familyId) {
        FamilyInfoResponse familyInfo = familyService.getFamilyInfo(familyId);
        return ResponseEntity.ok(familyInfo);
    }

    @PostMapping
    public ResponseEntity<CreateFamilyResponse> createFamily(@RequestBody CreateFamilyRequest createFamilyRequest) {
        authUtil.checkAuthority(Level.GUARDIAN);

        CreateFamilyResponse createFamilyResponse = familyService.registerFamily(createFamilyRequest);
        return ResponseEntity.ok(createFamilyResponse);
    }

    @PutMapping("/{family_id}")
    public ResponseEntity<UpdateFamilyResponse> updateFamily(@PathVariable(value = "family_id") Long familyId, @RequestBody UpdateFamilyRequest updateFamilyRequest) {
        authUtil.checkAuthority(Role.OWNER, Role.MANAGER);

        UpdateFamilyResponse updateFamilyResponse = familyService.updateFamily(familyId, updateFamilyRequest);
        return ResponseEntity.ok(updateFamilyResponse);
    }

    @PostMapping("/{family_id}/invite")
    public ResponseEntity<AddFamilyMemberResponse> addFamilyMember(@PathVariable(value = "family_id") Long family_id, @RequestBody AddFamilyMemberRequest addFamilyMemberRequest) {
        authUtil.checkAuthority(Role.OWNER);

        AddFamilyMemberResponse addFamilyMemberResponse = familyService.registerMember(family_id, addFamilyMemberRequest);
        return ResponseEntity.ok(addFamilyMemberResponse);
    }
}
