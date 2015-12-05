package ru.fizteh.fivt.students.nikitarykov.moduletests.library;

import static ru.fizteh.fivt.students.nikitarykov.moduletests.library.Constants.RADIUS_OF_THE_EARTH;
/**
 * Created by Nikita Rykov on 29.11.2015.
 */
public class Location {
    private double latitude;
    private double longitude;
    private double northeastLat;
    private double northeastLng;
    private double southwestLat;
    private double southwestLng;

    Location(double latitude, double longitude,
             double northeastLat, double northeastLng,
             double southwestLat, double southwestLng) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.northeastLat = northeastLat;
        this.northeastLng = northeastLng;
        this.southwestLat = southwestLat;
        this.southwestLng = southwestLng;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getNortheastLat() {
        return northeastLat;
    }

    public double getNortheastLng() {
        return northeastLng;
    }

    public double getSouthwestLat() {
        return southwestLat;
    }

    public double getSouthwestLng() {
        return southwestLng;
    }

    public boolean isInBounds(double lat, double lng) {
        if (lat >= Math.min(southwestLat, northeastLat)
            && lng > Math.min(southwestLng, northeastLng)
            && lat <= Math.max(southwestLat, northeastLat)
            && lng <= Math.max(southwestLng, northeastLng)) {
            return true;
        }
        return false;
    }

    public double getRadius() {
        Double latDistance = Math.toRadians(southwestLat - northeastLat);
        Double lonDistance = Math.toRadians(southwestLng - northeastLng);
        Double xCoordinte = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(southwestLat)) * Math.cos(Math.toRadians(northeastLat))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        Double angle = 2 * Math.atan2(Math.sqrt(xCoordinte), Math.sqrt(1 - xCoordinte));
        double radius = RADIUS_OF_THE_EARTH * angle / 2;
        return radius;
    }
}
