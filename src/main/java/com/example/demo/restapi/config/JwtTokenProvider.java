package com.example.demo.restapi.config;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {
	
	//토큰 키
	private Key key;
	
	//토큰 유효시간 30분
	private long tokenTime = 30 * 60 * 1000L;
	
	//키 Base64 encoding
	@PostConstruct
	protected void init(String tokenKey) {
		byte[] keyBytes = Decoders.BASE64.decode(tokenKey);
        key = Keys.hmacShaKeyFor(keyBytes);
	}
	
	//JWT 토큰 생성
	public String newToken(String userId, List<String> roles) {
		Claims claims = Jwts.claims().setSubject(userId);
		claims.put("roles", roles);
		Date now = new Date();
		return Jwts.builder()
				.setClaims(claims)
				.setIssuedAt(now)
				.setExpiration(new Date(now.getTime() + tokenTime))
				.signWith(key, SignatureAlgorithm.HS256)
				.compact();
	}
	
	// Request의 Header에서 token 값을 가져온다
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }
    
    // 토큰의 유효성 및 만료일자 확인
    public boolean validToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(key).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
    
    

}