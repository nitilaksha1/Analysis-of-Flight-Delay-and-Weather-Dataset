import csv
import sys

# Function to preprocess the Flight-Weather data to remove duplicate entries per flight 
# A new CSV file is generated which is used as input data to train the machine learning model
def preProcessFlightWeatherData():
    filename = sys.argv[1]
    flightmap = {}
    with open(filename, "rb") as file:
        rdr = csv.reader(x.replace('\0', '') for x in file)
        next(file)

        dst_file = "FlightWeatherPreProcessedDataset.csv"

        with open(dst_file, "wb") as result:
            wtr = csv.writer(result)

            for r in rdr:
                flightid = r[1]

                if flightid not in flightmap:
                    flightmap[flightid] = 1
                    wtr.writerow(r)
                else:
                    continue


preProcessFlightWeatherData()

