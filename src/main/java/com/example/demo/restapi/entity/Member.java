package com.example.demo.restapi.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name="member")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@AllArgsConstructor
@Builder
public class Member {
	@Id
	@Column(name = "userid", nullable = false, unique = true)
	private String userId;
	
	@Column(name = "userpw", nullable = false)
	private String password;
	
	@Column(name = "username", nullable = false)
	private String name;
	
	@Column(name = "regno", nullable = false)
	private String regNo;
	
	public void encodePassword(PasswordEncoder passwordEncoder){
        this.password = passwordEncoder.encode(password);
    }
}