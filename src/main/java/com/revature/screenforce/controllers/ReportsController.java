package com.revature.screenforce.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.revature.screenforce.services.ReportsServiceImp;

@RestController
@CrossOrigin
@RequestMapping(value="/reports")
public class ReportsController {
	@Autowired
	ReportsServiceImp reportsServiceImp;
	
	@GetMapping(value="/email", produces= MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<String> getAllEmails(@RequestParam(value = "email") String email){
		List<String> emails = this.reportsServiceImp.getAllEmails(email);
		return emails;
	}
	
	
}
