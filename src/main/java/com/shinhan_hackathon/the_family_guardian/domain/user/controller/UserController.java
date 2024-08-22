package com.shinhan_hackathon.the_family_guardian.domain.user.controller;

import com.shinhan_hackathon.the_family_guardian.domain.user.dto.SignupRequest;
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
    public ResponseEntity<?> signup(@RequestBody SignupRequest signupRequest) {

        userService.createUser(signupRequest);
        return ResponseEntity.ok("dd");
    }
}
