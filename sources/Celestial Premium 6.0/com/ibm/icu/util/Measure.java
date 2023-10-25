/*
 * Decompiled with CFR 0.150.
 */
package com.ibm.icu.util;

import com.ibm.icu.util.MeasureUnit;

public abstract class Measure {
    private Number number;
    private MeasureUnit unit;

    protected Measure(Number number, MeasureUnit unit) {
        if (number == null || unit == null) {
            throw new NullPointerException();
        }
        this.number = number;
        this.unit = unit;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        try {
            Measure m = (Measure)obj;
            return this.unit.equals(m.unit) && Measure.numbersEqual(this.number, m.number);
        }
        catch (ClassCastException e) {
            return false;
        }
    }

    private static boolean numbersEqual(Number a, Number b) {
        if (a.equals(b)) {
            return true;
        }
        return a.doubleValue() == b.doubleValue();
    }

    public int hashCode() {
        return this.number.hashCode() ^ this.unit.hashCode();
    }

    public String toString() {
        return this.number.toString() + ' ' + this.unit.toString();
    }

    public Number getNumber() {
        return this.number;
    }

    public MeasureUnit getUnit() {
        return this.unit;
    }
}

