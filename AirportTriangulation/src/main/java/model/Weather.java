package model;

public class Weather {
    private long stationId;
    private double lat;
    private double lon;

    public Weather(long stationId,
                   double lat,
                   double lon) {
        this.stationId = stationId;
        this.lat = lat;
        this.lon = lon;
    }

    public long getStationId() {
        return stationId;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }
}
