package com.example.demo.restapi.config;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenConfig {
	
	public final static long TOKEN_SECOND = 1000L * 60;
    public final static long REFRESH_TOKEN_SECOND = 1000L * 60 * 30;
    
    public final static String TOKEN_NAME = "loginToken";
    public final static String REFRESH_TOKEN_NAME = "refreshToken";
    

    private String SECRET_KEY = "szsprojectjwtsecrettokenkeyvalue";

    private Key getSigningKey(String secretKey) {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Claims extractAllClaims(String token) throws ExpiredJwtException {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey(SECRET_KEY))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    
    //토큰 생성
    public String createToken(String userId) {
        return createToken(userId, TOKEN_SECOND);
    }

    //토큰 refresh
    public String refreshToken(String userId) {
        return createToken(userId, REFRESH_TOKEN_SECOND);
    }

    //토큰에서 사용자 정보 추출
    public String getUserId(String token) {
        return extractAllClaims(token).get("userid", String.class);
    }

    //토큰 만료 확인
    public Boolean tokenExpiredCheck(String token) {
        final Date expiration = extractAllClaims(token).getExpiration();
        return expiration.before(new Date());
    }

    //토큰 생성
    public String createToken(String userId, long expireTime) {

        Claims claims = Jwts.claims();
        claims.put("userid", userId);

        String jwt = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireTime))
                .signWith(getSigningKey(SECRET_KEY), SignatureAlgorithm.HS256)
                .compact();

        return jwt;
    }

    //
    public Boolean validToken(String token, UserDetails userDetails) {
        final String userId = getUserId(token);

        return (userId.equals(userDetails.getUsername()) && !tokenExpiredCheck(token));
    }

}
