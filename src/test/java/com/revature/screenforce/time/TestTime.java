package com.revature.screenforce.time;

import java.time.LocalDate;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.revature.screenforce.util.Time;


public class TestTime {
	private Time time;
	
	@Before
	public void init() {
		time = new Time();
	}
	
	@Test
	public void testWeekBefore() {
		LocalDate today = LocalDate.of(2019, 2, 7);
		Assert.assertEquals(LocalDate.of(2019, 1, 31), time.getWeekToDate(today, 1));
		Assert.assertEquals(LocalDate.of(2019, 1, 7), time.getMonthToDate(today, 1));
		Assert.assertEquals(LocalDate.of(2018, 2, 7), time.getYearToDate(today));
	}
}
