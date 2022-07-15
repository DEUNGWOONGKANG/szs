package com.example.demo.restapi.service;

import org.apache.catalina.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.restapi.entity.Member;
import com.example.demo.restapi.repository.MemberRepository;

@Service
public class MemberServiceImpl implements MemberService {
	
	@Autowired
	private MemberRepository memberRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public void signup(Member member) {
		memberRepository.save(member);
	}

	@Override
	public Member login(String userId, String password) throws Exception {
		Member member = memberRepository.findByUserid(userId);
		if(member == null) throw new Exception ("해당 사용자가 없습니다.");
		if(!passwordEncoder.matches(password, member.getPassword())) throw new Exception("비밀번호가 틀렸습니다.");
		return member;
	}

	@Override
	public Member getMyInfo(String userId) {
		return memberRepository.findUsernameRegnoByUserid(userId);
	}

}