package com.revature.screenforce.util;

import java.time.LocalDate;

/** 
 * Convenience class to select a date based on period, ie WTD, MTD, YTD 
 **/
// http://tutorials.jenkov.com/java-date-time/localdate.html
public class Time {
	/**
	 * 
	 * @param from
	 * 	The end date of the period for reporting aggregate metrics
	 * @param numWeeks The number of weeks
	 * @return The start date
	 */
	public LocalDate getWeekToDate(LocalDate from, int numWeeks) {
		return from.minusWeeks(numWeeks);
	}
	
	/**
	 * 
	 * @param from
	 * 	The end date of the period for reporting aggregate metrics
	 * @param numMonths The number of months
	 * @return The start date
	 */
	public LocalDate getMonthToDate(LocalDate from, int numMonths) {
		return from.minusMonths(numMonths);
	}
	
	/**
	 * 
	 * @param from
	 * 	The end date of the period for reporting aggregate metrics
	 * @return The start date one year prior to from
	 */
	public LocalDate getYearToDate(LocalDate from) {
		return from.minusYears(1);
	}
}
