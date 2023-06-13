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

        /* For testing purposes
        List<Device> devices = new ArrayList<>();
        List<String> metrics = METRIC_MAPPING.values().stream().toList();
        List<String> locations = (Arrays.asList("Rotterdam", "Delft", "Den Haag", "Amsterdam", "Eindhoven", "Leiden",
                "Utrecht"));
        List<Status> statuses = new ArrayList<>(Arrays.asList(Status.ONLINE, Status.WARNING, Status.OFFLINE));
        Location location = new Location(1.0, 2.0, "le", "ln");
        Storage storage = new Storage(0.0, 0.0, 0.0, 0.0);
        Ram ram = new Ram(0.0, 0.0, 0.0, 0.0, 0.0);
        Bandwidth bandwidth = new Bandwidth(0.0, 0.0, 0.0, 0.0);

        for (int i = 0; i < 25; i++) {
            location.setName(locations.get(new Random().nextInt(locations.size())));
            Instrument instrument = new Instrument("instrument" + (i + 1), "it");
            Status s = statuses.get(new Random().nextInt(statuses.size()));
            devices.add(new Device("device" + (i + 1), instrument, location, s, storage, ram,
                    1.0, bandwidth, "t", null));
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

        /* For testing purposes
        List<Device> devices = new ArrayList<>();
        List<String> metrics = METRIC_MAPPING.values().stream().toList();
        List<String> locations = (Arrays.asList("Rotterdam", "Delft", "Den Haag", "Amsterdam", "Eindhoven", "Leiden",
                "Utrecht"));

        Location location = new Location(1.0, 2.0, "le", "ln");
        Storage storage = new Storage(0.0, 0.0, 0.0, 0.0);
        Ram ram = new Ram(0.0, 0.0, 0.0, 0.0, 0.0);
        Bandwidth bandwidth = new Bandwidth(0.0, 0.0, 0.0, 0.0);

        for (int i = 0; i < 25; i++) {
            location.setName(locations.get(new Random().nextInt(locations.size())));
            Instrument instrument = new Instrument("instrument" + (i + 1), "it");
            devices.add(new Device("device" + (i + 1), instrument, location, Status.ONLINE, storage, ram,
                    1.0, bandwidth, "t", null));
        }

        model.addAttribute("devices", devices);
        model.addAttribute("metrics", metrics); */

        return "device-list";
    }

}
