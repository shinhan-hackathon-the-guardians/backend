package com.shinhan_hackathon.the_family_guardian.domain.user.controller;

import com.shinhan_hackathon.the_family_guardian.domain.family.dto.FamilyInviteNotification;
import com.shinhan_hackathon.the_family_guardian.domain.user.dto.*;
import com.shinhan_hackathon.the_family_guardian.domain.user.service.UserService;
import com.shinhan_hackathon.the_family_guardian.global.auth.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthUtil authUtil;

    @PostMapping("/signup")
    public ResponseEntity<LoginResponse> signup(@RequestBody SignupRequest signupRequest) {
        LoginResponse signupResult = userService.createUser(signupRequest);
        return ResponseEntity.ok(signupResult);
    }

    @PostMapping("/accountAuthCode")
    public ResponseEntity<AccountAuthResponse> sendAccountAuthCode(
            @RequestBody AccountAuthSendRequest accountAuthSendRequest) {

        AccountAuthResponse accountAuthResponse = userService.openAccountAuth(accountAuthSendRequest.accountNumber());
        return ResponseEntity.ok(accountAuthResponse);
    }

    @PostMapping("/accountAuthCode/check")
    public ResponseEntity<AccountAuthResponse> checkAccountAuthCode(
            @RequestBody AccountAuthCheckRequest accountAuthCheckRequest) {
        AccountAuthResponse accountAuthSendResponse = userService.checkAccountAuth(
                accountAuthCheckRequest.accountNumber(),
                accountAuthCheckRequest.authCode(),
                accountAuthCheckRequest.csrfToken()
        );
        return ResponseEntity.ok(accountAuthSendResponse);
    }

    @PostMapping("/deviceToken")
    public ResponseEntity<UpdateDeviceTokenResponse> updateDeviceToken(
            @RequestBody UpdateDeviceTokenRequest updateDeviceTokenRequest) {
        UpdateDeviceTokenResponse updateDeviceTokenResponse = userService.setDeviceToken(
                updateDeviceTokenRequest.deviceToken());
        return ResponseEntity.ok(updateDeviceTokenResponse);
    }

    @GetMapping("/info")
    public ResponseEntity<?> getUserInfo() {
        log.info("UserController.getUserInfo() is called.");
        UserInfoResponse userInfo = userService.getUserInfo();

        return ResponseEntity.ok(userInfo);
    }

    @GetMapping("/invite")
    public ResponseEntity<List<FamilyInviteNotification>> getFamilyInviteRequest() {
        List<FamilyInviteNotification> familyInviteList = userService.findFamilyInviteRequest();
        return ResponseEntity.ok(familyInviteList);
    }
}
