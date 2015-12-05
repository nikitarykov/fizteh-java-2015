package ru.fizteh.fivt.students.nikitarykov.moduletests.library;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Nikita Rykov on 04.12.2015.
 */
public class LocationTest {
    private Location location;

    @Before
    public void setLocation() {
        location = new Location(55.947064, 37.4992755,
            55.998731, 37.5352053, 55.8968611, 37.46090909999999);
    }

    @Test
    public void testGetters() throws Exception {
        assertEquals(55.947064, location.getLatitude(), 0.00001);
        assertEquals(37.4992755, location.getLongitude(), 0.00001);
        assertEquals(55.998731, location.getNortheastLat(), 0.00001);
        assertEquals(37.5352053, location.getNortheastLng(), 0.00001);
        assertEquals(55.8968611, location.getSouthwestLat(), 0.00001);
        assertEquals(37.46090909999999, location.getSouthwestLng(), 0.00001);
    }

    @Test
    public void testIsInBounds() throws Exception {
        assertTrue(location.isInBounds(55.947064, 37.4992755));
        assertTrue(location.isInBounds(55.95, 37.5));
        assertFalse(location.isInBounds(56.0, 37.4992755));
        assertFalse(location.isInBounds(55.5, 37.4992755));
        assertFalse(location.isInBounds(55.947064, 37.4));
        assertFalse(location.isInBounds(55.947064, 37.6));
        assertFalse(location.isInBounds(56.0, 37.6));
    }

    @Test
    public void testGetRadius() throws Exception {
        assertEquals(6.117792, location.getRadius(), 0.00001);
    }
}