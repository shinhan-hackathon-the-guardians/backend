package com.shinhan_hackathon.the_family_guardian.global.auth.dto;

import com.shinhan_hackathon.the_family_guardian.domain.user.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public record
UserPrinciple (
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
}
