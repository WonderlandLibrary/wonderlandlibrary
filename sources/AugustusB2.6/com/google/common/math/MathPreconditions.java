// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.math;

import java.math.RoundingMode;
import java.math.BigInteger;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible
@CanIgnoreReturnValue
final class MathPreconditions
{
    static int checkPositive(final String role, final int x) {
        if (x <= 0) {
            throw new IllegalArgumentException(new StringBuilder(26 + String.valueOf(role).length()).append(role).append(" (").append(x).append(") must be > 0").toString());
        }
        return x;
    }
    
    static long checkPositive(final String role, final long x) {
        if (x <= 0L) {
            throw new IllegalArgumentException(new StringBuilder(35 + String.valueOf(role).length()).append(role).append(" (").append(x).append(") must be > 0").toString());
        }
        return x;
    }
    
    static BigInteger checkPositive(final String role, final BigInteger x) {
        if (x.signum() <= 0) {
            final String value = String.valueOf(x);
            throw new IllegalArgumentException(new StringBuilder(15 + String.valueOf(role).length() + String.valueOf(value).length()).append(role).append(" (").append(value).append(") must be > 0").toString());
        }
        return x;
    }
    
    static int checkNonNegative(final String role, final int x) {
        if (x < 0) {
            throw new IllegalArgumentException(new StringBuilder(27 + String.valueOf(role).length()).append(role).append(" (").append(x).append(") must be >= 0").toString());
        }
        return x;
    }
    
    static long checkNonNegative(final String role, final long x) {
        if (x < 0L) {
            throw new IllegalArgumentException(new StringBuilder(36 + String.valueOf(role).length()).append(role).append(" (").append(x).append(") must be >= 0").toString());
        }
        return x;
    }
    
    static BigInteger checkNonNegative(final String role, final BigInteger x) {
        if (x.signum() < 0) {
            final String value = String.valueOf(x);
            throw new IllegalArgumentException(new StringBuilder(16 + String.valueOf(role).length() + String.valueOf(value).length()).append(role).append(" (").append(value).append(") must be >= 0").toString());
        }
        return x;
    }
    
    static double checkNonNegative(final String role, final double x) {
        if (x < 0.0) {
            throw new IllegalArgumentException(new StringBuilder(40 + String.valueOf(role).length()).append(role).append(" (").append(x).append(") must be >= 0").toString());
        }
        return x;
    }
    
    static void checkRoundingUnnecessary(final boolean condition) {
        if (!condition) {
            throw new ArithmeticException("mode was UNNECESSARY, but rounding was necessary");
        }
    }
    
    static void checkInRangeForRoundingInputs(final boolean condition, final double input, final RoundingMode mode) {
        if (!condition) {
            final String value = String.valueOf(mode);
            throw new ArithmeticException(new StringBuilder(83 + String.valueOf(value).length()).append("rounded value is out of range for input ").append(input).append(" and rounding mode ").append(value).toString());
        }
    }
    
    static void checkNoOverflow(final boolean condition, final String methodName, final int a, final int b) {
        if (!condition) {
            throw new ArithmeticException(new StringBuilder(36 + String.valueOf(methodName).length()).append("overflow: ").append(methodName).append("(").append(a).append(", ").append(b).append(")").toString());
        }
    }
    
    static void checkNoOverflow(final boolean condition, final String methodName, final long a, final long b) {
        if (!condition) {
            throw new ArithmeticException(new StringBuilder(54 + String.valueOf(methodName).length()).append("overflow: ").append(methodName).append("(").append(a).append(", ").append(b).append(")").toString());
        }
    }
    
    private MathPreconditions() {
    }
}
