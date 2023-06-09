package tudelft.ewi.cse2000.ruisdael.monitoring.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;
import tudelft.ewi.cse2000.ruisdael.monitoring.entity.Bandwidth;
import tudelft.ewi.cse2000.ruisdael.monitoring.entity.Device;
import tudelft.ewi.cse2000.ruisdael.monitoring.entity.Instrument;
import tudelft.ewi.cse2000.ruisdael.monitoring.entity.Location;
import tudelft.ewi.cse2000.ruisdael.monitoring.entity.Ram;
import tudelft.ewi.cse2000.ruisdael.monitoring.entity.Status;
import tudelft.ewi.cse2000.ruisdael.monitoring.entity.Storage;

@Component
public class DeviceDataConverter {

    private static final String parseErrorMessage = "Provided values contain invalid or missing data.";

    /**
     * Parses a map of values from elastic to create a {@link Device} object with said data.
     *
     * @param name  - Name of the Node
     * @param status - Whether the node is Online, in a Warning state or Offline
     * @param values - ElasticSearch provided set of values
     * @throws IllegalArgumentException If the map of values from ElasticSearch contains invalid or missing data.
     */
    public static Device createDeviceFromElasticData(String name, Status status, Map values) throws IllegalArgumentException {
        try {
            //Identifiers and Metadata
            String timestamp = values.get("@timestamp").toString();
            Instrument instrument = extractInstrumentData(values);
            Location location = extractLocationData(values);

            //Metrics
            Storage storage = extractStorageData(values);
            Ram ram = extractRamData(values);
            double cpu = Double.parseDouble(values.get("CPU").toString());
            Bandwidth bandwidth = extractBandwithData(values);

            //Custom Metrics
            Map<String, String> data = extractCustomData(values);

            return new Device(name, instrument, location, status, storage, ram, cpu, bandwidth, timestamp, data);
        } catch (Exception e) {
            throw new IllegalArgumentException("Unable to convert data");
        }
    }

    /**
     * Extract the instrument values from the elastic provided value map.
     * @return {@link Instrument}
     * @throws IllegalArgumentException On invalid or missing data.
     */
    public static Instrument extractInstrumentData(Map values) throws IllegalArgumentException {
        try {
            String instrumentName = values.get("instrument.name").toString();
            String instrumentType = values.get("instrument.type").toString();

            return new Instrument(instrumentName, instrumentType);
        } catch (Exception e) {
            throw new IllegalArgumentException(parseErrorMessage, e.getCause());
        }
    }

    /**
     * Extract the location values from the elastic provided value map.
     * @return {@link Location}
     * @throws IllegalArgumentException On invalid or missing data.
     */
    public static Location extractLocationData(Map values) throws IllegalArgumentException {
        try {
            ArrayList<Double> location = (ArrayList<Double>) values.get("location.coordinates");
            String elevation = values.get("location.elevation").toString();
            String locationName = values.get("location.name").toString();

            return new Location(location.get(0), location.get(1), elevation, locationName);
        } catch (Exception e) {
            throw new IllegalArgumentException(parseErrorMessage, e.getCause());
        }
    }

    /**
     * Extract the storage values from the elastic provided value map.
     * @return {@link Storage}
     * @throws IllegalArgumentException On invalid or missing data.
     */
    public static Storage extractStorageData(Map values) throws IllegalArgumentException {
        try {
            long storageTotal = Long.parseLong(values.get("storage.total").toString());
            long storageUsedBytes = Long.parseLong(values.get("storage.used.bytes").toString());
            long storageFree = Long.parseLong(values.get("storage.free").toString());
            double storageUsedPerc = Double.parseDouble(values.get("storage.used.perc").toString());

            return new Storage(storageTotal, storageFree, storageUsedPerc, storageUsedBytes);
        } catch (Exception e) {
            throw new IllegalArgumentException(parseErrorMessage, e.getCause());
        }
    }

    /**
     * Extract the memory values from the elastic provided value map.
     * @return {@link Ram}
     * @throws IllegalArgumentException On invalid or missing data.
     */
    public static Ram extractRamData(Map values) throws IllegalArgumentException {
        try {
            long ramTotal = Long.parseLong(values.get("RAM.total").toString());
            long ramAvailable = Long.parseLong(values.get("RAM.available").toString());
            double ramUsedPerc = Double.parseDouble(values.get("RAM.used.perc").toString());
            long ramUsedBytes = Long.parseLong(values.get("RAM.used.bytes").toString());
            long ramFree = Long.parseLong(values.get("RAM.free").toString());

            return new Ram(ramTotal, ramAvailable, ramFree, ramUsedPerc, ramUsedBytes);
        } catch (Exception e) {
            throw new IllegalArgumentException(parseErrorMessage, e.getCause());
        }
    }

    /**
     * Extract the network values from the elastic provided value map.
     * @return {@link Bandwidth}
     * @throws IllegalArgumentException On invalid or missing data.
     */
    public static Bandwidth extractBandwithData(Map values) throws IllegalArgumentException {
        try {
            long uploadSize = Long.parseLong(values.get("upload.size").toString());
            long downloadSize = Long.parseLong(values.get("download.size").toString());
            double uploadSpeed = Double.parseDouble(values.get("upload.speed").toString());
            double downloadSpeed = Double.parseDouble(values.get("download.speed").toString());

            return new Bandwidth(uploadSize, downloadSize, uploadSpeed, downloadSpeed);
        } catch (Exception e) {
            throw new IllegalArgumentException(parseErrorMessage, e.getCause());
        }
    }

    /**
     * Extract the custom values from the elastic provided value map.
     * All keys prefixed with "custom." will be included in the returned map, without the prefix.
     * @return A Map of the custom fields, all parsed as strings.
     * @throws IllegalArgumentException On invalid data.
     */
    public static Map<String, String> extractCustomData(Map values) throws IllegalArgumentException {
        try {
            Map<String, String> data = new HashMap<>();

            values.keySet().stream()
                    .filter(key -> key.toString().startsWith("custom."))
                    .forEach(key -> {
                        data.put(key.toString().replaceFirst("custom.", ""),
                                String.valueOf(values.get(key)));
                    });

            return data;
        } catch (Exception e) {
            throw new IllegalArgumentException("Provided values contain invalid data.", e.getCause());
        }
    }
}
