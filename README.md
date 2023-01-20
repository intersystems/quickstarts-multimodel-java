# Multi-Model QuickStart for Java Code Sample

This code shows multi-model access to InterSystems IRIS data platform in Java.

This sample is used in the [Multi-Model exercise](https://learning.intersystems.com/course/view.php?name=Multimodel) on the InterSystems Learning site. 
It shows object, relational, and native access from a Java application to InterSystems IRIS. Airport data is stored using objects and retrieved using SQL, and a custom data structure is created using the Native API to handle route information between airports.

## Run the sample code in the InterSystems Learning Labs or Evaluator Edition (on AWS, GCP, or Azure)

1. In the integrated terminal, run the following lines to compile the classes:

  * `cd /home/project/quickstarts-multimodel-java/src`  
  * `javac -cp ".:../lib/intersystems-jdbc-3.2.0.jar:../lib/intersystems-xep-3.2.0.jar" multimodelQS.java`  
  
2. Run multimodelQS:

  * `java -cp ".:../lib/intersystems-xep-3.2.0.jar:../lib/intersystems-jdbc-3.2.0.jar" multimodelQS` 
  
## Run the sample code locally

1. Clone the repo and open in your Java IDE
2. In `multimodelQS.java`, change username, password, IP, port and namespace to point to your instance of InterSystems IRIS
3. Run multimodelQS

## Output

If all works correctly, you will see a list of airports output. Data is stored using XEP (objects) and retrieved using JDBC (relationally).  

If you would like to see how to store data natively using Java:
1. Find and uncomment the following lines:  
`// storeAirfare(irisNative);`  
`// checkAirfare(irisNative);`  
2. Enter departure airport: **BOS**
3. Enter destination airport: **AUS**

The output should say:  
>Printed to ^airport global. The distance in miles between BOS and AUS is: 1698.  
>The following routes exist for this path:  
>  -AA150: 450 USD  
>  -AA290: 550 USD 

Other routes may be null.

## Keep Exploring

To continue with another Java example with InterSystems IRIS, see the [Java QuickStart](https://gettingstarted.intersystems.com/language-quickstarts/java-quickstart/).
