package tudelft.ewi.cse2000.ruisdael.monitoring.entity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * Author: Dean Polimac.
 * Date:23/05/2023
 * <p>
 *     This class contains all the attributes of a instrument in the Ruisdael network. The attributes of the class
 *     represent all the data which needs to be monitored at each instrument in the network. The data collection
 *     instruments in the Ruisdael network, which are to be monitored, can be referred to as "device", "node", or
 *     "instrument".
 * </p>
 * Notes:
 * <p>
 *     The combination of name + location should be unique for each device.
 * </p>
 *
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Device {

    public static final DecimalFormat byteFormat = new DecimalFormat("#0.00");

    /**
     * These are all the attributes being sent from the node to the server.
     * data = {'RAM.total': ram[0],  # B(ytes)
     *         'RAM.available': ram[1],  # B
     *         'RAM.used.perc': ram[2],  # %
     *         'RAM.used.bytes': ram[3],  # B
     *         'RAM.free': ram[4],  # B
     *         'storage.total': mainmem[0],  # B
     *         'storage.used.bytes': mainmem[1],  # B
     *         'storage.free': mainmem[2],  # B
     *         'storage.used.perc': mainmem[3],  # %
     *         'CPU': cpu,  # %
     *         'upload.size': sent,  # B
     *         'download.size': rec,  # B
     *         'upload.speed': (sent - self.old_bytes_sent) / self.update_delay,  # B/s
     *         'download.speed': (rec - self.old_bytes_rec) / self.update_delay,  # B/s
     *         '@timestamp': datetime.utcnow().strftime('%Y-%m-%dT%H:%M:%SZ'),
     *         'location.coordinates': [device_details['longitude'], device_details['latitude']],
     *         'location.elevation': device_details['elevation'],   # String
     *         'instrument.name': device_details['instrument_name'],
     *         'location.name': device_details['location'],
     *         'instrument.type': device_details['instrument_type']
     *        }
     * The Device Entity should resemble it.
     */
    //Identifiers
    private String name;
    private Instrument instrument;
    private Location location;

    //Metrics
    private boolean online;
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
    public Device(boolean online, String name, Location location, double totalStorage, double availableStorage,
                  double totalRam, double availableRam, double freeRam) {
        this.online = online;
        this.name = name;
        this.location = location;
        this.storage = new Storage(totalStorage, availableStorage);
        this.ram = new Ram(totalRam, availableRam, freeRam);
    }

    /**
     * Takes the supplied value (a double representing an amount of bytes) and devides it by 10 ^ 9, to get the amount in gigabytes.
     * Then formats the value as XX.XX and returns this string.
     * This method is not marked static to easily access it in Thymeleaf.
     */
    public String getHumanReadableValueGbs(Double byteValue) {
        return byteFormat.format(byteValue / 1000000000);
    }

    /**
     * Takes the supplied value (a double representing an amount of bytes) and devides it by 10 ^ 6, to get the amount in megabytes.
     * Then formats the value as XX.XX and returns this string.
     * This method is not marked static to easily access it in Thymeleaf.
     */
    public String getHumanReadableValueMbs(Double byteValue) {
        return byteFormat.format(byteValue / 1000000);
    }

    /**
     * Takes the supplied value (a double representing an amount of bytes) and devides it by 10 ^ 3, to get the amount in kilobytes.
     * Then formats the value as XX.XX and returns this string.
     * This method is not marked static to easily access it in Thymeleaf.
     */
    public String getHumanReadableValueKbs(Double byteValue) {
        return byteFormat.format(byteValue / 1000);
    }

    /**
     * Method is used to compare a Device instance with another Object.
     * @param o - Instance of an object
     * @return true, iff the o is an instance of Device, and the name and location are the same, otherwise false.
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
        return this.name.equals(device.getName()) && this.name.equals(device.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, online, storage, ram, cpuUsage, bandwidth, location);
    }

    @Override
    @SuppressWarnings("PMD.AvoidDuplicateLiterals") // This is being suppressed since it raises an error due to the use of '\n'
    public String toString() {
        return "Device{"
                + "name=" + name + ",\n"
                + "online=" + online + ",\n"
                + "storage=" + storage + ",\n"
                + "ram=" + ram + ",\n"
                + "cpuUsage=" + cpuUsage + ",\n"
                + "bandwidth=" + bandwidth + ",\n"
                + "location='" + location + ",\n"
                + '}';
    }
}
