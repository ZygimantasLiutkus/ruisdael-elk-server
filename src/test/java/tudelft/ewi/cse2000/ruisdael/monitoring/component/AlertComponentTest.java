package tudelft.ewi.cse2000.ruisdael.monitoring.component;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tudelft.ewi.cse2000.ruisdael.monitoring.device.Bandwidth;
import tudelft.ewi.cse2000.ruisdael.monitoring.device.Device;
import tudelft.ewi.cse2000.ruisdael.monitoring.device.Instrument;
import tudelft.ewi.cse2000.ruisdael.monitoring.device.Location;
import tudelft.ewi.cse2000.ruisdael.monitoring.device.Ram;
import tudelft.ewi.cse2000.ruisdael.monitoring.device.Storage;
import tudelft.ewi.cse2000.ruisdael.monitoring.device.enums.Flag;
import tudelft.ewi.cse2000.ruisdael.monitoring.device.enums.Status;

@SuppressWarnings("PMD.AvoidDuplicateLiterals") //Map keys are causing this to pop up, this is necessary.
public class AlertComponentTest {

    // @Autowired
    private AlertComponent alertComponent = new AlertComponent();

    private Device device;
    
    /**
     * Make a device object for the methods.
     */
    @BeforeEach    
    public void setUp() {
        Instrument instrument = new Instrument("Instrument 1", "Type 1");
        Location location = new Location(52.0124, 4.8521, "10.0", "Location 1");
        Status status = Status.ONLINE;
        Storage storage = new Storage(102400, 51200, 50.0, 51200);
        Ram ram = new Ram(8192, 4096, 4096, 50.0, 4096);
        float cpu = 0.5f;
        Bandwidth bandwidth = new Bandwidth(1024, 2048, 10.0, 20.0);
        String timeStamp = "2023-05-26T12:00:00Z";
        Map map = Map.of();

        device = new Device("node", instrument, location, status, storage, ram, cpu, bandwidth, timeStamp, map);
    }

    /**
     * Testing for up-status Flags.
     */
    @DisplayName("Test Up-Status Flags")
    @Test
    void testUpStatusFlags() {
        HashMap<String, Flag> flags = alertComponent.getFlagsOf(device);
        assertEquals(flags.get("UP-STATUS"), Flag.GREEN);

        device.setStatus(Status.WARNING);
        flags = alertComponent.getFlagsOf(device);
        assertEquals(flags.get("UP-STATUS"), Flag.YELLOW);

        device.setStatus(Status.OFFLINE);
        flags = alertComponent.getFlagsOf(device);
        assertEquals(flags.get("UP-STATUS"), Flag.RED);
    }

    /**
     * Testing for CPU Flags.
     */
    @DisplayName("Test CPU Flags")
    @Test
    void testCpuFlags() {
        HashMap<String, Flag> flags = alertComponent.getFlagsOf(device);
        assertEquals(flags.get("CPU"), Flag.GREEN);

        device.setCpuUsage(81);
        flags = alertComponent.getFlagsOf(device);
        assertEquals(flags.get("CPU"), Flag.YELLOW);

        device.setCpuUsage(91);
        flags = alertComponent.getFlagsOf(device);
        assertEquals(flags.get("CPU"), Flag.RED);
    }

    /**
     * Testing for RAM Flags.
     */
    @DisplayName("Test RAM Flags")
    @Test
    void testRamFlags() {
        HashMap<String, Flag> flags = alertComponent.getFlagsOf(device);
        assertEquals(flags.get("RAM"), Flag.GREEN);

        Ram ram = new Ram(8192, 6696, 4096, 50.0, 4096);
        device.setRam(ram);
        flags = alertComponent.getFlagsOf(device);
        assertEquals(flags.get("RAM"), Flag.YELLOW);

        ram = new Ram(8192, 7996, 4096, 50.0, 4096);
        device.setRam(ram);
        flags = alertComponent.getFlagsOf(device);
        assertEquals(flags.get("RAM"), Flag.RED);
    }

    /**
     * Testing for Storage Flags.
     */
    @DisplayName("Test Storage Flags")
    @Test
    void testStorageFlags() {
        HashMap<String, Flag> flags = alertComponent.getFlagsOf(device);
        assertEquals(flags.get("STORAGE"), Flag.GREEN);

        Storage storage = new Storage(102400, 51200, 50.0, 51200);
        
        storage.setUsedPercStorage(81);
        device.setStorage(storage);
        flags = alertComponent.getFlagsOf(device);
        assertEquals(flags.get("STORAGE"), Flag.YELLOW);

        storage.setUsedPercStorage(91);
        device.setStorage(storage);
        flags = alertComponent.getFlagsOf(device);
        assertEquals(flags.get("STORAGE"), Flag.RED);
    }



}
