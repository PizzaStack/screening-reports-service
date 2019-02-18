package com.revature.screenforce.services;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.revature.screenforce.beans.Bucket;
import com.revature.screenforce.beans.Question;
import com.revature.screenforce.beans.QuestionScore;
import com.revature.screenforce.beans.ScheduledScreening;
import com.revature.screenforce.beans.Screener;
import com.revature.screenforce.beans.Screening;
import com.revature.screenforce.beans.SkillType;
import com.revature.screenforce.beans.SoftSkillViolation;
import com.revature.screenforce.beans.ViolationType;
import com.revature.screenforce.daos.QuestionDAO;
import com.revature.screenforce.daos.QuestionScoreRepository;
import com.revature.screenforce.daos.ScreenerRepository;
import com.revature.screenforce.daos.SkillTypeDAO;
import com.revature.screenforce.daos.SoftSkillViolationRepository;
import com.revature.screenforce.daos.ViolationTypeRepository;
import com.revature.screenforce.daos.WeightDAO;
import com.revature.screenforce.models.ReportByEmailAndWeeksModel;
import com.revature.screenforce.models.ReportByWeeksModel;

@Service
public class ReportsService {
	@Autowired ScreenerRepository screenerRepository;
	@Autowired SkillTypeDAO skillTypeDAO;
	@Autowired QuestionScoreRepository questionScoreRepository;
	@Autowired QuestionDAO questionDAO;
	@Autowired SoftSkillViolationRepository softSkillViolationRepository;
	@Autowired ViolationTypeRepository violationTypeRepository;
	@Autowired WeightDAO weightDAO;
	private Map<String, Double> scoresByQuestion = new HashMap<String, Double>();
	private Map<String, Integer> numScoresPerQuestion = new HashMap<String, Integer>();

	public List<String> getAllEmails(String email){
		List<Screener> screenerList = screenerRepository.findAllByEmailContainingIgnoreCase(email);
		//System.out.println(screenerList.size());
		List<String> emailList = new ArrayList<>();
		for(Screener screener : screenerList) {
				emailList.add(screener.getEmail());
		}
		return emailList;
	}
	
