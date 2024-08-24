package com.shinhan_hackathon.the_family_guardian.global.auth.util;

import com.shinhan_hackathon.the_family_guardian.domain.user.entity.User;
import com.shinhan_hackathon.the_family_guardian.global.auth.dto.UserPrincipal;
import com.shinhan_hackathon.the_family_guardian.global.auth.service.UserDetailsServiceImpl;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class AuthUtil {

    private final UserDetailsServiceImpl userDetailsServiceImpl;

    public AuthUtil(UserDetailsServiceImpl userDetailsServiceImpl) {
        this.userDetailsServiceImpl = userDetailsServiceImpl;
    }

    public Authentication createNewAuthentication(Authentication authentication, User user) {

        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(user.getUsername());
        UsernamePasswordAuthenticationToken authenticated = UsernamePasswordAuthenticationToken.authenticated(userDetails, authentication.getCredentials(), userDetails.getAuthorities());
        authenticated.setDetails(userDetails);
        return authenticated;
    }

    public void updateAuthentication(User user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Authentication newAuthentication = createNewAuthentication(authentication, user);
        SecurityContextHolder.getContext().setAuthentication(newAuthentication);
    }

    public UserPrincipal getUserPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() == null) {
            throw new RuntimeException("유저 정보가 세션에 존재하지 않습니다.");
        }
        return (UserPrincipal) authentication.getPrincipal();
    }
}
