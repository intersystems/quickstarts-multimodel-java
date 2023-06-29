# Multi-Model QuickStart for Java Code Sample

## In this repo
.  
├── LICENSE  
├── README.md  
├── lib
    └── .keep  
└── src  
    ├── Demo  
    │   ├── Airport.java  
    │   └── Location.java  
    └── multimodelQS.java  

## Guided Tutorial
For a guided tutorial using this sample, visit [Accessing Data in Java Using Multiple Data Models](https://learning.intersystems.com/course/view.php?name=JavaMultiModel) on the InterSystems learning site. 

## How to use this sample on your own
This sample code shows object, relational, and native access from a Java application to InterSystems IRIS. Airport data is stored using objects and retrieved using SQL, and a custom data structure is created using globals to handle route information between airports.

1. Start with an installation of Java and a running instance of InterSystems IRIS.
2. Download the appropriate JDBC and XEP drivers for your system from the [InterSystems Drivers Download page](https://intersystems-community.github.io/iris-driver-distribution/)
3. Clone this repository and open it in your preferred IDE.
4. Add the drivers to the `lib` folder of this repo, or to your CLASSPATH according to the [Connecting Your Application documentation page](https://docs.intersystems.com/components/csp/docbook/DocBook.UI.Page.cls?KEY=ADRIVE#ADRIVE_jdbc).
5. In `multimodelQS.java`, on lines 29-32, change username, password, IP, port and namespace to point to your instance of InterSystems IRIS
6. If you would like to see how to store data natively using Java, find and uncomment the following lines:  
    ```
    // storeAirfare(irisNative);  
    // checkAirfare(irisNative);  
    ```
7. Compile the project and run multimodelQS.java. If all works correctly, you will see a list of airports output. Data is stored using XEP (objects) and retrieved using JDBC (relationally). 
    1. Enter departure airport: **BOS**
    2. Enter destination airport: **AUS**

    The output should say:  
    >Printed to ^airport global. The distance in miles between BOS and AUS is: 1698.  
    >The following routes exist for this path:  
    >  -AA150: 450 USD  
    >  -AA290: 550 USD 

    Other routes may be null.
