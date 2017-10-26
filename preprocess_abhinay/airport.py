# This file saves the airport code and latitude, longitude mapping
import json
import csv

with open('airports.json') as data_file:
    data = json.load(data_file)

with open('airportsUS.csv', 'wb') as csvfile:
    spamwriter = csv.writer(csvfile, delimiter='\t', quotechar='|', quoting=csv.QUOTE_MINIMAL)

    airports = data["airports"]
    i = 1
    for a in airports:
        if a["country"] == "United States":
            #add in my csv some fields
            spamwriter.writerow([i, a["code"], a["lat"], a["lon"]])
            i = i + 1

