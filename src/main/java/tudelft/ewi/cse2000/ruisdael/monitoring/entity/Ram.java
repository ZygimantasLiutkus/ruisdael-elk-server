package tudelft.ewi.cse2000.ruisdael.monitoring.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;

import java.util.Objects;

@Getter
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

    public Ram() {

    }

    public Ram(double total, double available, double free) {
        this.total = total;
        this.available = available;
        this.free = free;
    }


    public double getUsedPercentage() {
        return ((total - available) / available) * 100.0;
    }

    public double getAvailablePercentage() {
        return (available / total) * 100.0;
    }

    public double getFreePercentage() {
        return (free / total) * 100;
    }

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

    @Override
    public int hashCode() {
        return Objects.hash(total, available);
    }

}
