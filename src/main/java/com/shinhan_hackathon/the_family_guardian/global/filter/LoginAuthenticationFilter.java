package com.shinhan_hackathon.the_family_guardian.global.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shinhan_hackathon.the_family_guardian.global.auth.dto.LoginRequest;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.context.SecurityContextRepository;

import java.io.IOException;

public class LoginAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public LoginAuthenticationFilter(
            String defaultFilterProcessesUrl,
            AuthenticationManager authenticationManager,
            SecurityContextRepository securityContextRepository,
            AuthenticationSuccessHandler authenticationSuccessHandler
    ) {
        super(defaultFilterProcessesUrl, authenticationManager);
        super.setSecurityContextRepository(securityContextRepository);
        super.setAuthenticationSuccessHandler(authenticationSuccessHandler);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        String method = request.getMethod();
        if (method == null || !method.equals(HttpMethod.POST.name())) {
            throw new RuntimeException("잘못된 HTTP Method 입니다.");
        }

        ServletInputStream inputStream = request.getInputStream();
        ObjectMapper objectMapper = new ObjectMapper();
        LoginRequest loginRequest = objectMapper.readValue(inputStream, LoginRequest.class);

        UsernamePasswordAuthenticationToken unauthenticatedToken = UsernamePasswordAuthenticationToken.unauthenticated(loginRequest.username(), loginRequest.password());
        return this.getAuthenticationManager().authenticate(unauthenticatedToken);
    }
}
