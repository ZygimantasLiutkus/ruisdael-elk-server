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
        location = new Location("c", "n", "e");
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
    void getCoordinates_ReturnsString() {
        assertEquals("c", location.getCoordinates());
    }

    @Test
    void getName_ReturnsString() {
        assertEquals("n", location.getName());
    }

    @Test
    void getElevation_ReturnsString() {
        assertEquals("e", location.getElevation());
    }

    @Test
    void setCoordinates_Test() {
        location.setCoordinates("coordinates");

        assertEquals("coordinates", location.getCoordinates());
    }

    @Test
    void setName_Test() {
        location.setName("name");

        assertEquals("name", location.getName());
    }

    @Test
    void setElevation_Test() {
        location.setElevation("elevation");

        assertEquals("elevation", location.getElevation());
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
        Location location1 = new Location("c", "n", "e");

        assertTrue(location.equals(location1));
    }

    @Test
    void hashCode_ReturnsInt() {
        assertEquals(Objects.hash("c", "n", "e"), location.hashCode());
    }

    @Test
    void toString_ReturnsString() {
        assertEquals("Location{coordinates=c, name=n, elevation=e}", location.toString());
    }
}