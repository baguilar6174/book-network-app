package com.baguilar.book_api.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    @Value("${application.security.jwt.secret-key}")
    private String privateKey;

    @Value("${application.security.jwt.user-generator}")
    private String userGenerator;

    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    public String createToken(Authentication authentication){
        Algorithm algorithm = Algorithm.HMAC256(this.privateKey);
        String username = authentication.getPrincipal().toString();
        String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));
        return JWT.create().withIssuer(this.userGenerator)
                .withSubject(username)
                .withClaim("authorities", authorities)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis()+ this.jwtExpiration))
                .withJWTId(UUID.randomUUID().toString())
                .withNotBefore(new Date(System.currentTimeMillis()))
                .sign(algorithm);
    }
}
