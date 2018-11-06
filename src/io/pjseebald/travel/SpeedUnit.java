package io.pjseebald.travel;

import java.util.concurrent.TimeUnit;

/**
 * <h1>SpeedUnit</h1>
 * Units of speed, dependent on units of time and distance. The distance and time units
 * are input and stored for each speed unit because time and distance are used for unit conversion.
 * 
 * @author PaulSEEBALD
 * @version 1.0
 * @since 2018-11-01
 */

public enum SpeedUnit {
	
	MILES_PER_HOUR(DistanceUnit.MILES, TimeUnit.HOURS, "mph"), 
	KILOMETERS_PER_HOUR(DistanceUnit.KILOMETERS, TimeUnit.HOURS, "kmh");
	
	// Since these are derivative units, don't need separate conversions
	
	private DistanceUnit distanceUnit;
	private TimeUnit timeUnit;
	private String rep;
	
	SpeedUnit(DistanceUnit d, TimeUnit t, String rep) {
		this.distanceUnit = d;
		this.timeUnit = t;
		this.rep = rep;
	}
	
	public DistanceUnit getDistanceUnit() {
		return this.distanceUnit;
	}
	
	public TimeUnit getTimeUnit() {
		return this.timeUnit;
	}
	
	public String getAsString() {
		return this.rep;
	}
	
}
