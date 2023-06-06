package tudelft.ewi.cse2000.ruisdael.monitoring.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class RamTest {

    private Ram ram;

    @BeforeEach
    void setup() {
        ram = new Ram(4.0, 2.0, 1.0, 4.0, 5.0);
    }

    @Test
    void noArgsConstructor_ReturnsRam() {
        Ram r = new Ram();

        assertThat(r).isNotNull();
    }

    @Test
    void allArgsConstructor_ReturnsRam() {
        assertThat(ram).isNotNull();
    }

    @Test
    void getTotal_ReturnsDouble() {
        assertEquals(4.0, ram.getTotal());
    }

    @Test
    void getAvailable_ReturnsDouble() {
        assertEquals(2.0, ram.getAvailable());
    }

    @Test
    void getFree_ReturnsDouble() {
        assertEquals(1.0, ram.getFree());
    }

    @Test
    void getUsedPerc_ReturnsDouble() {
        assertEquals(4.0, ram.getUsedPerc());
    }

    @Test
    void getUsedBytes_ReturnsDouble() {
        assertEquals(5.0, ram.getUsedBytes());
    }

    @Test
    void setTotal_Test() {
        ram.setTotal(6.0);

        assertEquals(6.0, ram.getTotal());
    }

    @Test
    void setAvailable_Test() {
        ram.setAvailable(7.0);

        assertEquals(7.0, ram.getAvailable());
    }

    @Test
    void setFree_Test() {
        ram.setFree(8.0);

        assertEquals(8.0, ram.getFree());
    }

    @Test
    void setUsedPerc_Test() {
        ram.setUsedPerc(9.0);

        assertEquals(9.0, ram.getUsedPerc());
    }

    @Test
    void setUsedBytes_Test() {
        ram.setUsedBytes(10.0);

        assertEquals(10.0, ram.getUsedBytes());
    }

    @Test
    void getUsedPercentage_ReturnsDouble() {
        assertEquals(50.0, ram.getUsedPercentage());
    }

    @Test
    void getAvailablePercentage_ReturnsDouble() {
        assertEquals(50.0, ram.getAvailablePercentage());
    }

    @Test
    void getFreePercentage_ReturnsDouble() {
        assertEquals(25.0, ram.getFreePercentage());
    }

    @Test
    void equals_True_SameObjects() {
        assertTrue(ram.equals(ram));
    }

    @Test
    @SuppressWarnings("PMD.EqualsNull")
    void equals_False_Null() {
        assertFalse(ram.equals(null));
    }

    @Test
    void equals_False_DifferentObject() {
        assertFalse(ram.equals("ram"));
    }

    @Test
    void equals_True_DifferentRam() {
        Ram ram1 = new Ram(4.0, 2.0, 0.0, 0.0, 0.0);

        assertTrue(ram.equals(ram1));
    }

    @Test
    void hashCode_ReturnsInt() {
        assertEquals(Objects.hash(4.0, 2.0), ram.hashCode());
    }

    @Test
    void toString_ReturnsString() {
        assertEquals("Ram{total=4.0, available=2.0, free=1.0}", ram.toString());
    }
}