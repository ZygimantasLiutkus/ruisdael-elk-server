package tudelft.ewi.cse2000.ruisdael.monitoring.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import tudelft.ewi.cse2000.ruisdael.monitoring.security.persistent.User;
import tudelft.ewi.cse2000.ruisdael.monitoring.security.persistent.UserRepository;

/**
 * Configuration class that sets application-level options.
 */
@Configuration
public class ApplicationConfig {

    @Autowired
    private UserRepository userRepository;

    // UI Configuration

    /**
     * Time that is required to pass before a device status changes to warning or offline in seconds.
     */
    public static final long warningTime = 121L;
    public static final long offlineTime = 301L;

    // Alert Configuration

    /**
     * Percentages for classifying as yellow or red.
     */
    public static final double CPU_CRITICAL_THRESHOLD = 90;
    public static final double CPU_WARNING_THRESHOLD = 80;

    public static final double RAM_CRITICAL_THRESHOLD = 90;
    public static final double RAM_WARNING_THRESHOLD = 80;

    public static final double STORAGE_CRITICAL_THRESHOLD = 90;
    public static final double STORAGE_WARNING_THRESHOLD = 80;

    /**
     * Delay between checking for a change in flags to report alerts on. In milliseconds.
     */
    public static final long FLAGS_CHECK_FREQUENCY = 30 * 1000L; //30 * 1000ms
    public static final int MAX_ALERTS_IN_HISTORY = 200;

    // GitLab Configuration

    /**
     * Repository base URL to link GitLab issues to.
     */
    public static final String gitlabURL = "https://gitlab.tudelft.nl/ruisdael/ruisdael-monitoring";

    // Kibana Host Configuration

    /**
     * This URL should be accessible by clients using the dashboard.
     */
    public static final String kibanaURL = "https://ruisdael-kibana.citg.tudelft.nl/kibana";


    // Websocket Configuration

    /**
     * The delay between updates on the overview and device list page, in milliseconds.
     */
    public static final int websocketDelay = 30000;


    // Account Configuration

    /**
     * Enable or disable the super-user (admin) account on startup.
     */
    public static final boolean enableAdmin = true;

    /**
     * Enable or disable the guest account on startup.
     */
    public static final boolean enableGuest = false;



    // Supporting methods, do not change.

    /**
     * Sets the account status based on configuration specified by {@link ApplicationConfig#enableAdmin} and {@link ApplicationConfig#enableGuest}.
     */
    @EventListener(ContextRefreshedEvent.class)
    public void setAccountStatus() {
        User admin = userRepository.findByUsername("admin").get();
        admin.setEnabled(enableAdmin);
        userRepository.saveAndFlush(admin);

        User guest = userRepository.findByUsername("guest").get();
        guest.setEnabled(enableGuest);
        userRepository.saveAndFlush(guest);
    }
}
