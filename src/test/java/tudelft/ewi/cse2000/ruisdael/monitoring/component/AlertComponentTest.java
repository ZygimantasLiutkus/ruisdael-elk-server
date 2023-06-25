package tudelft.ewi.cse2000.ruisdael.monitoring.component;

import static java.util.Map.entry;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import tudelft.ewi.cse2000.ruisdael.monitoring.component.AlertComponent.Flag;
import tudelft.ewi.cse2000.ruisdael.monitoring.entity.Bandwidth;
import tudelft.ewi.cse2000.ruisdael.monitoring.entity.Device;
import tudelft.ewi.cse2000.ruisdael.monitoring.entity.Instrument;
import tudelft.ewi.cse2000.ruisdael.monitoring.entity.Location;
import tudelft.ewi.cse2000.ruisdael.monitoring.entity.Ram;
import tudelft.ewi.cse2000.ruisdael.monitoring.entity.Status;
import tudelft.ewi.cse2000.ruisdael.monitoring.entity.Storage;


@SuppressWarnings("PMD.AvoidDuplicateLiterals") //Map keys are causing this to pop up, this is necessary.
public class AlertComponentTest {

    // @Autowired
    static AlertComponent alertComponent = new AlertComponent();

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

    static Device d;
    
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
        String timeStamp = "2023-05-26T12:00:00";
        Map map = Map.of();

        d = new Device("node", instrument, location, status, storage, ram, cpu, bandwidth, timeStamp, map);
    }

    /**
     * Testing for up-status Flags.
     */
    @DisplayName("Test Up-Status Flags")
    @Test
    void testUpStatusFlags() {
        HashMap<String, Flag> flags = alertComponent.getFlagsOf(d);
        assertEquals(flags.get("UP-STATUS"), Flag.GREEN);

        d.setStatus(Status.WARNING);
        flags = alertComponent.getFlagsOf(d);
        assertEquals(flags.get("UP-STATUS"), Flag.YELLOW);

        d.setStatus(Status.OFFLINE);
        flags = alertComponent.getFlagsOf(d);
        assertEquals(flags.get("UP-STATUS"), Flag.RED);
    }

    /**
     * Testing for CPU Flags.
     */
    @DisplayName("Test CPU Flags")
    @Test
    void testCpuFlags() {
        HashMap<String, Flag> flags = alertComponent.getFlagsOf(d);
        assertEquals(flags.get("CPU"), Flag.GREEN);

        d.setCpuUsage(.81);
        flags = alertComponent.getFlagsOf(d);
        assertEquals(flags.get("CPU"), Flag.YELLOW);

        d.setCpuUsage(.91);
        flags = alertComponent.getFlagsOf(d);
        assertEquals(flags.get("CPU"), Flag.RED);
    }

    /**
     * Testing for RAM Flags.
     */
    @DisplayName("Test RAM Flags")
    @Test
    void testRamFlags() {
        HashMap<String, Flag> flags = alertComponent.getFlagsOf(d);
        assertEquals(flags.get("RAM"), Flag.GREEN);

        Ram ram = new Ram(8192, 6696, 4096, 50.0, 4096);
        d.setRam(ram);
        flags = alertComponent.getFlagsOf(d);
        assertEquals(flags.get("RAM"), Flag.YELLOW);

        ram = new Ram(8192, 7996, 4096, 50.0, 4096);
        d.setRam(ram);
        flags = alertComponent.getFlagsOf(d);
        assertEquals(flags.get("RAM"), Flag.RED);
    }

    /**
     * Testing for Storage Flags.
     */
    @DisplayName("Test Storage Flags")
    @Test
    void testStorageFlags() {
        HashMap<String, Flag> flags = alertComponent.getFlagsOf(d);
        assertEquals(flags.get("STORAGE"), Flag.GREEN);

        Storage storage = new Storage(102400, 51200, 50.0, 51200);
        
        storage.setUsedPercStorage(81);
        d.setStorage(storage);
        flags = alertComponent.getFlagsOf(d);
        assertEquals(flags.get("STORAGE"), Flag.YELLOW);

        storage.setUsedPercStorage(91);
        d.setStorage(storage);
        flags = alertComponent.getFlagsOf(d);
        assertEquals(flags.get("STORAGE"), Flag.RED);
    }



}
