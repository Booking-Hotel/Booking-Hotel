package com.hotel.booking.global.secutiry.config;


import com.hotel.booking.global.secutiry.config.auth.PrincipleDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig {

    private final PrincipleDetailsService principleDetailsService;

    public SecurityConfig(PrincipleDetailsService principleDetailsService) {
        this.principleDetailsService = principleDetailsService;
    }

    @Bean
    public BCryptPasswordEncoder encodePwd() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // Disable CSRF for this example
        http.csrf().disable();
        // Set up access control
        http.authorizeHttpRequests(authorizeRequests -> authorizeRequests
            .requestMatchers("/api/user/**").hasAnyRole("USER", "SELLER", "ADMIN")
            .requestMatchers("/api/seller/**").hasAnyRole("SELLER", "ADMIN")
            .requestMatchers("/api/admin/**").hasRole("ADMIN")
            .anyRequest().permitAll()
        );
        http.formLogin(form -> form
            .loginPage("/loginForm")
            .loginProcessingUrl("/login")
            .successHandler((request, response, authentication) -> {
                // Role-based redirection
                String role = authentication.getAuthorities().iterator().next().getAuthority();
                if (role.equals("ROLE_USER")) {
                    response.sendRedirect("/page/main");
                } else if (role.equals("ROLE_SELLER")) {
                    response.sendRedirect("/page/accommodation/register");
                } else if (role.equals("ROLE_ADMIN")) {
                    response.sendRedirect("/page/admin");
                }
            })
            .permitAll()
        );

        // Configure exception handling
        http.exceptionHandling(exceptionHandling -> exceptionHandling
            .authenticationEntryPoint(authenticationEntryPoint())
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

