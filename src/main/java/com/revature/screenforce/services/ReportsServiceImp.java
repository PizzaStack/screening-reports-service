package com.revature.screenforce.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.revature.screenforce.beans.ScheduledScreening;
import com.revature.screenforce.beans.Screener;
import com.revature.screenforce.beans.Screening;
import com.revature.screenforce.beans.SkillType;
import com.revature.screenforce.daos.ScreenerRepository;
import com.revature.screenforce.daos.SkillTypeDAO;
import com.revature.screenforce.models.ReportByEmailAndWeeksModel;

/*
 NEED: 
 AVG(score) from skilltype,
 AVG(score) from bucket_description,
 COUNT(*) from scheduled_screenings #screener -> screening -> scheduled_screening
 (*) from question_values SORT BY value DESC LIMIT 5 #question -> question_values
 COUNT(*) from violations,
 
 */

@Service
public class ReportsServiceImp {
	@Autowired ScreenerRepository screenerRepository;
	@Autowired SkillTypeDAO skillTypeDAO;
	public String getJsonReportForEmailAndWeeks(String email, String weeks) {
		Gson gson = new Gson();
		Screener screener = screenerRepository.getByEmail(email);
		List<Screening> screenings = screener.getScreenings();
		List<ScheduledScreening> scheduledScreenings = new ArrayList<>();
		List<SkillType> skillTypes = new ArrayList<>();
		if (screenings != null) {
			for (Screening s : screenings) {
				ScheduledScreening ss = s.getScheduledScreening();
				scheduledScreenings.add(ss);
				int skillTypeId = ss.getSkillTypeId();
				SkillType st = skillTypeDAO.getBySkillTypeId(skillTypeId);
				skillTypes.add(st);
			}
		}
		ReportByEmailAndWeeksModel reportByEmailAndWeeksModel = new ReportByEmailAndWeeksModel(screener, screenings, scheduledScreenings, skillTypes);
		String json = gson.toJson(reportByEmailAndWeeksModel);
		return json;
	}

}
