/*
 * Decompiled with CFR 0.152.
 */
package com.google.common.math;

import com.google.common.base.Preconditions;
import java.math.BigInteger;

final class DoubleUtils {
    static final long SIGNIFICAND_MASK = 0xFFFFFFFFFFFFFL;
    static final long EXPONENT_MASK = 0x7FF0000000000000L;
    static final long SIGN_MASK = Long.MIN_VALUE;
    static final int SIGNIFICAND_BITS = 52;
    static final int EXPONENT_BIAS = 1023;
    static final long IMPLICIT_BIT = 0x10000000000000L;
    private static final long ONE_BITS = Double.doubleToRawLongBits(1.0);

    private DoubleUtils() {
    }

    static double nextDown(double d2) {
        return -Math.nextUp(-d2);
    }

    static long getSignificand(double d2) {
        Preconditions.checkArgument(DoubleUtils.isFinite(d2), "not a normal value");
        int exponent = Math.getExponent(d2);
        long bits = Double.doubleToRawLongBits(d2);
        return exponent == -1023 ? bits << 1 : (bits &= 0xFFFFFFFFFFFFFL) | 0x10000000000000L;
    }

    static boolean isFinite(double d2) {
        return Math.getExponent(d2) <= 1023;
    }

    static boolean isNormal(double d2) {
        return Math.getExponent(d2) >= -1022;
    }

    static double scaleNormalize(double x2) {
        long significand = Double.doubleToRawLongBits(x2) & 0xFFFFFFFFFFFFFL;
        return Double.longBitsToDouble(significand | ONE_BITS);
    }

    static double bigToDouble(BigInteger x2) {
        BigInteger absX = x2.abs();
        int exponent = absX.bitLength() - 1;
        if (exponent < 63) {
            return x2.longValue();
        }
        if (exponent > 1023) {
            return (double)x2.signum() * Double.POSITIVE_INFINITY;
        }
        int shift = exponent - 52 - 1;
        long twiceSignifFloor = absX.shiftRight(shift).longValue();
        long signifFloor = twiceSignifFloor >> 1;
        boolean increment = (twiceSignifFloor & 1L) != 0L && (((signifFloor &= 0xFFFFFFFFFFFFFL) & 1L) != 0L || absX.getLowestSetBit() < shift);
        long signifRounded = increment ? signifFloor + 1L : signifFloor;
        long bits = (long)(exponent + 1023) << 52;
        bits += signifRounded;
        return Double.longBitsToDouble(bits |= (long)x2.signum() & Long.MIN_VALUE);
    }

    static double ensureNonNegative(double value) {
        Preconditions.checkArgument(!Double.isNaN(value));
        if (value > 0.0) {
            return value;
        }
        return 0.0;
    }
}

