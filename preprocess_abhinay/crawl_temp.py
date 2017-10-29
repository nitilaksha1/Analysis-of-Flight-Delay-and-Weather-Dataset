# Author: Abhinay Agrawal

from BeautifulSoup import BeautifulSoup as bs
#from bs4 import BeautifulSoup as bs
from urllib2 import urlopen, HTTPError, URLError
import os, sys, tarfile, time, errno, csv, shutil


par_url = 'https://www.ncei.noaa.gov/data/global-hourly/access/'
file_links = list()
us_stations = ["72645014898.csv"]   # station code for Austin Intl. Airport


# Function go throught hte HTML of the webpage and creates a list of the link for the
# files present in that webpage.
def scrape_webpage(url):
    try:
        soup = bs(urlopen(url))
        files = soup.findAll('a')[5:]      #removing first 5 irrelevant links

        for i in range(0, len(files)):
            # Make the list save the links and not just filenames
            if files[i].get('href') in us_stations:
                file_links.append(url + files[i].get('href'))

    except HTTPError, e:
        print "HTTP Error:", e.code, url
    except URLError, e:
        print "URL Error:", e.reason, url


def download(file_url):
    # Downloading files now for the year provided
    try:
        f = urlopen(file_url)

        directory = os.getcwd() + "/temperature_Austin/"
        if not os.path.exists(directory):
            os.makedirs(directory)

        yr = len(par_url)
        file_name = directory + os.path.basename(file_url)[:-4] + "_" + file_url[yr:yr+4] + ".csv"
        with open(file_name, "wb") as local_file:
            local_file.write(f.read())


    except HTTPError, e:
        print "HTTP Error:", e.code, file_url
    except URLError, e:
        print "URL Error:", e.reason, file_url


# This function cleans the data by only keeping the required mandatory columns
def clean_data(src_file):
    with open(src_file, "rb") as source:

        rdr = csv.reader(x.replace('\0', '') for x in source)
        next(source)

        dst_file = src_file[:-4] + "n.csv"
        with open(dst_file,"wb") as result:
            wtr = csv.writer( result )
            wtr.writerow(("STN_ID", "DATE", "YEAR", "MONTH", "DAY", "TIME", "LAT", "LONG", "TMP"))


            for r in rdr:
                wind_speed = r[10].split(",")[3]

                temp = r[1].split("T")
                date = temp[0].split("-")

                time_t = temp[1].split(":")
                time = str(time_t[0]) + str(time_t[1]) + "T"

                wtr.writerow( (r[0], date[0]+date[1]+date[2], date[0], date[1], date[2], time, r[3], r[4],
                               r[13].split(",")[0]) )


def main(argv):
    #start_year = int(sys.argv[1])
    #end_year = int(sys.argv[2])


    """
    for i in xrange(start_year, end_year+1):
        year_link = par_url + str(i) + "/"
        scrape_webpage(year_link)

    with open("temperature_file_links", "wb") as dst:
        for l in file_links:
            dst.write(l + "\n");
    """

    file_links = [line.rstrip('\n') for line in open('temperature_file_links')]

    for i in file_links:
        download(i)

    directory = os.getcwd() + "/temperature_Austin/"

    for filename in os.listdir(directory[:-1]):
        f = os.path.join(directory, filename)

        clean_data(f)

        os.remove(f)


if __name__ == "__main__":
    main(sys.argv[1:])
