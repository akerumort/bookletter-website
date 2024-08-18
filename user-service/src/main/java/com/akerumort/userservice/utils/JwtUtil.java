package com.akerumort.userservice.utils;

import com.akerumort.userservice.exceptions.JwtValidationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.stereotype.Component;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class JwtUtil {

    private static final Logger logger = LogManager.getLogger(JwtUtil.class);
    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final int TOKEN_VALIDITY = 3600 * 1000; // 1 hour

    private final Set<String> blacklistedTokens = ConcurrentHashMap.newKeySet();

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        String token = Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY))
                .signWith(SECRET_KEY)
                .compact();
        System.out.println("Generated token: " + token);
        return token;
    }

    public Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (SignatureException e) {
            // logger.error("JWT signature does not match: {}", e.getMessage(), e);
            logger.error("JWT signature does not match: {}", e.getMessage());
            throw new JwtValidationException("JWT signature does not match locally computed signature.");
        } catch (ExpiredJwtException e) {
            // logger.error("JWT token has expired: {}", e.getMessage(), e);
            logger.error("JWT token has expired: {}", e.getMessage());
            throw new JwtValidationException("JWT token has expired.");
        } catch (Exception e) {
            // logger.error("JWT extraction failed: {}", e.getMessage(), e);
            logger.error("JWT extraction failed: {}", e.getMessage());
            throw new JwtValidationException("JWT validity cannot be asserted and should not be trusted.");
        }
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    public boolean validateToken(String token, String username) {
        return (extractUsername(token).equals(username) && !isTokenExpired(token));
    }

    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }

    public void invalidateToken(String token) {
        blacklistedTokens.add(token);
    }
}