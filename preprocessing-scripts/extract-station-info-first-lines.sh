##inputs
##first  : folder containing all US stations extracted from global dataset
##second : filename of result file for output

##Example:
## extract-station-info-first-lines.sh 1990_us 1990_us_station_coordinates.csv

us_stations_data="$1";
output_file="$2";

list=$(ls "$us_stations_data/." -lf | cat)
for item in $list; 
do 
	if [ -f "$us_stations_data/$item" ]; 
	then 
		sed -n 2,2p "$us_stations_data/$item";
	fi; 
done > "$output_file"
