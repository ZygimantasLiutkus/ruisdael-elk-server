package tudelft.ewi.cse2000.ruisdael.monitoring.component;

import static java.util.Map.entry;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tudelft.ewi.cse2000.ruisdael.monitoring.device.Bandwidth;
import tudelft.ewi.cse2000.ruisdael.monitoring.device.Device;
import tudelft.ewi.cse2000.ruisdael.monitoring.device.Instrument;
import tudelft.ewi.cse2000.ruisdael.monitoring.device.Location;
import tudelft.ewi.cse2000.ruisdael.monitoring.device.Ram;
import tudelft.ewi.cse2000.ruisdael.monitoring.device.Storage;
import tudelft.ewi.cse2000.ruisdael.monitoring.device.enums.Status;

@SuppressWarnings("PMD.AvoidDuplicateLiterals") //Map keys are causing this to pop up, this is necessary.
public class DeviceDataConverterTest {

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
            entry("location.coordinates", new ArrayList<>(List.of(52.0124, 4.8521))),
            entry("instrument.name", "Instrument 1"),
            entry("instrument.type", "Type 1"),
            entry("location.name", "Location 1"),
            entry("location.elevation", "10.0")
    );
    
    @DisplayName("Test valid instrument data extraction")
    @Test
    void testExtractInstrumentData() {
        // Setup
        final Map<String, Object> values = Map.of("instrument.name", "bucket", "instrument.type", "sensor");
        final Instrument expectedResult = new Instrument("bucket", "sensor");

        // Run the test
        final Instrument result = DeviceDataConverter.extractInstrumentData(values);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    /**
     * A typo in instrument.type.
     */
    @DisplayName("Test invalid instrument data extraction")
    @Test
    void testExtractInstrumentDataThrowsException() {
        //Setup
        final Map<String, Object> values = Map.of("instrument.name", "bucket", "instrument.typo", "sensor");

        //Run the test
        assertThrows(IllegalArgumentException.class, () -> DeviceDataConverter.extractInstrumentData(values));
    }

    @DisplayName("Test valid location data extraction")
    @Test
    void testExtractLocationData() {
        // Setup
        ArrayList<Double> coords = new ArrayList<>();
        coords.add(1.0);
        coords.add(2.0);

        final Map<String, Object> values = Map.of("location.name", "ewi", "location.elevation", "2m",
                "location.coordinates", coords);
        final Location expectedResult = new Location(1.0, 2.0, "2m", "ewi");

        // Run the test
        final Location result = DeviceDataConverter.extractLocationData(values);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    /**
     * Coordinates are not an array.
     */
    @DisplayName("Test invalid location data extraction")
    @Test
    void testExtractLocationDataThrowsException() {
        //Setup
        final Map<String, Object> values = Map.of("location.name", "ewi", "location.elevation", "2m",
                "location.coordinates", "0");
        //Run the test
        assertThrows(IllegalArgumentException.class, () -> DeviceDataConverter.extractLocationData(values));
    }

    @DisplayName("Test valid storage data extraction")
    @Test
    void testExtractStorageData() {
        // Setup
        final Map<String, Object> values = Map.of("storage.total", 20L, "storage.used.bytes", 3L,
                "storage.free", 10L, "storage.used.perc", 50.0);
        final Storage expectedResult = new Storage(20L, 10L, 50.0, 3L);

        // Run the test
        final Storage result = DeviceDataConverter.extractStorageData(values);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    /**
     * Uses incorrect format for storage.free.
     */
    @DisplayName("Test invalid storage data extraction")
    @Test
    void testExtractStorageDataThrowsException() {
        //Setup
        final Map<String, Object> values = Map.of("storage.total", 20L, "storage.used.bytes", 3L,
                "storage.free", "10 gigabytes", "storage.used.perc", 30f);
        //Run the test
        assertThrows(IllegalArgumentException.class, () -> DeviceDataConverter.extractStorageData(values));
    }

    @DisplayName("Test valid ram data extraction")
    @Test
    void testExtractRamData() {
        // Setup
        final Map<String, Object> values = Map.of("RAM.total", 60L, "RAM.available", 20L,
                "RAM.used.bytes", 10L, "RAM.used.perc", 50.0, "RAM.free", 30L);
        final Ram expectedResult = new Ram(60L, 20L, 30L, 50.0, 10L);

        // Run the test
        final Ram result = DeviceDataConverter.extractRamData(values);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    /**
     * Missing key-value pair.
     */
    @DisplayName("Test invalid ram data extraction")
    @Test
    void testExtractRamDataThrowsException() {
        //Setup
        final Map<String, Object> values = Map.of("RAM.total", 60L, "RAM.available", 20L,
                "RAM.used.bytes", 10L, "RAM.used.perc", 50.0);

        //Run the test
        assertThrows(IllegalArgumentException.class, () -> DeviceDataConverter.extractRamData(values));
    }

    @DisplayName("Test valid bandwith data extraction")
    @Test
    void testExtractBandwithData() {
        // Setup
        final Map<String, Object> values = Map.of("upload.size", 60L, "upload.speed", 10.0,
                "download.size", 10L, "download.speed", 50.0);
        final Bandwidth expectedResult = new Bandwidth(60L, 10L, 10.0, 50.0);

        // Run the test
        final Bandwidth result = DeviceDataConverter.extractBandwithData(values);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    /**
     * Missing value.
     */
    @DisplayName("Test invalid instrument data extraction")
    @Test
    void testExtractBandwidthDataThrowsException() {
        //Setup
        final Map<String, Object> values = Map.of("upload.size", 60L, "upload.speed", 10.0,
                "download.size", 10L, "download.speed", "");

        //Run the test
        assertThrows(IllegalArgumentException.class, () -> DeviceDataConverter.extractBandwithData(values));
    }

    @DisplayName("Test valid custom data extraction")
    @Test
    void testExtractCustomData() {
        // Setup
        final Map<String, Object> values = Map.of("custom.power", "230V AC", "custom.voltage", 110);
        final Map<String, String> expectedResult = Map.of("power", "230V AC", "voltage", "110");

        // Run the test
        final Map<String, String> result = DeviceDataConverter.extractCustomData(values);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    @DisplayName("Test valid custom data does not overwrite more than one prefix")
    @Test
    void testExtractCustomDataPrefix() {
        // Setup
        final Map<String, Object> values = Map.of("custom.custom.power", "230V AC", "custom.power.custom.voltage", 110);
        final Map<String, String> expectedResult = Map.of("custom.power", "230V AC", "power.custom.voltage", "110");

        // Run the test
        final Map<String, String> result = DeviceDataConverter.extractCustomData(values);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    /**
     * Any custom data sent is parsed, so a null value should raise an exception.
     * The method will normally always have map provided ,otherwise the calling method would have failed.
     */
    @DisplayName("Test invalid instrument data extraction")
    @Test
    void testExtractCustomDataThrowsException() {
        //Run the test
        assertThrows(IllegalArgumentException.class, () -> DeviceDataConverter.extractCustomData(null));
    }

    /**
     * Mocking static methods is a pain, so this is a rather bulky test instead.
     */
    @DisplayName("Test full data extraction with valid data")
    @Test
    void testCreateDeviceFromElasticData() {
        //Setup
        Device expectedResult = new Device("node", new Instrument("Instrument 1", "Type 1"),
                new Location(52.0124, 4.8521, "10.0", "Location 1"), Status.ONLINE,
                new Storage(102400, 51200, 50.0, 51200),
                new Ram(8192, 4096, 4096, 50.0, 4096), 0.5,
                new Bandwidth(1024, 2048, 10.0, 20.0),
                "2023-05-26T12:00:00", Map.of());

        // Run the test
        Device result = DeviceDataConverter.createDeviceFromElasticData("node", Status.ONLINE, VALUES);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    /**
     * Missing almost all data.
     */
    @DisplayName("Test full data extraction with invalid data")
    @Test
    void testCreateDeviceFromElasticDataThrowsException() {
        //Setup
        final Map<String, Object> values = Map.of("upload.size", 60L, "upload.speed", 10.0,
                "download.size", 10L, "download.speed", "");

        //Run the test
        assertThrows(IllegalArgumentException.class, () -> DeviceDataConverter.createDeviceFromElasticData("name", Status.ONLINE, values));
    }
}
