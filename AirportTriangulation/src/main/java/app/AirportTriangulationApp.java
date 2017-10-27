package app;

import args.AirportTriangulationAppArgs;
import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import com.beust.jcommander.JCommander;
import model.Flight;
import model.Weather;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AirportTriangulationApp {
    public static void main(String... args) throws IOException {
        AirportTriangulationAppArgs airportTriangulationAppArgs = new AirportTriangulationAppArgs();
        new JCommander(airportTriangulationAppArgs, args);

        String weatherSource = airportTriangulationAppArgs.getWeatherSource();
        String flightSource = airportTriangulationAppArgs.getFlightSource();
        String outputFile = airportTriangulationAppArgs.getOutput();

        CSVReader weatherReader = new CSVReader(new FileReader(weatherSource), ',');
        CSVReader flightReader = new CSVReader(new FileReader(flightSource), ',');

        List<Weather> weatherList = new ArrayList<Weather>();
        List<Flight> flightList = new ArrayList<Flight>();
        String[] record = null;

        while((record = weatherReader.readNext()) != null) {
            long stationId = Long.parseLong(record[0]);
            double lat = Double.parseDouble(record[3]);
            double lon = Double.parseDouble(record[4]);
            weatherList.add(new Weather(stationId, lat, lon));
        }

        record = null;
        while((record = flightReader.readNext()) != null) {
            int id = Integer.parseInt(record[0]);
            String code = record[1];
            double lat = Double.parseDouble(record[2]);
            double lon = Double.parseDouble(record[3]);
            flightList.add(new Flight(id, code, lat, lon));
        }

        for(Flight flight : flightList) {
            double min = Double.MAX_VALUE;
            long weatherStationId = 0;
            for(int i = 0; i < weatherList.size(); i++) {
                double dist = distance(flight.getLat(),
                        weatherList.get(i).getLat(),
                        flight.getLon(),
                        weatherList.get(i).getLon(),
                        0,0);
                if(dist < min && dist < 51) {
                    min = dist;
                    weatherStationId = weatherList.get(i).getStationId();
                }
            }
            flight.setNearestWeatherStationId(weatherStationId);
        }

        FileWriter fileWriter = new FileWriter(outputFile);
        CSVWriter csvWriter = new CSVWriter(fileWriter, ',');
        Iterator<Flight> iterator = flightList.iterator();

        List<String[]> records = new ArrayList<String[]>();
        while(iterator.hasNext()) {
            records.add(new String[] {iterator.next().getCode(), ""+iterator.next().getNearestWeatherStationId()});
        }
        csvWriter.writeAll(records);
        csvWriter.close();
    }

    public static double distance(double lat1, double lat2, double lon1, double lon2, double el1, double el2) {
        final int R = 6371;
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000;
        double height = el1 - el2;
        distance = Math.pow(distance, 2) + Math.pow(height, 2);
        return Math.sqrt(distance);
    }
}
