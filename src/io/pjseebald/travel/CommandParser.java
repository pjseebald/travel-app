package io.pjseebald.travel;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * <h1>CommandParser</h1>
 * Parses commands that are input as lists of string arrays. The string arrays separates the parameters
 * of the command, with the command name as the first element in the array. Executes the commands.
 * 
 * @author PaulSEEBALD
 * @version 1.0
 * @since 2018-11-02
 */
public class CommandParser {
	
	// Formatting time stamp
	private final static String timeFormatPattern = "HH:mm";
	private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern(timeFormatPattern);
	
	/**
	 * Takes a list of commands that are represented by String arrays.
	 * Parses each command individually in order.
	 * @param commandInputs		All input commands to execute, in order.
	 */
	public static void parseCommands(List<String[]> commandInputs) {
		for (String[] commandInput : commandInputs) {
			parseCommand(commandInput);
		}
	}
	
	/**
	 * Input is a string array of a command, with the command name as the first element in the array.
	 * Proceeding array elements are the parameters of the command.
	 * @param commandInputs		Command represented by String array.
	 */
	public static void parseCommand(String[] commandInputs) {
		
		String command = commandInputs[0];
		
		Map<String, Driver> drivers = MainApp.getDrivers();
		
		CommandType inputCommand = CommandType.valueOf(command.toUpperCase());
		
		String nameMatch = "^[a-zA-Z\\s]+";
		
		String name;
		switch(inputCommand) {
			case DRIVER:
				// Accounting for names that could have a space in them.
				name = "";
				for (int j = 1; j < commandInputs.length; j++) {
					String curInput = commandInputs[j];
					// Check that the current input matches an expected English name format
					if (curInput.matches(nameMatch)) {
						name += (j == 1 ? "" : " ") + commandInputs[j];
					} else {
						throw new IllegalStateException("Unexpected characters in name: " + curInput);
					}
				}
				drivers.put(name, new Driver(name));
				break;
			case TRIP:
				// Accounting for names that could have a space in them.
				name = "";
				int i = 1;		// Want i to be accessible after the loop to get the times and miles
				for ( ; i < commandInputs.length; i++) {
					String curWord = commandInputs[i];
					if (curWord.contains(":")) {
						// Recognize this as a time
						break;
					}
					
					if (name.length() > 0) {
						name += " ";
					}
					
					name += curWord;
				}
				
				// Now get times
				LocalTime startTime = LocalTime.parse(commandInputs[i], formatter);
				LocalTime endTime = LocalTime.parse(commandInputs[i+1], formatter);
				Double milesTraveled = Double.parseDouble(commandInputs[i+2]);
				
				drivers.get(name).addTrip(new Trip(startTime, endTime, milesTraveled));
				
				break;
			default:
				break;
		}
	}
	
	/**
	 * <h1>CommandType</h1>
	 * Enum with types of commands that can be input to this app.
	 * 
	 * DRIVER, TRIP
	 * 
	 * @author PaulSEEBALD
	 * @version 1.0
	 * @since 2018-11-02
	 */
	private enum CommandType {
		DRIVER, TRIP;
	}

}
