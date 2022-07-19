package com.example.demo.restapi.service;

import java.util.Map;

import com.example.demo.restapi.entity.Member;

public interface MemberService {
	Map<String, Object> signup(Member member);
	
	Member login(String userId, String password) throws Exception;

	Member getMyInfo(String userId);
	
	String changeValueString(long val);
}