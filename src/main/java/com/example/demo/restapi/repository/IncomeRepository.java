package com.example.demo.restapi.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.restapi.entity.Income;

public interface IncomeRepository extends JpaRepository<Income, Long> {

	@Transactional
	void deleteByUserid(String userid);

	List<Income> findByUserid(String userId);

}
