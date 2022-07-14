package com.example.demo.restapi.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.restapi.service.CookieService;
import com.example.demo.restapi.service.MemberDetailService;

import io.jsonwebtoken.ExpiredJwtException;

@Component
public class JwtFilter extends OncePerRequestFilter{
	
	@Autowired
    private MemberDetailService memberDetailService;
	
	@Autowired
	CookieService cookieService;
	
	@Autowired
	JwtTokenConfig jwtConfig;
	
	
	@Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

		final String requestTokenHeader = request.getHeader("Authorization");

        String userId = null;
        String jwtToken = null;

        // JWT Token is in the form "Bearer token". Remove Bearer word and get only the Token
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
            	userId = jwtConfig.getUserId(jwtToken);
            } catch (IllegalArgumentException e) {
                System.out.println("Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
                System.out.println("JWT Token has expired");
            }
        } else {
            logger.warn("JWT Token does not begin with Bearer String");
        }

        // 토큰을 받으면 유효성을 검사.
        if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = memberDetailService.loadUserByUsername(userId);

            // 토큰이 유효한 경우.. 수동으로 인증을 설정하도록 Spring Security를 구성
            if (jwtConfig.validToken(jwtToken, userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 컨텍스트에서 인증을 설정한 후, 현재 사용자가 인증되었음을 지정. Spring Security Configurations 성공적으로 통과.
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        chain.doFilter(request, response);
    }
}
