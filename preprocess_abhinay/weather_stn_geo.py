import csv, os, linecache


root_dir = os.getcwd() + "/data/"

dst = open("us_stn_geoloc.csv", "wb")
wtr = csv.writer(dst)
wtr.writerow(("STN_ID", "LAT", "LONG"))

for subdir, dirs, files in os.walk(root_dir):
    for file in files:
        src_file = os.path.join(subdir, file)

        #with open(src_file) as src:
            #rdr = csv.reader(src)

        row2 = linecache.getline(src_file, 2).split(",")
        wtr.writerow((row2[0], row2[3], row2[4]))

dst.close()
