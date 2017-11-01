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
* Hadoop
* HBase
* Spark
* Apache Phoenix
* Apache Zeppelin

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

## Status
* Data gathering (complete)
    * Flight data
    * Weather data 
* Preprocessing scripts (complete)
    * Web scraper to download data from NOAA website.
    * Script to extract weather data for US geography.
    * Script to extract latitude, longitude, and other information for each US weather station
    * Drop columns which are not required from weather/flight datasets.
    * Script to prepare the list of all US airports along with latitude/longitude.
* Spark ingestion job (complete)
    * Spark job to read multiple data files concurrently and load them to HBase.
* Java code to find the weather station nearest to an airport (complete)
* Machine learning code for predicting flight delays (complete)
* Queries (almost done)
* Unit tests (in progress)
* Correlate flight and weather data (in progress)
