package com.revature.screenforce.controllers;

<<<<<<< HEAD
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
=======
import java.util.Collections;
import java.util.List;

>>>>>>> master
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
<<<<<<< HEAD
import org.springframework.web.bind.annotation.ResponseBody;
=======
>>>>>>> master
import org.springframework.web.bind.annotation.RestController;

import com.revature.screenforce.services.ReportsServiceImp;

@RestController
@CrossOrigin
public class ReportsController {
<<<<<<< HEAD
	@Autowired
	ReportsServiceImp reportsServiceImp;
	
	@GetMapping(value="/email", produces= MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<String> getAllEmails(@RequestParam(value = "email") String email){
		List<String> emails = this.reportsServiceImp.getAllEmails(email);
		return emails;
	}
	
=======
//	@Autowired
	// TODO inject required services
	// CoolService
>>>>>>> master
	
	@GetMapping(value = "/data", produces = MediaType.APPLICATION_JSON_VALUE)
	// TODO refactor the list element to the proper return type
	public List<Object> getReports(
			@RequestParam(name="weeks") 
			String weeks, 
			@RequestParam(name="email", required=false)
			String email) {
		
		if (email != null) {
			// TODO service using both email and weeks
			return Collections.singletonList(new Object()
			);
		} else {
			// TODO service using weeks only
			return Collections.singletonList(new Object()
			);
		}
	}
}
