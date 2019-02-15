package com.revature.screenforce.models;


import java.util.List;

import com.revature.screenforce.beans.Bucket;
import com.revature.screenforce.beans.Question;
import com.revature.screenforce.beans.QuestionScore;
import com.revature.screenforce.beans.ScheduledScreening;
import com.revature.screenforce.beans.Screener;
import com.revature.screenforce.beans.Screening;
import com.revature.screenforce.beans.SkillType;
import com.revature.screenforce.beans.SoftSkillViolation;
import com.revature.screenforce.beans.ViolationType;

public class FullIndividualReportModel {
	private Screener screener;
	private List<Screening> screenings;
	private List<ScheduledScreening> scheduledScreenings;
	private List<SkillType> skillTypes;
	private List<List<QuestionScore>> questionScores;
	private List<List<Question>> questions;
	private List<SoftSkillViolation> softSkillViolations;
	private List<ViolationType> violationTypes;
	private List<List<Bucket>> buckets;
	
	public FullIndividualReportModel(
			Screener screener,
			List<Screening> screenings, 
			List<ScheduledScreening> scheduledScreenings,
			List<SkillType> skillTypes,
			List<List<Bucket>> buckets,
			/*List<List<QuestionScore>> questionScores,
			List<List<Question>> questions,*/
			List<SoftSkillViolation> softSkillViolations,
			List<ViolationType> violationTypes){
		this.screener = screener;
		this.screenings = screenings;
		this.scheduledScreenings = scheduledScreenings;
		this.skillTypes = skillTypes;
		//this.questionScores = questionScores;
		//this.questions = questions;
		this.softSkillViolations = softSkillViolations;
		this.buckets = buckets;
	}
}