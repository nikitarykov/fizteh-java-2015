package ru.fizteh.fivt.students.nikitarykov.moduletests.library;

import org.json.JSONObject;
import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Nikita Rykov on 29.11.2015.
 */
public class GeoNavigator {

    public String searchByIP(URL url) throws IOException {
        Scanner scanner = new Scanner(url.openStream());
        String response = scanner.useDelimiter("\\Z").next();
        scanner.close();
        JSONObject json = new JSONObject(response);
        return json.getString("city");
    }

    public Location searchByAddress(URL url) throws IOException {
        Scanner scanner = new Scanner(url.openStream());
        String response = scanner.useDelimiter("\\Z").next();
        scanner.close();
        JSONObject json = new JSONObject(response);
        JSONObject geometry = json.getJSONArray("results").getJSONObject(0).getJSONObject("geometry");
        JSONObject bounds = geometry.getJSONObject("bounds");
        JSONObject location = geometry.getJSONObject("location");
        JSONObject northeast = bounds.getJSONObject("northeast");
        JSONObject southwest = bounds.getJSONObject("southwest");
        double lat = location.getDouble("lat");
        double lng = location.getDouble("lng");
        double northeastLat = northeast.getDouble("lat");
        double northeastLng = northeast.getDouble("lng");
        double southwestLat = southwest.getDouble("lat");
        double southwestLng = southwest.getDouble("lng");
        return new Location(lat, lng, northeastLat, northeastLng,
                southwestLat, southwestLng);
    }
}
