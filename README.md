# storage-warriors-hbase
Weather-Flight Big Data Project using HBase

## Team
* Abhinay Agrawal
* Ambuj Nayan
* Niti Halakatti
* Rahul Sharma

## Introduction
The goal of this project was to build an application which could ingest, store, analyze, and extract meaningful insights from two different massive data stores. The first of these sources was NOAA (National Oceanic and Atmospheric Administration) and it provided us with hourly synoptic weather observations from station networks around the world. The second data source was UBTS (US Bureau of Transportation Services) and it provided us with flight history and delays.

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
