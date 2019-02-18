package com.revature.screenforce.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ReportByWeeksModel {
	List<ReportByEmailAndWeeksModel> screeners = new ArrayList<>();
	Map<String, Double> scoresByQuestion = new TreeMap<String, Double>();
	
	public ReportByWeeksModel(List<ReportByEmailAndWeeksModel> screeners, 
		Map<String, Double> scoresByQuestion) {
		this.scoresByQuestion = scoresByQuestion;
		this.screeners = screeners;
	}
}
