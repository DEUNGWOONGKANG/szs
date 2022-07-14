package com.example.demo.restapi.controller;


import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.restapi.config.JwtTokenConfig;
import com.example.demo.restapi.config.Seed;
import com.example.demo.restapi.entity.Member;
import com.example.demo.restapi.service.CookieService;
import com.example.demo.restapi.service.MemberService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;

@Api(tags = {"Member"}) 
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/szs")
public class MemberController {
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private JwtTokenConfig jwtTokenConfig;
	
	@Autowired
	private CookieService cookieService;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@ApiOperation(value = "회원 가입", notes = "신규 회원 가입")
	@PostMapping(value = "/signup")
	public Member signUp(@ApiParam(value = "아이디", required = true) @RequestParam String userId, 
            @ApiParam(value = "비밀번호", required = true) @RequestParam String password
            ,@ApiParam(value = "이름", required = true) @RequestParam String name
            ,@ApiParam(value = "주민등록번호", required = true) @RequestParam String regNo) {
		Member member = Member.builder()
				.userid(userId)
				.password(passwordEncoder.encode(password))
				.name(name)
				.regno(Seed.encrypt(regNo))
				.build();
		if((name.equals("홍길동") && regNo.equals("860824-1655068"))
				||(name.equals("김둘리") && regNo.equals("921108-1582816"))
				||(name.equals("마징가") && regNo.equals("880601-2455116"))
				||(name.equals("베지터") && regNo.equals("910411-1656116"))
				||(name.equals("손오공") && regNo.equals("820326-2715702"))) {
			memberService.signup(member);
		}
		return member;
	}
	
	@ApiOperation(value = "로그인", notes = "로그인")
	@GetMapping(value = "/login")
	public Map<String, Object> login(@ApiParam(value = "아이디", required = true) @RequestParam String userId, 
			@ApiParam(value = "비밀번호", required = true) @RequestParam String password, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> map = new HashMap<>();
		try {
			Member member = memberService.login(userId, password);
			String token = jwtTokenConfig.createToken(member.getUserid());
			String rToken = jwtTokenConfig.refreshToken(member.getUserid());
			
			Cookie loginToken = cookieService.createCookie(JwtTokenConfig.TOKEN_NAME, token);
			Cookie refreshToken = cookieService.createCookie(JwtTokenConfig.REFRESH_TOKEN_NAME, rToken);
			response.addCookie(loginToken);
			response.addCookie(refreshToken);
			map.put("result", "success");
			return map;
		} catch(Exception e) {
			map.put("result", "fail : " + e.getMessage());
			return map;
		}
	}
	

}
