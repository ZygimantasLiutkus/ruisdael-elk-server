package tudelft.ewi.cse2000.ruisdael.monitoring.controller;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
        model.addAttribute("devices", Arrays.asList(
                new Device(true, "Dev1", "Amsterdam", 2, 1, 4, 1),
                new Device(false, "Dev2", "Den Haag", 2, 1, 2, 0),
                new Device(true, "Dev3", "Eindhoven",16, 8, 8, 5),
                new Device(false, "Dev4", "Tilburg", 128, 54, 16, 5),
                new Device(true, "Dev5", "Rotterdam", 64, 32, 8, 3),
                new Device(false, "Dev6", "Groeningen",256, 86, 16, 4)
        ));
        // For testing the output of ElasticsearchService
        model.addAttribute("hits", elasticsearchService.getDistinctIndexNames());
        return "demo/overview";
    }

    /**
     * Handler for the /node/{node_index}/ pages on the dashboard.
     * Elastic will be queried on the latest data specified by the node with the name of {node_index}.
     * If no such node is found, or no data exists, an empty page will be displayed instead.
     */
    @GetMapping("/node/{node_index}")
    public String getNodePage(Model model, @PathVariable("node_index") String nodeIndex) {
        Device lastHitResult = elasticsearchService.getDeviceDetailsFromName(nodeIndex);
        if (lastHitResult == null) {
            model.addAttribute("noData", true);
        } else {
            model.addAttribute("device", lastHitResult);
        }

        return "node";
    }

}
