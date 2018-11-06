package io.pjseebald.travel;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class DriverReportTest {

	@Before
	public void setUpBeforeTest() throws Exception {
		// MainApp and drivers are static, so they will persist across tests.
		// Need to clear them for fresh start for each test.
		Map<String, Driver> drivers = MainApp.getDrivers();
		if (drivers != null) {
			drivers.clear();
		}
	}

	@Test
	public void testGetInformation() {
		String[] driverNames = {"Jane", "Dan", "Tim"};
		
		this.addDrivers(driverNames);
		
		String[][] trips = {{"Trip", "Jane", "01:55", "03:20", "55.4"}, {"Trip", "Dan", "11:15", "11:45", "32.3"}, {"Trip", "Dan", "18:04", "19:38", "115.6"}};
		
		this.addTrips(trips);
		
		String report = new DriverReport().getReport();
		
		String expectedReport = "Dan: 148 miles @ 72 mph" + System.lineSeparator() 
			+ "Jane: 55 miles @ 39 mph" + System.lineSeparator() 
			+ "Tim: 0 miles" + System.lineSeparator();
		
		assertEquals("MainApp reported information did not match expected report. Expected: " + System.lineSeparator() 
				+ expectedReport + System.lineSeparator() + System.lineSeparator() + "Returned: " + System.lineSeparator() + report, 
				expectedReport, report);
		
	}
	
	private void addDrivers(String[] driverNames) {
		for (String name : driverNames) {
			CommandParser.parseCommand(new String[] {"driver", name});
		}
	}
	
	private void addTrips(String[][] trips) {
		for (String[] trip : trips) {
			CommandParser.parseCommand(trip);
		}
	}

}
