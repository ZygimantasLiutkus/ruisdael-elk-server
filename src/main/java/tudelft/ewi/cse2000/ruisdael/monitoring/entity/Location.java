package tudelft.ewi.cse2000.ruisdael.monitoring.entity;

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
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Location location = (Location) o;

        if (Double.compare(location.longitude, longitude) != 0) {
            return false;
        }
        if (Double.compare(location.latitude, latitude) != 0) {
            return false;
        }
        if (!elevation.equals(location.elevation)) {
            return false;
        }
        return name.equals(location.name);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(longitude);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(latitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + elevation.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }
}
