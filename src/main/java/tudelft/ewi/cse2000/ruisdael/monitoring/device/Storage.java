package tudelft.ewi.cse2000.ruisdael.monitoring.device;

import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Structure to contain information about a node's storage metrics.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Storage {

    private double totalStorage;
    private double freeStorage;
    private double usedPercStorage;
    private double usedBytesStorage;

    /**
     * Equals method used to check if an instance of the Storage class is the same as a passed object.
     * @param o - Instance of an Object class to be compared with a given instance of a Storage class.
     * @return true, iff, the second instance is of the Storage class and contains the same amount of total and free
     *              storage as the instance of the Storage class with which it is being compared to.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Storage storage = (Storage) o;
        return Double.compare(storage.totalStorage, totalStorage) == 0
                && Double.compare(storage.freeStorage, freeStorage) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(totalStorage, freeStorage);
    }

    /**
     * Method used to represent the Storage class in a human-readable format.
     * @return string representation of the Storage class instance.
     */
    @Override
    public String toString() {
        return "Storage{"
                + "totalStorage=" + totalStorage
                + ", freeStorage=" + freeStorage
                + '}';
    }
}
