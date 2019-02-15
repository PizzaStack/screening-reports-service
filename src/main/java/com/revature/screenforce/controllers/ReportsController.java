package com.revature.screenforce.controllers;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.revature.screenforce.services.ReportsServiceImp;

import com.google.gson.JsonArray;
import com.revature.screenforce.services.ReportByCriteria;
import com.revature.screenforce.services.ReportsServiceImp;

@RestController
@CrossOrigin
public class ReportsController {

	@Autowired ReportsServiceImp reportsService;
	ReportByCriteria rbc = new ReportByCriteria();
	
	@GetMapping("/{email}/{weeks}")
	public @ResponseBody String getReportByEmailAndWeeks(
			@PathVariable(value="email") String email, 
			@PathVariable(value="weeks") String weeks) {
		String jsonReport = reportsService.getJsonReportForEmailAndWeeks(email, weeks);
		//String jsonReport = rbc.getIndividualReport(email, weeks);
		return jsonReport;
	}

	@GetMapping(value="/email", produces= MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<String> getAllEmails(@RequestParam(value = "email") String email){
		List<String> emails = this.reportsService.getAllEmails(email);
		return emails;
	}
	

//	@Autowired
	// TODO inject required services
	// CoolService
	
	@GetMapping(value = "/data", produces = MediaType.APPLICATION_JSON_VALUE)
	// TODO refactor the list element to the proper return type
	public String getReports(
			@RequestParam(name="weeks") 
			String weeks, 
			@RequestParam(name="email", required=false)
			String email) {
		
		if (email != null) {
			return reportsService.getJsonReportForEmailAndWeeks(email, weeks);
		} else {
			return null;
		}

	}
}
