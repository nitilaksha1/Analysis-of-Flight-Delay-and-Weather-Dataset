# Author: Abhinay Agrawal

from BeautifulSoup import BeautifulSoup as bs
#from bs4 import BeautifulSoup as bs
from urllib2 import urlopen, HTTPError, URLError
import os, sys, tarfile, time, errno, csv, shutil


par_url = 'https://www.ncei.noaa.gov/data/global-hourly/archive/'
file_links = list()

# Variables for provenance
files_total = 0
files_retained = 0
mand_col_total = 29
mand_col_retained = 13


# Function go throught hte HTML of the webpage and creates a list of the link for the
# files present in that webpage.
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
    # Downloading files now for the year provided
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


# This function cleans the data by only keeping the required mandatory columns
def clean_data(src_file):
    with open(src_file, "rb") as source:
        #rdr = csv.reader(source)
        rdr = csv.reader(x.replace('\0', '') for x in source)
        next(source)

        dst_file = src_file[:-4] + "n.csv"
        with open(dst_file,"wb") as result:
            wtr = csv.writer( result )
            wtr.writerow(("STN_ID", "DATE", "YEAR", "MONTH", "DAY", "TIME", "LAT", "LONG", "ELEVATION",
                          "WND_SPD", "CIG", "VIS", "TMP", "DEW", "SLP"))


            for r in rdr:
                wind_speed = r[10].split(",")[3]

                temp = r[1].split("T")
                date = temp[0].split("-")

                time_t = temp[1].split(":")
                time = str(time_t[0]) + str(time_t[1]) + "T"

                wtr.writerow( (r[0], date[0]+date[1]+date[2], date[0], date[1], date[2], time, r[3], r[4],
                               r[5], wind_speed, r[11].split(",")[0], r[12].split(",")[0], r[13].split(",")[0],
                               r[14].split(",")[0], r[15].split(",")[0]) )


# Function creates the directory
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
    us_stations = list()
    global files_total, files_retained, mand_col_total, mand_col_retained

    scrape_webpage()

    with open("us-stations.txt") as f:
        for line in f:
            us_stations.append(line.rstrip('\n'))

    for i in range(start_year, end_year+1):
        file_name = download(i - 1901)
        directory = untar(file_name)


        for filename in os.listdir(directory[:-1]):
            files_total = files_total + 1
            f = os.path.join(directory, filename)
            if filename in us_stations:
                clean_data(f)
                files_retained = files_retained + 1

            os.remove(f)

    # Storing provenance
    with open("provenance.txt", "wb") as f:
        f.writelines("Total files downloaded : " + str(files_total) + "\n")
        f.writelines("Files retained : " + str(files_retained) + "\n")
        f.writelines("Total mandatory columns : " + str(mand_col_total) + "\n")
        f.writelines("Mandatory columns retained : " + str(mand_col_retained) + "\n")

    #shutil.rmtree("zips/")


if __name__ == "__main__":
    main(sys.argv[1:])
