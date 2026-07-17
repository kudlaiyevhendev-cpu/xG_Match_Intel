package com.xgintel.common.model;

/**
 * An expected-goals value. A shot's xG is a probability in [0, 1]; aggregated xG
 * (e.g. a match total) can exceed 1 but is never negative.
 */
public record XgValue(double value) {

    public XgValue {
        if (value < 0) {
            throw new IllegalArgumentException("xG cannot be negative: " + value);
        }
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            throw new IllegalArgumentException("xG must be a finite number: " + value);
        }
    }

    public XgValue add(XgValue other) {
        return new XgValue(this.value + other.value);
    }

    public static XgValue zero() {
        return new XgValue(0.0);
    }
}
