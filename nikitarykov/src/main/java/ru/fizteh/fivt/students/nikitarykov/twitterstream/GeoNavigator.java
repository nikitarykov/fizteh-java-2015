package ru.fizteh.fivt.students.nikitarykov.twitterstream;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import org.json.*;
import twitter4j.TwitterException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import java.util.Scanner;

/**
 * Created by Nikita Rykov on 29.11.2015.
 */
public class GeoNavigator {
    public Location search(String address) throws TwitterException {
        if (address.equals("nearby")) {
            try {
                URL url = new URL("http://ipinfo.io/json");
                Scanner scanner = new Scanner(url.openStream());
                String response = scanner.useDelimiter("\\Z").next();
                scanner.close();
                JSONObject json = new JSONObject(response);
                address = json.getString("city");
            } catch (MalformedURLException exception) {
                System.err.println("Can't detect url.");
                throw new TwitterException("Error in connection");
            } catch (IOException exception) {
                System.err.println("Can't open stream.");
                throw new TwitterException("Error in connection");
            }
        }
        try {
            InputStream input = getClass().getResourceAsStream("/googlemaps.properties");
            Properties property = new Properties();
            property.load(input);
            String key = property.getProperty("key");
            GeoApiContext context = new GeoApiContext().setApiKey(key);
            GeocodingResult[] results = GeocodingApi.geocode(context, address).await();
            if (results.length == 0) {
                System.out.println("No place found.");
                return null;
            }
            return new Location(results[0].geometry.location.lat,
                    results[0].geometry.location.lng,
                    results[0].geometry.bounds.northeast.lat,
                    results[0].geometry.bounds.northeast.lng,
                    results[0].geometry.bounds.southwest.lat,
                    results[0].geometry.bounds.southwest.lng);
        } catch (Exception exception) {
            System.err.println("Unsuccessful geocoding.");
            throw new TwitterException("Error in connection");
        }
    }
}
