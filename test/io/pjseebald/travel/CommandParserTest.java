package io.pjseebald.travel;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class CommandParserTest {

	@Before
	public void setUpBeforeTest() throws Exception {
		Map<String, Driver> drivers = MainApp.getDrivers();
		if (drivers != null) {
			drivers.clear();
		}
		
		// Set trip requirements to standard
		Trip.setTimeRange(0., 60.*3600.);
		Trip.setDistanceRange(0., null);
		Trip.setSpeedRange(5., 100.);
	}

	@Test
	public void testParseCommandDriver() {
		
		String[] driverNames = {"Dan", "Jane", "Tim"};
		
		this.addDrivers(driverNames);

		Map<String, Driver> drivers = MainApp.getDrivers();
		assertEquals("Size of drivers map is incorrect. Expected: " 
				+ driverNames.length + "; returned: " + drivers.size(), driverNames.length, drivers.size());
	}
	
	@Test
	public void testParseCommandTrip() {
		String[] driverNames = {"Dan", "Jane", "Tim"};
		
		this.addDrivers(driverNames);
		
		String[][] trips = {{"trip", "Dan", "11:15", "11:45", "32.3"}, {"trip", "Dan", "18:04", "19:38", "115.6"}, {"trip", "Jane", "01:55", "03:20", "55.4"}};
		// Get number of trips for each driver
		int[] tripNums = new int[driverNames.length];
		for (int i = 0; i < trips.length; i++) {
			String name = trips[i][1];
			
			// Inefficient but acceptable for a small unit test
			int j = Arrays.asList(driverNames).indexOf(name);
			tripNums[j]++;
		}
		
		this.addTrips(trips);
		
		Map<String, Driver> drivers = MainApp.getDrivers();
		
		for (int i = 0; i < driverNames.length; i++) {
			String name = driverNames[i];
			Driver driver = drivers.get(name);
			assertEquals("MainApp driver " + name + " trips number is incorrect. Expected: " + tripNums[i] + "; returned: " + driver.getTrips().size(), 
					tripNums[i], driver.getTrips().size());
		}
		
		
	}
	
	@Test
	public void testParseCommandsCorrectOrder() {
		String[] driverNames = {"Dan", "Jane"};
		String[][] trips = {{"trip", "Dan", "11:15", "11:45", "32.3"}, {"trip", "Jane", "01:55", "03:20", "55.4"}};
		
		this.addDrivers(new String[] {driverNames[0]});
		this.addTrips(new String[][] {trips[0]});
		this.addDrivers(new String[] {driverNames[1]});
		this.addTrips(new String[][] {trips[1]});
		
		Map<String, Driver> drivers = MainApp.getDrivers();
		assertTrue("MainApp drivers were not added in order. Expected 2 drivers; returned: " + drivers.keySet().size(), 
				drivers.keySet().size() == 2);
		
		// Get number of trips for each driver
		Map<String, Integer> tripNums = new HashMap<>();
		for (int i = 0; i < trips.length; i++) {
			String name = trips[i][1];
			int baseValue = 0;
			if (tripNums.containsKey(name)) {
				baseValue = tripNums.get(name);
			}
			
			tripNums.put(name, baseValue+1);
		}
		
		for (String name : drivers.keySet()) {
			Driver driver = drivers.get(name);
			assertTrue("Number of trips for driver " + name + " did not match. Expected: " 
					+ tripNums.get(name) + "; returned: " + driver.getTrips().size(), 
					driver.getTrips().size() == tripNums.get(name));
		}
	}
	
	@Test(expected=NullPointerException.class)
	public void testParseCommandsIncorrectOrder() {
		String[] driverNames = {"Dan", "Jane"};
		String[][] trips = {{"Trip", "Dan", "11:15", "11:45", "32.3"}, {"trip", "Jane", "01:55", "03:20", "155.4"}};

		this.addDrivers(new String[] {driverNames[0]});
		this.addTrips(new String[][] {trips[1]});
		
		fail("MainApp expected to throw null pointer exception due to adding trip to driver that doesn't exist.");
	}
	
	@Test(expected=ArrayIndexOutOfBoundsException.class)
	public void testIncorrentNumberOfTripParameters() {
		String[] driverNames = {"Dan", "Jane"};
		String[][] trips = {{"Trip", "Dan", "11:15", "11:45", "32.3"}, {"trip", "Jane", "01:55", "03:20"}};
		
		this.addDrivers(driverNames);
		this.addTrips(trips);
		
		fail("App expected to throw index out of bounds exception when trying to reference parameter that doesn't exist.");
	}
	
	@Test
	public void testParseCommandDriverNameWithSpaces() {
	
		String name = "Dan the Mighty";
		String[] driverName = name.split(" ");
		String[] fullDriverCommand = new String[driverName.length+1];
		fullDriverCommand[0] = "driver";
		for (int i=0; i < driverName.length; i++) {
			fullDriverCommand[i+1] = driverName[i];
		}
		CommandParser.parseCommand(fullDriverCommand);
		
		Map<String, Driver> drivers = MainApp.getDrivers();
		
		String[] trip = {"Trip", "Dan", "the", "Mighty", "11:15", "11:45", "32.3"};
		CommandParser.parseCommand(trip);
		
		//Map<String, Driver> drivers = MainApp.getDrivers();
		Driver d1 = drivers.get(name);
		
		assertNotNull("MainApp error adding driver with spaces in name: map returned null driver from name.", d1);
		
		assertTrue("MainApp error adding trip to driver with spaces in name. Expected 1; returned: " + d1.getTrips().size(), 
				d1.getTrips().size() == 1);
		
	}
	
	@Test(expected=IllegalStateException.class)
	public void testAddIncorrectNamesAsDrivers() {
		String[] driverNames = {"Dan", "Jane", "J1"};
		this.addDrivers(driverNames);
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
