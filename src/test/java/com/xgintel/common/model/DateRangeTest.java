package com.xgintel.common.model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class DateRangeTest {

    @Test
    void containsInclusiveBounds() {
        DateRange range = new DateRange(LocalDate.of(2024, 8, 1), LocalDate.of(2025, 5, 31));
        assertTrue(range.contains(LocalDate.of(2024, 8, 1)));
        assertTrue(range.contains(LocalDate.of(2025, 5, 31)));
        assertTrue(range.contains(LocalDate.of(2024, 12, 1)));
        assertFalse(range.contains(LocalDate.of(2025, 6, 1)));
    }

    @Test
    void rejectsInvertedRange() {
        assertThrows(IllegalArgumentException.class,
                () -> new DateRange(LocalDate.of(2025, 1, 1), LocalDate.of(2024, 1, 1)));
    }

    @Test
    void rejectsNullBounds() {
        assertThrows(IllegalArgumentException.class, () -> new DateRange(null, LocalDate.now()));
    }
}
