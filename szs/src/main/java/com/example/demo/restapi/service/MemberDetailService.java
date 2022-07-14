package com.example.demo.restapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.restapi.entity.Member;
import com.example.demo.restapi.repository.MemberRepository;

@Service
public class MemberDetailService implements UserDetailsService {
	
	@Autowired
	MemberRepository memberRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Member member = memberRepository.findByUserid(username);
		UserDetails userDetail = (UserDetails) member;
        if (member != null) {
            return userDetail;
        } else {
            throw new UsernameNotFoundException("User not found with userId: " + username);
        }
	}

}
