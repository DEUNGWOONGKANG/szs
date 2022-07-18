package com.example.demo.restapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.restapi.entity.Income;

public interface IncomeRepository extends JpaRepository<Income, String> {

}
