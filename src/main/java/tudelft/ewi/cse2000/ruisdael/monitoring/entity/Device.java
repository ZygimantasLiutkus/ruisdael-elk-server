package tudelft.ewi.cse2000.ruisdael.monitoring.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.text.DecimalFormat;
import java.util.Objects;
import lombok.Getter;
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
 *     1. Currently the information that the device contains could be atomized by using other entities. Such an example
 *     would be creating an entity for the RAM and Storage usage, as well as for the Upload/Download speeds.
 *     2. Another thing that should be noted is the format in which we will store the timestamp.
 *     3. The combination of name + location should be unique for each device.
 * </p>
 *
 */
@Getter
@Setter
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
     *         'location': [loc.longitude, loc.latitude]
     *        }
     * The Device Entity should resemble it.
     */
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private boolean online;
    private double totalStorage;
    private double availableStorage;
    private double totalRam;
    private double availableRam;
    private double cpuUsage;
    private double uploadSize;
    private double downloadSize;
    private double uploadSpeed;
    private double downloadSpeed;
    private String location;
    private String timestamp;

    public Device() {

    }

    /**
     *  All argument constructor.
     *
     * @param name - Name of the device.
     * @param online - Status of the device, if the device is online then it is true, otherwise false.
     * @param totalStorage - The total memory storage the device has, in bytes.
     * @param availableStorage - The memory storage which is available, in bytes.
     * @param totalRam - The total RAM of the device, in bytes.
     * @param availableRam - The RAM being used, in bytes.
     * @param cpuUsage - The current CPU usage of the device, represented as a percentage.
     * @param uploadSize - The size of the information the device is sending to the server, represented as bytes.
     * @param downloadSize - The size of the information the device is downloading, represented as bytes.
     * @param uploadSpeed - The upload speed of the device in terms of bytes.
     * @param downloadSpeed - The download speed of the device in terms of bytes.
     * @param location - The location of the device. All the names of locations are provided by Ruisdael.
     */
    public Device(String name, boolean online, double totalStorage, double availableStorage, double totalRam,
                  double availableRam, double cpuUsage, double uploadSize, double downloadSize, double uploadSpeed,
                  double downloadSpeed, String location, String timestamp) {
        this.name = name;
        this.online = online;
        this.totalStorage = totalStorage;
        this.availableStorage = availableStorage;
        this.totalRam = totalRam;
        this.availableRam = availableRam;
        this.cpuUsage = cpuUsage;
        this.uploadSize = uploadSize;
        this.downloadSize = downloadSize;
        this.uploadSpeed = uploadSpeed;
        this.downloadSpeed = downloadSpeed;
        this.location = location;
        this.timestamp = timestamp;
    }

    /**
     * Constructor used for passing a device to the front-end.
     */
    public Device(boolean online, String name, String location, double totalStorage, double availableStorage,
                  double totalRam, double availableRam) {
        this.online = online;
        this.name = name;
        this.location = location;
        this.totalStorage = totalStorage;
        this.availableStorage = availableStorage;
        this.totalRam = totalRam;
        this.availableRam = availableRam;
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

    /**This method should be checked, I assume that the name of the.
     *
     * @param o - Object representing another device.
     * @return true, iff the two Device objects have all the same attributes.
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
        return online == device.online && Double.compare(device.totalStorage, totalStorage) == 0
                && Double.compare(device.availableStorage, availableStorage) == 0
                && Double.compare(device.totalRam, totalRam) == 0
                && Double.compare(device.availableRam, availableRam) == 0
                && Double.compare(device.cpuUsage, cpuUsage) == 0
                && Double.compare(device.uploadSize, uploadSize) == 0
                && Double.compare(device.downloadSize, downloadSize) == 0
                && Double.compare(device.uploadSpeed, uploadSpeed) == 0
                && Double.compare(device.downloadSpeed, downloadSpeed) == 0
                && Objects.equals(device.location, location)
                && Objects.equals(id, device.id)
                && Objects.equals(name, device.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, online, totalStorage, availableStorage, totalRam, availableRam, cpuUsage,
                uploadSize, downloadSize, uploadSpeed, downloadSpeed, location);
    }

    @Override
    public String toString() {
        return "The device " + name + " located at " + location  + "is currently " + (online ? "online" : "offline")
                + ". Its performance information is as follows: "
                + "\ntotal storage= " + totalStorage
                + "\navailable storage= " + availableStorage
                + "\ntotal RAM= " + totalRam
                + "\navailable RAM= " + availableRam
                + "\ncpu usage= " + cpuUsage
                + "\nupload size= " + uploadSize
                + "\ndownload size= " + downloadSize
                + "\nupload speed= " + uploadSpeed
                + "\ndownload speed= " + downloadSpeed;
    }
}
