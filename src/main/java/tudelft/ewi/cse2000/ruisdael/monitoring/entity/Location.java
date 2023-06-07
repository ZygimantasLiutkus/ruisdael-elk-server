package tudelft.ewi.cse2000.ruisdael.monitoring.entity;

import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Location {

    private double longitude;
    private double latitude;
    private String elevation;
    private String name;

    public String getHumanreadableCoordinates() {
        return String.format("%4.2f, %4.2f", longitude, latitude);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || o.getClass() != getClass()) {
            return false;
        }
        Location location = (Location) o;
        return location.longitude == this.longitude && location.latitude == this.latitude
                && location.name.equals(this.name) && location.elevation.equals(this.elevation);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(longitude, latitude, name, elevation);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "Location{"
                + "longitude=" + longitude
                + ", latitude=" + latitude
                + ", name=" + name
                + ", elevation=" + elevation
                + "}";
    }
}
