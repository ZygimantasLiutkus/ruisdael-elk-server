package tudelft.ewi.cse2000.ruisdael.monitoring.entity;

import java.util.Objects;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Bandwidth {

    private double uploadSize;
    private double downloadSize;
    private double uploadSpeed;
    private double downloadSpeed;

    /**
     * Constructor for the Bandwidth class used to represent the upload/download speed and size of devices.
     * @param uploadSize - Upload size.
     * @param downloadSize - Download size.
     * @param uploadSpeed - Upload speed of device
     * @param downloadSpeed - Download speed of device.
     */
    public Bandwidth(double uploadSize, double downloadSize, double uploadSpeed, double downloadSpeed) {
        this.uploadSize = uploadSize;
        this.downloadSize = downloadSize;
        this.uploadSpeed = uploadSpeed;
        this.downloadSpeed = downloadSpeed;
    }


    /**
     * Used to compare an instance of the Bandwidth class to another object instance.
     * @param o - Object instance to be compared to.
     * @return true, iff, the Object passed is an instance of the Bandwidth class, with the same upload/download speed
     *              and size.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Bandwidth bandwidth = (Bandwidth) o;
        return Double.compare(bandwidth.uploadSize, uploadSize) == 0
                && Double.compare(bandwidth.downloadSize, downloadSize) == 0
                && Double.compare(bandwidth.uploadSpeed, uploadSpeed) == 0
                && Double.compare(bandwidth.downloadSpeed, downloadSpeed) == 0;
    }

    /**
     * This method creates the hash code for each instance of the Bandwidth class, using all the classes attributes.
     * @return integer representing the hash code of the Bandwidth instance.
     */
    @Override
    public int hashCode() {
        return Objects.hash(uploadSize, downloadSize, uploadSpeed, downloadSpeed);
    }

    /**
     * Used to represent the Bandwidth instances in a human-readable format.
     * @return string representation of the Bandwidth instance.
     */
    @Override
    public String toString() {
        return "Bandwidth{"
                + "uploadSize=" + uploadSize
                + ", downloadSize=" + downloadSize
                + ", uploadSpeed=" + uploadSpeed
                + ", downloadSpeed=" + downloadSpeed
                + '}';
    }
}
