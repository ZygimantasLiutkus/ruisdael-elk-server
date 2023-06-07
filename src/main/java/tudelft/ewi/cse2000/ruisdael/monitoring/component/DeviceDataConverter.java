package tudelft.ewi.cse2000.ruisdael.monitoring.component;

import java.util.ArrayList;
import java.util.Map;
import org.springframework.stereotype.Component;
import tudelft.ewi.cse2000.ruisdael.monitoring.entity.Device;
import tudelft.ewi.cse2000.ruisdael.monitoring.entity.Status;

@Component
public class DeviceDataConverter {

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

            // Client added data:
            String givenName = values.get("instrument.name").toString();
            String type = values.get("instrument.type").toString();
            String locationName = values.get("location.name").toString();
            String elevation = values.get("location.elevation").toString();

            return new Device(name, givenName, type, status, storageTotal, storageFree, storageUsedPerc, storageUsedBytes,
                    ramTotal, ramAvailable, ramFree, ramUsedPerc, ramUsedBytes, cpu, uploadSize, downloadSize,
                    uploadSpeed, downloadSpeed, locationAsString, locationName, elevation, timestamp);
        } catch (Exception e) {
            throw new IllegalArgumentException("Unable to convert data");
        }
    }
}
