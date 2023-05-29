package tudelft.ewi.cse2000.ruisdael.monitoring.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class Bandwidth {

    private double uploadSize;
    private double downloadSize;
    private double uploadSpeed;
    private double downloadSpeed;

    public Bandwidth(double uploadSize, double downloadSize, double uploadSpeed, double downloadSpeed) {
        this.uploadSize = uploadSize;
        this.downloadSize = downloadSize;
        this.uploadSpeed = uploadSpeed;
        this.downloadSpeed = downloadSpeed;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bandwidth bandwidth = (Bandwidth) o;
        return Double.compare(bandwidth.uploadSize, uploadSize) == 0
                && Double.compare(bandwidth.downloadSize, downloadSize) == 0
                && Double.compare(bandwidth.uploadSpeed, uploadSpeed) == 0
                && Double.compare(bandwidth.downloadSpeed, downloadSpeed) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(uploadSize, downloadSize, uploadSpeed, downloadSpeed);
    }

    @Override
    public String toString() {
        return "Bandwidth{" +
                "uploadSize=" + uploadSize +
                ", downloadSize=" + downloadSize +
                ", uploadSpeed=" + uploadSpeed +
                ", downloadSpeed=" + downloadSpeed +
                '}';
    }
}
