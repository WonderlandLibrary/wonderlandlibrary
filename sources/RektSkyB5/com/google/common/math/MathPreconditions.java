/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package com.google.common.math;

import com.google.common.annotations.GwtCompatible;
import java.math.BigInteger;
import javax.annotation.Nullable;

@GwtCompatible
final class MathPreconditions {
    static int checkPositive(@Nullable String role, int x2) {
        if (x2 <= 0) {
            throw new IllegalArgumentException(role + " (" + x2 + ") must be > 0");
        }
        return x2;
    }

    static long checkPositive(@Nullable String role, long x2) {
        if (x2 <= 0L) {
            throw new IllegalArgumentException(role + " (" + x2 + ") must be > 0");
        }
        return x2;
    }

    static BigInteger checkPositive(@Nullable String role, BigInteger x2) {
        if (x2.signum() <= 0) {
            throw new IllegalArgumentException(role + " (" + x2 + ") must be > 0");
        }
        return x2;
    }

    static int checkNonNegative(@Nullable String role, int x2) {
        if (x2 < 0) {
            throw new IllegalArgumentException(role + " (" + x2 + ") must be >= 0");
        }
        return x2;
    }

    static long checkNonNegative(@Nullable String role, long x2) {
        if (x2 < 0L) {
            throw new IllegalArgumentException(role + " (" + x2 + ") must be >= 0");
        }
        return x2;
    }

    static BigInteger checkNonNegative(@Nullable String role, BigInteger x2) {
        if (x2.signum() < 0) {
            throw new IllegalArgumentException(role + " (" + x2 + ") must be >= 0");
        }
        return x2;
    }

    static double checkNonNegative(@Nullable String role, double x2) {
        if (!(x2 >= 0.0)) {
            throw new IllegalArgumentException(role + " (" + x2 + ") must be >= 0");
        }
        return x2;
    }

    static void checkRoundingUnnecessary(boolean condition) {
        if (!condition) {
            throw new ArithmeticException("mode was UNNECESSARY, but rounding was necessary");
        }
    }

    static void checkInRange(boolean condition) {
        if (!condition) {
            throw new ArithmeticException("not in range");
        }
    }

    static void checkNoOverflow(boolean condition) {
        if (!condition) {
            throw new ArithmeticException("overflow");
        }
    }

    private MathPreconditions() {
    }
}

