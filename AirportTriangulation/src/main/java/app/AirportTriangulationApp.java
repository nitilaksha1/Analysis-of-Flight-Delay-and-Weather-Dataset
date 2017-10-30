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
            double lat = Double.parseDouble(record[1]);
            double lon = Double.parseDouble(record[2]);
            weatherList.add(new Weather(stationId, lat, lon));
        }

        record = null;
        while((record = flightReader.readNext()) != null) {
            String code = record[0];
            double lat = Double.parseDouble(record[1]);
            double lon = Double.parseDouble(record[2]);
            flightList.add(new Flight(code, lat, lon));
        }

        for(Flight flight : flightList) {
            double min = Double.MAX_VALUE;
            long weatherStationId = 0;
            for(int i = 0; i < weatherList.size(); i++) {
                double dist = distanceInKmBetweenEarthCoordinates(flight.getLat(),
                        flight.getLon(),
                        weatherList.get(i).getLat(),
                        weatherList.get(i).getLon()
                        );
                if(dist < min && dist < 51) {
                    min = dist;
                    weatherStationId = weatherList.get(i).getStationId();
                }
            }
            flight.setNearestWeatherStationId(weatherStationId);
            flight.setDistanceToNearestWeatherStation(min);
        }

        FileWriter fileWriter = new FileWriter(outputFile);
        CSVWriter csvWriter = new CSVWriter(fileWriter, ',');
        Iterator<Flight> iterator = flightList.iterator();

        List<String[]> records = new ArrayList<String[]>();
        while(iterator.hasNext()) {
            Flight flight = iterator.next();
            if(flight.getDistanceToNearestWeatherStation() != Double.MAX_VALUE) {
                String distance = String.format("%10.2f", flight.getDistanceToNearestWeatherStation());
                records.add(new String[] {flight.getCode(),
                        ""+flight.getNearestWeatherStationId(),
                        distance.trim()});
            }
        }
        csvWriter.writeAll(records);
        csvWriter.close();
    }

    public static double degreesToRadians(double degrees) {
        return degrees * Math.PI / 180;
    }

    public static double distanceInKmBetweenEarthCoordinates(double lat1, double lon1, double lat2, double lon2) {
        double earthRadiusKm = 6371;

        double dLat = degreesToRadians(lat2-lat1);
        double dLon = degreesToRadians(lon2-lon1);

        lat1 = degreesToRadians(lat1);
        lat2 = degreesToRadians(lat2);

        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.sin(dLon/2) * Math.sin(dLon/2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return earthRadiusKm * c;
    }
}
