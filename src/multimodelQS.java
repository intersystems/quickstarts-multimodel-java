/* Purpose: This demo shows using objects, SQL, and native access side-by-side in a Java application, 
* connecting to InterSystems IRIS.
*
* To Test: Run to see objects and SQL working side-by-side. Then uncomment the lines to execute storeAirfare and checkAirfare to see how
* to create a custom data structure using the Native API.	
*/
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;


import com.intersystems.jdbc.IRIS;
import com.intersystems.jdbc.IRISIterator;
import com.intersystems.xep.Event;
import com.intersystems.xep.EventPersister;
import com.intersystems.xep.PersisterFactory;
import com.intersystems.xep.XEPException;
import com.intersystems.jdbc.IRISConnection;


// Purpose: This class shows a multi-model example in Java. SQL is used to display airports,
// objects are stored directly, and a custom data structure is used to determine airfare.
public class multimodelQS {
	
	public static void main(String[] args) {
		// If you are using a remote instance, update IP and password here
		String username = "tech";
		String password = "demo";
		String IP = "localhost";
		int port = 52773;
		
		try {
			// Connect to database using EventPersister, which is based on IRISDataSource
			// For more details on EventPersister, visit 
			// https://docs.intersystems.com/irislatest/csp/docbook/DocBook.UI.Page.cls?KEY=AFL_xep
	        EventPersister xepPersister = PersisterFactory.createPersister();
	        xepPersister.connect(IP, port, "User", username, password); 
	        System.out.println("Connected to InterSystems IRIS via JDBC."); 
	        xepPersister.deleteExtent("Demo.Airport");   // Remove old test data
	        xepPersister.importSchemaFull("Demo.Airport");   // Import full schema
	       
	        //***Initializations***
	        // Create XEP Event for object access
	        Event xepEvent = xepPersister.getEvent("Demo.Airport");

	        // Create JDBC statement object for SQL and IRIS Native access
	        Statement myStatement = xepPersister.createStatement();
	        
	        // Create IRIS Native object
	        IRIS irisNative = IRIS.createIRIS((IRISConnection)xepPersister);
	              
	        //***Running code***
	        System.out.println("Generating airport table...");
			
	        // Populate 5 airport objects and save to the database using XEP
			populateAirports(xepEvent);
			
			// Get all airports using JDBC
			getAirports(myStatement);
			
			// Store natively - Uncomment the following line for task 3
			// storeAirfare(irisNative);
			// checkAirfare(irisNative);
				
			// Close everything
		    xepEvent.close();
		    xepPersister.close();
						
		} catch (SQLException e) {
			 System.out.println("Error creating airport listing: " + e.getMessage());
		}
	        
	}
	
	// Store objects directly to InterSystems IRIS
	public static void populateAirports(Event xepEvent) {	
		ArrayList<Demo.Airport> airportList = new ArrayList<Demo.Airport>();
		
		// 1. Boston
		Demo.Airport newAirport = new Demo.Airport();
		newAirport.setName("Boston Logan International");
		newAirport.setCode("BOS");
		Demo.Location loc = new Demo.Location();
		loc.setCity("Boston");
		loc.setState("MA");
		loc.setZip("02128");
		newAirport.setLocation(loc);
		airportList.add(newAirport);
		
		// 2. Philadelphia
		Demo.Airport newAirport2 = new Demo.Airport();
		newAirport2.setName("Philadelphia International");
		newAirport2.setCode("PHL");
		Demo.Location loc2 = new Demo.Location();
		loc2.setCity("Philadelphia");
		loc2.setState("PA");
		loc2.setZip("19153");
		newAirport2.setLocation(loc2);
		airportList.add(newAirport2);
		
		// 3. Austin
		Demo.Airport newAirport3 = new Demo.Airport();
		newAirport3.setName("Austin-Bergstrom International");
		newAirport3.setCode("AUS");
		Demo.Location loc3 = new Demo.Location();
		loc3.setCity("Austin");
		loc3.setState("TX");
		loc3.setZip("78719");
		newAirport3.setLocation(loc3);
		airportList.add(newAirport3);
		
		// 4. San Francisco
		Demo.Airport newAirport4 = new Demo.Airport();
		newAirport4.setName("San Francisco International");
		newAirport4.setCode("SFO");
		Demo.Location loc4 = new Demo.Location();
		loc4.setCity("San Francisco");
		loc4.setState("CA");
		loc4.setZip("94128");
		newAirport4.setLocation(loc4);
		airportList.add(newAirport4);
		
		// 5. O'hare
		Demo.Airport newAirport5 = new Demo.Airport();
		newAirport5.setName("Chicago O'hare International");
		newAirport5.setCode("ORD");
		Demo.Location loc5 = new Demo.Location();
		loc5.setCity("Chicago");
		loc5.setState("IL");
		loc5.setZip("60666");
		newAirport5.setLocation(loc5);
		airportList.add(newAirport5);
		
		Demo.Airport[] airportArray = airportList.toArray(new Demo.Airport[airportList.size()]);
		xepEvent.store(airportArray);
		
		System.out.println("Stored 5 airports");
	}
	
