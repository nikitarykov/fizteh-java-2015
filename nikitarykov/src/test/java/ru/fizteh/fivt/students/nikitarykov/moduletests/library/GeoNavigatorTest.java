package ru.fizteh.fivt.students.nikitarykov.moduletests.library;

import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.net.URLStreamHandler;
import java.nio.charset.StandardCharsets;

import static ru.fizteh.fivt.students.nikitarykov.moduletests.library.Constants.URL_FOR_IP_NAVIGATION;
import static ru.fizteh.fivt.students.nikitarykov.moduletests.library.Constants.URL_FOR_GOOGLE_MAPS;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

/**
 * Created by Nikita Rykov on 04.12.2015.
 */
public class GeoNavigatorTest {

    @Test
    public void testSearchByIP() throws Exception {
        final URLConnection mockUrlConnection = mock(URLConnection.class);
        String result = "{\"country\":\"RU\"," +
                "\"loc\":\"55.9041,37.5606\"," +
                "\"hostname\":\"No Hostname\"," +
                "\"city\":\"Dolgoprudnyy\"," +
                "\"org\":\"AS5467 Non state educational institution Educational " +
                "Scientific and Experimental Center of Moscow Institute of Physics and Technology\"," +
                "\"ip\":\"93.175.2.122\"," +
                "\"postal\":\"141704\"," +
                "\"region\":\"Moscow Oblast\"}";
        InputStream input = new ByteArrayInputStream(result.getBytes(StandardCharsets.UTF_8));
        doReturn(input).when(mockUrlConnection).getInputStream();

        URLStreamHandler stubUrlHandler = new URLStreamHandler() {
            @Override
            protected URLConnection openConnection(URL u) throws IOException {
                return mockUrlConnection;
            }
        };
        URL mockUrl = new URL(null, URL_FOR_IP_NAVIGATION, stubUrlHandler);
        GeoNavigator navigator = new GeoNavigator();
        String city = navigator.searchByIP(mockUrl);
        assertEquals(city, "Dolgoprudnyy");
    }

    @Test
    public void testSearchByAddress() throws Exception {
        final URLConnection mockUrlConnection = mock(URLConnection.class);
        String result = "{\"results\":" +
                "[{\"formatted_address\":\"Moscow, Russia\"," +
                "\"types\":[\"locality\",\"political\"]," +
                "\"geometry\":" +
                "{\"viewport\":" +
                "{\"southwest\":{\"lng\":37.3193288,\"lat\":55.48992699999999}," +
                "\"northeast\":{\"lng\":37.9456611,\"lat\":56.009657}}," +
                "\"bounds\":" +
                "{\"southwest\":{\"lng\":37.3193288,\"lat\":55.48992699999999}," +
                "\"northeast\":{\"lng\":37.9456611,\"lat\":56.009657}}," +
                "\"location\":{\"lng\":37.6173,\"lat\":55.755826}," +
                "\"location_type\":\"APPROXIMATE\"}," +
                "\"address_components\":" +
                "[{\"types\":[\"locality\",\"political\"]," +
                "\"short_name\":\"Moscow\",\"long_name\":\"Moscow\"}," +
                "{\"types\":[\"administrative_area_level_2\", \"political\"]," +
                "\"short_name\":\"g. Moskva\", \"long_name\":\"gorod Moskva\"}," +
                "{\"types\":[\"administrative_area_level_1\",\"political\"]," +
                "\"short_name\":\"Moscow\",\"long_name\":\"Moscow\"}," +
                "{\"types\":[\"country\",\"political\"]," +
                "\"short_name\":\"RU\",\"long_name\":\"Russia\"}]," +
                "\"place_id\":\"ChIJybDUc_xKtUYRTM9XV8zWRD0\"}],\"status\":\"OK\"}";
        InputStream input = new ByteArrayInputStream(result.getBytes(StandardCharsets.UTF_8));
        doReturn(input).when(mockUrlConnection).getInputStream();
        URLStreamHandler stubUrlHandler = new URLStreamHandler() {
            @Override
            protected URLConnection openConnection(URL u) throws IOException {
                return mockUrlConnection;
            }
        };
        URL mockUrl = new URL(null, URL_FOR_GOOGLE_MAPS +  + '?' + "address=" +
                URLEncoder.encode("Москва", "utf-8") + "&sensor=false", stubUrlHandler);
        GeoNavigator navigator = new GeoNavigator();
        Location location = navigator.searchByAddress(mockUrl);
        assertEquals(location.getLatitude(), 55.755826, 0.00001);
        assertEquals(location.getLongitude(), 37.6173, 0.00001);
        assertEquals(location.getSouthwestLat(), 55.48992699999999, 0.00001);
        assertEquals(location.getSouthwestLng(), 37.3193288, 0.00001);
        assertEquals(location.getNortheastLat(), 56.009657, 0.00001);
        assertEquals(location.getNortheastLng(), 37.9456611, 0.00001);
    }
}

