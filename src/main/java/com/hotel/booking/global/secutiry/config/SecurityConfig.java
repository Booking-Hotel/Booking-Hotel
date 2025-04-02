package com.hotel.booking.global.secutiry.config;


import com.hotel.booking.global.secutiry.config.auth.PrincipleDetailsService;
import com.hotel.booking.global.secutiry.jwt.component.JwtTokenProvider;
import com.hotel.booking.global.secutiry.jwt.dto.CustomOAuth2User;
import com.hotel.booking.global.secutiry.jwt.dto.JwtDTO;
import com.hotel.booking.global.secutiry.jwt.filter.JwtAuthenticationFilter;
import com.hotel.booking.global.secutiry.oauth2.CustomOAuth2UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig {

    private final PrincipleDetailsService principleDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtTokenProvider jwtTokenProvider;

    private final CustomOAuth2UserService customOAuth2UserService;

    public SecurityConfig(PrincipleDetailsService principleDetailsService,
        JwtAuthenticationFilter jwtAuthenticationFilter,
        JwtTokenProvider jwtTokenProvider,
        CustomOAuth2UserService customOAuth2UserService) {
        this.principleDetailsService = principleDetailsService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.jwtTokenProvider = jwtTokenProvider;
        this.customOAuth2UserService = customOAuth2UserService;
    }

    @Bean
    public BCryptPasswordEncoder encodePwd() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.csrf().disable();
//        http.authorizeHttpRequests(authorizeRequests -> authorizeRequests
//            .requestMatchers("/api/user/**").hasAnyRole("USER", "SELLER", "ADMIN")
//            .requestMatchers("/api/seller/**").hasAnyRole("SELLER", "ADMIN")
//            .requestMatchers("/api/admin/**").hasRole("ADMIN")
//            .anyRequest().permitAll()
//        );
//        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
//
//        http.formLogin(form -> form
//            .loginPage("/loginForm")
//            .loginProcessingUrl("/login")
//            .successHandler((request, response, authentication) -> {
//                String userName = authentication.getName();
//                String userRole = authentication.getAuthorities().iterator().next().getAuthority();
//
//                // JWT 생성
//                JwtDTO jwtDTO = new JwtDTO(userRole, userName);
//                String token = jwtTokenProvider.generateToken(jwtDTO);
//
//                // JWT를 HttpOnly 쿠키에 저장
//                Cookie jwtCookie = new Cookie("JWT", token);
//                jwtCookie.setHttpOnly(true); // 클라이언트의 JavaScript에서 접근 불가
//                jwtCookie.setSecure(true); // HTTPS에서만 전송 (개발 환경에서는 HTTPS 설정 필요)
//                jwtCookie.setPath("/"); // 애플리케이션 전역에서 사용 가능
//
//                // 쿠키를 응답에 추가
//                response.addCookie(jwtCookie);
//
//                // SameSite 속성을 수동으로 설정
//                String cookieHeader = String.format("JWT=%s; HttpOnly; Secure; SameSite=Strict; Path=/; Max-Age=%d",
//                    token, 60 * 60 * 10);
//                response.setHeader("Set-Cookie", cookieHeader);
//
//                // 응답 데이터 반환 (필요 시 JSON 응답 추가)
//                response.setContentType("application/json;charset=UTF-8");
//                response.getWriter().write("{\"message\": \"Login successful\", " +
//                    "\"userRole\": \"" + jwtDTO.getUserRole() + "\", " +
//                    "\"userName\": \"" + jwtDTO.getUserName() + "\"}");
//                response.getWriter().flush();
//            })
//            .permitAll()
//        );
//
//        return http.build();
//    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/user/**").hasAnyRole("USER", "SELLER", "ADMIN")
            .requestMatchers("/api/seller/**").hasAnyRole("SELLER", "ADMIN")
            .requestMatchers("/api/admin/**").hasRole("ADMIN")
            .anyRequest().permitAll()
        );

        http.oauth2Login(oauth2 -> oauth2
            .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
            .successHandler((request, response, authentication) -> {
                // ✅ OAuth2User에서 사용자 정보 꺼내기
                Object principal = authentication.getPrincipal();

                if (principal instanceof CustomOAuth2User customUser) {
                    String userName = customUser.getRole().getUserName(); // email
                    String userRole = customUser.getRole().getUserRole();
                    Long userId = customUser.getUserId();

                    JwtDTO jwtDTO = new JwtDTO(userRole, userName, userId);
                    String token = jwtTokenProvider.generateToken(jwtDTO);

                    Cookie jwtCookie = new Cookie("JWT", token);
                    jwtCookie.setHttpOnly(true);
                    jwtCookie.setSecure(true);
                    jwtCookie.setPath("/");
                    jwtCookie.setMaxAge(60 * 60 * 10);

                    response.addCookie(jwtCookie);

                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().write("{\"message\": \"Google login successful\", " +
                        "\"userRole\": \"" + jwtDTO.getUserRole() + "\", " +
                        "\"userName\": \"" + jwtDTO.getUserName() + "\", " +
                        "\"userId\": \"" + jwtDTO.getUserId() + "\"}");
                    response.getWriter().flush();
                } else {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "OAuth2 로그인 사용자 정보를 가져올 수 없습니다.");
                }
            })
            .failureUrl("/login?error")
        );

        return http.build();
    }
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> {
            String uri = request.getRequestURI();
            if (uri.startsWith("/admin")) {
                response.sendRedirect("/"); // Redirect to admin login page
            } else if (uri.startsWith("/seller") || uri.startsWith("/user")) {
                response.sendRedirect("/"); // Redirect to kiosk login page
            } else {
                response.sendRedirect("/");
            }
        };
    }
}

