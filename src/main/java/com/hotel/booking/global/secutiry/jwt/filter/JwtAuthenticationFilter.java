package com.hotel.booking.global.secutiry.jwt.filter;

import com.hotel.booking.global.secutiry.config.auth.PrincipleDetailsService;
import com.hotel.booking.global.secutiry.jwt.component.JwtTokenProvider;
import com.hotel.booking.global.secutiry.jwt.dto.JwtDTO;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


//JWT 검증 필터
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private final PrincipleDetailsService principleDetailsService;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, PrincipleDetailsService principleDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.principleDetailsService = principleDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        // 1. Authorization 헤더에서 JWT 토큰 추출
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // "Bearer " 제거
            // 2. 토큰 유효성 검증
            if (jwtTokenProvider.validateToken(token)) {
                // 3. 토큰에서 사용자 정보 추출
                JwtDTO jwtDTO = jwtTokenProvider.getJwtDTOFromToken(token);
                var userDetails = principleDetailsService.loadUserByUsername(jwtDTO.getUserName());

                // 4. SecurityContext에 인증 정보 설정
                UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // 5. 다음 필터로 요청 전달
        filterChain.doFilter(request, response);
    }
}
