package tudelft.ewi.cse2000.ruisdael.monitoring.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import tudelft.ewi.cse2000.ruisdael.monitoring.configurations.ApplicationConfig;
import tudelft.ewi.cse2000.ruisdael.monitoring.entity.Alert;
import tudelft.ewi.cse2000.ruisdael.monitoring.repositories.AlertRepository;

@Controller
public class AlertController {

    @Autowired
    private AlertRepository alertRepository;

    /**
     * Handler for the /alerts pages on the dashboard.
     * This is the response to the first request.
     * @return The list of {@link Alert} 
     */
    @GetMapping("/alerts")
    public String getAlerts(Model model) {
        model.addAttribute("websocketDelay", ApplicationConfig.websocketDelay);
        List<Alert> alerts = alertRepository.findAll();
        Collections.sort(alerts, (b, a) -> a.getTimeStamp().compareTo(b.getTimeStamp()));
        model.addAttribute("alerts", alerts);
        return "alerts";
    }

    /**
     * Handler for the /alerts pages on the dashboard.
     * This is the response to each subsequent websocketRequest.
     * @return The list of {@link Alert}s 
     */
    @MessageMapping("/alerts")
    @SendTo("/topic/alerts")
    public List<Alert> updateAlerts() {
        List<Alert> alerts = alertRepository.findAll();
        Collections.sort(alerts, (b, a) -> a.getTimeStamp().compareTo(b.getTimeStamp()));
        return alerts;
    }

    /**
     * Helper Function to be called in DeviceController for each individual device.
     * @param nodeName  - Name of the Node
     * @return a list of {@link Alert}s 
     * */
    public List<Alert> getNodeAlerts(String nodeName) {
        List<Alert> deviceAlerts = alertRepository.findAll();
        deviceAlerts.removeIf(a -> !a.getDeviceName().equals(nodeName));
        return deviceAlerts;
    }
}
