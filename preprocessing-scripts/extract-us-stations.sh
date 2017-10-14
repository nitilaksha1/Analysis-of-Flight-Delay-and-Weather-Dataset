##inputs
##first  : filename of all US stations 
##second : source folder containing all data
##third  : destination folder to save only US data

##Example:
## extract-us-stations.sh us-stations.txt 1990 1990_us

us_stations_filename=$1
srcfolder=$2
destfolder=$3

while read -r line; 
do 
	fname="$line"; 

	if [ -f "$srcfolder/$fname" ]; 
	then 
		cp "$srcfolder/$fname" "$destfolder/$fname"; 
	fi; 

done < "$us_stations_filename"
