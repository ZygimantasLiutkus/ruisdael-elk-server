package tudelft.ewi.cse2000.ruisdael.monitoring.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class InstrumentTest {

    private Instrument instrument;

    @BeforeEach
    void setUp() {
        instrument = new Instrument("n", "t");
    }

    @Test
    void noArgsConstructor_ReturnsInstrument() {
        Instrument i = new Instrument();

        assertThat(i).isNotNull();
    }

    @Test
    void allArgsConstructor_ReturnsInstrument() {
        assertThat(instrument).isNotNull();
    }

    @Test
    void setGet_Name_Test() {
        instrument.setName("name");

        assertEquals("name", instrument.getName());
    }

    @Test
    void setGet_Type_Test() {
        instrument.setType("type");

        assertEquals("type", instrument.getType());
    }

    @Test
    void equals_True_SameObjects() {
        assertTrue(instrument.equals(instrument));
    }

    @Test
    @SuppressWarnings("PMD.EqualsNull")
    void equals_False_Null() {
        assertFalse(instrument.equals(null));
    }

    @Test
    void equals_False_DifferentClass() {
        assertFalse(instrument.equals("instrument"));
    }

    @Test
    void equals_True_DifferentInstrument() {
        Instrument instrument1 = new Instrument("n", "t");

        assertTrue(instrument.equals(instrument1));
    }

    @Test
    void equals_False_DifferentInstrument() {
        Instrument instrument1 = new Instrument("name", "t");
        assertFalse(instrument.equals(instrument1));

        Instrument instrument2 = new Instrument("n", "type");
        assertFalse(instrument.equals(instrument2));
    }

    @Test
    void hashCode_ReturnsInt() {
        int hash = "n".hashCode() * 31 + "t".hashCode();

        assertEquals(hash, instrument.hashCode());
    }
}