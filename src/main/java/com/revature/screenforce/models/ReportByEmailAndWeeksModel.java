package com.revature.screenforce.models;

import java.util.List;

import com.revature.screenforce.beans.ScheduledScreening;
import com.revature.screenforce.beans.Screener;
import com.revature.screenforce.beans.Screening;
import com.revature.screenforce.beans.SkillType;

public class ReportByEmailAndWeeksModel {
	private Screener screener;
	private List<Screening> screenings;
	private List<ScheduledScreening> scheduledScreenings;
	private List<SkillType> skillTypes;
	
	public ReportByEmailAndWeeksModel(
			Screener screener,
			List<Screening> screenings, 
			List<ScheduledScreening> scheduledScreenings,
			List<SkillType> skillTypes) {
		this.screener = screener;
		this.screenings = screenings;
		this.scheduledScreenings = scheduledScreenings;
		this.skillTypes = skillTypes;
	}
}
