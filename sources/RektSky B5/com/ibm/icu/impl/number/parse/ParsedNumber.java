/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl.number.parse;

import com.ibm.icu.impl.StringSegment;
import com.ibm.icu.impl.number.DecimalQuantity_DualStorageBCD;
import java.util.Comparator;

public class ParsedNumber {
    public DecimalQuantity_DualStorageBCD quantity;
    public int charEnd;
    public int flags;
    public String prefix;
    public String suffix;
    public String currencyCode;
    public static final int FLAG_NEGATIVE = 1;
    public static final int FLAG_PERCENT = 2;
    public static final int FLAG_PERMILLE = 4;
    public static final int FLAG_HAS_EXPONENT = 8;
    public static final int FLAG_HAS_DECIMAL_SEPARATOR = 32;
    public static final int FLAG_NAN = 64;
    public static final int FLAG_INFINITY = 128;
    public static final int FLAG_FAIL = 256;
    public static final Comparator<ParsedNumber> COMPARATOR = new Comparator<ParsedNumber>(){

        @Override
        public int compare(ParsedNumber o1, ParsedNumber o2) {
            return o1.charEnd - o2.charEnd;
        }
    };

    public ParsedNumber() {
        this.clear();
    }

    public void clear() {
        this.quantity = null;
        this.charEnd = 0;
        this.flags = 0;
        this.prefix = null;
        this.suffix = null;
        this.currencyCode = null;
    }

    public void copyFrom(ParsedNumber other) {
        this.quantity = other.quantity == null ? null : (DecimalQuantity_DualStorageBCD)other.quantity.createCopy();
        this.charEnd = other.charEnd;
        this.flags = other.flags;
        this.prefix = other.prefix;
        this.suffix = other.suffix;
        this.currencyCode = other.currencyCode;
    }

    public void setCharsConsumed(StringSegment segment) {
        this.charEnd = segment.getOffset();
    }

    public void postProcess() {
        if (this.quantity != null && 0 != (this.flags & 1)) {
            this.quantity.negate();
        }
    }

    public boolean success() {
        return this.charEnd > 0 && 0 == (this.flags & 0x100);
    }

    public boolean seenNumber() {
        return this.quantity != null || 0 != (this.flags & 0x40) || 0 != (this.flags & 0x80);
    }

    public Number getNumber() {
        return this.getNumber(0);
    }

    public Number getNumber(int parseFlags) {
        boolean integerOnly;
        boolean sawNaN = 0 != (this.flags & 0x40);
        boolean sawInfinity = 0 != (this.flags & 0x80);
        boolean forceBigDecimal = 0 != (parseFlags & 0x1000);
        boolean bl = integerOnly = 0 != (parseFlags & 0x10);
        if (sawNaN) {
            return Double.NaN;
        }
        if (sawInfinity) {
            if (0 != (this.flags & 1)) {
                return Double.NEGATIVE_INFINITY;
            }
            return Double.POSITIVE_INFINITY;
        }
        assert (this.quantity != null);
        if (this.quantity.isZero() && this.quantity.isNegative() && !integerOnly) {
            return -0.0;
        }
        if (this.quantity.fitsInLong() && !forceBigDecimal) {
            return this.quantity.toLong(false);
        }
        return this.quantity.toBigDecimal();
    }

    boolean isBetterThan(ParsedNumber other) {
        return COMPARATOR.compare(this, other) > 0;
    }
}

