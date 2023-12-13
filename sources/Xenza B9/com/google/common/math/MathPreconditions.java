// 
// Decompiled by Procyon v0.6.0
// 

package com.google.common.math;

import java.math.BigInteger;
import javax.annotation.Nullable;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible
final class MathPreconditions
{
    static int checkPositive(@Nullable final String role, final int x) {
        if (x <= 0) {
            final String value = String.valueOf(String.valueOf(role));
            throw new IllegalArgumentException(new StringBuilder(26 + value.length()).append(value).append(" (").append(x).append(") must be > 0").toString());
        }
        return x;
    }
    
    static long checkPositive(@Nullable final String role, final long x) {
        if (x <= 0L) {
            final String value = String.valueOf(String.valueOf(role));
            throw new IllegalArgumentException(new StringBuilder(35 + value.length()).append(value).append(" (").append(x).append(") must be > 0").toString());
        }
        return x;
    }
    
    static BigInteger checkPositive(@Nullable final String role, final BigInteger x) {
        if (x.signum() <= 0) {
            final String value = String.valueOf(String.valueOf(role));
            final String value2 = String.valueOf(String.valueOf(x));
            throw new IllegalArgumentException(new StringBuilder(15 + value.length() + value2.length()).append(value).append(" (").append(value2).append(") must be > 0").toString());
        }
        return x;
    }
    
    static int checkNonNegative(@Nullable final String role, final int x) {
        if (x < 0) {
            final String value = String.valueOf(String.valueOf(role));
            throw new IllegalArgumentException(new StringBuilder(27 + value.length()).append(value).append(" (").append(x).append(") must be >= 0").toString());
        }
        return x;
    }
    
    static long checkNonNegative(@Nullable final String role, final long x) {
        if (x < 0L) {
            final String value = String.valueOf(String.valueOf(role));
            throw new IllegalArgumentException(new StringBuilder(36 + value.length()).append(value).append(" (").append(x).append(") must be >= 0").toString());
        }
        return x;
    }
    
    static BigInteger checkNonNegative(@Nullable final String role, final BigInteger x) {
        if (x.signum() < 0) {
            final String value = String.valueOf(String.valueOf(role));
            final String value2 = String.valueOf(String.valueOf(x));
            throw new IllegalArgumentException(new StringBuilder(16 + value.length() + value2.length()).append(value).append(" (").append(value2).append(") must be >= 0").toString());
        }
        return x;
    }
    
    static double checkNonNegative(@Nullable final String role, final double x) {
        if (x < 0.0) {
            final String value = String.valueOf(String.valueOf(role));
            throw new IllegalArgumentException(new StringBuilder(40 + value.length()).append(value).append(" (").append(x).append(") must be >= 0").toString());
        }
        return x;
    }
    
    static void checkRoundingUnnecessary(final boolean condition) {
        if (!condition) {
            throw new ArithmeticException("mode was UNNECESSARY, but rounding was necessary");
        }
    }
    
    static void checkInRange(final boolean condition) {
        if (!condition) {
            throw new ArithmeticException("not in range");
        }
    }
    
    static void checkNoOverflow(final boolean condition) {
        if (!condition) {
            throw new ArithmeticException("overflow");
        }
    }
    
    private MathPreconditions() {
    }
}
