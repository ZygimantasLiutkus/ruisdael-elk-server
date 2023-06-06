package tudelft.ewi.cse2000.ruisdael.monitoring.component;

import static java.util.Map.entry;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import tudelft.ewi.cse2000.ruisdael.monitoring.entity.Device;

class DeviceDataConverterTest {

    private static final Map<String, Object> VALUES = Map.ofEntries(
            entry("RAM.total", "8192"),
            entry("RAM.available", "4096"),
            entry("RAM.used.perc", "50.0"),
            entry("RAM.used.bytes", "4096"),
            entry("RAM.free", "4096"),
            entry("storage.total", "102400"),
            entry("storage.used.bytes", "51200"),
            entry("storage.free", "51200"),
            entry("storage.used.perc", "50.0"),
            entry("CPU", "0.5"),
            entry("upload.size", "1024"),
            entry("download.size", "2048"),
            entry("upload.speed", "10.0"),
            entry("download.speed", "20.0"),
            entry("@timestamp", "2023-05-26T12:00:00"),
            entry("location.coordinates", new ArrayList<Double>(List.of(52.0124, 4.8521))),
            entry("instrument.name", "Instrument 1"),
            entry("instrument.type", "Type 1"),
            entry("location.name", "Location 1"),
            entry("location.elevation", "10.0")
    );

    @Test
    void createDeviceFromElasticData_ValidData_ReturnsDevice() {
        // Arrange
        String name = "Node1";
        boolean online = true;

        // Act
        Device device = DeviceDataConverter.createDeviceFromElasticData(name, online, VALUES);

        // Assert
        assertThat(device).isNotNull();
        assertEquals("Instrument 1", device.getName());
        assertEquals(online, device.isOnline());
    }

    @Test
    void createDeviceFromElasticData_InvalidData_ThrowsException() {
        // Arrange
        String name = "Node1";
        boolean online = true;
        Map<String, Object> badValues = new HashMap<>(VALUES);
        badValues.remove("CPU");

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () ->
                DeviceDataConverter.createDeviceFromElasticData(name, online, badValues));
    }
}