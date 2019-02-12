package com.revature.screenforce.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Collections;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import com.revature.screenforce.ScreeningReportServiceApplication;
import com.revature.screenforce.beans.Screener;
import com.revature.screenforce.services.ReportsServiceImp;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ScreeningReportServiceApplication.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@AutoConfigureTestDatabase
public class ReportsServiceImpTest {
	
	@Mock
	Screener screenerTest;
	
	@Rule public MockitoRule mockitoRule = MockitoJUnit.rule(); 
	
	@Autowired
	ReportsServiceImp repostsServiceImp;
	
	@Test
	public void testGetAllEmails() {
		List<String> test = repostsServiceImp.getAllEmails("smith");
		assertNotNull(test);
	}
	
	@Test
	public void testGetNoEmails() {
		List<String> test = repostsServiceImp.getAllEmails("asdf");
		assertEquals(test, Collections.emptyList());
	}
}
