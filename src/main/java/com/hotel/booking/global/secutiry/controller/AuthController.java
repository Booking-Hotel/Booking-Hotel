package com.hotel.booking.global.secutiry.controller;

import com.hotel.booking.domain.user.entity.User;
import com.hotel.booking.domain.user.repository.UserRepository;
import com.hotel.booking.global.secutiry.config.auth.PrincipleDetails;
import com.hotel.booking.global.secutiry.jwt.component.JwtTokenProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    public AuthController(JwtTokenProvider jwtTokenProvider, UserRepository userRepository){
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
    }
    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {
        // JWT 쿠키 삭제
        Cookie cookie = new Cookie("JWT", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return "redirect:/";
    }

    @GetMapping("/loginPage")
    public String login(){
        return "login";
    }

    @GetMapping("/")
    public String login1(){
        return "login";
    }

    @GetMapping("/info")
    public ResponseEntity<?> getUserInfo(HttpServletRequest request) {
        Long userId = jwtTokenProvider.extractUserIdFromRequest(request);
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("사용자 없음"));

        return ResponseEntity.ok(Map.of(
            "id", user.getId(),
            "name", user.getName()
        ));
    }
}
