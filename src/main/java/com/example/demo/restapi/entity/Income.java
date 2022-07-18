package com.example.demo.restapi.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name="income")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@AllArgsConstructor
@Builder
public class Income {
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(name = "username", nullable = false)
	private String name;
	
	@Column(name = "regno", nullable = false)
	private String regno;
	
	@Column(name = "type", nullable = false)
	private String type;
	
	@Column(name = "company", nullable = false)
	private String company;
	
	@Column(name = "companycode", nullable = false)
	private String companycode;
	
	@Column(name = "startdate", nullable = false)
	private String startdate;
	
	@Column(name = "enddate", nullable = false)
	private String enddate;
	
	@Column(name = "payday", nullable = false)
	private String payday;
	
	@Column(name = "paygbn", nullable = false)
	private String paygbn;
	
	@Column(name = "total", nullable = false)
	private long total;
	
}