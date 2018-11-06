package io.pjseebald.travel;

import java.util.ArrayList;
import java.util.List;


/**
 * <h1>Driver</h1>
 * A Driver is a person with a name who is able to go on trips.
 * It is possible to query how much time a Driver has spent traveling and how far they have traveled. 
 * 
 * @author PaulSEEBALD
 * @version 1.0
 * @since 2018-11-02
 *
 */
public class Driver {
	
	private String name;

	// Using list instead of array because the initial number of trips is unknown
	// With more app complexity, this would more likely be a result of a query to a database.
	private List<Trip> trips = new ArrayList<>();
	
	/**
	 * Constructor that requires a name for the Driver.
	 * @param name		String that can contain spaces.
	 */
	public Driver(String name) {
		this.name = name;
	}
	
	/**
	 * Getter for the Driver's name.
	 * @return	String		Driver's name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Getter for the Driver's list of Trips
	 * @return List<Trip>	Trips the Driver has gone on.
	 */
	public List<Trip> getTrips() {
		return this.trips;
	}

	/**
	 * Add a Trip to the Driver's list of Trips.
	 * @param trip		Trip the Driver has gone on.
	 */
	public boolean addTrip(Trip trip) {
		// Ensure trip meets the requirements to be added
		if (!Trip.checkTrip(trip)) {
			return false;
		}
		trips.add(trip);
		return true;
	}
	
	/**
	 * Get the total distance traveled by the Driver across all Trips.
	 * @return double		Returned in same units returned by Trip.getDistanceTraveled().
	 */
	public double getDistanceTraveled() {
		double distanceTraveled = 0;
		for (Trip trip : trips) {
			distanceTraveled += trip.getDistanceTraveled();
		}
		
		return distanceTraveled;
	}

	/**
	 * Get the total time traveled by the Driver across all Trips.
	 * @return double		Returned in same units returned by Trip.getTimeTraveled().
	 */
	public double getTimeTraveled() {
		double timeTraveled = 0;
		for (Trip trip: trips) {
			timeTraveled += trip.getTimeTraveled();
		}
		
		return timeTraveled;
	}
	
	/*
	 * Comment: In the current implementation, the report requires both distance and speed.
	 * If speed is queried often and list of trips is lengthy, would be appropriate to create a separate function for speed.
	 * This is because getting distance and time traveled for each driver will loop through the trips list twice.
	 * Speed could be obtained by looping only once and summing both distance and time within one loop.
	 */


}
