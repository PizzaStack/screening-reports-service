package com.revature.screenforce.models;

import java.util.ArrayList;
import java.util.List;

public class ReportByWeeksModel {
	List<ReportByEmailAndWeeksModel> screeners = new ArrayList<>();
	
	public ReportByWeeksModel(List<ReportByEmailAndWeeksModel> screeners) {
		this.screeners = screeners;
	}
}
