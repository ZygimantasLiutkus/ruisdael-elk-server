package tudelft.ewi.cse2000.ruisdael.monitoring;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RuisdaelMonitoringWebuiApplicationTests {

    /**
     * Empty test to verify the testing environment can load.
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
