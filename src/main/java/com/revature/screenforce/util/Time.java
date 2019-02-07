package com.revature.screenforce.util;

import java.time.LocalDate;

/** 
 * Convenience class to select a date based on period, ie WTD, MTD, YTD 
 **/
// http://tutorials.jenkov.com/java-date-time/localdate.html
public class Time {
	public LocalDate getWeekToDate(LocalDate from, int numWeeks) {
		return from.minusWeeks(numWeeks);
	}
	
	public LocalDate getMonthToDate(LocalDate from, int numMonths) {
		return from.minusMonths(numMonths);
	}
	
	public LocalDate getYearToDate(LocalDate from) {
		return from.minusYears(1);
	}
}
