package tudelft.ewi.cse2000.ruisdael.monitoring.device;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Structure to contain information about the equipment a node is monitoring.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Instrument {

    private String name;
    private String type;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Instrument that = (Instrument) o;

        if (!name.equals(that.name)) {
            return false;
        }
        return type.equals(that.type);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + type.hashCode();
        return result;
    }
}
