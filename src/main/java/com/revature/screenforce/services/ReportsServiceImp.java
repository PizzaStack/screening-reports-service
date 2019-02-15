package com.revature.screenforce.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.revature.screenforce.beans.Bucket;
import com.revature.screenforce.beans.Candidate;
import com.revature.screenforce.beans.Question;
import com.revature.screenforce.beans.QuestionScore;
import com.revature.screenforce.beans.ScheduledScreening;
import com.revature.screenforce.beans.Screener;
import com.revature.screenforce.beans.Screening;
import com.revature.screenforce.beans.SkillType;
import com.revature.screenforce.beans.SoftSkillViolation;
import com.revature.screenforce.beans.ViolationType;
import com.revature.screenforce.beans.Weight;
import com.revature.screenforce.daos.QuestionDAO;
import com.revature.screenforce.daos.QuestionScoreRepository;
import com.revature.screenforce.daos.ScreenerRepository;
import com.revature.screenforce.daos.SkillTypeDAO;
import com.revature.screenforce.daos.SoftSkillViolationRepository;
import com.revature.screenforce.daos.ViolationTypeRepository;
import com.revature.screenforce.daos.WeightDAO;
import com.revature.screenforce.models.FullIndividualReportModel;
import com.revature.screenforce.models.ReportByEmailAndWeeksModel;

@Service
public class ReportsServiceImp {
	@Autowired ScreenerRepository screenerRepository;
	@Autowired SkillTypeDAO skillTypeDAO;
	@Autowired QuestionScoreRepository questionScoreRepository;
	@Autowired QuestionDAO questionDAO;
	@Autowired SoftSkillViolationRepository softSkillViolationRepository;
	@Autowired ViolationTypeRepository violationTypeRepository;
	@Autowired WeightDAO weightDAO;
	
	public String getJsonReportForEmailAndWeeks(String email, String weeks) {
		Gson gson = new Gson();
		Screener screener = screenerRepository.getByEmail(email);
		List<Screening> screenings = screener.getScreenings();
		List<ScheduledScreening> scheduledScreenings = new ArrayList<>();
		List<SkillType> skillTypes = new ArrayList<>();
		List<List<Bucket>> buckets = new ArrayList<List<Bucket>>();

		List<SoftSkillViolation> softSkillViolations = new ArrayList<SoftSkillViolation>();
		List<ViolationType> violationTypes = new ArrayList<ViolationType>();
		
		Map<String, Double> scoresByDescription = new HashMap<String, Double>();
		Map<String, Integer> numScoresPerDescription = new HashMap<String, Integer>();
		
		if (screenings != null) {
			EntityManager em
			for (Screening s : screenings) {
				ScheduledScreening ss = s.getScheduledScreening();
				scheduledScreenings.add(ss);
				int skillTypeId = ss.getSkillTypeId();
				s.getSkillType();
				SkillType st = skillTypeDAO.getBySkillTypeId(skillTypeId);
				skillTypes.add(st);
				
				buckets.add(s.getBuckets());
				System.out.println("screening " + s.getScreenerId() + " has buckets: " + s.getBuckets());
				
				SoftSkillViolation softSkillViolation = new SoftSkillViolation();
				softSkillViolations.add(softSkillViolationRepository.getByScreeningId(s.getScreeningId()));
				
				violationTypes.add(softSkillViolation.getViolationType());
			}
			//System.out.println("buckets: " + buckets);
			for (List<Bucket> bl : buckets) {
				for (Bucket b : bl) {
					//System.out.println("bucket: " + b);
					//for (Question q : b.getQuestions()) {
						//System.out.println("q: " + q);
						String key = b.getBucketDescription();
						List<QuestionScore> questionScores = questionScoreRepository.findAllByBucketId(b.getBucketId());
						for (QuestionScore qs : questionScores) {
							//System.out.println("qs: " + qs);
							System.out.println("----- bucket" + b.getBucketId() + " has questionScores: " + qs);
							Double value = qs.getScore();
							if (key == null || value == null) continue;
							if (!scoresByDescription.containsKey(key)) {
								scoresByDescription.put(key, value);
								numScoresPerDescription.put(key, 1);
							}
							else {
								double currentSum = scoresByDescription.get(key);
								double newVal = currentSum + value;
								scoresByDescription.put(key, newVal);
								numScoresPerDescription.put(key, numScoresPerDescription.get(key)+1);
							}
						}
					//}
				}
			}
			System.out.println("scoresByDescription: " + scoresByDescription);
			System.out.println("numScoresPerDescription: " + numScoresPerDescription);
			Set<String> keyset = scoresByDescription.keySet();
			Iterator<String> iter = keyset.iterator();
			while (iter.hasNext()) {
				String key = iter.next();
				if (scoresByDescription.get(key) != 0) {
					double scoreByDescription = scoresByDescription.get(key) / (double) numScoresPerDescription.get(key);
					scoresByDescription.put(key, scoreByDescription);
				}
				System.out.println("Description: " + key + " | " + "Total Score: " + scoresByDescription.get(key));
			}
		}
		
		/*
		NEED: 
		AVG(score) from skilltype,
		AVG(score) from bucket_description,
		COUNT(*) from scheduled_screenings #screener -> screening -> scheduled_screening
		(*) from question_values SORT BY value DESC LIMIT 5 #question -> question_values
		COUNT(*) from violations
		*/
		/*
		Double sum = 0.0;
		double avgSkillTypeScore;
		if (screenings != null) {
			for (Screening s : screenings) {
				sum += ((s.getCompositeScore() != null) ? s.getCompositeScore() : 0);
			}
			avgSkillTypeScore = sum/screenings.size();
		}
		*/
	
		int numScheduledScreenings = scheduledScreenings.size();
		int numViolationTypes = violationTypes.size();
		ReportByEmailAndWeeksModel report = new ReportByEmailAndWeeksModel(
				/* skillType avg score, */
				scoresByDescription, 
				numScheduledScreenings,
				numViolationTypes );
		String json = gson.toJson(report);
		return json;
	}
}
