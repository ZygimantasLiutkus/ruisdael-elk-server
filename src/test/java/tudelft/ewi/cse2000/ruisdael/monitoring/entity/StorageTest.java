package tudelft.ewi.cse2000.ruisdael.monitoring.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class StorageTest {

    private Storage storage;

    @BeforeEach
    void setUp() {
        storage = new Storage(2.0, 1.0, 3.0, 4.0);
    }

    @Test
    void noArgsConstructor_ReturnsStorage() {
        Storage s = new Storage();

        assertThat(s).isNotNull();
    }

    @Test
    void allArgsConstructor_ReturnsStorage() {
        assertThat(storage).isNotNull();
    }

    @Test
    void getTotalStorage_ReturnsDouble() {
        assertEquals(2.0, storage.getTotalStorage());
    }

    @Test
    void getFreeStorage_ReturnsDouble() {
        assertEquals(1.0, storage.getFreeStorage());
    }

    @Test
    void getUsedPercStorage_ReturnsDouble() {
        assertEquals(3.0, storage.getUsedPercStorage());
    }

    @Test
    void getUsedBytesStorage_ReturnsDouble() {
        assertEquals(4.0, storage.getUsedBytesStorage());
    }

    @Test
    void setTotalStorage_Test() {
        storage.setTotalStorage(5.0);

        assertEquals(5.0, storage.getTotalStorage());
    }

    @Test
    void setFreeStorage_Test() {
        storage.setFreeStorage(6.0);

        assertEquals(6.0, storage.getFreeStorage());
    }

    @Test
    void setUsedPercStorage_Test() {
        storage.setUsedPercStorage(7.0);

        assertEquals(7.0, storage.getUsedPercStorage());
    }

    @Test
    void setUsedBytesStorage_Test() {
        storage.setUsedBytesStorage(8.0);

        assertEquals(8.0, storage.getUsedBytesStorage());
    }

    @Test
    void equals_True_SameObjects() {
        assertTrue(storage.equals(storage));
    }

    @Test
    @SuppressWarnings("PMD.EqualsNull")
    void equals_False_Null() {
        assertFalse(storage.equals(null));
    }

    @Test
    void equals_False_DifferentObjects() {
        assertFalse(storage.equals("storage"));
    }

    @Test
    void equals_True_DifferentStorages() {
        Storage storage1 = new Storage(2.0, 1.0, 3.0, 4.0);

        assertTrue(storage.equals(storage1));
    }

    @Test
    void hashCode_ReturnsInt() {
        assertEquals(Objects.hash(2.0, 1.0), storage.hashCode());
    }

    @Test
    void toString_ReturnsString() {
        assertEquals("Storage{totalStorage=2.0, freeStorage=1.0}", storage.toString());
    }
}