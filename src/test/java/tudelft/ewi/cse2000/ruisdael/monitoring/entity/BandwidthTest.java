package tudelft.ewi.cse2000.ruisdael.monitoring.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BandwidthTest {

    private Bandwidth bandwidth;

    @BeforeEach
    void setup() {
        bandwidth = new Bandwidth(1.0, 2.0, 3.0, 4.0);
    }

    @Test
    void noArgsConstructor_ReturnsBandwidth() {
        Bandwidth b = new Bandwidth();

        assertThat(b).isNotNull();
    }

    @Test
    void allArgsConstructor_ReturnsBandwidth() {
        assertThat(bandwidth).isNotNull();
    }

    @Test
    void getUploadSize_ReturnsDouble() {
        double us = bandwidth.getUploadSize();

        assertEquals(1.0, us);
    }

    @Test
    void getDownloadSize_ReturnsDouble() {
        double ds = bandwidth.getDownloadSize();

        assertEquals(2.0, ds);
    }

    @Test
    void getUploadSpeed_ReturnsDouble() {
        double us = bandwidth.getUploadSpeed();

        assertEquals(3.0, us);
    }

    @Test
    void getDownloadSpeed_ReturnsDouble() {
        double ds = bandwidth.getDownloadSpeed();

        assertEquals(ds, 4.0);
    }

    @Test
    void setUploadSize_Test() {
        bandwidth.setUploadSize(5.0);

        assertEquals(5.0, bandwidth.getUploadSize());
    }

    @Test
    void setDownloadSize_Test() {
        bandwidth.setDownloadSize(6.0);

        assertEquals(6.0, bandwidth.getDownloadSize());
    }

    @Test
    void setUploadSpeed_Test() {
        bandwidth.setUploadSpeed(7.0);

        assertEquals(7.0, bandwidth.getUploadSpeed());
    }

    @Test
    void setDownloadSpeed_Test() {
        bandwidth.setDownloadSpeed(8.0);

        assertEquals(8.0, bandwidth.getDownloadSpeed());
    }

    @Test
    void equals_True_SameObjects() {
        assertTrue(bandwidth.equals(bandwidth));
    }

    @Test
    @SuppressWarnings("PMD.EqualsNull")
    void equals_False_Null() {
        assertFalse(bandwidth.equals(null));
    }

    @Test
    void equals_False_NotBandwidth() {
        assertFalse(bandwidth.equals("String"));
    }

    @Test
    void equals_True_DifferentBandwidth() {
        Bandwidth other = new Bandwidth(1.0, 2.0, 3.0, 4.0);

        assertTrue(bandwidth.equals(other));
    }

    @Test
    void hashCode_ReturnsInt() {
        assertThat(bandwidth.hashCode()).isEqualTo(Objects.hash(1.0, 2.0, 3.0, 4.0));
    }

    @Test
    void toString_ReturnsString() {
        assertEquals("Bandwidth{uploadSize=1.0, downloadSize=2.0, uploadSpeed=3.0, downloadSpeed=4.0}",
                bandwidth.toString());
    }
}