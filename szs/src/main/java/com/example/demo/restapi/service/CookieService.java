package com.example.demo.restapi.service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.example.demo.restapi.config.JwtTokenConfig;


@Service
public class CookieService {
	
	public Cookie createCookie(String cookieName, String value) {
		Cookie cookie = new Cookie(cookieName, value);
		cookie.setHttpOnly(true);
		cookie.setMaxAge((int)JwtTokenConfig.TOKEN_SECOND);
		cookie.setPath("/");
		return cookie;
	}
	
	public Cookie getCookie(HttpServletRequest request, String cookieName) {
		Cookie[] cookies = request.getCookies();
		if(cookies == null) return null;
		for(Cookie cookie : cookies) {
			if(cookie.getName().equals(cookieName)) return cookie;
		}
		return null;
	}

}
