package io.pjseebald.travel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <h1>MainApp</h1>
 * Provides primary flow to app. Contains centralized managed list of drivers.
 * Also provides help and error strings for running the app.
 * 
 * @author PaulSEEBALD
 * @version 1.0
 * @since 2018-10-30
 */
public class MainApp {
	
	// Be able to retrieve driver by their name.
	private static Map<String, Driver> drivers = new HashMap<>();

	public static void main(String[] args) {
		
		if (args.length == 0) {
			String errorMessage = "Error: must include argument when calling this app with input file location. " 
					+ System.lineSeparator() + "Example: java -jar travel.jar input.txt" 
					+ System.lineSeparator() + "Can also get help using the -h option. Example: java -jar travel.jar -h";
			throw new IllegalArgumentException(errorMessage);
		} else if (args.length > 1) {
			String errorMessage = "Error: too many arguments. Only include location of the input file. " 
					+ System.lineSeparator() + "Example: java -jar travel.jar input.txt"
					+ System.lineSeparator() + "Can also get help using the -h option. Example: java -jar travel.jar -h";
			throw new IllegalArgumentException(errorMessage);
		} else if (args[0].equals("-h")) {
			// Output help for app
			String ls = System.lineSeparator();
			String helpMessage = "***** Welcome to the Travel App *****" 
					+ ls + ls + "This app is designed to input drivers and the trips they take."
					+ ls + "An input text file is required and the text file should have the following format:"
					+ ls + ls + "Driver <name>"
					+ ls + "Trip <name> <start-time> <end-time> <miles-traveled>"
					+ ls + ls + "Example:"
					+ ls + "Driver Maggie"
					+ ls + "Trip Maggie 11:40 13:10 85.6"
					+ ls + ls + "Note that any number of drivers can be included, and drivers can take any number of trips."
					+ ls + "The requirements are: "
					+ ls + "1. Drivers must be added with \"Driver <name>\" before their trips are listed."
					+ ls + "2. Trip start and end times must be in HH:MM format (e.g. 01:20)"
					+ ls + "3. Trips have to be within the same day (i.e. cannot pass midnight)."
					+ ls + "4. Trip numbers have to make logical sense. Miles and time traveled cannot be negative."
					+ ls + ls + "The app can be run using the command:"
					+ ls + "java -jar travel.jar <input-file-path>"
					+ ls + ls + "Example:"
					+ ls + "java -jar travel.jar sample-travel-input.txt"
					+ ls + ls + "To run the unit tests, run the command:"
					+ ls + "java -cp travel.jar io.pjseebald.travel.AllTests"
					+ ls + ls + "Note: unit tests require both junit.jar and hamcrest-core-1.3.jar (v. 1.3) in the .\\lib folder"
					;
			System.out.println(helpMessage);
			return;
		}
		
		String inputFileLocation = args[0];
		
		// Parse the input file
		List<String[]> allInputs = FileParser.parseInputFile(inputFileLocation);
		
		// Execute the commands
		CommandParser.parseCommands(allInputs);
		
		// Retrieve report
		String report = new DriverReport().getReport();
		
		// Print the report.
		System.out.println(report);
		
	}
	
	public static Map<String, Driver> getDrivers() {
		return drivers;
	}

}
