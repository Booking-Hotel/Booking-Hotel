package com.hotel.booking.global.secutiry.oauth2;

import com.hotel.booking.domain.role.entity.Role;
import com.hotel.booking.domain.role.repository.RoleRepository;
import com.hotel.booking.domain.user.entity.User;
import com.hotel.booking.domain.user.repository.UserRepository;
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

        // Google 계정 정보 가져오기
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        // Role 테이블에 사용자 정보가 없으면 저장
        Role role = roleRepository.findByUserName(email);
        if (role == null) {
            role = new Role();
            role.setUserName(email);    // Google 이메일을 userName으로 사용
            role.setUserPassword(null); // 비밀번호는 null로 설정
            role.setUserRole("ROLE_USER");
            role.setStatus("1");        // 활성 상태
            roleRepository.save(role);
            User user = new User();
            user.setRole(role);
            user.setName(name);
            user.setBirth(null); // Google에서는 생년월일을 제공하지 않으므로 기본값으로 설정
            userRepository.save(user);
        }
        return oAuth2User;
    }
}
