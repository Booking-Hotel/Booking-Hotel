package com.hotel.booking.global.secutiry.jwt.component;


import com.hotel.booking.global.secutiry.jwt.dto.JwtDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {
//    private final String SECRET_KEY = "e3124f7a6d52605ed91c90fef1ddc9024823390387b9078a578779c606f21269";
    private final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    private final long EXPIRATION_TIME = 1000 * 60 * 60 * 10; // 토큰 유효기간: 10시간

    public String generateToken(JwtDTO jwtDTO) {
        return Jwts.builder()
            .setSubject(jwtDTO.getUserName())
            .claim("role", jwtDTO.getUserRole())
            .claim("userId", jwtDTO.getUserId())
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
            .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
            .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public JwtDTO getJwtDTOFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
            .setSigningKey(SECRET_KEY)
            .build()
            .parseClaimsJws(token)
            .getBody();

        String role = claims.get("role", String.class);
        String userName = claims.getSubject(); // 또는 claims.get("userName", String.class);
        Long userId = claims.get("userId", Integer.class).longValue(); // or get as Long.class in 최신 JJWT

        return new JwtDTO(role, userName, userId);
    }

    public Long extractUserIdFromRequest(HttpServletRequest request) {
        String token = resolveToken(request);
        if (token != null && validateToken(token)) {
            Claims claims = getClaims(token);
            Object userIdObj = claims.get("userId");

            if (userIdObj instanceof Integer) {
                return ((Integer) userIdObj).longValue();
            } else if (userIdObj instanceof Long) {
                return (Long) userIdObj;
            } else if (userIdObj instanceof String) {
                return Long.parseLong((String) userIdObj);
            }
        }
        throw new IllegalArgumentException("유효하지 않은 JWT 토큰이거나 userId가 없습니다.");
    }

    public String resolveToken(HttpServletRequest request) {
        // 1. 쿠키에서 토큰 찾기
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("JWT".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        // 2. Authorization 헤더에서 토큰 찾기 (Bearer 방식)
        String bearer = request.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }

        return null;
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(SECRET_KEY)
            .build()
            .parseClaimsJws(token)
            .getBody();
    }
}
