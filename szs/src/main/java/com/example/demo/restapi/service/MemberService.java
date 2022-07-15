package com.example.demo.restapi.service;

import com.example.demo.restapi.entity.Member;

public interface MemberService {
	void signup(Member member);
	
	Member login(String userId, String password) throws Exception;

	Member getMyInfo(String userId);
}
