package io.pjseebald.travel;

import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({UnitConversionTest.class, TripTest.class, DriverTest.class, DriverReportTest.class, CommandParserTest.class, FileParserTest.class })

public class AllTests {

	public static void main(String[] args) throws Exception {
		System.out.println("Running all unit tests...");
		JUnitCore.main("io.pjseebald.travel.AllTests");
	}


}
