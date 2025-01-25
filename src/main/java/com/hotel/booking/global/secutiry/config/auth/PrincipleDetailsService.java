package com.hotel.booking.global.secutiry.config.auth;


import com.hotel.booking.domain.role.entity.Role;
import com.hotel.booking.domain.role.repository.RoleRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


//시큐리티 설정에서 .loginProcessingUrl("/login") 요청이 오면
//UserDetailsService 타입으로 Ioc 되어 있는 loadUserByUsername 함수 실행 => 자동
@Service
public class PrincipleDetailsService implements UserDetailsService {

    private final RoleRepository roleRepository;

    public PrincipleDetailsService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    //시큐리티 session(내부 Authentication(내부 UserDetails)) 들어감
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Role role = roleRepository.findByUserName(username);
        if (role != null) {
            return new PrincipleDetails(role);
        }
        return null;
    }
}
