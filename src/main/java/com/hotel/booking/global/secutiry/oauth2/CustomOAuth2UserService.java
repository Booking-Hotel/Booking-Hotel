package com.hotel.booking.global.secutiry.oauth2;

import com.hotel.booking.domain.role.entity.Role;
import com.hotel.booking.domain.role.repository.RoleRepository;
import com.hotel.booking.domain.user.entity.User;
import com.hotel.booking.domain.user.repository.UserRepository;
import com.hotel.booking.global.secutiry.jwt.dto.CustomOAuth2User;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public CustomOAuth2UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        // Role 저장
        Role role = roleRepository.findByUserName(email);
        if (role == null) {
            role = new Role();
            role.setUserName(email);
            role.setUserPassword(null);
            role.setUserRole("ROLE_USER");
            role.setStatus("1");
            roleRepository.save(role);
        }

        // User 저장
        User user = userRepository.findByRole(role);
        if (user == null) {
            user = new User();
            user.setName(name);
            user.setBirth(null); // 구글은 생일 안 줌
            user.setRole(role);
            user = userRepository.save(user);
        }
        // ✅ 사용자 정보를 CustomOAuth2User에 담아서 반환!
        return new CustomOAuth2User(oAuth2User, role, user.getId());
    }
}
