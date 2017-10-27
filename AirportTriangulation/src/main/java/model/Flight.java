package model;

public class Flight {
    private int id;
    private String code;
    private double lat;
    private double lon;
    private long nearestWeatherStationId;

    public Flight(int id, String code, double lat, double lon) {
        this.id = id;
        this.code = code;
        this.lat = lat;
        this.lon = lon;
    }

    public void setNearestWeatherStationId(long nearestWeatherStationId) {
        this.nearestWeatherStationId = nearestWeatherStationId;
    }

    public int getId() {

        return id;
    }

    public String getCode() {
        return code;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public long getNearestWeatherStationId() {
        return nearestWeatherStationId;
    }
}
