# Author: Abhinay Agrawal

import csv, os

dict = {}
par_dir = os.getcwd() + "/flights/"

for fn in os.listdir(par_dir):
    with open("airportsUS.csv") as f:
        for line in f:
            temp = line.split(',')

            key = temp[0]
            dict[key] = (temp[1], temp[2][:-2])


    src_file = os.path.join(par_dir + fn)
    print src_file
    with open(src_file, "rb") as file:
        rdr = csv.reader(file)
        next(file)

        dst_file = src_file[:-4] + "n.csv"
        with open(dst_file, "wb") as result:
            wtr = csv.writer(result)
            wtr.writerow(("FL_DATE", "AIRLINE_ID", "FL_NUM", "ORIGIN", "CITY_NAME", "LAT", "LONG",
                          "DEP_TIME", "DEP_DELAY_TOTAL", "DEP_DEL15", "W_CANCELLED",
                          "DIVERTED", "WEATHER_DELAY"))

            for r in rdr:

                port_code = r[4]
                if port_code not in dict:
                    continue
                lat = dict[port_code][0]
                lon = dict[port_code][1]

                # if cancellation is not because of weather then just remove it
                if r[16] != "" and r[16] != "B":
                    continue

                fl_date = r[0].split('-')
                date = fl_date[0] + fl_date[1] + fl_date[2]

                dep_time = r[9]
                if dep_time == '':
                    dep_time = "9999"

                dep_delay = r[10]
                if dep_delay == '':
                    dep_delay = 9999

                dep_delay15 = r[11]
                if dep_delay15 == '':
                    dep_delay15 = 1

                w_delay = r[18]
                if w_delay == '':
                    w_delay = 0

                fl_num = str(r[2])
                while len(fl_num) < 5:
                    fl_num = "0" + fl_num
                fl_num = "T" + fl_num

                wtr.writerow( (date, r[1], fl_num, r[4], r[5], lat, lon, dep_time + "T", dep_delay, dep_delay15,
                               r[15], r[17], w_delay) )

    os.remove(src_file)