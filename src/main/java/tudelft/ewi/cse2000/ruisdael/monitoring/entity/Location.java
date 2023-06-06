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

    private String coordinates;
    private String name;
    private String elevation;

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || o.getClass() != getClass()) {
            return false;
        }
        Location location = (Location) o;
        return location.coordinates.equals(this.coordinates) && location.name.equals(this.name)
                && location.elevation.equals(this.elevation);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(coordinates, name, elevation);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "Location{"
                + "coordinates=" + coordinates
                + ", name=" + name
                + ", elevation=" + elevation
                + "}";
    }
}
