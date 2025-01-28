package com.hotel.booking.global.secutiry.jwt.component;


import com.hotel.booking.global.secutiry.jwt.dto.JwtDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
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
        Claims claims = Jwts.parser()
            .setSigningKey(SECRET_KEY)
            .parseClaimsJws(token)
            .getBody();
        return new JwtDTO(
            claims.get("role", String.class),
            claims.getSubject()
        );
    }
}
