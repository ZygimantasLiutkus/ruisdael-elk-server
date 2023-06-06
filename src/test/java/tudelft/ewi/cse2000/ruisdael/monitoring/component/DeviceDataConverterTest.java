package tudelft.ewi.cse2000.ruisdael.monitoring.component;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tudelft.ewi.cse2000.ruisdael.monitoring.entity.Bandwidth;
import tudelft.ewi.cse2000.ruisdael.monitoring.entity.Device;
import tudelft.ewi.cse2000.ruisdael.monitoring.entity.Instrument;
import tudelft.ewi.cse2000.ruisdael.monitoring.entity.Location;
import tudelft.ewi.cse2000.ruisdael.monitoring.entity.Ram;
import tudelft.ewi.cse2000.ruisdael.monitoring.entity.Storage;

@SuppressWarnings("PMD.AvoidDuplicateLiterals") //Map keys are causing this to pop up, this is necessary.
public class DeviceDataConverterTest {

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
        final Storage expectedResult = new Storage(20L, 10L);

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
        final Ram expectedResult = new Ram(60L, 20L, 30L);

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
    void testExtractBandwithDataThrowsException() {
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
        ArrayList<Double> coords = new ArrayList<>();
        coords.add(1.0);
        coords.add(2.0);

        Map<String, Object> values = new java.util.HashMap<>(Map.of("instrument.name", "bucket", "instrument.type", "sensor",
                "location.name", "ewi", "location.elevation", "2m", "location.coordinates", coords,
                "storage.total", 20L, "storage.used.bytes", 3L, "storage.free", 10L, "storage.used.perc", 50.0, "CPU", "10.0"));
        values.putAll(Map.of("RAM.total", 60L, "RAM.available", 20L, "RAM.used.bytes", 10L,
                "RAM.used.perc", 50.0, "RAM.free", 30L, "upload.size", 60L, "upload.speed", 10.0,
                "download.size", 10L, "download.speed", 50.0, "@timestamp", "today"));
        Device expectedResult = new Device("node", new Instrument("bucket", "sensor"), new Location(1.0, 2.0, "2m", "ewi"), true,
                new Storage(20L, 10L), new Ram(60L, 20L, 30L), 10.0, new Bandwidth(60L, 10L, 10.0, 50.0),
                "today", Map.of());

        // Run the test
        Device result = DeviceDataConverter.createDeviceFromElasticData("node", true, values);

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
        assertThrows(IllegalArgumentException.class, () -> DeviceDataConverter.createDeviceFromElasticData("name", true, values));
    }
}
