package tudelft.ewi.cse2000.ruisdael.monitoring.controller;

import java.util.Arrays;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import tudelft.ewi.cse2000.ruisdael.monitoring.entity.Device;

@Controller
public class DeviceController {

    /**
     * Handler for the /overview page on the dashboard.
     *
     * @return See Thymeleaf overview template.
     */
    @GetMapping("/overview")
    public String getOverview(Model model) {
        model.addAttribute("devices", Arrays.asList(
                new Device(true, "Dev1", "Amsterdam", 2, 1, 4, 1),
                new Device(false, "Dev2", "Den Haag", 2, 1, 2, 0),
                new Device(true, "Dev3", "Eindhoven",16, 8, 8, 5),
                new Device(false, "Dev4", "Tilburg", 128, 54, 16, 5),
                new Device(true, "Dev5", "Rotterdam", 64, 32, 8, 3),
                new Device(false, "Dev6", "Groeningen",256, 86, 16, 4)
        ));
        return "demo/overview";
    }

}
