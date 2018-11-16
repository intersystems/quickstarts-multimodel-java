/* Purpose: This demo shows using objects, SQL, and native access side-by-side in a Java application, 
* connecting to InterSystems IRIS.
*
* To Test: Run to see objects and SQL working side-by-side. Then uncomment the line to execute StoreAirfare to see
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


//Purpose: This class shows a multimodel example in Java. SQL is used to display airports,
//objects are stored directly, and a custom data structure is used to determine airfare.
public class multimodelQS {
	
	public static void main(String[] args) {
		String user = "SuperUser";
		String pass = "SYS";
		String server = "localhost";
		int port = 51773;
		
		try {
			//Connect to database using EventPersister, which is based on IRISDataSource
	        EventPersister xepPersister = PersisterFactory.createPersister();
	        xepPersister.connect(server,port,"User",user,pass); 
	        System.out.println("Connected to InterSystems IRIS via JDBC."); 
	        xepPersister.deleteExtent("Demo.Location");   // remove old test data
	        xepPersister.importSchemaFull("Demo.Location");   // import flat schema
	        xepPersister.deleteExtent("Demo.Airport");   // remove old test data
	        xepPersister.importSchema("Demo.Airport");   // import flat schema
	       
	        //***Initializations***
	        //Create XEP Event for object access
	        Event xepEvent = xepPersister.getEvent("Demo.Airport");

	        //Create JDBC statement object for SQL and IRIS Native access
	        Statement myStatement = xepPersister.createStatement();
	        
	        //Create IRIS Native object
	        IRIS irisNative = IRIS.createIRIS((IRISConnection)xepPersister);
	        
	        
	        //***Running code***
	        System.out.println("Generating stock info table...");
			
	        // Populate 5 airport objects and save to the database using XEP
			populateAirports(xepEvent);
			
			// Get all airports using JDBC
			getAirports(myStatement);
			
			// Store natively - Uncomment the following line for task 3
			//StoreAirfare(irisNative);
				
			//Close everything
		    xepEvent.close();
		    xepPersister.close();
						
		} catch (SQLException e) {
			 System.out.println("Error creating stock listing: " + e.getMessage());
		}
	        
	}
	
	//Store objects directly to InterSystems IRIS
	public static void populateAirports(Event xepEvent) {	
		Demo.Airport[] airportArray = new Demo.Airport[5];
		
		//1. Boston
		Demo.Airport newAirport = new Demo.Airport();
		newAirport.setName("Boston Logan International");
		newAirport.setCode("BOS");
		Demo.Location loc = new Demo.Location();
		loc.setCity("Boston");
		loc.setState("MA");
		loc.setZip("02128");
		newAirport.setLocation(loc);
		airportArray[0] = newAirport;
		
		//2. Philadephia
		Demo.Airport newAirport2 = new Demo.Airport();
		newAirport2.setName("Philadephia International");
		newAirport2.setCode("PHL");
		Demo.Location loc2 = new Demo.Location();
		loc2.setCity("Philadelphia");
		loc2.setState("PA");
		loc2.setZip("19153");
		newAirport2.setLocation(loc2);
		airportArray[1] = newAirport2;
		
		//3. Austin
		Demo.Airport newAirport3 = new Demo.Airport();
		newAirport3.setName("Austin–Bergstrom International");
		newAirport3.setCode("AUS");
		Demo.Location loc3 = new Demo.Location();
		loc3.setCity("Austin");
		loc3.setState("TX");
		loc3.setZip("78719");
		newAirport3.setLocation(loc3);
		airportArray[2] = newAirport3;
		
		//4. San Francisco
		Demo.Airport newAirport4 = new Demo.Airport();
		newAirport4.setName("San Francisco International");
		newAirport4.setCode("SFO");
		Demo.Location loc4 = new Demo.Location();
		loc4.setCity("San Francisco");
		loc4.setState("CA");
		loc4.setZip("94128");
		newAirport4.setLocation(loc4);
		airportArray[3] = newAirport4;
		
		//5. O'hare
		Demo.Airport newAirport5 = new Demo.Airport();
		newAirport5.setName("Chicago O'hare International");
		newAirport5.setCode("ORD");
		Demo.Location loc5 = new Demo.Location();
		loc5.setCity("Chicago");
		loc5.setState("IL");
		loc5.setZip("60666");
		newAirport5.setLocation(loc5);
		airportArray[4] = newAirport5;
		
		for (Demo.Airport a : airportArray){
			xepEvent.store(a);
		}
		System.out.println("Stored 5 airports");
	}
	
	///Display all airports using JDBC
	public static void getAirports(Statement myStatement)
	{
		ResultSet myRS;
		try {
			myRS = myStatement.executeQuery("SELECT name, code, location->city, location->state, location->zip FROM demo.airport");
		
			System.out.println("Name\t\t\t\t\tCode\t\tLocation");
			while(myRS.next())
			{
				System.out.println(
						myRS.getString("name") + "\t\t" + 
						myRS.getString("code")+"\t\t" + 
						myRS.getString("city") + ", " + 
						myRS.getString("state") + " " + 
						myRS.getString("zip")
						);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	///Create a custom data structure to store airfare in a graph-like structure and retrieve airfare based on nodes
	///Takes departure airport and arrival airport as arguments
	public static void StoreAirfare(IRIS irisNative)
	{		
		//Store routes and distance between airports 
		//This API sets the value, for a global, with the following keys
		//For example, ^AIRPORT("BOS","AUS") = 1698
		irisNative.set("1698","^AIRPORT", "BOS","AUS");
		irisNative.set("450","^AIRPORT", "BOS","AUS","AA150");
		irisNative.set("550","^AIRPORT", "BOS","AUS","AA290");
		irisNative.set("200","^AIRPORT", "BOS","PHL","UA110");
		irisNative.set("700","^AIRPORT", "BOS","BIS","AA330");
		irisNative.set("710","^AIRPORT", "BOS","BIS","UA208");
		
		//Prompt
		Scanner scanner = new Scanner(System.in);
		System.out.print("Enter departure airport: ");
		String fromAirport = scanner.next();
		System.out.print("Enter destination airport: ");
		String toAirport = scanner.next();
		
		//Query for routes based on input
		String hasRoutes = "This path has no routes";
		int isDefined = irisNative.isDefined("^AIRPORT", fromAirport, toAirport);
				
		if (isDefined==11 || isDefined==1 ) { hasRoutes =  "This path has routes"; } 
		System.out.println("");
		System.out.println("Printed to ^AIRPORT global. The distance in miles between "+ fromAirport + " and " + toAirport + 
				" is: " + irisNative.getString("^AIRPORT", fromAirport, toAirport) + ". " + hasRoutes );
			
			scanner.close();
		}

}