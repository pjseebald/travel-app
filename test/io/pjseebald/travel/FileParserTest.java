package io.pjseebald.travel;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class FileParserTest {

	@Test
	public void testParsingString() {
		String sep = System.lineSeparator();
		String testString = "Driver Jane" + sep;
		BufferedReader br = new BufferedReader(new StringReader(testString));
		
		List<String[]> output = FileParser.parseInput(br);
		List<String[]> expectedOutput = new ArrayList<>();
		String[] expectedStringArray = testString.split("\\s+");
		
		expectedOutput.add(expectedStringArray);
		
		this.compareLists(expectedOutput, output);
		
		String testString2 = "Driver Ed" + sep + "Driver Haley" + sep 
				+ "Trip Ed 11:40 12:40 43.5" + sep + "Trip Ed 01:01 02:45 89.2" + sep 
				+ "Trip Haley 15:01 19:20 251.4" + sep + "Trip Haley 07:33 08:02 20.5" 
				+ sep + "Driver T";
		
		// Split by line separation
		String[] testString2ByLine = testString2.split("(\\r\\n|\\r|\\n)");
		List<String[]> expectedOutput2 = new ArrayList<>();
		for (String command : testString2ByLine) {
			expectedOutput2.add(command.split(" "));
		}
		
		List<String[]> output2 = FileParser.parseInput(new BufferedReader(new StringReader(testString2)));
		
		this.compareLists(expectedOutput2, output2);
		
	}
	
	private void compareLists(List<String[]> expected, List<String[]> returned) {
		String sep = System.lineSeparator();
		
		assertSame("Size of lists did not match. Expected: " + expected.size() + "; returned: " + returned.size(), 
				expected.size(), returned.size());
		
		for (int i = 0; i < returned.size(); i++) {
			assertTrue("Expected: " + sep + String.join(" ", expected.get(i)) + sep + "Returned: " 
					+ sep + String.join(" ", returned.get(i)), 
					Arrays.equals(expected.get(i), returned.get(i)));
		}
	}
	
	@Test(expected=NullPointerException.class)
	public void testNonexistentFile() {
		String fileLocation = "this.file.most.certainly.does.not-exist";
		FileParser.parseInputFile(fileLocation);
		
		// Shouldn't reach this point, as parseInputFile() should return a NullPointerException
		fail("Did not return exception when parsing a file that does not exist.");
	}
	
	@Test
	public void testEmptyFile() {
		String testString = "";
		BufferedReader br = new BufferedReader(new StringReader(testString));
		List<String[]> returned = FileParser.parseInput(br);
		assertEquals("Analyzing empty file resulted in non-zero string list. Expected size: 0; returned: " 
				+ returned.size(), 
				0, returned.size());
	}


}
