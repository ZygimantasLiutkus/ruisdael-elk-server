package tudelft.ewi.cse2000.ruisdael.monitoring.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String deviceName;
    private String metric;
    private String oldFlag;
    private String newFlag;
    private LocalDateTime timeStamp;

    /**
     * Used to compare an instance of the Alert class to another object instance.
     * @param o - Object instance to be compared to.
     * @return true, iff, the Object passed is an instance of the Alert class, with the same alert.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Alert alert = (Alert) o;
        return alert.deviceName.equals(deviceName) 
                && alert.metric.equals(metric)
                && alert.oldFlag.equals(oldFlag) 
                && alert.newFlag.equals(newFlag)
                && alert.timeStamp.equals(timeStamp);
    }

    /**
     * This method creates the hash code for each instance of the Alert class, using all the classes attributes.
     * @return integer representing the hash code of the Alert instance.
     */
    @Override
    public int hashCode() {
        return Objects.hash(deviceName);
    }

    /**
     * Used to represent the Alert instances in a human-readable format.
     * @return string representation of the Alert instance.
     */
    @Override
    public String toString() {
        return "Alert{"
                + "deviceName=" + deviceName
                + ", metric=" + metric
                + ", oldFlag=" + oldFlag
                + ", newFlag=" + newFlag
                + ", timeStamp=" + timeStamp
                + '}';
    }

}
