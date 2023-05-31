package tudelft.ewi.cse2000.ruisdael.monitoring.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import tudelft.ewi.cse2000.ruisdael.monitoring.entity.Device;
import tudelft.ewi.cse2000.ruisdael.monitoring.service.ElasticsearchService;


@Controller
public class DeviceController {

    /**
     * An instance of the ElasticsearchService bean.
     */
    @Autowired
    private ElasticsearchService elasticsearchService;

    /**
     * Handler for the /overview page on the dashboard.
     *
     * @return See Thymeleaf overview template.
     */
    @GetMapping("/overview")
    public String getOverview(Model model) {
        List<Device> devices = new ArrayList<>();
        List<String> locations = (Arrays.asList("Rotterdam", "Delft", "Den Haag", "Amsterdam", "Eindhoven", "Leiden",
                "Utrecht"));

        for (int i = 0; i < 45; i++) {
            Device d = new Device();
            d.setName("Device " + (i + 1));
            d.setLocation(locations.get(new Random().nextInt(locations.size())));
            d.setOnline(true);
            if (i % 4 == 0) {
                d.setOnline(false);
            }
            devices.add(d);
        }

        model.addAttribute("devices", devices);
        model.addAttribute("hits", elasticsearchService.getDistinctIndexNames());
        return "demo/overview";
    }

}
