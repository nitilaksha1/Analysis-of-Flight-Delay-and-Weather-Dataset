# Author: Abhinay Agrawal

from BeautifulSoup import BeautifulSoup as bs
from urllib2 import urlopen, HTTPError, URLError
import os, sys, subprocess, tarfile, time, errno, csv


par_url = 'https://www.ncei.noaa.gov/data/global-hourly/archive/'
file_links = list()


def scrape_webpage():
    try:
        soup = bs(urlopen(par_url))
        tar_files = soup.findAll('a')[5:]      #removing first 5 irrelevant links

        for i in range(0, len(tar_files)):
            # Make the list save the links and not just filenames
            file_links.append(par_url + tar_files[i].get('href'))
            #print file_links[i]

    except HTTPError, e:
        print "HTTP Error:", e.code, par_url
    except URLError, e:
        print "URL Error:", e.reason, par_url


def download(year):
    # Downloading files now
    try:
        file_url = file_links[year]
        f = urlopen(file_url)

        directory = os.getcwd() + "/zips/"
        if not os.path.exists(directory):
            os.makedirs(directory)

        with open(directory + os.path.basename(file_url), "wb") as local_file:
            local_file.write(f.read())

        file_name = file_url[-11:]

        return file_name

    except HTTPError, e:
        print "HTTP Error:", e.code, file_url
    except URLError, e:
        print "URL Error:", e.reason, file_url


def clean_data (src_file):
    with open(src_file, "rb") as source:
        rdr = csv.reader( source )
        next(source)

        dst_file = src_file[:-4] + "n.csv"
        with open(dst_file,"wb") as result:
            wtr = csv.writer( result )
            wtr.writerow(("STN_ID", "DATE", "TIME", "LAT", "LONG", "ELEVATION", "STN_NAME",
                          "WND_SPD", "CIG", "VIS", "TMP", "DEW", "SLP"))
            for r in rdr:
                wind_speed = r[10].split(",")[4]

                date = r[1].split("T")[0]
                time = (r[1].split("T")[1]).split(":")

                time_n = time[0] + "-" + time[1] + "-" + time[2]
                wtr.writerow( (r[0], date, time_n, r[3], r[4], r[5], r[6], wind_speed,
                               r[11].split(",")[0], r[12].split(",")[0], r[13].split(",")[0],
                               r[14].split(",")[0], r[15].split(",")[0]) )


def ensure_dir(directory):
    #directory = os.path.dirname(file_path)
    try:
        if not os.path.exists(directory):
            os.makedirs(directory)
    except OSError as e:
        if e.errno != errno.EEXIST:
            raise


def untar(file_name):

    file_path = os.getcwd() + "/data/" + file_name[:4] + "/"
    ensure_dir(file_path)

    print "Downloading file: " + file_name
    file_name = os.getcwd() + "/zips/" + file_name

    while not os.path.exists(file_name):
        print "."
        time.sleep(2)

    tar = tarfile.open(file_name)
    tar.extractall(path=file_path)
    tar.close()

    return file_path


def main(argv):
    start_year = int(sys.argv[1])
    end_year = int(sys.argv[2])

    scrape_webpage()

    us_stations = list()
    with open("us-stations.txt") as f:
        for line in f:
            us_stations.append(line.rstrip('\n'))

    for i in range(start_year, end_year+1):
        file_name = download(i - 1901)
        directory = untar(file_name)


        for filename in os.listdir(directory[:-1]):
            f = os.path.join(directory, filename)
            if filename in us_stations:
                clean_data(f)

            os.remove(f)



if __name__ == "__main__":
    main(sys.argv[1:])
