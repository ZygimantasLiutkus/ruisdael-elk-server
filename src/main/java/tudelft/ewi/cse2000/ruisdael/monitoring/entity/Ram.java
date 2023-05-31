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
public class Ram {
    /**
     * RAM.total': ram[0],  # B(ytes)
     * RAM.available': ram[1],  # B
     * RAM.used.perc': ram[2],  # %
     * RAM.used.bytes': ram[3],  # B
     * RAM.free': ram[4],  # B  // What is the difference between this and available
     */

    private double total;   // Represents the total RAM the device has
    private double available;   // Represents the readily available RAM
    private double free;    // Represents the RAM which is free, but not immediately available.


    /**
     * Used to represent the RAM used as a percentage.
     * @return double representing the used RAM as a percentage.
     */
    public double getUsedPercentage() {
        return ((total - available) / total) * 100.0;
    }

    /**
     * Used to represent the available RAM as a percentage.
     * @return double representing the available RAM as a percentage.
     */
    public double getAvailablePercentage() {
        return (available / total) * 100.0;
    }

    /**
     * Used to represent free RAM as a percentage.
     * @return double representing free RAM.
     */
    public double getFreePercentage() {
        return (free / total) * 100.0;
    }

    /**
     * Used to compare an instance of the RAM class with another Object instance.
     * @param o - Object instance to be compared to.
     * @return true, iff, the Object instance is also an instance of the RAM class, and its attributes are equal to the
     *              instance it is being compared to.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Ram ram = (Ram) o;
        return Double.compare(ram.total, total) == 0 && Double.compare(ram.available, available) == 0;
    }

    /**
     * Used to create a hash code for the RAM instance.
     * @return integer representing the instances hash code.
     */
    @Override
    public int hashCode() {
        return Objects.hash(total, available);
    }

    /**
     * Represents the RAM instance in a human-readable format.
     * @return string representing the RAM instance.
     */
    @Override
    public String toString() {
        return "Ram{"
                + "total=" + total
                + ", available=" + available
                + ", free=" + free
                + '}';
    }
}
