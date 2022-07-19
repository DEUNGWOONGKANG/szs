package com.example.demo.restapi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.restapi.entity.Income;
import com.example.demo.restapi.repository.IncomeRepository;
@Service
public class IncomeServiceImpl implements IncomeService {
	
	@Autowired
	IncomeRepository incomeRepository;

	@Override
	public void saveIncome(Income income) {
		incomeRepository.save(income);
	}

	@Override
	public void deleteIncome(String userid) {
		incomeRepository.deleteByUserid(userid);
		
	}

	@Override
	public long getLimit(String userId) {
		List<Income> income = incomeRepository.findByUserid(userId);
		if(income.size() > 0) {
			long val = 0;
			for(Income i : income) {
				val += i.getTotal();
			}
			if(val <= 33000000) {
				return 740000;
			}else if(val <= 70000000) {
				long returnVal = (long) (740000 - ((val-33000000)*0.008));
				if(returnVal < 660000) {
					return 660000;
				}else{
					return returnVal;
				}
			}else{
				long returnVal = (long) (660000-((val-70000000)*0.5));
				if(returnVal < 500000) {
					return 500000;
				}else {
					return returnVal;
				}
			}
		}else{
			return 0;
		}
	}

}
