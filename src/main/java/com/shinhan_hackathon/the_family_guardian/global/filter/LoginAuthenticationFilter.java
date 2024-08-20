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

import java.io.IOException;

public class LoginAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public LoginAuthenticationFilter(
            String defaultFilterProcessesUrl,
            AuthenticationManager authenticationManager
    ) {
        super(defaultFilterProcessesUrl, authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        // extract login dto
        String method = request.getMethod();
        if (method == null || !method.equals(HttpMethod.POST.name())) {
            throw new RuntimeException();
        }

        ServletInputStream inputStream = request.getInputStream();
        ObjectMapper objectMapper = new ObjectMapper();
        LoginRequest loginRequest = objectMapper.readValue(inputStream, LoginRequest.class);

        UsernamePasswordAuthenticationToken unauthenticatedToken = UsernamePasswordAuthenticationToken.unauthenticated(loginRequest.email(), loginRequest.password());
        return this.getAuthenticationManager().authenticate(unauthenticatedToken);
    }
}
