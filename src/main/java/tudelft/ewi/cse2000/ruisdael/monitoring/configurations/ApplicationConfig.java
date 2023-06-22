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

    // GitLab Configuration

    /**
     * Repository base URL to link GitLab issues to.
     */
    public static final String gitlabURL = "https://gitlab.tudelft.nl/ruisdael/ruisdael-monitoring";

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
