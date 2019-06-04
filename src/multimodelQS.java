/* Purpose: This demo shows using objects, SQL, and native access side-by-side in a Java application, 
* connecting to InterSystems IRIS.
*
* To Test: Run to see objects and SQL working side-by-side. Then uncomment the line to execute storeAirfare to see
* creating a custom data structure using the Native API.	
*/
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;


import com.intersystems.jdbc.IRIS;
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
		String username = "SuperUser";
		String password = "SYS";
		String IP = "localhost";
		int port = 51773;
		
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
	
	// Create a custom data structure to store airfare in a graph-like structure and retrieve airfare based on nodes
	// Takes departure airport and arrival airport as arguments
	public static void storeAirfare(IRIS irisNative)
	{		
		// Store routes and distance between airports 
		// This API sets the value, for a global, with the following keys
		// For example, ^airport("BOS","AUS") = 1698
		irisNative.set("1698","^airport", "BOS","AUS");
		irisNative.set("450","^airport", "BOS","AUS","AA150");
		irisNative.set("550","^airport", "BOS","AUS","AA290");
		irisNative.set("200","^airport", "BOS","PHL","UA110");
		irisNative.set("700","^airport", "BOS","BIS","AA330");
		irisNative.set("710","^airport", "BOS","BIS","UA208");
		
		// Prompt
		Scanner scanner = new Scanner(System.in);
		System.out.print("Enter departure airport: ");
		String fromAirport = scanner.next();
		System.out.print("Enter destination airport: ");
		String toAirport = scanner.next();
		
		// Query for routes based on input
		String hasRoutes = "This path has no routes";
		int isDefined = irisNative.isDefined("^airport", fromAirport, toAirport);
				
		if (isDefined==11 || isDefined==1 ) { hasRoutes =  "This path has routes"; } 
		System.out.println("");
		System.out.println("Printed to ^airport global. The distance in miles between "+ fromAirport + " and " + toAirport + 
				" is: " + irisNative.getString("^airport", fromAirport, toAirport) + ". " + hasRoutes );
			
			scanner.close();
		}

}
