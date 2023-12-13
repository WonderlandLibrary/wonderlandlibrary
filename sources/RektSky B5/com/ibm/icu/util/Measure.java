/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.util;

import com.ibm.icu.util.MeasureUnit;

public class Measure {
    private final Number number;
    private final MeasureUnit unit;

    public Measure(Number number, MeasureUnit unit) {
        if (number == null || unit == null) {
            throw new NullPointerException("Number and MeasureUnit must not be null");
        }
        this.number = number;
        this.unit = unit;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Measure)) {
            return false;
        }
        Measure m2 = (Measure)obj;
        return this.unit.equals(m2.unit) && Measure.numbersEqual(this.number, m2.number);
    }

    private static boolean numbersEqual(Number a2, Number b2) {
        if (a2.equals(b2)) {
            return true;
        }
        return a2.doubleValue() == b2.doubleValue();
    }

    public int hashCode() {
        return 31 * Double.valueOf(this.number.doubleValue()).hashCode() + this.unit.hashCode();
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

