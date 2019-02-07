package com.revature.screenforce.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.revature.screenforce.daos.ReportsRepository;

@Service
public class ReportsServiceImp implements ReportsService{
	@Autowired
	ReportsRepository reportsDao;
}
