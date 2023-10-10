/*
 * Decompiled with CFR 0.152.
 */
package com.google.common.math;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.math.IntMath;
import com.google.common.math.MathPreconditions;
import java.math.RoundingMode;

@GwtCompatible(emulated=true)
public final class LongMath {
    @VisibleForTesting
    static final long MAX_POWER_OF_SQRT2_UNSIGNED = -5402926248376769404L;
    @VisibleForTesting
    static final byte[] maxLog10ForLeadingZeros = new byte[]{19, 18, 18, 18, 18, 17, 17, 17, 16, 16, 16, 15, 15, 15, 15, 14, 14, 14, 13, 13, 13, 12, 12, 12, 12, 11, 11, 11, 10, 10, 10, 9, 9, 9, 9, 8, 8, 8, 7, 7, 7, 6, 6, 6, 6, 5, 5, 5, 4, 4, 4, 3, 3, 3, 3, 2, 2, 2, 1, 1, 1, 0, 0, 0};
    @GwtIncompatible(value="TODO")
    @VisibleForTesting
    static final long[] powersOf10 = new long[]{1L, 10L, 100L, 1000L, 10000L, 100000L, 1000000L, 10000000L, 100000000L, 1000000000L, 10000000000L, 100000000000L, 1000000000000L, 10000000000000L, 100000000000000L, 1000000000000000L, 10000000000000000L, 100000000000000000L, 1000000000000000000L};
    @GwtIncompatible(value="TODO")
    @VisibleForTesting
    static final long[] halfPowersOf10 = new long[]{3L, 31L, 316L, 3162L, 31622L, 316227L, 3162277L, 31622776L, 316227766L, 3162277660L, 31622776601L, 316227766016L, 3162277660168L, 31622776601683L, 316227766016837L, 3162277660168379L, 31622776601683793L, 316227766016837933L, 3162277660168379331L};
    @VisibleForTesting
    static final long FLOOR_SQRT_MAX_LONG = 3037000499L;
    static final long[] factorials = new long[]{1L, 1L, 2L, 6L, 24L, 120L, 720L, 5040L, 40320L, 362880L, 3628800L, 39916800L, 479001600L, 6227020800L, 87178291200L, 1307674368000L, 20922789888000L, 355687428096000L, 6402373705728000L, 121645100408832000L, 2432902008176640000L};
    static final int[] biggestBinomials = new int[]{Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, 3810779, 121977, 16175, 4337, 1733, 887, 534, 361, 265, 206, 169, 143, 125, 111, 101, 94, 88, 83, 79, 76, 74, 72, 70, 69, 68, 67, 67, 66, 66, 66, 66};
    @VisibleForTesting
    static final int[] biggestSimpleBinomials = new int[]{Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, 2642246, 86251, 11724, 3218, 1313, 684, 419, 287, 214, 169, 139, 119, 105, 95, 87, 81, 76, 73, 70, 68, 66, 64, 63, 62, 62, 61, 61, 61};

    public static boolean isPowerOfTwo(long x2) {
        return x2 > 0L & (x2 & x2 - 1L) == 0L;
    }

    @VisibleForTesting
    static int lessThanBranchFree(long x2, long y2) {
        return (int)((x2 - y2 ^ 0xFFFFFFFFFFFFFFFFL ^ 0xFFFFFFFFFFFFFFFFL) >>> 63);
    }

    public static int log2(long x2, RoundingMode mode) {
        MathPreconditions.checkPositive("x", x2);
        switch (mode) {
            case UNNECESSARY: {
                MathPreconditions.checkRoundingUnnecessary(LongMath.isPowerOfTwo(x2));
            }
            case DOWN: 
            case FLOOR: {
                return 63 - Long.numberOfLeadingZeros(x2);
            }
            case UP: 
            case CEILING: {
                return 64 - Long.numberOfLeadingZeros(x2 - 1L);
            }
            case HALF_DOWN: 
            case HALF_UP: 
            case HALF_EVEN: {
                int leadingZeros = Long.numberOfLeadingZeros(x2);
                long cmp = -5402926248376769404L >>> leadingZeros;
                int logFloor = 63 - leadingZeros;
                return logFloor + LongMath.lessThanBranchFree(cmp, x2);
            }
        }
        throw new AssertionError((Object)"impossible");
    }

