package com.example.demo;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.demo.restapi.config.Seed;
import com.example.demo.restapi.entity.Member;
import com.example.demo.restapi.service.MemberService;

@SpringBootTest
public class ApiTest {
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	MemberService memberService;
	
	public void signUp() {
		Map<String, Object> result = new LinkedHashMap<>();
		String name = "홍길동";
		String userId = "hong1234";
		String regNo = "860824-1655068";
		String password = "1234";
		Member newMember = Member.builder()
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
			memberService.signup(newMember);
			
			result.put("status", "success");
			result.put("name", name);
			result.put("regNo", regNo);
		}else {
			result.put("status", "fail");
			result.put("error", "가입 불가능한 사용자 입니다.");
		}
		
	}
	
}
