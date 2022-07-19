package com.example.demo.restapi.service;

import com.example.demo.restapi.entity.Income;

public interface IncomeService {
	void saveIncome(Income income);

	void deleteIncome(String userid);

	long getLimit(String userId);
}
