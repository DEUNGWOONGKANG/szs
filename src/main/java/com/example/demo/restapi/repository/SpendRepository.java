package com.example.demo.restapi.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.restapi.entity.Spend;

public interface SpendRepository extends JpaRepository<Spend, Long> {

	@Transactional
	void deleteByUserid(String userid);

	List<Spend> findByUserid(String userId);

}
