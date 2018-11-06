package io.pjseebald.travel;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <h1>UnitConversion</h1>
 * Helper class to convert between various important units for traveling.
 * Default units are set and can be used across the application.
 * However, the conversion methods can be used for any units included in the enums of:
 * TimeUnit, DistanceUnit, and SpeedUnit.
 * 
 * @author PaulSEEBALD
 * @version 1.0
 * @since 2018-10-30
 */
public class UnitConversion {

	/*
	 *  Maps of conversion values from the default time unit to given time unit
	 *  Usage: (default unit value) * (map value) = (map key unit value)
	 * 	Example with miles as default:	(10 miles) * (1.61) = (16.1 km)
	 * 	Where map key/value pair is: (DistanceUnit.KILOMETERS, 1.61)
	 * 	Or more generally converting between two non-default units:
	 *  Usage: (from units value) * (to units map value) / (from units map value) = (to units value)
	 *  Example with meters as default: (10 miles) * (0.001) / (0.0006213) = (16.1 km)
	 *  Where map key/value pairs are: (DistanceUnit.KILOMETERS, 0.001), (DistanceUnit.MILES, 0.0006213), 
	 */
	private static Map<TimeUnit, Double> timeConversion = new HashMap<>();
	private static Map<DistanceUnit, Double> distanceConversion = new HashMap<>();
	
	// Default unit values	
	// Seconds is what is returned from LocalDate.toSecondOfDay() function
	private static final TimeUnit defaultTimeUnit = TimeUnit.HOURS;
	private static final DistanceUnit defaultDistanceUnit = DistanceUnit.MILES;
	// Derived from default units above
	private static final SpeedUnit defaultSpeedUnit = SpeedUnit.MILES_PER_HOUR;
	
	// Initialize values in a static block.
	// More standard to store these values somewhere, like a database table with four columns: 
	// measurement (e.g. distance), from units (e.g. miles), to units (e.g. kilometers), conversion value (e.g. 1.61)
	// Combined with a reference table that contains two columns: measurement and units to list all units used.
	static {
		timeConversion.put(defaultTimeUnit, new Double(1.));
		timeConversion.put(TimeUnit.SECONDS, new Double(3600.));
		timeConversion.put(TimeUnit.MINUTES, new Double(60.));
		
		distanceConversion.put(defaultDistanceUnit, 1.);
		distanceConversion.put(DistanceUnit.KILOMETERS, 1.60934);
	}
	
	/**
	 * Converts a double value from one available TimeUnit to another TimeUnit.
	 * @param value				Input value for converting
	 * @param fromTime			Units to convert from
	 * @param toTime			Units to convert to
	 * @return double			Converted value in the toTime units.
	 */
	public static double convert(double value, TimeUnit fromTime, TimeUnit toTime) {
		if (timeConversion.containsKey(fromTime)) {
			if (timeConversion.containsKey(toTime)) {
				return value * timeConversion.get(toTime) / timeConversion.get(fromTime);
			} else {
				throw new IllegalArgumentException("Conversion value for TimeUnit: " + toTime 
						+ " does not exist.");
			}
		} else {
			throw new IllegalArgumentException("Conversion value for TimeUnit: " + fromTime 
					+ " does not exist.");
		}
	}
	
	/**
	 * Converts a double value from one available DistanceUnit to another DistanceUnit
	 * @param value				Input value for converting
	 * @param fromDistance		Units to convert from
	 * @param toDistance		Units to convert to
	 * @return double			Converted value in toDistance units
	 */
	public static double convert(double value, DistanceUnit fromDistance, DistanceUnit toDistance) {
		if (distanceConversion.containsKey(fromDistance)) {
			if (distanceConversion.containsKey(toDistance)) {
				return value * distanceConversion.get(toDistance) / distanceConversion.get(fromDistance);
			} else {
				throw new IllegalArgumentException("Conversion value for DistanceUnit: " + toDistance 
						+ " does not exist.");
			}
		} else {
			throw new IllegalArgumentException("Conversion value for DistanceUnit: " + fromDistance 
					+ " does not exist.");
		}
	}
	
	/**
	 * Converts a double value from one available SpeedUnit to another SpeedUnit
	 * @param value			Input value for converting
	 * @param fromSpeed		Units to convert from
	 * @param toSpeed		Units to convert to
	 * @return double		Converted value in toSpeed units.
	 */
	public static double convert(double value, SpeedUnit fromSpeed, SpeedUnit toSpeed) {
		// Get derivative units to get conversions for new speed.
		DistanceUnit fromDist = fromSpeed.getDistanceUnit();
		DistanceUnit toDist = toSpeed.getDistanceUnit();
		TimeUnit fromTime = fromSpeed.getTimeUnit();
		TimeUnit toTime = toSpeed.getTimeUnit();
		
		// Converts distance and then speed (but speed is denominator, so have to switch toTime and fromTime).
		return convert(convert(value, fromDist, toDist), toTime, fromTime);
	}
	
	/**
	 * Getter for the default time unit, for reference.
	 * @return	TimeUnit		Default time unit.
	 */
	public static TimeUnit getDefaultTimeUnit() {
		return defaultTimeUnit;
	}
	
	/**
	 * Getter for the default distance unit, for reference.
	 * @return DistanceUnit		Default distance unit.
	 */
	public static DistanceUnit getDefaultDistanceUnit() {
		return defaultDistanceUnit;
	}
	
	/**
	 * Getter for the default speed unit, for reference.
	 * @return SpeedUnit	Default speed unit.
	 */
	public static SpeedUnit getDefaultSpeedUnit() {
		return defaultSpeedUnit;
	}
	
}
