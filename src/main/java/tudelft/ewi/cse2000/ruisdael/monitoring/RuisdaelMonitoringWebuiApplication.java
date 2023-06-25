package tudelft.ewi.cse2000.ruisdael.monitoring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main entrance method for the Spring Application.
 */
@SpringBootApplication
@EnableScheduling
public class RuisdaelMonitoringWebuiApplication {

    public static void main(String[] args) {
        SpringApplication.run(RuisdaelMonitoringWebuiApplication.class, args);
    }
}
