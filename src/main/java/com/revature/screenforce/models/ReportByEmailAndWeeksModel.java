package com.revature.screenforce.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.revature.screenforce.beans.Bucket;
import com.revature.screenforce.beans.Question;
import com.revature.screenforce.beans.QuestionScore;
import com.revature.screenforce.beans.ScheduledScreening;
import com.revature.screenforce.beans.Screener;
import com.revature.screenforce.beans.Screening;
import com.revature.screenforce.beans.SkillType;
import com.revature.screenforce.beans.SoftSkillViolation;
import com.revature.screenforce.beans.ViolationType;

public class ReportByEmailAndWeeksModel {
	Map<String, Double> scoresByDescription = new HashMap<String, Double>();
	int numScheduledScreenings;
	int numViolationTypes;
	
	public ReportByEmailAndWeeksModel(
			Map<String, Double> scoresByDescription, 
			int numScheduledScreenings,
			int numViolationTypes) {
		/* this.scoresByType = scoredByType; */
		this.scoresByDescription = scoresByDescription;
		this.numScheduledScreenings = numScheduledScreenings;
		this.numViolationTypes = numViolationTypes;
	}
}

