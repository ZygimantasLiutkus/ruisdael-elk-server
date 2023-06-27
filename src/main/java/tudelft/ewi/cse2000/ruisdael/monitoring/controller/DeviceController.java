package tudelft.ewi.cse2000.ruisdael.monitoring.controller;

import static java.util.Map.entry;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import tudelft.ewi.cse2000.ruisdael.monitoring.configurations.ApplicationConfig;
import tudelft.ewi.cse2000.ruisdael.monitoring.device.Device;
import tudelft.ewi.cse2000.ruisdael.monitoring.entity.Alert;
import tudelft.ewi.cse2000.ruisdael.monitoring.entity.Index;
import tudelft.ewi.cse2000.ruisdael.monitoring.repositories.IndexRepository;
import tudelft.ewi.cse2000.ruisdael.monitoring.service.ElasticsearchService;

/**
 * Controller class that controls all endpoints relating to nodes.
 */
@Controller
public class DeviceController {

    /**
     * An instance of the ElasticsearchService bean.
     */
    @Autowired
    private ElasticsearchService elasticsearchService;

    @Autowired
    private IndexRepository indexRepository;

    @Autowired
    private AlertController alertController;

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
        model.addAttribute("websocketDelay", ApplicationConfig.websocketDelay);
        model.addAllAttributes(Map.of(
            "CPU_CRITICAL_THRESHOLD", ApplicationConfig.CPU_CRITICAL_THRESHOLD,
                "CPU_WARNING_THRESHOLD", ApplicationConfig.CPU_WARNING_THRESHOLD,
                "RAM_CRITICAL_THRESHOLD", ApplicationConfig.RAM_CRITICAL_THRESHOLD,
                "RAM_WARNING_THRESHOLD", ApplicationConfig.RAM_WARNING_THRESHOLD,
                "STORAGE_CRITICAL_THRESHOLD", ApplicationConfig.STORAGE_CRITICAL_THRESHOLD,
                "STORAGE_WARNING_THRESHOLD", ApplicationConfig.STORAGE_WARNING_THRESHOLD
        ));

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

        // For alert table
        model.addAttribute("websocketDelay", ApplicationConfig.websocketDelay);
        model.addAttribute("gitlabURL", ApplicationConfig.gitlabURL);
        List<Alert> deviceAlerts = alertController.getNodeAlerts(nodeIndex);
        Collections.sort(deviceAlerts, (b, a) -> a.getTimeStamp().compareTo(b.getTimeStamp()));
        model.addAttribute("deviceAlerts", deviceAlerts);


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
        model.addAttribute("websocketDelay", ApplicationConfig.websocketDelay);

        List<String> metrics = elasticsearchService.getMetricTypes().stream()
                .map(METRIC_MAPPING::get)
                .toList();

        model.addAttribute("metrics", metrics);

        /* For testing purposes
        List<Device> devices = new ArrayList<>();
        List<Status> statuses = new ArrayList<>(Arrays.asList(Status.ONLINE, Status.WARNING, Status.OFFLINE));
        List<String> locations = (Arrays.asList("Rotterdam", "Delft", "Den Haag", "Amsterdam", "Eindhoven", "Leiden",
                "Utrecht"));

        Location location = new Location(1.0, 2.0, "le", "ln");
        Storage storage = new Storage(0.0, 0.0, 0.0, 0.0);
        Ram ram = new Ram(0.0, 0.0, 0.0, 0.0, 0.0);
        Bandwidth bandwidth = new Bandwidth(0.0, 0.0, 0.0, 0.0);

        for (int i = 0; i < 25; i++) {
            location.setName(locations.get(new Random().nextInt(locations.size())));
            Instrument instrument = new Instrument("instrument" + (i + 1), "it");
            devices.add(new Device("device" + (i + 1), instrument, location,
                    statuses.get(new Random().nextInt(statuses.size())), storage, ram,1.0, bandwidth,
                    "t", null));
        }

        model.addAttribute("devices", devices); */

        return "device-list";
    }

    /**
     * Returns a list of devices from the elasticsearch service.
     *
     * @return a response entity with a list of devices from Elasticsearch.
     */
//    @MessageMapping("/devices") // /app/devices
//    @SendTo("/topic/devices")
    @GetMapping("/device-update")
    public ResponseEntity<List<Device>> updateDevices() {

        /* For testing purposes
        List<Device> devices = new ArrayList<>();
        List<String> metrics = METRIC_MAPPING.values().stream().toList();
        List<Status> statuses = new ArrayList<>(Arrays.asList(Status.ONLINE, Status.WARNING, Status.OFFLINE));
        List<String> locations = (Arrays.asList("Rotterdam", "Delft", "Den Haag", "Amsterdam", "Eindhoven", "Leiden",
                "Utrecht"));

        Location location = new Location(1.0, 2.0, "le", "ln");
        Storage storage = new Storage(0.0, 0.0, 0.0, 0.0);
        Ram ram = new Ram(0.0, 0.0, 0.0, 0.0, 0.0);
        Bandwidth bandwidth = new Bandwidth(0.0, 0.0, 0.0, 0.0);

        for (int i = 0; i < 25; i++) {
            location.setName(locations.get(new Random().nextInt(locations.size())));
            Instrument instrument = new Instrument("instrument" + (i + 1), "it");
            devices.add(new Device("device" + (i + 1), instrument, location,
                    statuses.get(new Random().nextInt(statuses.size())), storage, ram,1.0, bandwidth,
                    "t", null));
        }
        return ResponseEntity.ok(devices) */

        return ResponseEntity.ok(elasticsearchService.getAllDevices());
    }

    /**
     * Deletes an index from Elasticsearch.
     *
     * @param index the index to be deleted.
     * @return boolean of success.
     */
    @GetMapping("/delete/{index}")
    public ResponseEntity<Boolean> deleteIndex(@PathVariable("index") String index) {
        return ResponseEntity.ok(elasticsearchService.deleteIndex(index).acknowledged());
    }

    /**
     * Disables a node by adding it to the disabled node local repository if the node is not already disabled.
     *
     * @param index the index to be disabled.
     * @return boolean of success.
     */
    @GetMapping("/disable/{index}")
    public ResponseEntity<Boolean> disableIndex(@PathVariable("index") String index) {
        if (indexRepository.existsByIndexValue(index)) {
            return ResponseEntity.ok(false);
        } else {
            indexRepository.saveAndFlush(new Index(0L, index));
            return ResponseEntity.ok(true);
        }
    }

    /**
     * Enables a node by removing it from the disabled node local repository, if the node is found.
     *
     * @param index the index to be enabled.
     * @return boolean of success.
     */
    @GetMapping("/enable/{index}")
    public ResponseEntity<Boolean> enableIndex(@PathVariable("index") String index) {
        if (indexRepository.existsByIndexValue(index)) {
            indexRepository.delete(indexRepository.findByIndexValue(index));
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.ok(false);
        }
    }
}
