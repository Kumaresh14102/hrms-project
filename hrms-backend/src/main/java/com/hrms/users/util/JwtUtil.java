package com.hrms.users.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    private final String SECRET = "hrms-jwt-secret-key-1234567890-abcdef";
    // private final Key SIGNING_KEY = Keys.hmacShaKeyFor(SECRET.getBytes());
    private final SecretKey SIGNING_KEY = Keys.hmacShaKeyFor(SECRET.getBytes());

    public String generateToken(String email, String role) {

        // return Jwts.builder()
        //         .setSubject(email)
        //         .claim("role", role)
        //         .setIssuedAt(new Date())
        //         .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
        //         .signWith(Keys.hmacShaKeyFor(SECRET.getBytes()), SignatureAlgorithm.HS256)
        //         .compact();
        return Jwts.builder()
        .subject(email)
        .claim("role", role)
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + 86400000)) // 24 hours
        .signWith(SIGNING_KEY) // Algorithm is detected automatically from the key
        .compact();
    }

    private Claims extractClaims(String token) {
        // return Jwts.parserBuilder()
        //         .setSigningKey(Keys.hmacShaKeyFor(SECRET.getBytes()))
        //         .build()
        //         .parseClaimsJws(token)
        //         .getBody();
        return Jwts.parser()
        .verifyWith(SIGNING_KEY)
        .build()
        .parseSignedClaims(token) // Changed from parseClaimsJws
        .getPayload(); // Changed from getBody()
    }
    
    public String getEmailFromToken(String token) {
    	return extractClaims(token).getSubject(); 
    }
    public String getRoleFromToken(String token) {
        return extractClaims(token).get("role", String.class);  //
    }
    public boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }
}
