package com.example.demo.restapi.service;

import java.util.HashMap;
import java.util.Map;

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
	public Map<String, Object> signup(Member member) {
		Map<String, Object> rtn = new HashMap<>();
		Member mem = memberRepository.findByUserid(member.getUserid());
		if(mem == null) {
			memberRepository.save(member);
			rtn.put("status", "success");
			rtn.put("userid", member.getUserid());
			rtn.put("username", member.getName());
			rtn.put("regno", member.getRegno());
		}else {
			rtn.put("status", "fail");
			rtn.put("error", "이미 가입된 사용자 입니다.");
		}
		return rtn;
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

	@Override
	public String changeValueString(long val) {
		String value = String.valueOf(val);
		String returnString = "";
		if(value.length() >= 5 ) {
			String val1 = value.substring(0, value.length()-4);
			String val2 = value.substring(value.length()-4, value.length()-3);
			if(val2.equals("0")) {
				returnString += val1 + "만원";
			}else {
				returnString += val1 + "만 " + val2 + "천원";
			}
		}else if(value.length() == 4){
			String val1 = value.substring(0, 1);
			returnString += val1 + "천원";
		}
		
		return returnString;
	}

}