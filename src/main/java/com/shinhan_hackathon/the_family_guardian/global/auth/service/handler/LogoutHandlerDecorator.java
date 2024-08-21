package com.shinhan_hackathon.the_family_guardian.global.auth.service.handler;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.util.WebUtils;

@Slf4j
public class LogoutHandlerDecorator extends SecurityContextLogoutHandler {

    private final String JSESSIONID = "JSESSIONID";

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        super.logout(request, response, authentication);

        Cookie jsessionid = WebUtils.getCookie(request, JSESSIONID);

        if (jsessionid != null) {
            log.info("remove jsessionid cookie: {}", jsessionid.getValue());
            jsessionid.setMaxAge(0);
            jsessionid.setPath("/");
            response.addCookie(jsessionid);
        }
    }
}
