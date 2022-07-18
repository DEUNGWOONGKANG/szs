package com.example.demo.restapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.restapi.entity.Spend;

public interface SpendRepository extends JpaRepository<Spend, String> {

}
