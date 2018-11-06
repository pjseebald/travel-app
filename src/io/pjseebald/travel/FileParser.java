package io.pjseebald.travel;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * <h1>FileParser</h1>
 * Used to check for file and parse the lines of the input file, returning them as a string array
 * that is the line split by spaces.
 * 
 * @author PaulSEEBALD
 * @version 1.0
 * @since 2018-11-02
 */
public class FileParser {
	
	/**
	 * Takes textual file and starts the reader of the text. Will check if file exists.
	 * @param inputFileLocation			String of file location.
	 * @return List<String[]>			Lines of file output as a list of string arrays.
	 */
	public static List<String[]> parseInputFile(String inputFileLocation) {
		
		try (BufferedReader gridStream = new BufferedReader(new FileReader(inputFileLocation)) ) {
			return parseInput(gridStream);
		} catch (FileNotFoundException e) {
			throw new NullPointerException("Input file is not found at location: " + inputFileLocation);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Error reading from file");
		}		
	}
	
	/**
	 * Parses each line of the input text reader, splitting it by spaces.
	 * Each line becomes a string array that is added to a list. The list is returned.
	 * @param stream				Reader containing text to read.
	 * @return List<String[]>		Lines of file output as a list of string arrays.
	 */
	public static List<String[]> parseInput(BufferedReader stream) {
		List<String[]> inputs = new ArrayList<>();

		try {
			String curLine = stream.readLine();
			while (curLine != null) {
				String[] curWords = curLine.split("\\s+");
				inputs.add(curWords);
				curLine = stream.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Error reading from file");
		}
		
		return inputs;
	}

}
