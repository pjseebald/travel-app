package io.pjseebald.travel;

import java.time.LocalTime;
import java.util.concurrent.TimeUnit;


/**
 * <h1>Trip</h1>
 * Contains relevant information for a trip being taken by a traveler or travelers.
 * Also contains static criteria that trips must meet to be deemed acceptable.
 * A trip contains a start and end time, distance traveled and the method of the travel.
 * 
 * @author PaulSEEBALD
 * @version 1.0
 * @since 2018-11-02
 */
public class Trip {
	
	private LocalTime startTime;
	private LocalTime endTime;
	private double timeTraveled;	// Uses default time units, per UnitConversion class.
	
	// Using an Object (Integer) instead of primitive because I want an initialized value of null instead of a number.
	// Need to know if it's been initialized or not.
	private Double distanceTraveled = null;
	
	/*
	 * Trip Requirements
	 * Ranges of time, distance, speed for acceptable trips.
	 * These are two-element arrays set up as [min, max]
	 * Units are the default units defined in UnitConversion
	 * Note: default distance unit is miles
	 * Note: This checks if the trip meets the desired possible ranges. 
	 * Physically possible trips (e.g. negative time, negative miles) are checked upon Trip creation because
	 * these values may be changed to be non-zero minimum time or distance.
	 */
	private static Double[] timeRange = {0., 24.};
	private static Double[] distanceRange = {0., null};
	private static Double[] speedRange = {5., 100.};
	
	/**
	 * Create a Trip based off of start, end times, distance traveled and the travel method.
	 * @param startTime		Time when the trip started.
	 * @param endTime		Time when the trip ended.
	 * @param distance		Distance traveled during the trip.
	 * @param method		Method of travel.
	 */
	Trip(LocalTime startTime, LocalTime endTime, double distance) {
		this.startTime = startTime;
		this.endTime = endTime;
		this.setTimeTraveled();
		
		this.distanceTraveled = distance;
		
		this.checkForImpossibilities();
	}
	
	/*
	 * *******************************************
	 * Error checking
	 */
	
	/**
	 * Check this Trip for physical impossibilities, like traveling negative distance
	 * or starting a trip after its ending time. Will throw an IllegalStateException if 
	 * a physical impossibility is found.
	 * Note: Physically impossible trip is considered different than a trip that doesn't meet criteria.
	 */
	private void checkForImpossibilities() {
		String exceptionMessage = "";
		boolean exceptionOccurred = false;
		if (!this.checkForImpossibleDistance()) {
			exceptionOccurred = true;
			exceptionMessage += "Invalid input: distance traveled is a negative value." + System.lineSeparator();
		}
		
		if (!this.checkForImpossibleTimes()) {
			exceptionOccurred = true;
			exceptionMessage += "Invalid input: time traveled is s negative value. Start time cannot be later than end time." + System.lineSeparator();
		}
		
		if (exceptionOccurred) {
			throw new IllegalStateException(exceptionMessage);
		}
	}
	
	/**
	 * Check if endTime is after startTime. This method needs adjusting if a driver can go past midnight.
	 * @return boolean		true if times are possible, false if impossible.
	 */
	private boolean checkForImpossibleTimes() {
		return endTime.isAfter(startTime);
	}
	
	/**
	 * Check if distance traveled is positive.
	 * @return boolean		true if distance is positive or zero, false if negative. 
	 */
	private boolean checkForImpossibleDistance() {
		if (distanceTraveled != null) {
			if (distanceTraveled < 0) {
				return false;
			}
		}
		return true;
	}
	
	/*
	 * *******************************************
	 * Setters for values that are not required in all constructors
	 */
	
	/**
	 * Setter for the timeTraveled variable based on start and end times. Called in constructor.
	 * Converted from seconds to the default time unit (per UnitConversion.class).
	 */
	public void setTimeTraveled() {
		this.timeTraveled = UnitConversion.convert(endTime.toSecondOfDay() - startTime.toSecondOfDay(), 
				TimeUnit.SECONDS, UnitConversion.getDefaultTimeUnit());
	}

