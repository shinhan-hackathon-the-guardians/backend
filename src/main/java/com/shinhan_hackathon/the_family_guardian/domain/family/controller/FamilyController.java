package com.shinhan_hackathon.the_family_guardian.domain.family.controller;

import com.shinhan_hackathon.the_family_guardian.domain.family.dto.AddFamilyMemberRequest;
import com.shinhan_hackathon.the_family_guardian.domain.family.dto.CreateFamilyRequest;
import com.shinhan_hackathon.the_family_guardian.domain.family.dto.CreateFamilyResponse;
import com.shinhan_hackathon.the_family_guardian.domain.family.dto.UpdateFamilyRequest;
import com.shinhan_hackathon.the_family_guardian.domain.family.service.FamilyService;
import com.shinhan_hackathon.the_family_guardian.domain.user.entity.Level;
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

    @GetMapping("/{family_id}")
    public ResponseEntity getFamilyInfoById(@PathVariable(value = "family_id") Long familyId) {
        return ResponseEntity.ok(familyId);
    }

    @PostMapping
    public ResponseEntity<CreateFamilyResponse> createFamily(@RequestBody CreateFamilyRequest createFamilyRequest) {
        SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .filter(authority -> authority.getAuthority().equals(Level.GUARDIAN.name()))
                .findFirst()
                .orElseThrow(() -> new AccessDeniedException("접근 권한이 없습니다."));

        CreateFamilyResponse createFamilyResponse = familyService.registerFamily(createFamilyRequest);
        return ResponseEntity.ok(createFamilyResponse);
    }

    @PutMapping("/{family_id}")
    public ResponseEntity updateFamily(@PathVariable(value = "family_id") Long familyId, @RequestBody UpdateFamilyRequest updateFamilyRequest) {
        return ResponseEntity.ok(updateFamilyRequest);
    }

    @PostMapping("/{family_id}/invite")
    public ResponseEntity addFamilyMember(@PathVariable(value = "family_id") String family_id, @RequestBody AddFamilyMemberRequest addFamilyMemberRequest) {
        return ResponseEntity.ok(addFamilyMemberRequest);

    }
}
