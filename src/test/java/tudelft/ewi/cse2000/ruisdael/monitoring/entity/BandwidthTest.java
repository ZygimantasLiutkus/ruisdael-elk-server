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
    void setGet_UploadSize_Test() {
        bandwidth.setUploadSize(5.0);

        assertEquals(5.0, bandwidth.getUploadSize());
    }

    @Test
    void setGet_DownloadSize_Test() {
        bandwidth.setDownloadSize(6.0);

        assertEquals(6.0, bandwidth.getDownloadSize());
    }

    @Test
    void setGet_UploadSpeed_Test() {
        bandwidth.setUploadSpeed(7.0);

        assertEquals(7.0, bandwidth.getUploadSpeed());
    }

    @Test
    void setGet_DownloadSpeed_Test() {
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
    void equals_False_DifferentBandwidth() {
        Bandwidth bandwidth1 = new Bandwidth(0.0, 2.0, 3.0, 4.0);
        assertFalse(bandwidth.equals(bandwidth1));

        Bandwidth bandwidth2 = new Bandwidth(1.0, 1.0, 3.0, 4.0);
        assertFalse(bandwidth.equals(bandwidth2));

        Bandwidth bandwidth3 = new Bandwidth(1.0, 2.0, 2.0, 4.0);
        assertFalse(bandwidth.equals(bandwidth3));

        Bandwidth bandwidth4 = new Bandwidth(1.0, 2.0, 3.0, 3.0);
        assertFalse(bandwidth.equals(bandwidth4));
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