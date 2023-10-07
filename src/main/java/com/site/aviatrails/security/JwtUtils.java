package com.site.aviatrails.security;

import com.site.aviatrails.domain.Role;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;

@Slf4j
@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    public String generatedJwtToken(String login) {
        return Jwts.builder()
                .setSubject(login)
                .setExpiration(new Date(new Date().getTime() + expiration))
                .signWith(SignatureAlgorithm.HS256, secret).compact();
    }

    public Boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            log.info("Invalid JWT Signature: " + e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token: " + e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token: " + e);
        } catch (IllegalArgumentException e) {
            log.info("Illegal arguments: " + e);
        }
        return false;
    }

    public String getTokenFromHttpRequest(HttpServletRequest request) {
        final String bearerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public String getLoginFromJwt(String token) {
        try {
            return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
        } catch (Exception e) {
            log.info("Cannot get login from JWT: " + e);
        }
        return null;
    }

//    public boolean canView(HttpMethod auth, String request) {
//        Authentication authentication;
//        request = authentication.getName();
//    }
}
