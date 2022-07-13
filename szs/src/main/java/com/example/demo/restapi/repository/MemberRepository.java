package com.example.demo.restapi.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.restapi.entity.Member;

public interface MemberRepository extends JpaRepository<Member, String> {
}
