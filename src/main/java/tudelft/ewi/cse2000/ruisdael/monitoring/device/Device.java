package tudelft.ewi.cse2000.ruisdael.monitoring.device;

import java.text.DecimalFormat;
import java.util.Map;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tudelft.ewi.cse2000.ruisdael.monitoring.device.enums.Status;

/**
 * This class contains all the attributes of a instrument in the Ruisdael network. The attributes of the class
 * represent all the data which needs to be monitored at each instrument in the network. The data collection
 * instruments in the Ruisdael network, which are to be monitored, can be referred to as "device", "node", or
 * "instrument".
 * </p>
 * <p>
 * Note: The combination of name + location should be unique for each device.
 * </p>
 * See also: {@link Bandwidth}, {@link Instrument}, {@link Location}, {@link Ram}, {@link Status}, {@link Storage}
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Device {
    public static final DecimalFormat byteFormat = new DecimalFormat("#0.00");

    //Identifiers
    private String name;
    private Instrument instrument;
    private Location location;

    //Metrics
    private Status status;
    private Storage storage;
    private Ram ram;
    private double cpuUsage;
    private Bandwidth bandwidth;

    //Metadata
    private String timestamp;

    //Custom Metrics
    private Map<String, String> customFields;

    /**
     * Constructor used for passing a device to the front-end.
     */
    public Device(Status status, String name, Location location,
                  double totalStorage, double availableStorage, double usedPercStorage, double usedBytesStorage,
                  double totalRam, double availableRam, double freeRam, double usedPercRam, double usedBytesRam) {
        this.status = status;
        this.name = name;
        this.location = location;
        this.storage = new Storage(totalStorage, availableStorage, usedPercStorage, usedBytesStorage);
        this.ram = new Ram(totalRam, availableRam, freeRam, usedPercRam, usedBytesRam);
    }

    /**
     * Takes the supplied value (a double representing an amount of bytes) and divides it by 10 ^ 9, to get the amount in gigabytes.
     * Then formats the value as XX.XX and returns this string.
     * This method is not marked static to easily access it in Thymeleaf.
     */
    public String getHumanReadableValueGbs(Double byteValue) {
        return byteFormat.format(byteValue / 1000000000);
    }

    /**
     * Takes the supplied value (a double representing an amount of bytes) and divides it by 10 ^ 6, to get the amount in megabytes.
     * Then formats the value as XX.XX and returns this string.
     * This method is not marked static to easily access it in Thymeleaf.
     */
    public String getHumanReadableValueMbs(Double byteValue) {
        return byteFormat.format(byteValue / 1000000);
    }

    /**
     * Takes the supplied value (a double representing an amount of bytes) and divides it by 10 ^ 3, to get the amount in kilobytes.
     * Then formats the value as XX.XX and returns this string.
     * This method is not marked static to easily access it in Thymeleaf.
     */
    public String getHumanReadableValueKbs(Double byteValue) {
        return byteFormat.format(byteValue / 1000);
    }

    /**
     * Method is used to compare a Device instance with another Object.
     * @param o - Instance of an object
     * @return true, iff the o is an instance of Device, and all values are equal, otherwise false.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Device device = (Device) o;

        if (status != device.status) {
            return false;
        }
        if (Double.compare(device.cpuUsage, cpuUsage) != 0) {
            return false;
        }
        if (!Objects.equals(name, device.name)) {
            return false;
        }
        if (!Objects.equals(instrument, device.instrument)) {
            return false;
        }
        if (!Objects.equals(location, device.location)) {
            return false;
        }
        if (!Objects.equals(storage, device.storage)) {
            return false;
        }
        if (!Objects.equals(ram, device.ram)) {
            return false;
        }
        if (!Objects.equals(bandwidth, device.bandwidth)) {
            return false;
        }
        if (!Objects.equals(timestamp, device.timestamp)) {
            return false;
        }
        return Objects.equals(customFields, device.customFields);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, status, storage, ram, cpuUsage, bandwidth, location);
    }

    @Override
    @SuppressWarnings("PMD.AvoidDuplicateLiterals") // This is being suppressed since it raises an error due to the use of '\n'
    public String toString() {
        return "Device{"
                + "name=" + name + ",\n"
                + "status=" + status + ",\n"
                + "storage=" + storage + ",\n"
                + "ram=" + ram + ",\n"
                + "cpuUsage=" + cpuUsage + ",\n"
                + "bandwidth=" + bandwidth + ",\n"
                + "location=" + location + ",\n"
                + '}';
    }
}
