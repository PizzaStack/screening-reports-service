package com.revature.screenforce.models;

import java.util.HashMap;
import java.util.Map;

public class ReportByEmailAndWeeksModel {
	Integer screenerId;
	String email;
	Map<String, Double> scoresBySkillType = new HashMap<String, Double>();
	Map<String, Double> scoresByDescription = new HashMap<String, Double>();
	Map<String, Integer> numViolationsByType = new HashMap<String, Integer>();
	int numScheduledScreenings;
	
	public ReportByEmailAndWeeksModel(
			Integer screenerId, 
			String email,
			Map<String, Double> scoresBySkillType,
			Map<String, Double> scoresByDescription,
			Map<String, Integer> numViolationsByType,
			int numScheduledScreenings) {

		this.screenerId = screenerId;
		this.email = email;
		this.scoresBySkillType = scoresBySkillType;
		this.scoresByDescription = scoresByDescription;
		this.numScheduledScreenings = numScheduledScreenings;
		this.numViolationsByType = numViolationsByType;
	}
}

