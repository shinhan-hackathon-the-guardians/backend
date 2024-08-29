package com.shinhan_hackathon.the_family_guardian.domain.user.controller;

import com.shinhan_hackathon.the_family_guardian.domain.user.dto.AccountAuthCheckRequest;
import com.shinhan_hackathon.the_family_guardian.domain.user.dto.AccountAuthResponse;
import com.shinhan_hackathon.the_family_guardian.domain.user.dto.AccountAuthSendRequest;
import com.shinhan_hackathon.the_family_guardian.domain.user.dto.SignupRequest;
import com.shinhan_hackathon.the_family_guardian.domain.user.dto.UpdateDeviceTokenRequest;
import com.shinhan_hackathon.the_family_guardian.domain.user.dto.UpdateDeviceTokenResponse;
import com.shinhan_hackathon.the_family_guardian.domain.user.dto.UserInfoResponse;
import com.shinhan_hackathon.the_family_guardian.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequest signupRequest) {
        userService.createUser(signupRequest);
        return ResponseEntity.ok("회원가입 성공");
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
}
