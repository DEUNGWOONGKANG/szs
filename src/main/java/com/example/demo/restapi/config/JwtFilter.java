package com.example.demo.restapi.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
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

		final Cookie jwtToken = cookieService.getCookie(request,JwtTokenConfig.TOKEN_NAME);

        String userId = null;
        String jwt = null;
        String refreshJwt = null;

        try{
            if(jwtToken != null){
                jwt = jwtToken.getValue();
                userId = jwtConfig.getUserId(jwt);
            }
            if(userId!=null){
                UserDetails userDetails = memberDetailService.loadUserByUsername(userId);

                if(jwtConfig.validToken(jwt,userDetails)){
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
        }catch (ExpiredJwtException e){
            Cookie refreshToken = cookieService.getCookie(request,JwtTokenConfig.REFRESH_TOKEN_NAME);
            if(refreshToken!=null){
                refreshJwt = refreshToken.getValue();
            }
        }catch(Exception e){

        }

        try{
            if(refreshJwt != null){
            	userId = jwtConfig.getUserId(refreshJwt);
                UserDetails userDetails = memberDetailService.loadUserByUsername(userId);
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

                String newToken =jwtConfig.createToken(userId);

                Cookie newAccessToken = cookieService.createCookie(JwtTokenConfig.TOKEN_NAME,newToken);
                response.addCookie(newAccessToken);
            }
        }catch(ExpiredJwtException e){

        }

        chain.doFilter(request,response);
    }
}