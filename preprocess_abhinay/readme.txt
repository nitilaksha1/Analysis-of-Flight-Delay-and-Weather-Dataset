To run preprocessing, run crawl.py.

python crawl.py <start_year> <end_year>

example, if i want data for 1920 then,
python crawl.py 1920 1920

if i want 1920 to 1930

python crawl.py 1920 1930

...............

airport.py

airportsUS.csv contains the latitude and longitude mapping of US airports.
This was created by script airport.py (which u don't need to run again).

...............

air_clean.py

This file cleans the airport delay data.
It takes airportsUS.csv file as an input. Place this python in some directory which contains 
"flights/" directory. Flights dir will have all the flight delay data.

...............

weather_stn_geo.py

This file generates a new file with "STN_ID", "LAT", "LONG" from the downloaded and cleaned 
weather data. The generated file "us_stn_geoloc.csv" willbe used for minimum distance 
calculation from airports.
