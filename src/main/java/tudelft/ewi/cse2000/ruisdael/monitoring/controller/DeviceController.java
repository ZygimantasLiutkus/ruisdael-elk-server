package tudelft.ewi.cse2000.ruisdael.monitoring.controller;

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
        //Use for Production
        model.addAttribute("devices", elasticsearchService.getAllDevices());

        /* For testing purposes
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
        */
        return "overview";
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