    @GwtIncompatible(value="TODO")
    public static int log10(long x2, RoundingMode mode) {
        MathPreconditions.checkPositive("x", x2);
        int logFloor = LongMath.log10Floor(x2);
        long floorPow = powersOf10[logFloor];
        switch (mode) {
            case UNNECESSARY: {
                MathPreconditions.checkRoundingUnnecessary(x2 == floorPow);
            }
            case DOWN: 
            case FLOOR: {
                return logFloor;
            }
            case UP: 
            case CEILING: {
                return logFloor + LongMath.lessThanBranchFree(floorPow, x2);
            }
            case HALF_DOWN: 
            case HALF_UP: 
            case HALF_EVEN: {
                return logFloor + LongMath.lessThanBranchFree(halfPowersOf10[logFloor], x2);
            }
        }
        throw new AssertionError();
    }

    @GwtIncompatible(value="TODO")
    static int log10Floor(long x2) {
        byte y2 = maxLog10ForLeadingZeros[Long.numberOfLeadingZeros(x2)];
        return y2 - LongMath.lessThanBranchFree(x2, powersOf10[y2]);
    }

    @GwtIncompatible(value="TODO")
    public static long pow(long b2, int k2) {
        MathPreconditions.checkNonNegative("exponent", k2);
        if (-2L <= b2 && b2 <= 2L) {
            switch ((int)b2) {
                case 0: {
                    return k2 == 0 ? 1L : 0L;
                }
                case 1: {
                    return 1L;
                }
                case -1: {
                    return (k2 & 1) == 0 ? 1L : -1L;
                }
                case 2: {
                    return k2 < 64 ? 1L << k2 : 0L;
                }
                case -2: {
                    if (k2 < 64) {
                        return (k2 & 1) == 0 ? 1L << k2 : -(1L << k2);
                    }
                    return 0L;
                }
            }
            throw new AssertionError();
        }
        long accum = 1L;
        while (true) {
            switch (k2) {
                case 0: {
                    return accum;
                }
                case 1: {
                    return accum * b2;
                }
            }
            accum *= (k2 & 1) == 0 ? 1L : b2;
            b2 *= b2;
            k2 >>= 1;
        }
    }

    @GwtIncompatible(value="TODO")
    public static long sqrt(long x2, RoundingMode mode) {
        MathPreconditions.checkNonNegative("x", x2);
        if (LongMath.fitsInInt(x2)) {
            return IntMath.sqrt((int)x2, mode);
        }
        long guess = (long)Math.sqrt(x2);
        long guessSquared = guess * guess;
        switch (mode) {
            case UNNECESSARY: {
                MathPreconditions.checkRoundingUnnecessary(guessSquared == x2);
                return guess;
            }
            case DOWN: 
            case FLOOR: {
                if (x2 < guessSquared) {
                    return guess - 1L;
                }
                return guess;
            }
            case UP: 
            case CEILING: {
                if (x2 > guessSquared) {
                    return guess + 1L;
                }
                return guess;
            }
            case HALF_DOWN: 
            case HALF_UP: 
            case HALF_EVEN: {
                long sqrtFloor = guess - (long)(x2 < guessSquared ? 1 : 0);
                long halfSquare = sqrtFloor * sqrtFloor + sqrtFloor;
                return sqrtFloor + (long)LongMath.lessThanBranchFree(halfSquare, x2);
            }
        }
        throw new AssertionError();
    }

