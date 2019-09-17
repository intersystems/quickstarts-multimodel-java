# Multi-Model QuickStart for Java Code Sample

This code shows multi-model access to InterSystems IRIS data platform in Java.

This sample is used in the [Multi-Model QuickStart](https://gettingstarted.intersystems.com/multimodel-overview/multimodel-quickstart/). 
It shows object, relational, and native access from a Java application to InterSystems IRIS. Airport data is stored using objects and retrieved using SQL, and a custom data structure is created using the Native API to handle route information between airports.

## Run the Sample

1. In the integrated terminal, run the following lines to compile the classes:

  * `cd /home/project/quickstarts-multimodel-java/src`  
  * `javac -cp ".:../lib/intersystems-jdbc-3.0.0.jar:../lib/intersystems-xep-3.0.0.jar" multimodelQS.java`  
  
2. Run multimodelQS:

  * `java -cp ".:../lib/intersystems-xep-3.0.0.jar:../lib/intersystems-jdbc-3.0.0.jar" multimodelQS`  

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