	// Display all airports using JDBC
	public static void getAirports(Statement myStatement)
	{
		ResultSet myRS;
		try {
			// This query uses a special shorthand notation (->, known as an implicit join) 
			// to retrieve data from a related table without requiring you to think about how to join tables
			myRS = myStatement.executeQuery("SELECT name, code, location->city, location->state, location->zip FROM demo.airport");
		
			System.out.println("Name\t\t\t\t\tCode\t\tLocation");
			while(myRS.next())
			{
				System.out.println(
						myRS.getString("name") + "\t\t" + 
						myRS.getString("code") + "\t\t" + 
						myRS.getString("city") + ", " + 
						myRS.getString("state") + " " + 
						myRS.getString("zip")
						);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// Create a custom data structure to store airport distance and airfare information in a graph-like structure.
	// Query this information with the interactive checkAirfare() method
	public static void storeAirfare(IRIS irisNative)
	{		
		// Store routes and distance between airports 
		// The global we'll populate has two levels:
		// ^airport(from, to) = distance
		// ^airport(from, to, flight) = fare
		
		// This IRIS native API sets the value for a global at the specified subscript level(s)
		// For example, to set ^airport("BOS","AUS") = 1698
		irisNative.set("1698","^airport", "BOS","AUS");
		irisNative.set("450","^airport", "BOS","AUS","AA150");
		irisNative.set("550","^airport", "BOS","AUS","AA290");
		
		irisNative.set("280","^airport", "BOS","PHL");
		irisNative.set("200","^airport", "BOS","PHL","UA110");
		
		irisNative.set("1490","^airport", "BOS","BIS");
		irisNative.set("700","^airport", "BOS","BIS","AA330");
		irisNative.set("710","^airport", "BOS","BIS","UA208");
		
		System.out.println("Stored fare and distance data in ^airport global.");
	}
	
	// Simple interactive method using IRIS native API to consult the data structure populated in storeAirfare()
	public static void checkAirfare(IRIS irisNative)
	{		
		// Prompt for input
		Scanner scanner = new Scanner(System.in);
		System.out.print("Enter departure airport: (e.g. BOS)");
		String fromAirport = scanner.next();
		System.out.print("Enter destination airport: (e.g. AUS)");
		String toAirport = scanner.next();
		scanner.close();
		
		// ^airport(from, to) = distance
		System.out.println("");
		System.out.println("The distance in miles between "+ fromAirport + " and " + toAirport + 
					" is: " + irisNative.getString("^airport", fromAirport, toAirport) + ".");
		
		// Now loop through routes: ^airport(from, to, flight) = fare
		int isDefined = irisNative.isDefined("^airport", fromAirport, toAirport);
		if (isDefined==11) {
			System.out.println("The following routes exist for this path:");
			IRISIterator iterator = irisNative.getIRISIterator("^airport", fromAirport, toAirport);
			while (iterator.hasNext()) {
				String flightNumber = iterator.next();
                		String fare = (String) iterator.getValue();
				System.out.println("  - " + flightNumber + ": " + fare + " USD");
			}
		} else {
			System.out.println("No routes exist for this path.");
		}
	}

}