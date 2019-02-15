package com.revature.screenforce.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonArray;
import com.revature.screenforce.services.ReportByCriteria;
import com.revature.screenforce.services.ReportsServiceImp;

@RestController
@CrossOrigin
@RequestMapping(value="/reports")
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
}
