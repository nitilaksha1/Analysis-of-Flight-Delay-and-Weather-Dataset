package model;

public class Flight {
    private String code;
    private double lat;
    private double lon;
    private long nearestWeatherStationId;
    private double distanceToNearestWeatherStation;

    public Flight(String code, double lat, double lon) {
        this.code = code;
        this.lat = lat;
        this.lon = lon;
    }

    public void setNearestWeatherStationId(long nearestWeatherStationId) {
        this.nearestWeatherStationId = nearestWeatherStationId;
    }

    public void setDistanceToNearestWeatherStation(double distanceToNearestWeatherStation) {
        this.distanceToNearestWeatherStation = distanceToNearestWeatherStation;
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

    public double getDistanceToNearestWeatherStation() {
        return distanceToNearestWeatherStation;
    }
}
