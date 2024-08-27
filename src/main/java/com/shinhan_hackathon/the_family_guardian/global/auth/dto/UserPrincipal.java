package com.shinhan_hackathon.the_family_guardian.global.auth.dto;

import com.shinhan_hackathon.the_family_guardian.domain.family.entity.Family;
import com.shinhan_hackathon.the_family_guardian.domain.user.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;

public record UserPrincipal(
        User user
) implements UserDetails {

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authList = new ArrayList<>();
        authList.add(() -> user.getLevel().name());
        authList.add(() -> user.getRole().name());
        return authList;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return String.valueOf(user.getId());
    }

    public String getAccountNumber() {
        return user.getAccountNumber();
    }

    public Family getFamily() {
        return user.getFamily();
    }

    public String getDeviceToken() {
        String deviceToken = user.getDeviceToken();
        if (!StringUtils.hasText(deviceToken)) {
            throw new RuntimeException("device token이 저장되지 않았습니다.");
        }
        return deviceToken;
    }
}
