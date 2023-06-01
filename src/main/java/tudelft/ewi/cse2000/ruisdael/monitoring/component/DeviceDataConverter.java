package tudelft.ewi.cse2000.ruisdael.monitoring.component;

import java.util.ArrayList;
import java.util.Map;
import org.springframework.stereotype.Component;
import tudelft.ewi.cse2000.ruisdael.monitoring.entity.Device;

@Component
public class DeviceDataConverter {

    /**
     * Parses a map of values from elastic to create a {@link Device} object with said data.
     *
     * @param name Name of the Node
     * @param online Whether the node is online
     * @param values ElasticSearch provided set of values
     * @throws IllegalArgumentException If the map of values from ElasticSearch contains invalid or missing data.
     */
    public static Device createDeviceFromElasticData(String name, boolean online, Map values) throws IllegalArgumentException {
        try {
            long ramTotal = Long.parseLong(values.get("RAM.total").toString());
            long ramAvailable = Long.parseLong(values.get("RAM.available").toString());
            double ramUsedPerc = Double.parseDouble(values.get("RAM.used.perc").toString());
            long ramUsedBytes = Long.parseLong(values.get("RAM.used.bytes").toString());
            long ramFree = Long.parseLong(values.get("RAM.free").toString());

            long storageTotal = Long.parseLong(values.get("storage.total").toString());
            long storageUsedBytes = Long.parseLong(values.get("storage.used.bytes").toString());
            long storageFree = Long.parseLong(values.get("storage.free").toString());
            double storageUsedPerc = Double.parseDouble(values.get("storage.used.perc").toString());

            double cpu = Double.parseDouble(values.get("CPU").toString());

            long uploadSize = Long.parseLong(values.get("upload.size").toString());
            long downloadSize = Long.parseLong(values.get("download.size").toString());
            double uploadSpeed = Double.parseDouble(values.get("upload.speed").toString());
            double downloadSpeed = Double.parseDouble(values.get("download.speed").toString());

            String timestamp = values.get("@timestamp").toString();
            //FIXME location includes more data than just this now. Implement ASAP!
            ArrayList<Double> location = (ArrayList<Double>) values.get("location.coordinates");
            String locationAsString = String.format("%4.2f, %4.2f", location.get(0), location.get(1));

            return new Device(name, online, storageTotal, storageFree, ramTotal, ramAvailable, ramFree, cpu,
                    uploadSize, downloadSize, uploadSpeed, downloadSpeed, locationAsString, timestamp);
        } catch (Exception e) {
            throw new IllegalArgumentException("Unable to convert data");
        }
    }
}
