package com.example.demo.restapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.restapi.entity.Member;

public interface MemberRepository extends JpaRepository<Member, String> {

	Optional<Member> findByUserid(String userId);
	
}
