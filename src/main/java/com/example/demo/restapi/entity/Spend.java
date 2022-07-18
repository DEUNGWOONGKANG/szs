package com.example.demo.restapi.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name="spend")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@AllArgsConstructor
@Builder
public class Spend {
	
//	"총사용금액": "2,000,000",
//	"소득구분": "산출세액"
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(name = "username", nullable = false)
	private String name;
	
	@Column(name = "regno", nullable = false)
	private String regno;
	
	@Column(name = "type", nullable = false)
	private String type;
	
	@Column(name = "total", nullable = false)
	private long total;
	
}