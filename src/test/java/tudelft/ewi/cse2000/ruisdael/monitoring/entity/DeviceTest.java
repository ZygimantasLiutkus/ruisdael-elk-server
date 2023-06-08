package tudelft.ewi.cse2000.ruisdael.monitoring.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class DeviceTest {

    private static final String NAME = "name";
    private Device device;
    private Location location;
    private Storage storage;
    private Ram ram;
    private Bandwidth bandwidth;
    private Map<String, String> custom;
    private Instrument instrument;

    @BeforeEach
    void setUp() {
        instrument = new Instrument("in", "it");
        location = new Location(1.0, 2.0, "le", "ln");
        storage = new Storage(0.0, 0.0, 0.0, 0.0);
        ram = new Ram(0.0, 0.0, 0.0, 0.0, 0.0);
        bandwidth = new Bandwidth(0.0, 0.0, 0.0, 0.0);
        custom = Map.of();
        device = new Device(NAME, instrument, location, true, storage, ram,
                1.0, bandwidth, "t", custom);
    }

    @Test
    void noArgsConstructor_ReturnsDevice() {
        Device d = new Device();

        assertThat(d).isNotNull();
    }

    @Test
    void allArgsConstructor_ReturnsDevice() {
        assertThat(device).isNotNull();
    }

    @Test
    void deviceConstructor_ReturnsDevice() {
        Device d = new Device(true, NAME, new Location(), 1.0, 2.0,
                3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0);

        assertThat(d).isNotNull();
    }

    @Test
    void getHumanReadableValueGbs_ReturnsString() {
        assertEquals("1.00", device.getHumanReadableValueGbs(1000000000.0));
    }

    @Test
    void getHumanReadableValueMbs_ReturnsString() {
        assertEquals("1.00", device.getHumanReadableValueMbs(1000000.0));
    }

    @Test
    void getHumanReadableValueKbs_ReturnsString() {
        assertEquals("1.00", device.getHumanReadableValueKbs(1000.0));
    }

    @Test
    void setGet_Name_Test() {
        device.setName("n");

        assertEquals("n", device.getName());
    }

    @Test
    void setGet_Instrument_Test() {
        Instrument instrument1 = new Instrument();
        device.setInstrument(instrument1);

        assertEquals(instrument1, device.getInstrument());
    }

    @Test
    void setGet_Location_Test() {
        Location location1 = new Location();
        device.setLocation(location1);

        assertEquals(location1, device.getLocation());
    }

    @Test
    void setGet_Online_Test() {
        device.setOnline(false);

        assertFalse(device.isOnline());
    }

    @Test
    void setGet_Storage_Test() {
        Storage storage1 = new Storage();
        device.setStorage(storage1);

        assertEquals(storage1, device.getStorage());
    }

    @Test
    void setGet_Ram_Test() {
        Ram ram1 = new Ram();
        device.setRam(ram1);

        assertEquals(ram1, device.getRam());
    }

    @Test
    void setGet_CpuUsage_Test() {
        device.setCpuUsage(2.0);

        assertEquals(2.0, device.getCpuUsage());
    }

    @Test
    void setGet_Bandwidth_Test() {
        Bandwidth bandwidth1 = new Bandwidth();
        device.setBandwidth(bandwidth1);

        assertEquals(bandwidth1, device.getBandwidth());
    }

    @Test
    void setGet_Timestamp_Test() {
        device.setTimestamp("time");

        assertEquals("time", device.getTimestamp());
    }

    @Test
    void setGet_CustomFields_Test() {
        Map<String, String> custom1 = new HashMap<>();
        device.setCustomFields(custom1);

        assertEquals(custom1, device.getCustomFields());
    }

    @Test
    void equals_True_SameObjects() {
        assertTrue(device.equals(device));
    }

    @Test
    @SuppressWarnings("PMD.EqualsNull")
    void equals_False_Null() {
        assertFalse(device.equals(null));
    }

    @Test
    void equals_False_DifferentClass() {
        assertFalse(device.equals("device"));
    }

    @Test
    void equals_True_DifferentDevices() {
        Device device1 = new Device(NAME, new Instrument("in", "it"), new Location(1.0, 2.0, "le", "ln"),
                true, new Storage(0.0, 0.0, 0.0, 0.0), new Ram(0.0, 0.0, 0.0, 0.0, 0.0),
                1.0, new Bandwidth(0.0, 0.0, 0.0, 0.0), "t", Map.of());

        assertTrue(device.equals(device1));
    }

    @Test
    void equals_False_DifferentDevices() {
        Device device1 = new Device(NAME, instrument, location, true, storage, ram,
                1.0, bandwidth, "t", null);
        assertFalse(device.equals(device1));

        Device device2 = new Device(NAME, instrument, location, true, storage, ram,
                1.0, bandwidth, "time", custom);
        assertFalse(device.equals(device2));

        Device device3 = new Device(NAME, instrument, location, true, storage, ram,
                1.0, null, "t", custom);
        assertFalse(device.equals(device3));

        Device device4 = new Device(NAME, instrument, location, true, storage, ram,
                0.0, bandwidth, "t", custom);
        assertFalse(device.equals(device4));

        Device device5 = new Device(NAME, instrument, location, true, storage, null,
                1.0, bandwidth, "t", custom);
        assertFalse(device.equals(device5));

        Device device6 = new Device(NAME, instrument, location, true, null, ram,
                1.0, bandwidth, "t", custom);
        assertFalse(device.equals(device6));

        Device device7 = new Device(NAME, instrument, location, false, storage, ram,
                1.0, bandwidth, "t", custom);
        assertFalse(device.equals(device7));

        Device device8 = new Device(NAME, instrument, null, true, storage, ram,
                1.0, bandwidth, "t", custom);
        assertFalse(device.equals(device8));

        Device device9 = new Device(NAME, null, location, true, storage, ram,
                1.0, bandwidth, "t", custom);
        assertFalse(device.equals(device9));

        Device device10 = new Device("", instrument, location, true, storage, ram,
                1.0, bandwidth, "t", custom);
        assertFalse(device.equals(device10));
    }

    @Test
    void hashCode_ReturnsInt() {
        assertEquals(Objects.hash(NAME, true, storage, ram, 1.0, bandwidth, location),
                device.hashCode());
    }

    @Test
    void toString_ReturnsString() {
        assertEquals("Device{name=name,\nonline=true,\nstorage=Storage{totalStorage=0.0, freeStorage=0.0},\n"
                        + "ram=Ram{total=0.0, available=0.0, free=0.0},\ncpuUsage=1.0,\nbandwidth=Bandwidth{uploadSize=0.0, "
                        + "downloadSize=0.0, uploadSpeed=0.0, downloadSpeed=0.0},\nlocation=Location{longitude=1.0, latitude=2.0, "
                        + "name=ln, elevation=le},\n}",
                device.toString());
    }

}