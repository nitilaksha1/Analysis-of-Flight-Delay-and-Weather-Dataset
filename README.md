# storage-warriors-hbase
Analysis of Flight Delays and Weather Datasets using NoSQL

## Team
* Abhinay Agrawal
* Ambuj Nayan
* Niti Halakatti
* Rahul Sharma

## Introduction
* The goal of this project was to build an application which could ingest, store, analyze, and extract meaningful insights from two different massive data stores.
* The first of these sources was NOAA (National Oceanic and Atmospheric Administration) and it provided us with hourly synoptic weather observations from station networks around the world. 
* The second data source was UBTS (US Bureau of Transportation Services) and it provided us with flight history and delays.

## Tech Stack
* Python
* Java
* SQL
* Hadoop
* HBase
* Spark
* Apache Phoenix
* Apache Zeppelin
* Scikit-learn
* Pandas

## Criteria For Deciding Tech Stack
* The respective sizes of weather and flight datasets were 750 GB and 225 GB approximately. 
* The huge data volume pushed us towards building a scalable and distributed NoSQL database such as HBASE to store the data
* The datasets in their raw form were not conducive for analysis and needed considerable amount of pre-processing. Custom python scripts were used to pre-process the data
* Post pre-processing, we needed a scalable and distributed process which could perform bulk upload to HBase. Apache Spark was a good fit here because of its unique in-memory processing capabilities which allow it to process large-scale data at very high speeds
* This application had to provide ease of access to its users. Since most of the users in the current world are already familiar with SQL and SQL reduces the amount of code that needs to be written, we decided to integrate HBase with Apache Phoenix. 
* Phoenix is an open source SQL skin for HBase and saves us the hassle of writing application code to query data using HBase APIs
* Apache Zeppelin is a web-based notebook application that enables data-driven, interactive data analytics, and collaboration with SQL

## Infrastructure Setup

### Hadoop Setup
* Installation of Hadoop involves downloading the distribution from Apache and modifying the configuration files as mentioned in the following link : <br />
http://hadoop.apache.org/docs/current/hadoop-project-dist/hadoop-common/SingleCluster.html#Pseudo-Distributed_Operation
* HBase from Apache can be downloaded and configured as per the guidelines provided in the following link:<br />
https://hbase.apache.org/book.html#quickstart
* Upon the completion of installation and configuration, the services related to DFS, YARN, and HBASE can be started

### Apache Phoenix Setup
* The installation can be downloaded at the following link:<br />
https://phoenix.apache.org/download.html
* Copy the phoenix server jar into HBase lib directory.
* Restart HBase and after successful restart, we start the query server

### Apache Zeppelin Setup
* The installation and setup can be done as per the instruction in the following link:<br />
https://zeppelin.apache.org/docs/0.7.3/install/install.html
* Configure Zeppelin as a thin client and used a JDBC connection to connect it to Apache Phoenix

## Requirement Analysis
* The main requirement of this project is to provide the users with an easy interface to query and analyze large datasets.
* Users should be able to run analytical query and visualize flight delay and weather data.
* The application should also provide an automated mechanism for data ingestion
* The system should be able to scale because these datasets will witness exponential growth. Though the current requirement is to support historical analysis of weather and flight data, the system design should be flexible enough to support transactional analysis in future
* A nice to have feature is to help users find correlation between weather and flight data. This enables the users to predict flight delays based on forecasted weather information by using machine learning algorithm

## Business Questions

### Business Question 1
For a given weather station and year, report the highest observed temperatures (degree centigrade) by month. Provide a mechanism to visualize this information.

### Business Question 2
For a given weather station and year, report the average observed temperatures (degree centigrade) by month. Provide a mechanism to visualize this information.

### Business Question 3
For a given weather station and year, report the lowest observed temperatures (degree centigrade) by month. Provide a mechanism to visualize this information.

### Business Question 4
For a given weather station and month, show the variation in mean temperature (degree centigrade) over a period of 20 years. Provide a mechanism to visualize this information.

### Business Question 5
Find the relation between temperature and elevation (altitude). Ideally as elevation increases, temperature decreases, and the rate of decrease is 6.5 degree centigrade for each 1 km of altitude change. Validate if the observed data provides support for this trend along with providing a mechanism for visualizing this information.

### Business Question 6
Report the count of those weather stations which have witnessed Hurricane force winds. Beaufort wind force scale is an empirical measure that relates wind speed to observed conditions at sea or on land. Hurricane force winds are categorized as those which have wind speeds greater than equal to 118 km/hr.

### Business Question 7
Report the count of those weather stations which have very low visibility. Areas with visibility of less than 100 metres (330 ft.) are usually reported as very low visibility areas.

### Business Question 8
Find the maximum observed flight delay (in minutes) in the entire dataset.

### Business Question 9
Find the average observed flight delay (in minutes) in the entire dataset.

### Business Question 10
Report the number of instances where flights have been delayed by more than 15 minutes.

### Business Question 11
Find out the correlation between flight and weather data. Note that there is a spatiotemporal relation between weather and flight data.

### Business Question 12
Use the correlation between weather and flight data to predict if a flight will be delayed based on forecasted weather metrics.

## Challenges Faced
* The data size being too large (750GB), saving the data on local machine or on any free cloud service was not possible. Hence, we had to take a decision to download limited data.
* Weather data was organized in a good way but few of the information need to answer the business questions were not available directly. For example, the country name of the weather station as not mentioned
* Flight delay data did not contain the geolocation information which we had to search online and extract, connect the relevant info to our data
* Flight delay data has a lot of missing values which we had to take care of. Also, during querying of the tables from HBase, we had to keep in mind that data has missing values.
* Choosing the right tool to use for querying and visualization took a lot of time and effort. In the end, we decided to go with Apache Phoenix and Apache Zeppelin to do this work. These tools also had a lot of shortcomings like:
   * Although they provide SQL interface to NoSQL databases not all commands are supported
   * Support for joins are limited
   * There are issues when the already created tables in HBase are imported into Phoenix. It is unable to read the metadata info for the tables from HBase and hence assumes every field to be a byte
   
## Future Work
* The primary requirement is to scale our project to handle the needs of big data
   * This can be done by distributing our database over a set of servers in a cluster
   * Setting up Hadoop cluster for operation in distributed environment
* Using a custom solution to visualize the HBase data to answer queries
* The Apache Phoenix solution used in this project is not scalable due to its SQL structure and strict constraint to maintain the table in a format to query the data
* A custom query application would entail the following:
   * JavaScript Front End Framework (like AngularJS) for user query
   * REST API’s to interact with HBase
   * D3 charts to visualize the data returned from the queries
   * Due to the NoSQL nature of the database it very time consuming to answer some queries as it requires a full table scan in HBase due to the field not being a row key. This required the use of secondary index on the fields that are frequently queried. 
   * The usage of a secondary index on a field is non-trivial in HBase since it requires a large amount of coding to be performed by the developer including but not handling the update of the secondary index
* The prediction of a flight delay at an airport given the weather conditions can be leveraged in a sample application which needs this kind of analytics
* From a software engineering perspective, the following aspects need to be added to promote code maintainability:
   * Unit Testing
   * Integration Testing
   * System Testing
   * Performance Testing
