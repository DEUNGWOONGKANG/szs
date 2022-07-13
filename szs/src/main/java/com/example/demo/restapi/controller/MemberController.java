package com.example.demo.restapi.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.restapi.config.Seed;
import com.example.demo.restapi.entity.Member;
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
	PasswordEncoder passwordEncoder;
	
	@ApiOperation(value = "회원 가입", notes = "신규 회원 가입")
	@PostMapping(value = "/signup")
	public Member signUp(@ApiParam(value = "아이디", required = true) @RequestParam String userId, 
            @ApiParam(value = "비밀번호", required = true) @RequestParam String password
            ,@ApiParam(value = "이름", required = true) @RequestParam String name
            ,@ApiParam(value = "주민등록번호", required = true) @RequestParam String regNo) {
		Member member = Member.builder()
				.userId(userId)
				.password(passwordEncoder.encode(password))
				.name(name)
				.regNo(Seed.encrypt(regNo))
				.build();
		memberService.signup(member);
		return member;
	}
	
	@ApiOperation(value = "로그인", notes = "로그인")
	@PostMapping(value = "/login")
	public void login(@ApiParam(value = "아이디", required = true) @RequestParam String userId, 
			@ApiParam(value = "비밀번호", required = true) @RequestParam String password) {
	}
	

}
