# multimodel-java-quickstart
This quickstart can be found at [https://learning.intersystems.com/course/view.php?name=Multimodel](https://learning.intersystems.com/course/view.php?name=Multimodel). 
It shows object, relational, and native access from a Java application to InterSystems IRIS. Airport data is stored using objects, retrieved using SQL, and a custom data structure is created using the Native API to handle route information between airports.

## To run in InterSystems Learning Labs, AWS, GCP, or Azure
1. Open multimodelQS.java and modify the IP and port to be the values for your cloud node. Verify username and password as well.
2. In the integrated terminal, run the following lines to compile the classes  
`javac -cp ".:../lib/intersystems-jdbc-3.0.0.jar:../lib/intersystems-xep-3.0.0.jar" Demo/Airport.java`  
`javac -cp ".:../lib/intersystems-jdbc-3.0.0.jar:../lib/intersystems-xep-3.0.0.jar" Demo/Location.java`  
`javac -cp ".:../lib/intersystems-jdbc-3.0.0.jar:../lib/intersystems-xep-3.0.0.jar" multimodelQS.java`  
3. Run multimodelQS  
`java -cp ".:../lib/intersystems-xep-3.0.0.jar:../lib/intersystems-jdbc-3.0.0.jar" multimodelQS`  

## How to run locally
1. Clone the repo and open in your Java IDE
2. In `multimodelQS.java`, change username, password, IP, port and namespace to point to your instance of InterSystems IRIS
3. Run multimodelQS

## Output
If all works correctly, you will see a list of airports output. Data is stored using XEP (objects) and retrieved using JDBC (relationally).  

If you would like to see how to store data natively using Java:
1. Find and uncomment the following line:  
`// StoreAirfare(irisNative);`
2. Enter departure airport: **BOS**
3. Enter destination airport: **AUS**

The output should say:  
>Printed to ^AIRPORT global. The distance in miles between BOS and AUS is: 1698. This path has routes

Other routes may be null.
