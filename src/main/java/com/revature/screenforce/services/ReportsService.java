package com.revature.screenforce.services;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
import com.revature.screenforce.util.Time;

@Service
public class ReportsService {
	@Autowired ScreenerRepository screenerRepository;
	@Autowired SkillTypeDAO skillTypeDAO;
	@Autowired QuestionScoreRepository questionScoreRepository;
	@Autowired QuestionDAO questionDAO;
	@Autowired SoftSkillViolationRepository softSkillViolationRepository;
	@Autowired ViolationTypeRepository violationTypeRepository;
	@Autowired WeightDAO weightDAO;
	
	private Map<String, Double> scoresByQuestion = new HashMap<>();
	private Map<String, Integer> numScoresPerQuestion = new HashMap<>();
	private Map<String, Double> top5HardestQuestions = new TreeMap<>();
	private ArrayList<String> questionKeys = new ArrayList<>();
	private List<SoftSkillViolation> softSkillViolations = new ArrayList<>();
	private List<ViolationType> violationTypes = new ArrayList<>();
	
	private Map<String, List<Bucket>> bucketsBySkillType = new HashMap<>();
	private Map<String, Double> scoresByDescription = new HashMap<>();
	private Map<String, Integer> numScoresPerDescription = new HashMap<>();
	private Map<String, Double> scoresBySkillType = new HashMap<>();
	private Map<String, Integer> numScoresPerSkillType = new HashMap<>();
	
	private Map<String, Integer> numViolationsByType = new HashMap<>();
	
	int numScheduledScreenings = 0;
	
	public List<String> getAllEmails(String email){
		List<Screener> screenerList = screenerRepository.findAllByEmailContainingIgnoreCase(email);
		List<String> emailList = new ArrayList<>();
		for(Screener screener : screenerList) {
				emailList.add(screener.getEmail());
		}
		return emailList;
	}
	
	public ReportByEmailAndWeeksModel getJsonReportForEmailAndWeeks(String email, String weeks) {
		Time time = new Time();
		LocalDate startDate = time.getWeekToDate(Integer.parseInt(weeks));
		LocalDate endDate = LocalDate.now(ZoneOffset.UTC);
		
		Screener screener = screenerRepository.getByEmail(email);

		if (screener.getScreenings() == null) return null;
		List<Screening> screenings = screener.getScreenings();
		List<ScheduledScreening> scheduledScreenings = new ArrayList<>();
		
		if (screenings != null) {
			for (Screening s : screenings) {
				if(s.getEndDateTime().isAfter(startDate) && s.getEndDateTime().isBefore(endDate)) {
				
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
				
				numScheduledScreenings += scheduledScreenings.size();
			}
			}

			Set<String> skillTypeTypes = bucketsBySkillType.keySet();
			for (String st : skillTypeTypes) {
				List<Bucket> bl = bucketsBySkillType.get(st);
				for (Bucket b : bl) {
					String bucketKey = b.getBucketDescription();
					for (Question q : b.getQuestions()) {
						String questionText = q.getQuestionText();
						for (QuestionScore qs : q.getQuestionScores()) {
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
		}
		
		return new ReportByEmailAndWeeksModel(
				screener.getScreenerId(),
				screener.getEmail(),
				screener.getName());
	}

	public String getReport(String email, String weeks) {
		reset();
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
		
		Set<String> keyset = scoresByDescription.keySet();
		Iterator<String> iter = keyset.iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			if (scoresByDescription.get(key) != 0) {
				double scoreByDescription = scoresByDescription.get(key) / (double) numScoresPerDescription.get(key);
				scoresByDescription.put(key, scoreByDescription);
			}
		}
		
		iter = scoresBySkillType.keySet().iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			double scoreBySkillType = scoresBySkillType.get(key) / (double) numScoresPerSkillType.get(key);
			scoresBySkillType.put(key,  scoreBySkillType);
		}
		
		iter = scoresByQuestion.keySet().iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			double scoreByQuestion = scoresByQuestion.get(key) / (double) numScoresPerQuestion.get(key);
			scoresByQuestion.put(key,  scoreByQuestion);
			questionKeys.add(key);
		}
		System.out.println(questionKeys);
		top5HardestQuestions = top5HardestSort(scoresByQuestion, questionKeys);
		
		for (ViolationType v : violationTypes) {
			String key = v.getViolationTypeText();
			if (!numViolationsByType.containsKey(key)) {
				numViolationsByType.put(key, 1);
			} else if (numViolationsByType.containsKey(key)) {
				numViolationsByType.put(key, numViolationsByType.get(key) + 1);
			}
		}
		
		ReportByWeeksModel reportByWeeksModel = new ReportByWeeksModel(
				reports, 
				scoresByDescription, 
				scoresBySkillType, 
				top5HardestQuestions,
				numViolationsByType,
				numScheduledScreenings);
		Gson gson = new Gson();
		String json = gson.toJson(reportByWeeksModel);
		return json;
	}
	
	public TreeMap<String, Double> top5HardestSort(
			Map<String, 
			Double> mapToSort, 
			ArrayList<String> keys){
		
		TreeMap<String, Double> sortedTreeMap = new TreeMap<>();
		TreeSet<String> keysAndValues = new TreeSet<>();
		
		for(String key : keys) {
			keysAndValues.add(String.valueOf(mapToSort.get(key)) + ";" + key);
		}
		
		int count = 1;
		
		for(String keyAndValue : keysAndValues) {
			if(count < 6) {
				String key = keyAndValue.substring(keyAndValue.indexOf(";") + 1, keyAndValue.length());
				Double value = Double.parseDouble(keyAndValue.substring(0, keyAndValue.indexOf(";")));
				sortedTreeMap.put(String.valueOf(count) + ". " + key, value);
			}
			count++;
		}
		
		return sortedTreeMap;
	}
	public void reset() {
		numScheduledScreenings = 0;
		if (!scoresByQuestion.isEmpty()) scoresByQuestion.clear();
		if (!numScoresPerQuestion.isEmpty()) numScoresPerQuestion.clear();
		if (!top5HardestQuestions.isEmpty()) top5HardestQuestions.clear();
		if (!questionKeys.isEmpty()) questionKeys.clear();
		if (!softSkillViolations.isEmpty()) softSkillViolations.clear();
		if (!violationTypes.isEmpty()) violationTypes.clear();
		if (!bucketsBySkillType.isEmpty()) bucketsBySkillType.clear();
		if (!scoresByDescription.isEmpty()) scoresByDescription.clear();
		if (!numScoresPerDescription.isEmpty()) numScoresPerDescription.clear();
		if (!scoresBySkillType.isEmpty()) scoresBySkillType.clear();
		if (!numScoresPerSkillType.isEmpty()) numScoresPerSkillType.clear();
		if (!numViolationsByType.isEmpty()) numViolationsByType.clear();
	}
}




