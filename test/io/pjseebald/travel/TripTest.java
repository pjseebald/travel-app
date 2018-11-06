package io.pjseebald.travel;

import static org.junit.Assert.*;

import java.time.LocalTime;

import org.junit.BeforeClass;
import org.junit.Test;

public class TripTest {
	
	// Standard values for a trip
	static final int minuteDifference = 32;
	static LocalTime start;
	static LocalTime end;
	static double miles;
	
	static Trip trip;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		start = LocalTime.of(5, 11);
		end = start.plusMinutes(minuteDifference);
		miles = 33.4;
		
		// Establish one legitimate trip
		trip = new Trip(start, end, miles);
	}

	@Test(expected=IllegalStateException.class)
	public void testNegativeMilesTraveled() {
		double negMiles = -1*miles;
		
		new Trip(start, end, negMiles);
		
		// Should not get here, exception should be thrown during trip creation
		fail("Trip: exception should have been thrown for negative miles traveled.");
	}
	
	@Test(expected=IllegalStateException.class)
	public void testNegativeTimeTraveled() {
		LocalTime end = start.minusMinutes(15);
		
		new Trip(start, end, miles);
		
		// Should not get here, exception should be thrown during trip creation
		fail("Trip: exception should have been thrown for negative time traveled.");
	}
	
	@Test
	public void testSettingMilesTraveled() {
		double newMiles = 101.3;
		double delta = 0.01;
		
		trip.setDistanceTraveled(newMiles);
		assertEquals("Trip miles traveled (setting) is incorrect. Expected: " + newMiles + "; returned: " + trip.getDistanceTraveled(), 
				newMiles, trip.getDistanceTraveled(), delta);
	}
	
	@Test
	public void testGettingMilesTraveled() {
		double delta = 0.001;
		assertEquals("Trip miles traveled (getting) is incorrect. Expected: " + miles + "; returned: " + trip.getDistanceTraveled(), 
				trip.getDistanceTraveled(), miles, delta);
	}
	
	@Test
	public void testGetHoursTraveled() {
		double hoursTraveled = trip.getTimeTraveled();
		double expectedValue = minuteDifference/60.;
		double delta = 0.001;
		
		assertEquals("Expected to travel for " + minuteDifference +  "minutes but did not get that expected value returned to delta of " + delta, 
				expectedValue, hoursTraveled, delta);
		
		int increaseHours = 12;
		Trip t = new Trip(start, start.plusHours(increaseHours), miles);
		
		assertEquals("Expected to travel for " + increaseHours + " and returned: " + t.getTimeTraveled(), 
				increaseHours, t.getTimeTraveled(), delta);
		
	}
	
	@Test
	public void testCheckTripRequirements() {
		// Set requirements so physically possible trips can be outside the criteria
		Trip.setTimeRange(1., 2.);
		Trip.setDistanceRange(50., 200.);
		Trip.setSpeedRange(40., 150.);
		
		// Create three trips: one below minimum time, one above maximum, and one in the range
		// Meets criteria
		Trip trip = new Trip(LocalTime.of(5, 1), LocalTime.of(6, 31), 150.);
		this.checkTrip(true, trip, "Meets criteria");
		// Below min time
		Trip tripBelowMinTime = new Trip(LocalTime.of(5, 1), LocalTime.of(5, 31), 51.);
		this.checkTrip(false, tripBelowMinTime, "Below minimum time");
		// Above max time
		Trip tripAboveMaxTime = new Trip(LocalTime.of(5, 1), LocalTime.of(8, 1), 199.);
		this.checkTrip(false, tripAboveMaxTime, "Above maximum time");
		
		// Create two more trips: one below minimum distance, one above maximum distance
		// Below min distance
		Trip tripBelowMinDistance = new Trip(LocalTime.of(5, 1), LocalTime.of(6, 2), 45.);
		this.checkTrip(false, tripBelowMinDistance, "Below minimum distance");
		// Above max distance
		Trip tripAboveMaxDistance = new Trip(LocalTime.of(5, 1), LocalTime.of(7, 0), 205.);
		this.checkTrip(false, tripAboveMaxDistance, "Above maximum distance");
		
		// Create two more trips: one below minimum speed, one above maximum speed
		// Below min speed
		Trip tripBelowMinSpeed = new Trip(LocalTime.of(5, 1), LocalTime.of(7, 0), 51.);
		this.checkTrip(false, tripBelowMinSpeed, "Below minimum speed");
		// Above max speed
		Trip tripAboveMaxSpeed = new Trip(LocalTime.of(5, 1), LocalTime.of(6, 2), 175.);
		this.checkTrip(false, tripAboveMaxSpeed, "Above maximum speed");
		
	}
	
	@Test
	public void testSettingRequirements() {
		Trip.setTimeRange(1., 2.);
		checkDoubleArraysAreEqual(Trip.getTimeRange(), new Double[] {1., 2.});
		
		Trip.setDistanceRange(50., 200.);
		checkDoubleArraysAreEqual(Trip.getDistanceRange(), new Double[] {50., 200.});
		
		Trip.setSpeedRange(40., 150.);
		checkDoubleArraysAreEqual(Trip.getSpeedRange(), new Double[] {40., 150.});
		
	}
	
	private void checkDoubleArraysAreEqual(Double[] a, Double[] b) {
		double delta = 0.001;
		if (a.length == b.length) {
			for (int i = 0; i < a.length; i++) {
				assertEquals("", a[i], b[i], delta);
			}
		}
	}
	
	private void checkTrip(boolean expected, Trip t, String failLabel) {
		boolean returned = Trip.checkTrip(t);
		assertFalse("Trip requirements were not evaluated properly for trip with label: " 
				+ failLabel + ". Expected: " + expected + "; returned: " + returned, 
				returned ^ expected);
	}

}
