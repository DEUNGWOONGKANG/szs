package com.example.demo.restapi.entity;
import java.util.Collection;
import org.springframework.security.core.userdetails.User;

import lombok.Getter;


@Getter
public class CustomMember extends User{
	
	private static final long serialVersionUID = 1L;

	public CustomMember(String userId, String password, Collection authorities) {
		super(userId, password, authorities);
	}

}