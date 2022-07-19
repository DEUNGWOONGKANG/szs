package com.example.demo.restapi.service;

import com.example.demo.restapi.entity.Spend;

public interface SpendService {
	void saveSpend(Spend spend);

	void deleteSpend(String userid);

	long getTax(String userId);
}
