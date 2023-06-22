package tudelft.ewi.cse2000.ruisdael.monitoring;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RuisdaelMonitoringWebuiApplicationTests {

    /**
     * Empty test to verify the testing environment can load.
     * If this test fails: The H2 database is in use!
     * Check if the application or any other process that accesses the H2 database is running while you are attempting to test.
     * These should be stoppped / shut down.
     */
    @Test
    void contextLoads() {
    }

    /**
     * Appease the JaCoCo overlords.
     */
    @Test
    void mainMethodRuns() {
        RuisdaelMonitoringWebuiApplication.main(new String[] {});
    }
}
