package io.pjseebald.travel;

import static org.junit.Assert.*;

import java.time.LocalTime;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class DriverTest {

	static String name = "Daniel";
	static Driver driver;
	
	@Before
	public void setUpBefore() throws Exception {
		driver = new Driver(name);
		
		// Set trip requirements to standard
		Trip.setTimeRange(0., 60.*3600.);
		Trip.setDistanceRange(0., null);
		Trip.setSpeedRange(5., 100.);
	}

	@Test
	public void testGetDriverName() {
		assertEquals("Expected name " + name + " does not match name of driver: " + driver.getName(), 
				name, driver.getName());
	}
	
	@Test
	public void testAddTrip() {
		int hoursDifference = 3;
		LocalTime lt1 = LocalTime.of(15, 18);
		LocalTime lt2 = lt1.plusHours(hoursDifference);
		double milesTraveled = 73.9;
		assertTrue("Trip was not added as expected.", 
				driver.addTrip(new Trip(lt1, lt2, milesTraveled)));
		
		assertEquals("Number of trips is incorrect. Expected: " + 1 + "; returned: " + driver.getTrips().size(), 
				1, driver.getTrips().size());
		
		double milesDelta = 0.001;
		double hoursDelta = 0.001;
		
		assertEquals("Miles of added trip did not return correctly. Expected: " + milesTraveled + "; returned:" + driver.getDistanceTraveled(), 
				milesTraveled, driver.getDistanceTraveled(), milesDelta);
		assertEquals("Hours of added trip did not return correctly. Expected: " + hoursDifference + "; returned: " + driver.getTimeTraveled(), 
				hoursDifference, driver.getTimeTraveled(), hoursDelta);
		
	}
	
	@Test
	public void testAddTripsMeetRequirements() {
		// Set requirements to make sure we know the trips created will meet the criteria
		// Note: we don't know for certain if driver trips have to meet the criteria or not when added
		// But if they do, we want to make sure these are added correctly.
		// This means we can't always test if trips were *not* added.
		// We will use the TripTest to test if trips meet requirements
		// This allows flexibility to check if trips meet requirements anywhere in the app.
		Trip.setTimeRange(1., 2.);
		Trip.setDistanceRange(50., 251.);
		Trip.setSpeedRange(40., 150.);
		
		driver.addTrip(new Trip(LocalTime.of(5, 1), LocalTime.of(6, 31), 150.));
		driver.addTrip(new Trip(LocalTime.of(20, 1), LocalTime.of(21, 3), 60.));
		driver.addTrip(new Trip(LocalTime.of(10, 51), LocalTime.of(12, 40), 250.));
		
		List<Trip> driverTrips = driver.getTrips();
		
		assertEquals("Trip requirements were not evaluated properly. Expected: 3; returned: " + driverTrips.size(), 
				3, driverTrips.size());
		
	}

	@Test
	public void testGetMilesTraveled() {
		double milesDelta = 0.001;
		
		int minDiff = 100;
		LocalTime ltStart = LocalTime.of(15, 18);
		
		double[] milesTraveledArray = {100.1, 14.8, 27.4};
		
		double totalMiles = 0;
		for (double m : milesTraveledArray) {
			totalMiles += m;
			driver.addTrip(new Trip(ltStart, ltStart.plusMinutes(minDiff), m));
		}
		
		assertEquals("Driver miles traveled are incorrect. Expected: " + totalMiles + "; returned: " + driver.getDistanceTraveled(), 
				totalMiles, driver.getDistanceTraveled(), milesDelta);
		
	}
	
	@Test
	public void testGetHoursTraveled() {
		double hoursDelta = 0.001;
		
		LocalTime ltStart = LocalTime.of(1, 1);
		
		int[] minDiff = {168, 599, 42, 861, 44};
		double[] milesTraveled = {200, 500, 20, 560, 40};		// Need to make sure they meet requirements.
		
		double totalHours = 0;
		for (int i = 0; i < minDiff.length; i++) {
			int mins = minDiff[i];
			double miles = milesTraveled[i];
			totalHours += new Double(mins/60.);
			driver.addTrip(new Trip(ltStart, ltStart.plusMinutes(mins), miles));
		}
		
		assertEquals("Driver miles traveled are incorrect. Expected: " + totalHours + "; returned: " + driver.getTimeTraveled(), 
				totalHours, driver.getTimeTraveled(), hoursDelta);
	}

}
