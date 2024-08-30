package com.shinhan_hackathon.the_family_guardian.domain.family.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.shinhan_hackathon.the_family_guardian.domain.family.dto.*;
import com.shinhan_hackathon.the_family_guardian.domain.family.service.FamilyService;
import com.shinhan_hackathon.the_family_guardian.domain.user.entity.Level;
import com.shinhan_hackathon.the_family_guardian.domain.user.entity.Role;
import com.shinhan_hackathon.the_family_guardian.global.auth.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/{family_id}/users")
    public ResponseEntity<FamilyUserResponse> getFamilyUsers(@PathVariable(value = "family_id") Long familyId) {
        FamilyUserResponse familyUsers = familyService.findFamilyUsers(familyId);
        return ResponseEntity.ok(familyUsers);
    }

    @PostMapping("/{family_id}/invite")
    public ResponseEntity<AddFamilyMemberResponse> addFamilyMember(@PathVariable(value = "family_id") Long family_id, @RequestBody AddFamilyMemberRequest addFamilyMemberRequest) throws JsonProcessingException {
        authUtil.checkAuthority(Role.OWNER);

        AddFamilyMemberResponse addFamilyMemberResponse = familyService.registerMember(family_id, addFamilyMemberRequest);
        return ResponseEntity.ok(addFamilyMemberResponse);
    }

    @PostMapping("/{family_id}/userRole")
    public ResponseEntity updateFamilyUserRole(@PathVariable(value = "family_id") Long familyId, @RequestBody UpdateUserRoleRequest updateUserRoleRequest) {
        authUtil.checkAuthority(Role.OWNER);
        if (updateUserRoleRequest.newRole().equals(Role.NONE) ||
        updateUserRoleRequest.newRole().equals(Role.OWNER)) {
            throw new IllegalArgumentException("변경할 수 없는 가족 역할입니다.");
        }

        UpdateUserRoleResponse updateUserRoleResponse = familyService.manageFamilyUserRole(familyId, updateUserRoleRequest.targetUserId(), updateUserRoleRequest.newRole());
        return ResponseEntity.ok(updateUserRoleResponse);
    }
}
