# storage-warriors-hbase
Weather-Flight Big Data Project using HBase

## Team
* Abhinay Agrawal<br /> Ambuj Nayan<br /> Niti Halakatti<br /> Rahul Sharma<br />

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