	public ReportByEmailAndWeeksModel getJsonReportForEmailAndWeeks(String email, String weeks) {
		Screener screener = screenerRepository.getByEmail(email);

		if (screener.getScreenings() == null) return null;
		List<Screening> screenings = screener.getScreenings();
		List<ScheduledScreening> scheduledScreenings = new ArrayList<ScheduledScreening>();
		List<SoftSkillViolation> softSkillViolations = new ArrayList<SoftSkillViolation>();
		List<ViolationType> violationTypes = new ArrayList<ViolationType>();
		
		Map<String, List<Bucket>> bucketsBySkillType = new HashMap<String, List<Bucket>>();
		Map<String, Double> scoresByDescription = new HashMap<String, Double>();
		Map<String, Integer> numScoresPerDescription = new HashMap<String, Integer>();
		Map<String, Double> scoresBySkillType = new HashMap<String, Double>();
		Map<String, Integer> numScoresPerSkillType = new HashMap<String, Integer>();
		
		Map<String, Integer> numViolationsByType = new HashMap<String, Integer>();
		
		if (screenings != null) {
			for (Screening s : screenings) {
				ScheduledScreening ss = s.getScheduledScreening();
				scheduledScreenings.add(ss);
				
				SkillType st = skillTypeDAO.getBySkillTypeId(s.getSkillTypeId());
				bucketsBySkillType.put(st.getTitle(), s.getBuckets());
				
				if (softSkillViolationRepository.existsByScreeningId(s.getScreeningId())) {
					SoftSkillViolation softSkillViolation = softSkillViolationRepository.getByScreeningId(s.getScreeningId());
					softSkillViolations.add(softSkillViolation);
					if (softSkillViolation.hasViolationType())
						violationTypes.add(softSkillViolation.getViolationType());
				}
			}

			Set<String> skillTypeTypes = bucketsBySkillType.keySet();
			//System.out.println("SkillTypeTypes: " + skillTypeTypes + "\n");
			for (String st : skillTypeTypes) {
				List<Bucket> bl = bucketsBySkillType.get(st);
				//System.out.println("BucketList: " + bl);
				for (Bucket b : bl) {
					String bucketKey = b.getBucketDescription();
					//List<QuestionScore> questionScores = questionScoreRepository.findAllByBucketId(b.getBucketId());
					for (Question q : b.getQuestions()) {
						String questionText = q.getQuestionText();
						for (QuestionScore qs : q.getQuestionScores()) {
							//System.out.println("\n---- bucket" + b.getBucketId() + " has questionScores: " + qs);
							Double value = qs.getScore();
							if (value == null) break;
							if (!scoresBySkillType.containsKey(st)) {
								scoresBySkillType.put(st, value);
								numScoresPerSkillType.put(st, 1);
							} else {
								if (st == null) {
									bucketsBySkillType.remove(st);
								} else {
									double currentSkillSum = scoresBySkillType.get(st);
									double newSkillSum = currentSkillSum + value;
									scoresBySkillType.put(st, newSkillSum);
									numScoresPerSkillType.put(st, numScoresPerSkillType.get(st) + 1);
								}
							}
							
							if (bucketKey != null && !scoresByDescription.containsKey(bucketKey)) {
								scoresByDescription.put(bucketKey, value);
								numScoresPerDescription.put(bucketKey, 1);
							} else {
								double currentBucketSum = scoresByDescription.get(bucketKey);
								double newBucketVal = currentBucketSum + value;
								scoresByDescription.put(bucketKey, newBucketVal);
								numScoresPerDescription.put(bucketKey, numScoresPerDescription.get(bucketKey) + 1);
							}
						
							if (questionText != null && !scoresByQuestion.containsKey(questionText)) {
								scoresByQuestion.put(questionText, value);
								numScoresPerQuestion.put(questionText, 1);
							} else {
								double currentQuestionSum = scoresByQuestion.get(questionText);
								double newQuestionSum = currentQuestionSum + value;
								scoresByQuestion.put(questionText, newQuestionSum);
								numScoresPerQuestion.put(questionText, numScoresPerQuestion.get(questionText) + 1);
							}
						}
					}
				}
			}
			
			Set<String> keyset = scoresByDescription.keySet();
			Iterator<String> iter = keyset.iterator();
			while (iter.hasNext()) {
				String key = iter.next();
				if (scoresByDescription.get(key) != 0) {
					double scoreByDescription = scoresByDescription.get(key) / (double) numScoresPerDescription.get(key);
					scoresByDescription.put(key, scoreByDescription);
				}
				//System.out.println("Description=" + key + "&Total Score=" + scoresByDescription.get(key));
			}
			
			iter = scoresBySkillType.keySet().iterator();
			while (iter.hasNext()) {
				String key = iter.next();
				//System.out.print("key=" + key + "&value=");
				double scoreBySkillType = scoresBySkillType.get(key) / (double) numScoresPerSkillType.get(key);
				//System.out.println(scoreBySkillType);
				scoresBySkillType.put(key,  scoreBySkillType);
			}
			
			//System.out.println("scoresByQuestion = " + scoresByQuestion);
		}
		
		if (violationTypes != null) {
			for (ViolationType v : violationTypes) {
				String key = v.getViolationTypeText();
				if (!numViolationsByType.containsKey(key)) {
					numViolationsByType.put(key, 1);
				} else if (numViolationsByType.containsKey(key)) {
					numViolationsByType.put(key, numViolationsByType.get(key) + 1);
				}
			}
		}
		
		int numScheduledScreenings = scheduledScreenings.size();
		ReportByEmailAndWeeksModel report = new ReportByEmailAndWeeksModel(
				new Integer(screener.getScreenerId()),
				screener.getEmail(),
				scoresBySkillType, 
				scoresByDescription,
				numViolationsByType, 
				numScheduledScreenings);
		return report;
	}

	public String getReport(String email, String weeks) {
		List<ReportByEmailAndWeeksModel> reports = new ArrayList<>();
		if (email == null || email.isEmpty() || email.equals("null")) {
			List<Screener> screeners = screenerRepository.findAll();
			for (Screener s : screeners) {
				if (s.hasScreenings())
					reports.add(getJsonReportForEmailAndWeeks(s.getEmail(), weeks));
			}
		} else {
			reports.add(getJsonReportForEmailAndWeeks(email, weeks));
		}
		
		Iterator<String> iter = scoresByQuestion.keySet().iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			//System.out.print("questionText=" + key + "&value=");
			double scoreByQuestion = scoresByQuestion.get(key) / (double) numScoresPerQuestion.get(key);
			//System.out.println(scoreByQuestion);
			scoresByQuestion.put(key,  scoreByQuestion);
		}
		
		ReportByWeeksModel reportByWeeksModel = new ReportByWeeksModel(reports, scoresByQuestion);
		Gson gson = new Gson();
		String json = gson.toJson(reportByWeeksModel);
		return json;
	}
}




