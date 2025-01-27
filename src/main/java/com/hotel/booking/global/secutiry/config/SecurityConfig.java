package com.hotel.booking.global.secutiry.config;


import com.hotel.booking.global.secutiry.config.auth.PrincipleDetailsService;
import com.hotel.booking.global.secutiry.jwt.component.JwtTokenProvider;
import com.hotel.booking.global.secutiry.jwt.dto.JwtDTO;
import com.hotel.booking.global.secutiry.jwt.filter.JwtAuthenticationFilter;
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

    public SecurityConfig(PrincipleDetailsService principleDetailsService,
        JwtAuthenticationFilter jwtAuthenticationFilter,
        JwtTokenProvider jwtTokenProvider) {
        this.principleDetailsService = principleDetailsService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Bean
    public BCryptPasswordEncoder encodePwd() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeHttpRequests(authorizeRequests -> authorizeRequests
            .requestMatchers("/api/user/**").hasAnyRole("USER", "SELLER", "ADMIN")
            .requestMatchers("/api/seller/**").hasAnyRole("SELLER", "ADMIN")
            .requestMatchers("/api/admin/**").hasRole("ADMIN")
            .anyRequest().permitAll()
        );
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        http.formLogin(form -> form
            .loginPage("/loginForm")
            .loginProcessingUrl("/login")
            .successHandler((request, response, authentication) -> {
                String userName = authentication.getName();
                String userRole = authentication.getAuthorities().iterator().next().getAuthority();

                JwtDTO jwtDTO = new JwtDTO(userRole, userName);
                String token = jwtTokenProvider.generateToken(jwtDTO);

                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"token\": \"" + token + "\", " +
                    "\"userRole\": \"" + jwtDTO.getUserRole() + "\", " +
                    "\"userName\": \"" + jwtDTO.getUserName() + "\"}");
                response.getWriter().flush();
            })
            .permitAll()
        );

        return http.build();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> {
            String uri = request.getRequestURI();
            if (uri.startsWith("/admin")) {
                response.sendRedirect("/loginForm"); // Redirect to admin login page
            } else if (uri.startsWith("/seller") || uri.startsWith("/user")) {
                response.sendRedirect("/loginForm"); // Redirect to kiosk login page
            } else {
                response.sendRedirect("/loginForm");
            }
        };
    }
}