    @GwtIncompatible(value="TODO")
    public static long divide(long p2, long q2, RoundingMode mode) {
        boolean increment;
        Preconditions.checkNotNull(mode);
        long div = p2 / q2;
        long rem = p2 - q2 * div;
        if (rem == 0L) {
            return div;
        }
        int signum = 1 | (int)((p2 ^ q2) >> 63);
        switch (mode) {
            case UNNECESSARY: {
                MathPreconditions.checkRoundingUnnecessary(rem == 0L);
            }
            case DOWN: {
                increment = false;
                break;
            }
            case UP: {
                increment = true;
                break;
            }
            case CEILING: {
                increment = signum > 0;
                break;
            }
            case FLOOR: {
                increment = signum < 0;
                break;
            }
            case HALF_DOWN: 
            case HALF_UP: 
            case HALF_EVEN: {
                long absRem = Math.abs(rem);
                long cmpRemToHalfDivisor = absRem - (Math.abs(q2) - absRem);
                if (cmpRemToHalfDivisor == 0L) {
                    increment = mode == RoundingMode.HALF_UP | mode == RoundingMode.HALF_EVEN & (div & 1L) != 0L;
                    break;
                }
                increment = cmpRemToHalfDivisor > 0L;
                break;
            }
            default: {
                throw new AssertionError();
            }
        }
        return increment ? div + (long)signum : div;
    }

    @GwtIncompatible(value="TODO")
    public static int mod(long x2, int m2) {
        return (int)LongMath.mod(x2, (long)m2);
    }

    @GwtIncompatible(value="TODO")
    public static long mod(long x2, long m2) {
        if (m2 <= 0L) {
            throw new ArithmeticException("Modulus must be positive");
        }
        long result = x2 % m2;
        return result >= 0L ? result : result + m2;
    }

    public static long gcd(long a2, long b2) {
        MathPreconditions.checkNonNegative("a", a2);
        MathPreconditions.checkNonNegative("b", b2);
        if (a2 == 0L) {
            return b2;
        }
        if (b2 == 0L) {
            return a2;
        }
        int aTwos = Long.numberOfTrailingZeros(a2);
        a2 >>= aTwos;
        int bTwos = Long.numberOfTrailingZeros(b2);
        b2 >>= bTwos;
        while (a2 != b2) {
            long delta = a2 - b2;
            long minDeltaOrZero = delta & delta >> 63;
            a2 = delta - minDeltaOrZero - minDeltaOrZero;
            b2 += minDeltaOrZero;
            a2 >>= Long.numberOfTrailingZeros(a2);
        }
        return a2 << Math.min(aTwos, bTwos);
    }

    @GwtIncompatible(value="TODO")
    public static long checkedAdd(long a2, long b2) {
        long result = a2 + b2;
        MathPreconditions.checkNoOverflow((a2 ^ b2) < 0L | (a2 ^ result) >= 0L);
        return result;
    }

    @GwtIncompatible(value="TODO")
    public static long checkedSubtract(long a2, long b2) {
        long result = a2 - b2;
        MathPreconditions.checkNoOverflow((a2 ^ b2) >= 0L | (a2 ^ result) >= 0L);
        return result;
    }

    @GwtIncompatible(value="TODO")
    public static long checkedMultiply(long a2, long b2) {
        int leadingZeros = Long.numberOfLeadingZeros(a2) + Long.numberOfLeadingZeros(a2 ^ 0xFFFFFFFFFFFFFFFFL) + Long.numberOfLeadingZeros(b2) + Long.numberOfLeadingZeros(b2 ^ 0xFFFFFFFFFFFFFFFFL);
        if (leadingZeros > 65) {
            return a2 * b2;
        }
        MathPreconditions.checkNoOverflow(leadingZeros >= 64);
        MathPreconditions.checkNoOverflow(a2 >= 0L | b2 != Long.MIN_VALUE);
        long result = a2 * b2;
        MathPreconditions.checkNoOverflow(a2 == 0L || result / a2 == b2);
        return result;
    }

