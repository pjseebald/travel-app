How to use Paul Seebald's code for Travel app:

*******************************************************************************************
HOW TO BUILD

1) Import the project into Eclipse. Use JRE 1.8+ and include JUnit 4+ library and run build.xml as ant build.

2) Use installed Ant 1.7+ to run build.xml. Open the command line and change directory to the project's folder.
	Run the following command:
		ant
	
Note that the travel.jar file is included and can be run without needing to build the code.

*******************************************************************************************
HOW TO RUN JAR

Requires JRE 1.8+

Inputs:
1) *.* file
	Text input file that contains all of the commands for the application in the format:
	
	<command> <parameters>
	
	where parameters are separated by a space and only one command per line. Example:
	
	Driver Dan
	Trip Dan 11:26 12:31 48.4
	Driver Tim
	Driver Janice
	Trip Janice 02:30 04:20 100.7
	
	The two available commands are:
	A) Driver
		Creates a driver.
		This command is followed by a name (can have spaces in it). The name is the only parameter.
	B) Trip
		Creates a trip and adds it to the driver's list of trips. Note that a trip needs to meet certain criteria before it will be added to a driver's list. Currently, that criteria is an average speed of >=5 mph or <= 100 mph.
		This command is followed by four parameters:
		i) Driver name (can have spaces)
		ii) Start time in HH:MM format. 24-hour clock. e.g. 01:05
		iii) End time in HH:MM format. 24-hour clock. Cannot go past midnight (trip has to end on same day as start time).
		iv) Distance in miles traveled.
		
	A sample file is included as sample-travel-input.txt.
		
		
	
Outputs:
All output is to the command line through System.out.

-- Run as a standard jar file (runs with default.properties and default.grid as inputs):
	java -jar travel.jar

-- Run to view the help:
	java -jar travel.jar -h

-- Run with sample properties and sample grid files:
	java -jar travel.jar sample-travel-input.txt

*******************************************************************************************
HOW TO RUN UNIT TESTS

Requires:
1) junit.jar and hamcrest-core-1.3.0.jar files in the local .\lib folder.

-- Run unit tests
	java -cp travel.jar io.pjseebald.travel.AllTests
	
*******************************************************************************************
Design Considerations:

Creating an app to log trips for drivers as commanded by input. Standard input as file, but command parsing

Overall:
	- I did not want to add an overabundance of abstractions, but I wanted to add a couple slightly unnecessary abstractions (particularly unit conversion) to show a grasp of the larger picture and potential design for a similar and larger, more complex app.
	- In general, I prefer to use primitives (less memory, faster, etc.) unless there is a specific reason I need an object
		- Some reasons are:
			1) Wanting to assign the object a null value at some point.
			2) Storing values in a Java Collection or Object (more complex data).
	- Data should be flowing through the MainApp.
	- Separate the app's flow into responsibilities:
		1) Read file
		2) Read commands
		3) Execute commands
			a) Add driver
			b) Add trips
		4) Manage units for calculations
		5) Make calculations
		6) Generate report
		7) Deliver report
		- Note: a few of these were not separated as classes (read and execute commands are together, making calculations is done in the Trip and Driver classes), but the others are. The commands were simple enough that reading a command essentially meant there was just one more line to execute it and create the relevant objects.
	- Composition:
		- Decided to have Driver object contain a collection of Trips for a couple reasons:
			1) In this format, one Driver can have many Trips. But one Trip can only have one Driver.
			2) The report is aggregate Driver statistics, not Trip statistics. Calculations are possible by keeping track of Trips, but easier and more logical this way.
			3) A Driver object could be added to a Trip if needed (it was not needed in this implementation).
	- Inheritance:
		- Decided it was not needed. There was not a logical "parent" class or interface/methods for any combination of classes used. If more travel methods were used than Driving, more types of input files (properties), etc., then inheritance could easily be used. 

			
*******************************************************************************************

Specific class thoughts:

(Classes discussed below are referenced in the general order they would appear in the app.)

MainApp:
	- Would be simpler without the help text, but I like to include that because it helps anyone who doesn't read the README files (I'm sure that never happens!).
	- Keeps it clean to put simple, one-line steps that follow the flow of the overall app. Could have included some of the parsing functions in this class, but violates OOP.
	
FileParser:
	- Splitting up the functions to get input file stream and then parse the input because of single responsibility and better for unit testing.
	- Storing the commands in an ArrayList because again, we don't know how many commands there will be (eliminates array), and want to be able to reference them in the order we added them.
