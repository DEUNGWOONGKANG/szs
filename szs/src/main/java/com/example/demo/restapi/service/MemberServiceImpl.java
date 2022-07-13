package com.example.demo.restapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.restapi.entity.Member;
import com.example.demo.restapi.repository.MemberRepository;

@Service
public class MemberServiceImpl implements MemberService {
	
	@Autowired
	private MemberRepository memberRepository;

	@Override
	public void signup(Member member) {
		if((member.getName().equals("홍길동") && member.getRegNo().equals("860824-1655068"))
				||(member.getName().equals("김둘리") && member.getRegNo().equals("921108-1582816"))
				||(member.getName().equals("마징가") && member.getRegNo().equals("880601-2455116"))
				||(member.getName().equals("베지터") && member.getRegNo().equals("910411-1656116"))
				||(member.getName().equals("손오공") && member.getRegNo().equals("820326-2715702"))) {
			memberRepository.save(member);
		}
	}

	@Override
	public Member login(String userId, String password) {
		// TODO Auto-generated method stub
		return null;
	}

}
