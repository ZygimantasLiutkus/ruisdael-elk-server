package tudelft.ewi.cse2000.ruisdael.monitoring.controller;

import static java.util.Map.entry;

import java.util.List;
import java.util.Map;

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

    private static final Map<String, String> METRIC_MAPPING = Map.ofEntries(
            entry("Status", "Status"),
            entry("RAM.total", "Total RAM"),
            entry("RAM.available", "Available RAM"),
            entry("RAM.used.perc", "Used RAM (%)"),
            entry("RAM.used.bytes", "Used RAM (B)"),
            entry("RAM.free", "Free RAM"),
            entry("storage.total", "Total Storage"),
            entry("storage.used.bytes", "Used Storage (B)"),
            entry("storage.free", "Free Storage"),
            entry("storage.used.perc", "Used Storage (%)"),
            entry("CPU", "CPU"),
            entry("upload.size", "Upload Size"),
            entry("download.size", "Download Size"),
            entry("upload.speed", "Upload Speed"),
            entry("download.speed", "Download Speed"),
            entry("@timestamp", "Latest Timestamp"),
            entry("location.coordinates", "Location Coordinates"),
            entry("location.elevation", "Location Elevation"),
            entry("instrument.name", "Instrument Name"),
            entry("location.name", "Location Name"),
            entry("instrument.type", "Instrument Type"));

    /**
     * Handler for the /overview page on the dashboard.
     *
     * @return See Thymeleaf overview template.
     */
    @GetMapping("/overview")
    public String getOverview(Model model) {
        /* Use for Production */
        model.addAttribute("devices", elasticsearchService.getAllDevices());

        List<String> metrics = elasticsearchService.getMetricTypes().stream()
                .map(METRIC_MAPPING::get)
                .toList();

        model.addAttribute("metrics", metrics);
        model.addAttribute("hits", elasticsearchService.getDistinctIndexNames());

        /* For testing purposes
        List<Device> devices = new ArrayList<>();
        List<String> metrics = METRIC_MAPPING.values().stream().toList();
        List<String> locations = (Arrays.asList("Rotterdam", "Delft", "Den Haag", "Amsterdam", "Eindhoven", "Leiden",
                "Utrecht"));

        for (int i = 0; i < 45; i++) {
            Device d = new Device();
            d.setName("Device " + (i + 1));
            Location location = new Location();
            location.setName(locations.get(new Random().nextInt(locations.size())));
            d.setLocation(location);
            d.setStatus(Status.ONLINE);
            if (i % 4 == 0) {
                d.setStatus(Status.OFFLINE);
            } else if (i % 3 == 0) {
                d.setStatus(Status.WARNING);
            }
            devices.add(d);
        }

        model.addAttribute("devices", devices);
        model.addAttribute("metrics", metrics); */
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

    /**
     * This method is used to send data to the Device Controller page.
     *
     * @param model - The model through which the data is fed to the Thymeleaf template.
     * @return See Thymeleaf device-list template.
     */
    @GetMapping("/device-list")
    public String getDeviceList(Model model) {
        /* Use for Production */
        model.addAttribute("devices", elasticsearchService.getAllDevices());

        /* For Testing Purposes
        List<Device> devices = new ArrayList<>();
        List<String> locations = (Arrays.asList("Rotterdam", "Delft", "Den Haag", "Amsterdam", "Eindhoven", "Leiden",
                "Utrecht"));

        for (int i = 0; i < 45; i++) {
            Device d = new Device();
            d.setName("Device " + (i + 1));
            Location location = new Location();
            location.setName(locations.get(new Random().nextInt(locations.size())));
            d.setLocation(location);
            d.setStatus(Status.ONLINE);
            if (i % 4 == 0) {
                d.setStatus(Status.OFFLINE);
            } else if (i % 3 == 0) {
                d.setStatus(Status.WARNING);
            }
            devices.add(d);
        }
        model.addAttribute("devices", devices); */

        return "device-list";
    }

}
