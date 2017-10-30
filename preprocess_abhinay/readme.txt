This readme file contains the description for all the preproccesing done on the weather
and flight delay data.

Weather data preprocessing:

1. Run crawl.py using following command

python crawl.py <start_year> <end_year>

For example, if you want data for only 1920 then,
python crawl.py 1920 1920

Another example, if I want 1920 to 1930

python crawl.py 1920 1930

This file downloads and saves the clean weather data in csv format.
...................

airport.py

airportsUS.csv contains the latitude and longitude mapping of US airports.
This was created by script airport.py (which u don't need to run again).

Input: airportsUS.json
Output: airportsUS.csv

...............

Flight delay data preprocessing:

air_clean.py

This file cleans the airport delay data.
It takes airportsUS.csv file as an input. Place this python in some directory which contains 
"flights/" directory. Flights dir will have all the raw flight delay data.

NOTE: It replaces the raw data, so save the raw data elsewhere too.

...............

weather_stn_geo.py

This file generates a new file with "STN_ID", "LAT", "LONG" from the downloaded and cleaned 
weather data. The generated file "us_stn_geoloc.csv" willbe used for minimum distance 
calculation from airports.
