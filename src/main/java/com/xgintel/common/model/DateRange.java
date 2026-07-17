package com.xgintel.common.model;

import java.time.LocalDate;

/**
 * An inclusive range of dates, used to scope metric queries (e.g. form over a
 * season window).
 */
public record DateRange(LocalDate from, LocalDate to) {

    public DateRange {
        if (from == null || to == null) {
            throw new IllegalArgumentException("DateRange bounds must not be null");
        }
        if (from.isAfter(to)) {
            throw new IllegalArgumentException("DateRange 'from' (" + from + ") is after 'to' (" + to + ")");
        }
    }

    public boolean contains(LocalDate date) {
        return !date.isBefore(from) && !date.isAfter(to);
    }
}
