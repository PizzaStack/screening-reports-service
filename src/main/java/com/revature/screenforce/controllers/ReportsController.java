package com.revature.screenforce.controllers;

import java.util.Collections;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class ReportsController {
//	@Autowired
	// TODO inject required services
	// CoolService
	
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
