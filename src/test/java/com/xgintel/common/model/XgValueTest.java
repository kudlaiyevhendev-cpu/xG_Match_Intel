package com.xgintel.common.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class XgValueTest {

    @Test
    void addsValues() {
        XgValue total = new XgValue(0.3).add(new XgValue(0.45));
        assertEquals(0.75, total.value(), 1e-9);
    }

    @Test
    void zeroIsZero() {
        assertEquals(0.0, XgValue.zero().value(), 1e-9);
    }

    @Test
    void rejectsNegative() {
        assertThrows(IllegalArgumentException.class, () -> new XgValue(-0.1));
    }

    @Test
    void rejectsNonFinite() {
        assertThrows(IllegalArgumentException.class, () -> new XgValue(Double.NaN));
        assertThrows(IllegalArgumentException.class, () -> new XgValue(Double.POSITIVE_INFINITY));
    }
}
