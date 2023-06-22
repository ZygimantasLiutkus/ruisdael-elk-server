package tudelft.ewi.cse2000.ruisdael.monitoring.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tudelft.ewi.cse2000.ruisdael.monitoring.component.DeviceDataConverter;
import tudelft.ewi.cse2000.ruisdael.monitoring.configurations.ApplicationConfig;
import tudelft.ewi.cse2000.ruisdael.monitoring.entity.Device;
import tudelft.ewi.cse2000.ruisdael.monitoring.entity.Status;

/**
 * Service class for Elasticsearch operations.
 */
@Service
public class ElasticsearchService {
    private static final String INDEXPREFIX = "collector_";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final ElasticsearchClient client;

    /**
     * Constructs an instance of ElasticsearchService with the specified Elasticsearch client.
     *
     * @param client The ElasticsearchClient dependency injected automatically by the Spring framework.
     */
    @Autowired
    public ElasticsearchService(ElasticsearchClient client) {
        this.client = client;
    }

    /**
     * Queries elastic to receive the latest data on a given node, and converts this to a @{@link Device}.
     * @param nodeIndexName The name of the node to look up data for.
     * @return A {@link Device} object with the latest data, or null if no such device exists, or no data is available.
     */
    public Device getDeviceDetailsFromName(String nodeIndexName) {
        try {
            SearchResponse<Map> response = client.search(s -> s
                    .index(INDEXPREFIX + nodeIndexName)
                    .sort(new SortOptions.Builder()
                            .field(field -> field.field("@timestamp").order(SortOrder.Desc))
                            .build()), Map.class);

            List<Hit<Map>> allHits = response.hits().hits();

            if (allHits.size() == 0) { //No results
                return null;
            }

            Hit<Map> lastHit = allHits.get(0); //Get latest result
            String deviceName = lastHit.index().replace(INDEXPREFIX, "");
            // The following line is added so that the Status of the node can be determined before creation,
            // the method `getStatus()` is at the bottom of the ElasticsearchService class.
            String timestamp = lastHit.source().get("@timestamp").toString();

            return DeviceDataConverter.createDeviceFromElasticData(deviceName, getStatus(timestamp), lastHit.source());
        } catch (Exception e) { //IOException or IllegalArgumentException
            return null;
        }
    }

    /**
     * Queries elastic to receive data on all nodes, the order of devices will be according to their names.
     * In order to obtain a list of devices with their appropriate statuses, this method should be used.
     * @return A list of all nodes with the latest data in a {@link Device} object.
     */
    public List<Device> getAllDevices() {
        List<Device> distinctIndexNames = new ArrayList<>(getDistinctIndexNames()
                .stream()
                .map(this::getDeviceDetailsFromName)
                .filter(Objects::nonNull)
                .toList());
        distinctIndexNames.sort(Comparator.comparing(Device::getName));
        return distinctIndexNames;
    }

    /**
     * Retrieves a list of distinct index names generated by the monitoring software.
     *
     * @return A list of distinct index names.
     */
    public List<String> getDistinctIndexNames() {
        try {
            List<String> uniqueIndices = new ArrayList<>();

            client.indices().stats().indices().keySet().forEach(index -> {
                if (index.startsWith(INDEXPREFIX)) {
                    uniqueIndices.add(index.replaceFirst(INDEXPREFIX, ""));
                }
            });

            return uniqueIndices;
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    /**
     * Retrieves a list of all metric names sent from the client.
     *
     * @return A list of all metric field names.
     */
    public List<String> getMetricTypes() {
        try {
            Optional<String> optionalIndex = client.indices().stats().indices().keySet()
                    .stream()
                    .filter(i -> i.startsWith(INDEXPREFIX))
                    .findFirst();

            if (optionalIndex.isPresent()) {
                String exampleIndex = optionalIndex.get();

                SearchResponse<Map> response = client.search(s -> s
                        .index(exampleIndex)
                        .sort(build -> build.field(f -> f.field("@timestamp").order(SortOrder.Desc))),
                        Map.class);

                List<Hit<Map>> hits = response.hits().hits();

                if (hits.size() == 0) {
                    return List.of();
                }

                List<String> metrics = new ArrayList<String>(hits.get(0)
                        .source()
                        .keySet()
                        .stream()
                        .toList());
                metrics.add(0, "Status");
                return metrics;
            } else {
                return List.of();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    /**
     * This method takes in a Device instance, and depending on its most recent timestamp, determines the devices
     * status which can either be Online, Warning, or Offline. The status is determined based on the following:
     * |responseTime - currentTime| {@literal <} 2min (in milliseconds) -> Online
     * |responseTime - currentTime| {@literal <} 5min (in milliseconds) -> Warning
     * |responseTime - currentTime| {@literal >} 5min (in milliseconds) -> Offline
     *
     * @param timestamp - Timestamp representing the most recent time of receiving a server request from a Device.
     *                  The format of the timestamp is expected to be "yyyy-MM-dd'T'HH:mm:ss".
     * @return Status of the device, being Online, Warning, or Offline depending on the latest time of receiving a
     *              request from the node, on the Elasticsearch server.
     */
    public Status getStatus(String timestamp) {
        Instant clockInstant = Clock.systemUTC().instant();
        long currentTime = clockInstant.getEpochSecond();

        String time = timestamp.replace("T", " ").replace("Z", "");
        long deviceTime = LocalDateTime.parse(time, formatter).atZone(ZoneId.of("UTC")).toEpochSecond();

        if (Math.abs(currentTime - deviceTime) < ApplicationConfig.warningTime) {
            return Status.ONLINE;
        } else if (Math.abs(currentTime - deviceTime) < ApplicationConfig.offlineTime) {
            // If the device did not send a request to the server in the last 2 minutes, set its status to WARNING
            return Status.WARNING;
        }
        // If the device did not send a request to the server in the last 5 minutes, set its status to OFFLINE
        return Status.OFFLINE;
    }
}
