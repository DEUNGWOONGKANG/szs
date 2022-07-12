package com.example.demo.restapi.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import lombok.RequiredArgsConstructor;

// JwtTokenProvider가 검증을 끝낸 Jwt로부터 유저 정보를 조회해와서 UserPasswordAuthenticationFilter 로 전달합니다.
@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean{
	private final JwtTokenProvider jwtTokenProvider;
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		//헤더에서 Jwt를 받아옴
		String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);
		//유효토큰 체크
		if(token != null && jwtTokenProvider.validToken(token)) {
			//Authentication auth = jwtTokenProvider.getAuthentication(token);
			//SecurityContext에 저장
			//SecurityContextHolder.getContext().setAuthentication(auth);
		}
		chain.doFilter(request, response);
	}
}
