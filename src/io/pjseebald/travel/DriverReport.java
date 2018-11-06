package io.pjseebald.travel;

import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.TimeUnit;

/**
 * <h1>Driver Report</h1>
 * Collects report information, organizes it as needed and combines it together to one report string.
 * 
 * @author PaulSEEBALD
 * @version 1.0
 * @since 2018-11-01
 */
public class DriverReport {

	/**
	 * This function collects all the report information objects about each driver
	 * and sorts them by whichever measurement is desired to sort on (largest to smallest). 
	 * Returns report with all drivers included.
	 * @return String report	Full report as a String.
	 */
	public String getReport() {
		
		Map<String, Driver> drivers = MainApp.getDrivers();
		
		// Collect information needed for report. Use queue to sort by desired measure (see ReportInformation.getComparingValue())
		PriorityQueue<ReportInformation> queue = new PriorityQueue<ReportInformation>(drivers.keySet().size());
		for (String name : drivers.keySet()) {
			Driver driver = drivers.get(name);
			queue.add(getInformation(driver));
		}
		
		String report = "";
		while (!queue.isEmpty()) {
			report += queue.poll().toString() + System.lineSeparator();
		}
		
		return report;
	}
	
	/**
	 * Collects information and passes it to an object that stores the relevant info
	 * for a report. A report is a string containing desired measurements (distance, speed, etc.).
	 * @param driver	Driver to get information on
	 * @return ReportInformation reportInfo		Information on the input driver. Stored as object.
	 */
	public ReportInformation getInformation(Driver driver) {
		
		double distanceTraveled = driver.getDistanceTraveled();
		double hoursTraveled = driver.getTimeTraveled();
		
		double avgSpeed = 0.;
		double hoursTraveledCheck = UnitConversion.convert(1, TimeUnit.MINUTES, TimeUnit.HOURS);
		// Check if driver has actually traveled anywhere. Smallest time traveled is a minute, so check if 
		// hours traveled is less than one minute. Checking hours because it is the denominator (avoid calc error).
		if (hoursTraveled > hoursTraveledCheck) {
			avgSpeed = distanceTraveled / hoursTraveled;
		}
		
		ReportInformation reportInfo = this.new ReportInformation(driver.getName(), 
				distanceTraveled, hoursTraveled, avgSpeed);

		return reportInfo;
	}
	
	/**
	 * <h1>Report Information</h1>
	 * Report Information contains any relevant measurements that might be used in a report for a single driver.
	 * It formats standardized report strings and inserts the relevant data.
	 * Finally, it contains a comparison between objects that allows app to sort reports by measurements.
	 * 
	 * @author PaulSEEBALD
	 * @version 1.0
	 * @since 2018-11-01
	 */
	private class ReportInformation implements Comparable<ReportInformation> {
		
		String name;
		double speed;
		double distance;
		double time;
		
		double noTripsDistance = 0.0001;	// Need at least this distance to say Driver took 1+ trip(s)
		boolean noTrips;
		
		// Define report phrase constants
		final static String namePhrase = "%s:";
		final static String unitValuePhrase = "%s %s";
		final static String distancePrePhrase = "";
		final static String speedPrePhrase = "@ ";
		final static String sep = " ";
		
		/**
		 * Constructor for ReportInformation.
		 * Requires driver name, plus distance and time traveled over all trips. Speed is average speed
		 * (i.e. = total distance / total time (assuming total time is not zero).
		 * @param name				Driver name
		 * @param distance			Total distance traveled by Driver
		 * @param time				Total time traveled by Driver
		 * @param speed				Average speed over Driver's trips.
		 */
		ReportInformation(String name, double distance, double time, double speed) {
			this.name = name;
			this.distance = distance;
			this.time = time;
			this.speed = speed;
			this.noTrips = this.distance < noTripsDistance;
		}
		
		/**
		 * toString method, making it easy to get a report string out of this object.
		 * Formatted in a specific way for specific report requirements.
		 */
		@Override
		public String toString() {
			String endString = this.noTrips ? "" : String.format(sep + speedPrePhrase + unitValuePhrase, 
					this.getRoundedInt(speed), UnitConversion.getDefaultSpeedUnit().getAsString());
			return String.format(namePhrase + sep + distancePrePhrase + unitValuePhrase + endString, 
					this.name, this.getRoundedInt(this.distance), 
					UnitConversion.getDefaultDistanceUnit().toString().toLowerCase());
		}
		
		/**
		 * Rounds doubles and casts to integer.
		 * @param value		Input value as a double to be rounded.
		 * @return int		Rounded input value as integer
		 */
		int getRoundedInt(double value) {
			return (int) Math.round(value);
		}
		
		/**
		 * Measurement used to sort drivers, can change return value if different measurement is desired.
		 * @return double		Value to sort these objects by.
		 */
		double getComparingValue() {
			return this.distance;
		}

		/**
		 * Used for sorting objects by the comparing value.
		 * @return int		Comparison of objects.
		 */
		@Override
		public int compareTo(ReportInformation o) {
			if (this.getComparingValue() < o.getComparingValue()) {
				return 1;
			} else if (this.getComparingValue() > o.getComparingValue()) {
				return -1;
			} else {
				return 0;
			}
		}
		
	}
	
}