----- Tests:
		- Want to test failures about as much as successes, so exception testing as well for no file existing.
		- Test different types of inputs from the file and how they are returned

		
CommandParser:
	- Used a private enum for CommandType because it doesn't need to be referenced at all outside of the CommandParser class. If command execution was split into a separate class, then the access modifier would need to be changed or the class put in a separate file altogether.
	- LocalTime object was very convenient to use for that time formatting and had all necessary methods for analyzing time traveled.
----- Tests:
		- Check some potential incorrect inputs for the commands. e.g. Commands entered in incorrect order, command with incorrect number of parameters, etc.


Driver:
	- Trips are included as a List instead of array because we don't know how many trips will be added to the Driver object. Allows flexibility.
	- Could have created a method to calculate average speed by only looping over trips once. Currently loops twice (once in getting distance, once in getting time). This is acceptable for a small app like this, especially since the distance already needs to be calculated. For the report, would be looped over twice either way. However, in another app, if average speed is referenced more often for a larger list, a method should be created.
	- Briefly thought about caching "distance traveled" as a variable to be able to sort the driver information in the final report. However, this kind of variable does not make sense since Trips can still be added to the list and it would need to be updated if they were. That measurement is something that should be calculated on an as-needed basis.
----- Tests:
		- These should not depend on whether trips are accepted or not, in case that logic moves elsewhere.
	
	
Trip
	- Could include a Driver object as an input to the Trip constructor (composition). However, in the current app, this was unnecessary, and when considering a larger app, would want more information about how people traveling should be handled.
	- Added the static requirements ranges here as methods that can be referenced anywhere in the app.
	- The requirements ranges were made to be arrays because it is known there are two elements: a minimum and a maximum. They were made to be arrays of objects (Double) because even when reasonable maximums or minimums can be set (e.g. maximum speed of 10000 mph), a null value still makes more sense when you don't want to set a minimum or maximum and easily check if a min or max exists.
	- There are a few changes that would be made if drivers could drive past midnight - use LocalDate instead of LocalTime for start/end times. Also need to refine calculation of whether amount of time spent traveling is physically impossible or not.
	- A timeTraveled variable was added instead of calculating as needed from start/end times because the time traveled won't change for a given Trip, and this way the value can be stored initially in the default time units instead of converting every time it's referenced.
	- Note that a Trip being physically impossible is considered conceptually different from a Trip not meeting requirements, even if requirements are set to minimum of 0 time or 0 distance traveled.
	- Checking the Trip for requirements can be referenced anywhere in the process that it is deemed reasonable (even could be done in the Trip constructor after the information has been set). It was included in the Driver.addTrip() method because then the Trip could be created and still stored elsewhere if desired. Also, the requirements are because of how they'd affect driver statistics.
	- It is easier to follow the flow of the app if any unit-based values are returned as default units from Trip object. This way when coding around it, only need to worry about converting them to non-default units.
----- Tests:
		- Test trip object/instance code and static requirements.


UnitConversion:
	- Default units seem obvious based on current implementation, but could be anything as long as conversion values are stored.
	- Included a way to convert speed units based only on distance and time because it is a derivative unit and conversion values would just be duplicated.
	- Decided to split up checking whether to/from units existed for distance and time. In this case it seemed valuable to inform the programmer which units were not available.
	- Did not include a method to set/change the default units. There was no need for such a method and when the Trip requirements were introduced, it meant too much coupling. These criteria values are initiated in the default units, and if default units are changed, it would need create a domino effect where the criteria values need to be changed as well.
----- Tests:
		- Need to test all unit conversions, including converting from one unit to itself.
		- Test errors when referencing units that aren't included (these are only TimeUnits, since SpeedUnit and DistanceUnit are written here).
		
	
SpeedUnit:
	- Added units it derives from in the constructor (for unit conversion) as well as a string representation (for the report).
	- Could add a similar string representation for DistanceUnit if different representation is desired (e.g. "mi" for miles).

	
DriverReport:
	- How to sort results? There are a few other convoluted ways to do this, but ultimately the best choice was creating an object that contains the relevant report information plus any values needed to sort on. A PriorityQueue collection in Java is ideal for sorting objects. This combination is by far the easiest to follow and avoids using multiple collections and sorting one list based off of another, etc.
	- Within this ReportInformation object, report phrases are generalized
----- Tests:
		- Close to an end to end test.