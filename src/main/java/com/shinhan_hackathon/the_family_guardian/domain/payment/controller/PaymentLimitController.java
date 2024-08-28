package com.shinhan_hackathon.the_family_guardian.domain.payment.controller;

import com.shinhan_hackathon.the_family_guardian.domain.payment.dto.UpdateLimitRequest;
import com.shinhan_hackathon.the_family_guardian.domain.payment.dto.UpdateLimitResponse;
import com.shinhan_hackathon.the_family_guardian.domain.payment.service.PaymentLimitService;
import com.shinhan_hackathon.the_family_guardian.domain.user.entity.Role;
import com.shinhan_hackathon.the_family_guardian.global.auth.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/limit")
@RequiredArgsConstructor
public class PaymentLimitController {

    private final PaymentLimitService paymentLimitService;
    private final AuthUtil authUtil;

    @PutMapping
    public ResponseEntity<UpdateLimitResponse> updateMemberLimit(@RequestBody UpdateLimitRequest updateLimitRequest) {
        authUtil.checkAuthority(Role.OWNER);

        UpdateLimitResponse updateLimitResponse = paymentLimitService.manageMemberLimit(updateLimitRequest);
        return ResponseEntity.ok(updateLimitResponse);
    }
}
