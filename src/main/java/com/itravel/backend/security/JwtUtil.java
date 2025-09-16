package com.itravel.backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String key;

    @Value("${jwt.expiration.ms}")
    private Long jwtExpirationTime;

    private SecretKey secretKey;

    @PostConstruct
    private void initKey() {
        byte[] byteKey = Base64.getDecoder().decode(key);
        this.secretKey = new SecretKeySpec(byteKey, "HmacSHA256");
    }

    public String generateToken(String email, String userId) {

        Date now = new Date();
        Date expiration = new Date(now.getTime() + jwtExpirationTime);
        String token = Jwts.builder().claim("userID", userId).subject(email).issuedAt(now).expiration(expiration)
                .signWith(secretKey).compact();
        return token;

    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            System.out.println("Token scaduto: " + e.getMessage());
            return false;
        } catch (UnsupportedJwtException e) {
            System.out.println("Token non supportato: " + e.getMessage());
            return false;
        } catch (MalformedJwtException e) {
            System.out.println("Token malformato: " + e.getMessage());
            return false;
        } catch (IllegalArgumentException e) {
            System.out.println("Token vuoto o null: " + e.getMessage());
            return false;
        }
    }

    public String extractEmail(String token) {
        try {
            Claims claims = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
            return claims.getSubject();
        } catch (Exception e) {
            return null;
        }
    }

}