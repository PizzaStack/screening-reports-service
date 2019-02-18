package com.revature.screenforce.models;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class ReportByEmailAndWeeksModel {
	Integer screenerId;
	String email;
	Map<String, Double> scoresBySkillType = new HashMap<String, Double>();
	Map<String, Double> scoresByDescription = new HashMap<String, Double>();
	Map<String, Double> scoresByQuestion = new TreeMap<String, Double>();
	Map<String, Integer> numViolationsByType = new HashMap<String, Integer>();
	int numScheduledScreenings;
	
	public ReportByEmailAndWeeksModel(
			Integer screenerId, 
			String email,
			Map<String, Double> scoresBySkillType,
			Map<String, Double> scoresByDescription,
			Map<String, Double> scoresByQuestion,
			Map<String, Integer> numViolationsByType,
			int numScheduledScreenings) {

		this.screenerId = screenerId;
		this.email = email;
		this.scoresBySkillType = scoresBySkillType;
		this.scoresByDescription = scoresByDescription;
		this.scoresByQuestion = scoresByQuestion;
		this.numScheduledScreenings = numScheduledScreenings;
		this.numViolationsByType = numViolationsByType;
	}
}

