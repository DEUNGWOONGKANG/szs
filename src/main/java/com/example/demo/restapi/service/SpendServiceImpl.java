package com.example.demo.restapi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.restapi.entity.Spend;
import com.example.demo.restapi.repository.SpendRepository;
@Service
public class SpendServiceImpl implements SpendService {
	
	@Autowired
	SpendRepository spendRepository;

	@Override
	public void saveSpend(Spend spend) {
		spendRepository.save(spend);
	}

	@Override
	public void deleteSpend(String userid) {
		spendRepository.deleteByUserid(userid);
		
	}

	@Override
	public long getTax(String userId) {
		List<Spend> spend = spendRepository.findByUserid(userId);
		long tax = 0;
		for(Spend s : spend) {
			if(s.getType().equals("산출세액"))	tax += s.getTotal();
		}
		
		if(tax <= 1300000) {
			return (long) (tax*0.55);
		}else {
			return (long) (715000 + ((tax-1300000)*0.3));
		}
	}

}
