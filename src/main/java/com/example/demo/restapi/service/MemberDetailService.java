package com.example.demo.restapi.service;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.restapi.entity.CustomMember;
import com.example.demo.restapi.entity.Member;
import com.example.demo.restapi.repository.MemberRepository;

@Service
public class MemberDetailService implements UserDetailsService {
	
	@Autowired
	MemberRepository memberRepository;

	@Override
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
		Member member = memberRepository.findByUserid(userId);
        if (member != null) {
        	 Collection<GrantedAuthority> authorities = new ArrayList<>();
             //userDetails로 변환
             CustomMember userDetails = new CustomMember(member.getUserid(), member.getPassword(), authorities);
             return userDetails;
        } else {
            throw new UsernameNotFoundException("User not found with userId: " + userId);
        }
	}

}