    @GwtIncompatible(value="TODO")
    public static long checkedPow(long b2, int k2) {
        MathPreconditions.checkNonNegative("exponent", k2);
        if (b2 >= -2L & b2 <= 2L) {
            switch ((int)b2) {
                case 0: {
                    return k2 == 0 ? 1L : 0L;
                }
                case 1: {
                    return 1L;
                }
                case -1: {
                    return (k2 & 1) == 0 ? 1L : -1L;
                }
                case 2: {
                    MathPreconditions.checkNoOverflow(k2 < 63);
                    return 1L << k2;
                }
                case -2: {
                    MathPreconditions.checkNoOverflow(k2 < 64);
                    return (k2 & 1) == 0 ? 1L << k2 : -1L << k2;
                }
            }
            throw new AssertionError();
        }
        long accum = 1L;
        while (true) {
            switch (k2) {
                case 0: {
                    return accum;
                }
                case 1: {
                    return LongMath.checkedMultiply(accum, b2);
                }
            }
            if ((k2 & 1) != 0) {
                accum = LongMath.checkedMultiply(accum, b2);
            }
            if ((k2 >>= 1) <= 0) continue;
            MathPreconditions.checkNoOverflow(b2 <= 3037000499L);
            b2 *= b2;
        }
    }

    @GwtIncompatible(value="TODO")
    public static long factorial(int n2) {
        MathPreconditions.checkNonNegative("n", n2);
        return n2 < factorials.length ? factorials[n2] : Long.MAX_VALUE;
    }

    public static long binomial(int n2, int k2) {
        MathPreconditions.checkNonNegative("n", n2);
        MathPreconditions.checkNonNegative("k", k2);
        Preconditions.checkArgument(k2 <= n2, "k (%s) > n (%s)", k2, n2);
        if (k2 > n2 >> 1) {
            k2 = n2 - k2;
        }
        switch (k2) {
            case 0: {
                return 1L;
            }
            case 1: {
                return n2;
            }
        }
        if (n2 < factorials.length) {
            return factorials[n2] / (factorials[k2] * factorials[n2 - k2]);
        }
        if (k2 >= biggestBinomials.length || n2 > biggestBinomials[k2]) {
            return Long.MAX_VALUE;
        }
        if (k2 < biggestSimpleBinomials.length && n2 <= biggestSimpleBinomials[k2]) {
            long result = n2--;
            for (int i2 = 2; i2 <= k2; ++i2) {
                result *= (long)n2;
                result /= (long)i2;
                --n2;
            }
            return result;
        }
        int nBits = LongMath.log2(n2, RoundingMode.CEILING);
        long result = 1L;
        long numerator = n2--;
        long denominator = 1L;
        int numeratorBits = nBits;
        int i3 = 2;
        while (i3 <= k2) {
            if (numeratorBits + nBits < 63) {
                numerator *= (long)n2;
                denominator *= (long)i3;
                numeratorBits += nBits;
            } else {
                result = LongMath.multiplyFraction(result, numerator, denominator);
                numerator = n2;
                denominator = i3;
                numeratorBits = nBits;
            }
            ++i3;
            --n2;
        }
        return LongMath.multiplyFraction(result, numerator, denominator);
    }

    static long multiplyFraction(long x2, long numerator, long denominator) {
        if (x2 == 1L) {
            return numerator / denominator;
        }
        long commonDivisor = LongMath.gcd(x2, denominator);
        return (x2 /= commonDivisor) * (numerator / (denominator /= commonDivisor));
    }

    static boolean fitsInInt(long x2) {
        return (long)((int)x2) == x2;
    }

    public static long mean(long x2, long y2) {
        return (x2 & y2) + ((x2 ^ y2) >> 1);
    }

    private LongMath() {
    }
}

