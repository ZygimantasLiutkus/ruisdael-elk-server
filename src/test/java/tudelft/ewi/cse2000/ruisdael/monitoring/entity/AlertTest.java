package tudelft.ewi.cse2000.ruisdael.monitoring.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class AlertTest {

    private Alert alertUnderTest;

    @BeforeEach
    void setUp() {
        alertUnderTest = new Alert(0L, "deviceName", "metric", "oldFlag", "newFlag",
                LocalDateTime.of(2020, 1, 1, 0, 0, 0));
    }

    @Test
    void testEquals() {
        assertEquals(new Alert(0L, "deviceName", "metric", "oldFlag", "newFlag",
                LocalDateTime.of(2020, 1, 1, 0, 0, 0)), alertUnderTest);
        assertEquals(alertUnderTest, alertUnderTest);
        assertFalse(alertUnderTest.equals(null));
        assertFalse(alertUnderTest.equals(new Object()));
    }

    @Test
    void testHashCode() {
        assertEquals(780988960, alertUnderTest.hashCode());
    }

    @Test
    void testToString() {
        assertEquals("Alert{deviceName=deviceName, metric=metric, oldFlag=oldFlag, newFlag=newFlag, timeStamp=2020-01-01T00:00}",
                alertUnderTest.toString());
    }
}
