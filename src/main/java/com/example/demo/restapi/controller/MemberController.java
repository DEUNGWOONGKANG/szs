package com.example.demo.restapi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.restapi.entity.Member;
import com.example.demo.restapi.repository.MemberRepository;

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
	private MemberRepository repo;
	
	@ApiOperation(value = "회원 조회", notes = "모든 회원 조회")
	@GetMapping(value = "/find")
    public List<Member> findAllUser() { 
        return repo.findAll();
    }
	
	@ApiOperation(value = "회원 가입", notes = "신규 회원 가입")
	@PostMapping(value = "/signup")
	public void signUp(@ApiParam(value = "아이디", required = true) @RequestParam String userId, 
            @ApiParam(value = "비밀번호", required = true) @RequestParam String password
            ,@ApiParam(value = "이름", required = true) @RequestParam String name
            ,@ApiParam(value = "주민등록번호", required = true) @RequestParam String regNo) {

		if((userId.equals("홍길동") && regNo.equals("860824-1655068"))
				||(userId.equals("김둘리") && regNo.equals("921108-1582816"))
				||(userId.equals("마징가") && regNo.equals("880601-2455116"))
				||(userId.equals("베지터") && regNo.equals("910411-1656116"))
				||(userId.equals("손오공") && regNo.equals("820326-2715702"))) {
		Member member = Member.builder()
				.userId(userId)
				.password(password)
				.name(name)
				.regNo(regNo)
				.build();
		repo.save(member);
		}
	}
	
	@ApiOperation(value = "로그인", notes = "로그인")
	@PostMapping(value = "/login")
	public void login(@ApiParam(value = "아이디", required = true) @RequestParam String userId, 
			@ApiParam(value = "비밀번호", required = true) @RequestParam String password) {
	}
	

}