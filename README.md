# multimodel-java-quickstart
This QuickStart can be found at the [multi-model QuickStart page](https://gettingstarted.intersystems.com/multimodel-overview/multimodel-quickstart/). 
It shows object, relational, and native access from a Java application to InterSystems IRIS. Airport data is stored using objects, retrieved using SQL, and a custom data structure is created using the Native API to handle route information between airports.

## To run in InterSystems Learning Labs or Evaluator Edition (on AWS, GCP, or Azure)
1. For AWS, GCP, or Azure ONLY: Open multimodelQS.java and modify the IP to be "try-iris". Verify username and password as well. (Please skip this step if using InterSystems Learning Labs)
2. In the integrated terminal, run the following lines to compile the classes  
`cd quickstarts-multimodel-java/src`  
`javac -cp ".:../lib/intersystems-jdbc-3.0.0.jar:../lib/intersystems-xep-3.0.0.jar" multimodelQS.java`  
3. Run multimodelQS  
`java -cp ".:../lib/intersystems-xep-3.0.0.jar:../lib/intersystems-jdbc-3.0.0.jar" multimodelQS`  

## To run locally
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
