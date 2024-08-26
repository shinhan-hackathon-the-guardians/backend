package com.shinhan_hackathon.the_family_guardian.domain.user.controller;

import com.shinhan_hackathon.the_family_guardian.domain.user.dto.*;
import com.shinhan_hackathon.the_family_guardian.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public ResponseEntity<AccountAuthResponse> sendAccountAuthCode(@RequestBody AccountAuthSendRequest accountAuthSendRequest) {

        AccountAuthResponse accountAuthResponse = userService.openAccountAuth(accountAuthSendRequest.accountNumber());
        return ResponseEntity.ok(accountAuthResponse);
    }

    @PostMapping("/accountAuthCode/check")
    public ResponseEntity<AccountAuthResponse> checkAccountAuthCode(@RequestBody AccountAuthCheckRequest accountAuthCheckRequest) {
        AccountAuthResponse accountAuthSendResponse = userService.checkAccountAuth(
                accountAuthCheckRequest.accountNumber(),
                accountAuthCheckRequest.authCode(),
                accountAuthCheckRequest.csrfToken()
        );
        return ResponseEntity.ok(accountAuthSendResponse);
    }

}
