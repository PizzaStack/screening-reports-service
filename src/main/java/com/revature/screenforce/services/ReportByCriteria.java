package com.revature.screenforce.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.revature.screenforce.beans.Screening;
import com.revature.screenforce.models.ReportByEmailAndWeeksModel;

@Service
public class ReportByCriteria {
	@Autowired EntityManager em;
	Map<String, Double> scoresByBucketDescription = new HashMap<String, Double>();
	
	private void setScoresByBucketDescription(){
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Screening> cqScreenings = cb.createQuery(Screening.class);
		Root<Screening> rscreening = cqScreenings.from(Screening.class);
		cqScreenings.select(rscreening);
		TypedQuery<Screening> tqscreening = em.createQuery(cqScreenings);
		List<Screening> screenings = tqscreening.getResultList();
		System.out.println("Screenings: " + screenings);
		
		scoresByBucketDescription.put("screening:", 5.0);
	}
	
	public String getIndividualReport(String email, String weeks) {
		Gson gson = new Gson();
		setScoresByBucketDescription();
		ReportByEmailAndWeeksModel report = new ReportByEmailAndWeeksModel(scoresByBucketDescription, 2, 2);
		String json = gson.toJson(report);
		return json;
	}
}
