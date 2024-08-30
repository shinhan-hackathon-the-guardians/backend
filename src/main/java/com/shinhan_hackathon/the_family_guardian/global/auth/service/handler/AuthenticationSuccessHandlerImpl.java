package com.shinhan_hackathon.the_family_guardian.global.auth.service.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shinhan_hackathon.the_family_guardian.domain.user.dto.LoginResponse;
import com.shinhan_hackathon.the_family_guardian.global.auth.dto.UserPrincipal;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

public class AuthenticationSuccessHandlerImpl implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException{
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

        Long familyId = null;
        String familyName = null;
        if (principal.getFamily() != null) {
            familyId = principal.getFamily().getId();
            familyName = principal.getFamily().getName();
        }
        LoginResponse loginResponse = new LoginResponse(
                Long.valueOf(principal.getUsername()),
                principal.user().getName(),
                principal.user().getLevel(),
                principal.user().getRole(),
                familyId,
                familyName
        );

        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().print(objectMapper.writeValueAsString(loginResponse));
    }
}
