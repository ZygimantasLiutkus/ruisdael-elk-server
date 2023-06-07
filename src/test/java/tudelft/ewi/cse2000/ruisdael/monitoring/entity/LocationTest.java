package tudelft.ewi.cse2000.ruisdael.monitoring.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class LocationTest {

    private Location location;

    @BeforeEach
    void setup() {
        location = new Location(1.0, 2.0, "e", "n");
    }

    @Test
    void noArgsConstructor_ReturnsLocation() {
        Location l = new Location();

        assertThat(l).isNotNull();
    }

    @Test
    void allArgsConstructor_ReturnsLocation() {
        assertThat(location).isNotNull();
    }

    @Test
    void setGet_Longitude_Test() {
        location.setLongitude(3.0);

        assertEquals(3.0, location.getLongitude());
    }

    @Test
    void setGet_Latitude_Test() {
        location.setLatitude(4.0);

        assertEquals(4.0, location.getLatitude());
    }

    @Test
    void setGet_Name_Test() {
        location.setName("name");

        assertEquals("name", location.getName());
    }

    @Test
    void setGet_Elevation_Test() {
        location.setElevation("elevation");

        assertEquals("elevation", location.getElevation());
    }

    @Test
    void getHumanReadableCoordinates_ReturnsString() {
        assertEquals("1.00, 2.00", location.getHumanreadableCoordinates());
    }

    @Test
    void equals_True_SameObjects() {
        assertTrue(location.equals(location));
    }

    @Test
    @SuppressWarnings("PMD.EqualsNull")
    void equals_False_Null() {
        assertFalse(location.equals(null));
    }

    @Test
    void equals_False_DifferentClass() {
        assertFalse(location.equals("location"));
    }

    @Test
    void equals_True_DifferentLocations() {
        Location location1 = new Location(1.0, 2.0, "e", "n");

        assertTrue(location.equals(location1));
    }

    @Test
    void equals_False_DifferentLocations() {
        Location location1 = new Location(2.0, 2.0, "e", "n");
        assertFalse(location.equals(location1));

        Location location2 = new Location(1.0, 1.0, "e", "n");
        assertFalse(location.equals(location2));

        Location location3 = new Location(1.0, 2.0, "elevation", "n");
        assertFalse(location.equals(location3));

        Location location4 = new Location(1.0, 2.0, "e", "name");
        assertFalse(location.equals(location4));
    }

    @Test
    void hashCode_ReturnsInt() {
        assertEquals(Objects.hash(1.0, 2.0, "n", "e"), location.hashCode());
    }

    @Test
    void toString_ReturnsString() {
        assertEquals("Location{longitude=1.0, latitude=2.0, name=n, elevation=e}",
                location.toString());
    }
}