	/**
	 * Setter for distance traveled.
	 * @param distanceTraveled		Distance traveled during the trip.
	 */
	public void setDistanceTraveled(double distanceTraveled) {
		this.distanceTraveled = distanceTraveled;
	}
	
	/*
	 * *******************************************
	 * Getting statistics related to the trip
	 */

	/**
	 * Getter for time traveled on the trip.
	 * @return double		Time traveled in default time units during the trip.
	 */
	public double getTimeTraveled() {
		return this.timeTraveled;
	}
	
	/**
	 * Getter for distance traveled on the trip.
	 * @return double		Distance traveled in default distance units during the trip.
	 */
	public double getDistanceTraveled() {
		if (distanceTraveled != null) {
			return distanceTraveled.doubleValue();
		} else {
			// Calculate from origin, destination, travel method.
			// Would need more details about route.
		}
		
		return -1;
	}
	
	/**
	 * Checks if an input Trip meets acceptable requirements.
	 * Note: this is different from physical impossibilities. A physically impossible trip
	 * is an input error. An unacceptable trip is one that doesn't meet certain filtering criteria
	 * or requirements. These criteria can be set by default or manually.
	 * @param trip			Trip to check if it meets requirements.
	 * @return boolean		true if trip meets criteria, false if not.
	 */
	public static boolean checkTrip(Trip trip) {
		
		// Check time range
		double time = trip.getTimeTraveled();
		if ((timeRange[0] != null && time < timeRange[0]) || (timeRange[1] != null && time > timeRange[1])) {
			return false;
		}
		
		// Check distance range
		double distance = trip.getDistanceTraveled();
		if ((distanceRange[0] != null && distance < distanceRange[0]) || (distanceRange[1] != null && distance > distanceRange[1])) {
			return false;
		}
		
		// Check speed range
		double speed = distance/time;
		if ((speedRange[0] != null && speed < speedRange[0]) || (speedRange[1] != null && speed > speedRange[1])) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * Returns the time criteria that trips have to meet to be acceptable.
	 * Set in default time units (per UnitConversion.class).
	 * Defined as an array of two [min, max]
	 * @return Double[]		Acceptable time range criteria [min, max]
	 */
	public static Double[] getTimeRange() {
		return timeRange;
	}

	/**
	 * Returns the distance criteria that trips have to meet to be acceptable.
	 * Set in default distance units (per UnitConversion.class).
	 * Defined as an array of two [min, max]
	 * @return Double[]		Acceptable distance range criteria [min, max]
	 */
	public static Double[] getDistanceRange() {
		return distanceRange;
	}

	/**
	 * Returns the speed criteria that trips have to meet to be acceptable.
	 * Set in default speed units (per UnitConversion.class).
	 * Defined as an array of two [min, max]
	 * @return Double[]		Acceptable speed range criteria [min, max]
	 */
	public static Double[] getSpeedRange() {
		return speedRange;
	}

	/**
	 * Set the min and max time values (default time units) for a trip to be acceptable.
	 * @param min		minimum time an acceptable trip needs to have (default time units).
	 * @param max		maximum time an acceptable trip can have (default time units).
	 */
	public static void setTimeRange(Double min, Double max) {
		timeRange[0] = min;
		timeRange[1] = max;
	}
	
	/**
	 * Set the min and max distance values (default distance units) for a trip to be acceptable.
	 * @param min		minimum distance an acceptable trip needs to have (default distance units).
	 * @param max		maximum distance an acceptable trip can have (default distance units).
	 */
	public static void setDistanceRange(Double min, Double max) {
		distanceRange[0] = min;
		distanceRange[1] = max;
	}
	
	/**
	 * Set the min and max speed values (default speed units) for a trip to be acceptable.
	 * @param min		minimum speed an acceptable trip needs to have (default speed units).
	 * @param max		maximum speed an acceptable trip can have (default speed units).
	 */
	public static void setSpeedRange(Double min, Double max) {
		speedRange[0] = min;
		speedRange[1] = max;
	}
	
	

}
