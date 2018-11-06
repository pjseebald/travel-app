package io.pjseebald.travel;

import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;

public class UnitConversionTest {

	@Before
	public void setUpBefore() throws Exception {
		
	}

	@Test
	public void testConvertTimeUnits() {
		double timeDelta = 0.01;
		
		int mins = 120;
		double expectedMinsToHrs = new Double(mins/60.);
		double expectedMinsToSecs = new Double(mins*60.);
		double returnedMinsToHrs = UnitConversion.convert(mins, TimeUnit.MINUTES, TimeUnit.HOURS);
		double returnedMinsToSecs = UnitConversion.convert(mins, TimeUnit.MINUTES, TimeUnit.SECONDS);
		double returnedMinsToMins = UnitConversion.convert(mins, TimeUnit.MINUTES, TimeUnit.MINUTES);
		
		this.assertionCheck(expectedMinsToHrs, returnedMinsToHrs, "minutes to hours", timeDelta);
		this.assertionCheck(expectedMinsToSecs, returnedMinsToSecs, "minutes to seconds", timeDelta);
		this.assertionCheck(mins, returnedMinsToMins, "minutes to minutes", timeDelta);
		
		int seconds = 60*122;
		double expectedSecsToHrs = new Double(seconds/3600.);
		double expectedSecsToMins = new Double(seconds/60.);
		double returnedSecsToHrs = UnitConversion.convert(seconds, TimeUnit.SECONDS, TimeUnit.HOURS);
		double returnedSecsToMins = UnitConversion.convert(seconds, TimeUnit.SECONDS, TimeUnit.MINUTES);
		double returnedSecsToSecs = UnitConversion.convert(seconds, TimeUnit.SECONDS, TimeUnit.SECONDS);
		
		this.assertionCheck(expectedSecsToHrs, returnedSecsToHrs, "seconds to hours", timeDelta);
		this.assertionCheck(expectedSecsToMins, returnedSecsToMins, "seconds to minutes", timeDelta);
		this.assertionCheck(seconds, returnedSecsToSecs, "seconds to seconds", timeDelta);
		
		double hours = 13.2;
		double expectedHrsToSecs = new Double(hours*3600.);
		double expectedHrsToMins = new Double(hours*60.);
		double returnedHrsToSecs = UnitConversion.convert(hours, TimeUnit.HOURS, TimeUnit.SECONDS);
		double returnedHrsToMins = UnitConversion.convert(hours, TimeUnit.HOURS, TimeUnit.MINUTES);
		double returnedHrsToHrs = UnitConversion.convert(hours, TimeUnit.HOURS, TimeUnit.HOURS);
		
		this.assertionCheck(expectedHrsToSecs, returnedHrsToSecs, "hours to seconds", timeDelta);
		this.assertionCheck(expectedHrsToMins, returnedHrsToMins, "hours to minutes", timeDelta);
		this.assertionCheck(hours, returnedHrsToHrs, "hours to hours", timeDelta);
		
	}
	
	@Test
	public void testConvertDistanceUnits() {
		double distanceDelta = 0.01;
		
		double convertValue = 1.60934;	// km/mi
		
		// Set independent distance values
		double miles = 133.8;
		double kilometers = 94.4;
		
		double expectedMiToKm = new Double(miles * convertValue);
		double returnedMiToKm = UnitConversion.convert(miles, DistanceUnit.MILES, DistanceUnit.KILOMETERS);
		double returnedMiToMi = UnitConversion.convert(miles, DistanceUnit.MILES, DistanceUnit.MILES);
		
		this.assertionCheck(expectedMiToKm, returnedMiToKm, "miles to kilometers", distanceDelta);
		this.assertionCheck(miles, returnedMiToMi, "miles to miles", distanceDelta);
		
		double expectedKmToMi = new Double(kilometers / convertValue);
		double returnedKmToMi = UnitConversion.convert(kilometers, DistanceUnit.KILOMETERS, DistanceUnit.MILES);
		double returnedKmToKm = UnitConversion.convert(kilometers, DistanceUnit.KILOMETERS, DistanceUnit.KILOMETERS);
		
		this.assertionCheck(expectedKmToMi, returnedKmToMi, "kilometers to miles", distanceDelta);
		this.assertionCheck(kilometers, returnedKmToKm, "kilometers to kilometers", distanceDelta);
	}
	
	@Test
	public void testConvertSpeedUnits() {
		double speedDelta = 0.01;
		
		double convertValue = 1.60934;	// km/hr per mph
		
		// Set independent speed values
		double mph = 72.4;
		double kmh = 88.7;
		
		double expectedMphToKmh = new Double(mph * convertValue); 
		double returnedMphToKmh = UnitConversion.convert(mph, SpeedUnit.MILES_PER_HOUR, SpeedUnit.KILOMETERS_PER_HOUR);
		double returnedMphToMph = UnitConversion.convert(mph, SpeedUnit.MILES_PER_HOUR, SpeedUnit.MILES_PER_HOUR);
		
		double expectedKmhToMph = new Double(kmh / convertValue);
		double returnedKmhToMph = UnitConversion.convert(kmh, SpeedUnit.KILOMETERS_PER_HOUR, SpeedUnit.MILES_PER_HOUR);
		double returnedKmhToKmh = UnitConversion.convert(kmh, SpeedUnit.KILOMETERS_PER_HOUR, SpeedUnit.KILOMETERS_PER_HOUR);
		
		this.assertionCheck(expectedMphToKmh, returnedMphToKmh, "mph to kmh", speedDelta);
		this.assertionCheck(mph, returnedMphToMph, "mph to mph", speedDelta);
		this.assertionCheck(expectedKmhToMph, returnedKmhToMph, "kmh to mph", speedDelta);
		this.assertionCheck(kmh, returnedKmhToKmh, "kmh to kmh", speedDelta);
		
	}
	
	@Test
	public void testConvertNonExistentUnits() {
		double testValue = 1101.33;
		int numExceptions = 0;
		int expectedExceptions = 2;
		try {
			UnitConversion.convert(testValue, TimeUnit.MICROSECONDS, TimeUnit.HOURS);
		} catch (IllegalArgumentException e) {
			numExceptions++;
		}
		
		try {
			UnitConversion.convert(testValue, TimeUnit.HOURS, TimeUnit.MILLISECONDS);
		} catch (IllegalArgumentException e) {
			numExceptions++;
		}
		
		assertTrue("Did not get expected exceptions when converting nonexistent time units.", 
				numExceptions == expectedExceptions);
		
	}
	
	private void assertionCheck(double expected, double returned, String conversionLabel, double delta) {
		assertEquals("Error converting " + conversionLabel + ". Expected: " + expected + "; returned: " + returned, 
				expected, returned, delta);
	}
}
