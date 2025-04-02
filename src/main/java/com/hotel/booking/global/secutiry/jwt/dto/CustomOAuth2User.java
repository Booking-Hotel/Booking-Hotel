package com.hotel.booking.global.secutiry.jwt.dto;

import com.hotel.booking.domain.role.entity.Role;
import java.util.Collection;
import java.util.Map;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;


@Getter
public class CustomOAuth2User implements OAuth2User {

    private final OAuth2User delegate;
    private final Role role;
    private final Long userId;

    public CustomOAuth2User(OAuth2User delegate, Role role, Long userId) {
        this.delegate = delegate;
        this.role = role;
        this.userId = userId;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return delegate.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return delegate.getAuthorities();
    }

    @Override
    public String getName() {
        return delegate.getName();
    }
}
