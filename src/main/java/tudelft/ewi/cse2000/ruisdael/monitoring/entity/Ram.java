package tudelft.ewi.cse2000.ruisdael.monitoring.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.Objects;

public class Ram {
    /**
     * RAM.total': ram[0],  # B(ytes)
     * RAM.available': ram[1],  # B
     * RAM.used.perc': ram[2],  # %
     * RAM.used.bytes': ram[3],  # B
     * RAM.free': ram[4],  # B  // What is the difference between this and available
     */
    private double total;
    private double available;

    public Ram() {}

    public Ram(double total, double available) {
        this.total = total;
        this.available = available;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getAvailable() {
        return available;
    }

    public void setAvailable(double used) {
        this.available = used;
    }

    public double getUsedPercentage() {
        return (total - available) / 100.0;
    }

    public double getAvailablePercentage() {
        return available / 100.0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ram ram = (Ram) o;
        return Double.compare(ram.total, total) == 0 && Double.compare(ram.available, available) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(total, available);
    }

}
