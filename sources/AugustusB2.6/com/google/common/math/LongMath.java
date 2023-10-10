// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.math;

import com.google.common.primitives.UnsignedLongs;
import com.google.common.primitives.Longs;
import com.google.common.base.Preconditions;
import java.math.RoundingMode;
import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible(emulated = true)
public final class LongMath
{
    @VisibleForTesting
    static final long MAX_SIGNED_POWER_OF_TWO = 4611686018427387904L;
    @VisibleForTesting
    static final long MAX_POWER_OF_SQRT2_UNSIGNED = -5402926248376769404L;
    @VisibleForTesting
    static final byte[] maxLog10ForLeadingZeros;
    @GwtIncompatible
    @VisibleForTesting
    static final long[] powersOf10;
    @GwtIncompatible
    @VisibleForTesting
    static final long[] halfPowersOf10;
    @VisibleForTesting
    static final long FLOOR_SQRT_MAX_LONG = 3037000499L;
    static final long[] factorials;
    static final int[] biggestBinomials;
    @VisibleForTesting
    static final int[] biggestSimpleBinomials;
    private static final int SIEVE_30 = -545925251;
    private static final long[][] millerRabinBaseSets;
    
    @Beta
    public static long ceilingPowerOfTwo(final long x) {
        MathPreconditions.checkPositive("x", x);
        if (x > 4611686018427387904L) {
            throw new ArithmeticException(new StringBuilder(70).append("ceilingPowerOfTwo(").append(x).append(") is not representable as a long").toString());
        }
        return 1L << -Long.numberOfLeadingZeros(x - 1L);
    }
    
    @Beta
    public static long floorPowerOfTwo(final long x) {
        MathPreconditions.checkPositive("x", x);
        return 1L << 63 - Long.numberOfLeadingZeros(x);
    }
    
    public static boolean isPowerOfTwo(final long x) {
        return x > 0L & (x & x - 1L) == 0x0L;
    }
    
    @VisibleForTesting
    static int lessThanBranchFree(final long x, final long y) {
        return (int)(~(~(x - y)) >>> 63);
    }
    
    public static int log2(final long x, final RoundingMode mode) {
        MathPreconditions.checkPositive("x", x);
        switch (mode) {
            case UNNECESSARY: {
                MathPreconditions.checkRoundingUnnecessary(isPowerOfTwo(x));
            }
            case DOWN:
            case FLOOR: {
                return 63 - Long.numberOfLeadingZeros(x);
            }
            case UP:
            case CEILING: {
                return 64 - Long.numberOfLeadingZeros(x - 1L);
            }
            case HALF_DOWN:
            case HALF_UP:
            case HALF_EVEN: {
                final int leadingZeros = Long.numberOfLeadingZeros(x);
                final long cmp = -5402926248376769404L >>> leadingZeros;
                final int logFloor = 63 - leadingZeros;
                return logFloor + lessThanBranchFree(cmp, x);
            }
            default: {
                throw new AssertionError((Object)"impossible");
            }
        }
    }
    
    @GwtIncompatible
    public static int log10(final long x, final RoundingMode mode) {
        MathPreconditions.checkPositive("x", x);
        final int logFloor = log10Floor(x);
        final long floorPow = LongMath.powersOf10[logFloor];
        switch (mode) {
            case UNNECESSARY: {
                MathPreconditions.checkRoundingUnnecessary(x == floorPow);
            }
            case DOWN:
            case FLOOR: {
                return logFloor;
            }
            case UP:
            case CEILING: {
                return logFloor + lessThanBranchFree(floorPow, x);
            }
            case HALF_DOWN:
            case HALF_UP:
            case HALF_EVEN: {
                return logFloor + lessThanBranchFree(LongMath.halfPowersOf10[logFloor], x);
            }
            default: {
                throw new AssertionError();
            }
        }
    }
    
    @GwtIncompatible
    static int log10Floor(final long x) {
        final int y = LongMath.maxLog10ForLeadingZeros[Long.numberOfLeadingZeros(x)];
        return y - lessThanBranchFree(x, LongMath.powersOf10[y]);
    }
    
    @GwtIncompatible
    public static long pow(long b, int k) {
        MathPreconditions.checkNonNegative("exponent", k);
        if (-2L <= b && b <= 2L) {
            switch ((int)b) {
                case 0: {
                    return (k == 0) ? 1 : 0;
                }
                case 1: {
                    return 1L;
                }
                case -1: {
                    return ((k & 0x1) == 0x0) ? 1L : -1L;
                }
                case 2: {
                    return (k < 64) ? (1L << k) : 0L;
                }
                case -2: {
                    if (k < 64) {
                        return ((k & 0x1) == 0x0) ? (1L << k) : (-(1L << k));
                    }
                    return 0L;
                }
                default: {
                    throw new AssertionError();
                }
            }
        }
        else {
            long accum = 1L;
            while (true) {
                switch (k) {
                    case 0: {
                        return accum;
                    }
                    case 1: {
                        return accum * b;
                    }
                    default: {
                        accum *= (((k & 0x1) == 0x0) ? 1L : b);
                        b *= b;
                        k >>= 1;
                        continue;
                    }
                }
            }
        }
    }
    
    @GwtIncompatible
    public static long sqrt(final long x, final RoundingMode mode) {
        MathPreconditions.checkNonNegative("x", x);
        if (fitsInInt(x)) {
            return IntMath.sqrt((int)x, mode);
        }
        final long guess = (long)Math.sqrt((double)x);
        final long guessSquared = guess * guess;
        switch (mode) {
            case UNNECESSARY: {
                MathPreconditions.checkRoundingUnnecessary(guessSquared == x);
                return guess;
            }
            case DOWN:
            case FLOOR: {
                if (x < guessSquared) {
                    return guess - 1L;
                }
                return guess;
            }
            case UP:
            case CEILING: {
                if (x > guessSquared) {
                    return guess + 1L;
                }
                return guess;
            }
            case HALF_DOWN:
            case HALF_UP:
            case HALF_EVEN: {
                final long sqrtFloor = guess - ((x < guessSquared) ? 1 : 0);
                final long halfSquare = sqrtFloor * sqrtFloor + sqrtFloor;
                return sqrtFloor + lessThanBranchFree(halfSquare, x);
            }
            default: {
                throw new AssertionError();
            }
        }
    }
    
    @GwtIncompatible
    public static long divide(final long p, final long q, final RoundingMode mode) {
        Preconditions.checkNotNull(mode);
        final long div = p / q;
        final long rem = p - q * div;
        if (rem == 0L) {
            return div;
        }
        final int signum = 0x1 | (int)((p ^ q) >> 63);
        boolean increment = false;
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
                increment = (signum > 0);
                break;
            }
            case FLOOR: {
                increment = (signum < 0);
                break;
            }
            case HALF_DOWN:
            case HALF_UP:
            case HALF_EVEN: {
                final long absRem = Math.abs(rem);
                final long cmpRemToHalfDivisor = absRem - (Math.abs(q) - absRem);
                if (cmpRemToHalfDivisor == 0L) {
                    increment = (mode == RoundingMode.HALF_UP | (mode == RoundingMode.HALF_EVEN & (div & 0x1L) != 0x0L));
                    break;
                }
                increment = (cmpRemToHalfDivisor > 0L);
                break;
            }
            default: {
                throw new AssertionError();
            }
        }
        return increment ? (div + signum) : div;
    }
    
    @GwtIncompatible
    public static int mod(final long x, final int m) {
        return (int)mod(x, (long)m);
    }
    
    @GwtIncompatible
    public static long mod(final long x, final long m) {
        if (m <= 0L) {
            throw new ArithmeticException("Modulus must be positive");
        }
        final long result = x % m;
        return (result >= 0L) ? result : (result + m);
    }
    
    public static long gcd(long a, long b) {
        MathPreconditions.checkNonNegative("a", a);
        MathPreconditions.checkNonNegative("b", b);
        if (a == 0L) {
            return b;
        }
        if (b == 0L) {
            return a;
        }
        final int aTwos = Long.numberOfTrailingZeros(a);
        a >>= aTwos;
        final int bTwos = Long.numberOfTrailingZeros(b);
        long delta;
        long minDeltaOrZero;
        for (b >>= bTwos; a != b; a = delta - minDeltaOrZero - minDeltaOrZero, b += minDeltaOrZero, a >>= Long.numberOfTrailingZeros(a)) {
            delta = a - b;
            minDeltaOrZero = (delta & delta >> 63);
        }
        return a << Math.min(aTwos, bTwos);
    }
    
    @GwtIncompatible
    public static long checkedAdd(final long a, final long b) {
        final long result = a + b;
        MathPreconditions.checkNoOverflow((a ^ b) < 0L | (a ^ result) >= 0L, "checkedAdd", a, b);
        return result;
    }
    
    @GwtIncompatible
    public static long checkedSubtract(final long a, final long b) {
        final long result = a - b;
        MathPreconditions.checkNoOverflow((a ^ b) >= 0L | (a ^ result) >= 0L, "checkedSubtract", a, b);
        return result;
    }
    
    public static long checkedMultiply(final long a, final long b) {
        final int leadingZeros = Long.numberOfLeadingZeros(a) + Long.numberOfLeadingZeros(~a) + Long.numberOfLeadingZeros(b) + Long.numberOfLeadingZeros(~b);
        if (leadingZeros > 65) {
            return a * b;
        }
        MathPreconditions.checkNoOverflow(leadingZeros >= 64, "checkedMultiply", a, b);
        MathPreconditions.checkNoOverflow(a >= 0L | b != Long.MIN_VALUE, "checkedMultiply", a, b);
        final long result = a * b;
        MathPreconditions.checkNoOverflow(a == 0L || result / a == b, "checkedMultiply", a, b);
        return result;
    }
    
    @GwtIncompatible
    public static long checkedPow(long b, int k) {
        MathPreconditions.checkNonNegative("exponent", k);
        if (b >= -2L & b <= 2L) {
            switch ((int)b) {
                case 0: {
                    return (k == 0) ? 1 : 0;
                }
                case 1: {
                    return 1L;
                }
                case -1: {
                    return ((k & 0x1) == 0x0) ? 1L : -1L;
                }
                case 2: {
                    MathPreconditions.checkNoOverflow(k < 63, "checkedPow", b, k);
                    return 1L << k;
                }
                case -2: {
                    MathPreconditions.checkNoOverflow(k < 64, "checkedPow", b, k);
                    return ((k & 0x1) == 0x0) ? (1L << k) : (-1L << k);
                }
                default: {
                    throw new AssertionError();
                }
            }
        }
        else {
            long accum = 1L;
            while (true) {
                switch (k) {
                    case 0: {
                        return accum;
                    }
                    case 1: {
                        return checkedMultiply(accum, b);
                    }
                    default: {
                        if ((k & 0x1) != 0x0) {
                            accum = checkedMultiply(accum, b);
                        }
                        k >>= 1;
                        if (k > 0) {
                            MathPreconditions.checkNoOverflow(-3037000499L <= b && b <= 3037000499L, "checkedPow", b, k);
                            b *= b;
                            continue;
                        }
                        continue;
                    }
                }
            }
        }
    }
    
    @Beta
    public static long saturatedAdd(final long a, final long b) {
        final long naiveSum = a + b;
        if ((a ^ b) < 0L | (a ^ naiveSum) >= 0L) {
            return naiveSum;
        }
        return Long.MAX_VALUE + (naiveSum >>> 63 ^ 0x1L);
    }
    
    @Beta
    public static long saturatedSubtract(final long a, final long b) {
        final long naiveDifference = a - b;
        if ((a ^ b) >= 0L | (a ^ naiveDifference) >= 0L) {
            return naiveDifference;
        }
        return Long.MAX_VALUE + (naiveDifference >>> 63 ^ 0x1L);
    }
    
    @Beta
    public static long saturatedMultiply(final long a, final long b) {
        final int leadingZeros = Long.numberOfLeadingZeros(a) + Long.numberOfLeadingZeros(~a) + Long.numberOfLeadingZeros(b) + Long.numberOfLeadingZeros(~b);
        if (leadingZeros > 65) {
            return a * b;
        }
        final long limit = Long.MAX_VALUE + ((a ^ b) >>> 63);
        if (leadingZeros < 64 | (a < 0L & b == Long.MIN_VALUE)) {
            return limit;
        }
        final long result = a * b;
        if (a == 0L || result / a == b) {
            return result;
        }
        return limit;
    }
    
    @Beta
    public static long saturatedPow(long b, int k) {
        MathPreconditions.checkNonNegative("exponent", k);
        if (b >= -2L & b <= 2L) {
            switch ((int)b) {
                case 0: {
                    return (k == 0) ? 1 : 0;
                }
                case 1: {
                    return 1L;
                }
                case -1: {
                    return ((k & 0x1) == 0x0) ? 1L : -1L;
                }
                case 2: {
                    if (k >= 63) {
                        return Long.MAX_VALUE;
                    }
                    return 1L << k;
                }
                case -2: {
                    if (k >= 64) {
                        return Long.MAX_VALUE + (k & 0x1);
                    }
                    return ((k & 0x1) == 0x0) ? (1L << k) : (-1L << k);
                }
                default: {
                    throw new AssertionError();
                }
            }
        }
        else {
            long accum = 1L;
            final long limit = Long.MAX_VALUE + (b >>> 63 & (long)(k & 0x1));
            while (true) {
                switch (k) {
                    case 0: {
                        return accum;
                    }
                    case 1: {
                        return saturatedMultiply(accum, b);
                    }
                    default: {
                        if ((k & 0x1) != 0x0) {
                            accum = saturatedMultiply(accum, b);
                        }
                        k >>= 1;
                        if (k <= 0) {
                            continue;
                        }
                        if (-3037000499L > b | b > 3037000499L) {
                            return limit;
                        }
                        b *= b;
                        continue;
                    }
                }
            }
        }
    }
    
    @GwtIncompatible
    public static long factorial(final int n) {
        MathPreconditions.checkNonNegative("n", n);
        return (n < LongMath.factorials.length) ? LongMath.factorials[n] : Long.MAX_VALUE;
    }
    
    public static long binomial(int n, int k) {
        MathPreconditions.checkNonNegative("n", n);
        MathPreconditions.checkNonNegative("k", k);
        Preconditions.checkArgument(k <= n, "k (%s) > n (%s)", k, n);
        if (k > n >> 1) {
            k = n - k;
        }
        switch (k) {
            case 0: {
                return 1L;
            }
            case 1: {
                return n;
            }
            default: {
                if (n < LongMath.factorials.length) {
                    return LongMath.factorials[n] / (LongMath.factorials[k] * LongMath.factorials[n - k]);
                }
                if (k >= LongMath.biggestBinomials.length || n > LongMath.biggestBinomials[k]) {
                    return Long.MAX_VALUE;
                }
                if (k < LongMath.biggestSimpleBinomials.length && n <= LongMath.biggestSimpleBinomials[k]) {
                    long result = n--;
                    for (int i = 2; i <= k; ++i) {
                        result *= n;
                        result /= i;
                        --n;
                    }
                    return result;
                }
                final int nBits = log2(n, RoundingMode.CEILING);
                long result2 = 1L;
                long numerator = n--;
                long denominator = 1L;
                int numeratorBits = nBits;
                for (int j = 2; j <= k; ++j, --n) {
                    if (numeratorBits + nBits < 63) {
                        numerator *= n;
                        denominator *= j;
                        numeratorBits += nBits;
                    }
                    else {
                        result2 = multiplyFraction(result2, numerator, denominator);
                        numerator = n;
                        denominator = j;
                        numeratorBits = nBits;
                    }
                }
                return multiplyFraction(result2, numerator, denominator);
            }
        }
    }
    
    static long multiplyFraction(long x, final long numerator, long denominator) {
        if (x == 1L) {
            return numerator / denominator;
        }
        final long commonDivisor = gcd(x, denominator);
        x /= commonDivisor;
        denominator /= commonDivisor;
        return x * (numerator / denominator);
    }
    
    static boolean fitsInInt(final long x) {
        return (int)x == x;
    }
    
    public static long mean(final long x, final long y) {
        return (x & y) + ((x ^ y) >> 1);
    }
    
    @GwtIncompatible
    @Beta
    public static boolean isPrime(final long n) {
        if (n < 2L) {
            MathPreconditions.checkNonNegative("n", n);
            return false;
        }
        if (n < 66L) {
            final long mask = 722865708377213483L;
            return (mask >> (int)n - 2 & 0x1L) != 0x0L;
        }
        if ((0xDF75D77D & 1 << (int)(n % 30L)) != 0x0) {
            return false;
        }
        if (n % 7L == 0L || n % 11L == 0L || n % 13L == 0L) {
            return false;
        }
        if (n < 289L) {
            return true;
        }
        for (final long[] baseSet : LongMath.millerRabinBaseSets) {
            if (n <= baseSet[0]) {
                for (int i = 1; i < baseSet.length; ++i) {
                    if (!MillerRabinTester.test(baseSet[i], n)) {
                        return false;
                    }
                }
                return true;
            }
        }
        throw new AssertionError();
    }
    
    @GwtIncompatible
    public static double roundToDouble(final long x, final RoundingMode mode) {
        final double roundArbitrarily = (double)x;
        final long roundArbitrarilyAsLong = (long)roundArbitrarily;
        int cmpXToRoundArbitrarily;
        if (roundArbitrarilyAsLong == Long.MAX_VALUE) {
            cmpXToRoundArbitrarily = -1;
        }
        else {
            cmpXToRoundArbitrarily = Longs.compare(x, roundArbitrarilyAsLong);
        }
        switch (mode) {
            case UNNECESSARY: {
                MathPreconditions.checkRoundingUnnecessary(cmpXToRoundArbitrarily == 0);
                return roundArbitrarily;
            }
            case FLOOR: {
                return (cmpXToRoundArbitrarily >= 0) ? roundArbitrarily : DoubleUtils.nextDown(roundArbitrarily);
            }
            case CEILING: {
                return (cmpXToRoundArbitrarily <= 0) ? roundArbitrarily : Math.nextUp(roundArbitrarily);
            }
            case DOWN: {
                if (x >= 0L) {
                    return (cmpXToRoundArbitrarily >= 0) ? roundArbitrarily : DoubleUtils.nextDown(roundArbitrarily);
                }
                return (cmpXToRoundArbitrarily <= 0) ? roundArbitrarily : Math.nextUp(roundArbitrarily);
            }
            case UP: {
                if (x >= 0L) {
                    return (cmpXToRoundArbitrarily <= 0) ? roundArbitrarily : Math.nextUp(roundArbitrarily);
                }
                return (cmpXToRoundArbitrarily >= 0) ? roundArbitrarily : DoubleUtils.nextDown(roundArbitrarily);
            }
            case HALF_DOWN:
            case HALF_UP:
            case HALF_EVEN: {
                double roundFloorAsDouble;
                long roundFloor;
                double roundCeilingAsDouble;
                long roundCeiling;
                if (cmpXToRoundArbitrarily >= 0) {
                    roundFloorAsDouble = roundArbitrarily;
                    roundFloor = roundArbitrarilyAsLong;
                    roundCeilingAsDouble = Math.nextUp(roundArbitrarily);
                    roundCeiling = (long)Math.ceil(roundCeilingAsDouble);
                }
                else {
                    roundCeilingAsDouble = roundArbitrarily;
                    roundCeiling = roundArbitrarilyAsLong;
                    roundFloorAsDouble = DoubleUtils.nextDown(roundArbitrarily);
                    roundFloor = (long)Math.floor(roundFloorAsDouble);
                }
                final long deltaToFloor = x - roundFloor;
                long deltaToCeiling = roundCeiling - x;
                if (roundCeiling == Long.MAX_VALUE) {
                    ++deltaToCeiling;
                }
                final int diff = Longs.compare(deltaToFloor, deltaToCeiling);
                if (diff < 0) {
                    return roundFloorAsDouble;
                }
                if (diff > 0) {
                    return roundCeilingAsDouble;
                }
                switch (mode) {
                    case HALF_EVEN: {
                        return ((DoubleUtils.getSignificand(roundFloorAsDouble) & 0x1L) == 0x0L) ? roundFloorAsDouble : roundCeilingAsDouble;
                    }
                    case HALF_DOWN: {
                        return (x >= 0L) ? roundFloorAsDouble : roundCeilingAsDouble;
                    }
                    case HALF_UP: {
                        return (x >= 0L) ? roundCeilingAsDouble : roundFloorAsDouble;
                    }
                    default: {
                        throw new AssertionError((Object)"impossible");
                    }
                }
                break;
            }
            default: {
                throw new AssertionError((Object)"impossible");
            }
        }
    }
    
    private LongMath() {
    }
    
    static {
        maxLog10ForLeadingZeros = new byte[] { 19, 18, 18, 18, 18, 17, 17, 17, 16, 16, 16, 15, 15, 15, 15, 14, 14, 14, 13, 13, 13, 12, 12, 12, 12, 11, 11, 11, 10, 10, 10, 9, 9, 9, 9, 8, 8, 8, 7, 7, 7, 6, 6, 6, 6, 5, 5, 5, 4, 4, 4, 3, 3, 3, 3, 2, 2, 2, 1, 1, 1, 0, 0, 0 };
        powersOf10 = new long[] { 1L, 10L, 100L, 1000L, 10000L, 100000L, 1000000L, 10000000L, 100000000L, 1000000000L, 10000000000L, 100000000000L, 1000000000000L, 10000000000000L, 100000000000000L, 1000000000000000L, 10000000000000000L, 100000000000000000L, 1000000000000000000L };
        halfPowersOf10 = new long[] { 3L, 31L, 316L, 3162L, 31622L, 316227L, 3162277L, 31622776L, 316227766L, 3162277660L, 31622776601L, 316227766016L, 3162277660168L, 31622776601683L, 316227766016837L, 3162277660168379L, 31622776601683793L, 316227766016837933L, 3162277660168379331L };
        factorials = new long[] { 1L, 1L, 2L, 6L, 24L, 120L, 720L, 5040L, 40320L, 362880L, 3628800L, 39916800L, 479001600L, 6227020800L, 87178291200L, 1307674368000L, 20922789888000L, 355687428096000L, 6402373705728000L, 121645100408832000L, 2432902008176640000L };
        biggestBinomials = new int[] { Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, 3810779, 121977, 16175, 4337, 1733, 887, 534, 361, 265, 206, 169, 143, 125, 111, 101, 94, 88, 83, 79, 76, 74, 72, 70, 69, 68, 67, 67, 66, 66, 66, 66 };
        biggestSimpleBinomials = new int[] { Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, 2642246, 86251, 11724, 3218, 1313, 684, 419, 287, 214, 169, 139, 119, 105, 95, 87, 81, 76, 73, 70, 68, 66, 64, 63, 62, 62, 61, 61, 61 };
        millerRabinBaseSets = new long[][] { { 291830L, 126401071349994536L }, { 885594168L, 725270293939359937L, 3569819667048198375L }, { 273919523040L, 15L, 7363882082L, 992620450144556L }, { 47636622961200L, 2L, 2570940L, 211991001L, 3749873356L }, { 7999252175582850L, 2L, 4130806001517L, 149795463772692060L, 186635894390467037L, 3967304179347715805L }, { 585226005592931976L, 2L, 123635709730000L, 9233062284813009L, 43835965440333360L, 761179012939631437L, 1263739024124850375L }, { Long.MAX_VALUE, 2L, 325L, 9375L, 28178L, 450775L, 9780504L, 1795265022L } };
    }
    
    private enum MillerRabinTester
    {
        SMALL(0) {
            @Override
            long mulMod(final long a, final long b, final long m) {
                return a * b % m;
            }
            
            @Override
            long squareMod(final long a, final long m) {
                return a * a % m;
            }
        }, 
        LARGE(1) {
            private long plusMod(final long a, final long b, final long m) {
                return (a >= m - b) ? (a + b - m) : (a + b);
            }
            
            private long times2ToThe32Mod(long a, final long m) {
                int remainingPowersOf2 = 32;
                do {
                    final int shift = Math.min(remainingPowersOf2, Long.numberOfLeadingZeros(a));
                    a = UnsignedLongs.remainder(a << shift, m);
                    remainingPowersOf2 -= shift;
                } while (remainingPowersOf2 > 0);
                return a;
            }
            
            @Override
            long mulMod(final long a, final long b, final long m) {
                final long aHi = a >>> 32;
                final long bHi = b >>> 32;
                final long aLo = a & 0xFFFFFFFFL;
                final long bLo = b & 0xFFFFFFFFL;
                long result = this.times2ToThe32Mod(aHi * bHi, m);
                result += aHi * bLo;
                if (result < 0L) {
                    result = UnsignedLongs.remainder(result, m);
                }
                result += aLo * bHi;
                result = this.times2ToThe32Mod(result, m);
                return this.plusMod(result, UnsignedLongs.remainder(aLo * bLo, m), m);
            }
            
            @Override
            long squareMod(final long a, final long m) {
                final long aHi = a >>> 32;
                final long aLo = a & 0xFFFFFFFFL;
                long result = this.times2ToThe32Mod(aHi * aHi, m);
                long hiLo = aHi * aLo * 2L;
                if (hiLo < 0L) {
                    hiLo = UnsignedLongs.remainder(hiLo, m);
                }
                result += hiLo;
                result = this.times2ToThe32Mod(result, m);
                return this.plusMod(result, UnsignedLongs.remainder(aLo * aLo, m), m);
            }
        };
        
        static boolean test(final long base, final long n) {
            return ((n <= 3037000499L) ? MillerRabinTester.SMALL : MillerRabinTester.LARGE).testWitness(base, n);
        }
        
        abstract long mulMod(final long p0, final long p1, final long p2);
        
        abstract long squareMod(final long p0, final long p1);
        
        private long powMod(long a, long p, final long m) {
            long res = 1L;
            while (p != 0L) {
                if ((p & 0x1L) != 0x0L) {
                    res = this.mulMod(res, a, m);
                }
                a = this.squareMod(a, m);
                p >>= 1;
            }
            return res;
        }
        
        private boolean testWitness(long base, final long n) {
            final int r = Long.numberOfTrailingZeros(n - 1L);
            final long d = n - 1L >> r;
            base %= n;
            if (base == 0L) {
                return true;
            }
            long a = this.powMod(base, d, n);
            if (a == 1L) {
                return true;
            }
            int j = 0;
            while (a != n - 1L) {
                if (++j == r) {
                    return false;
                }
                a = this.squareMod(a, n);
            }
            return true;
        }
        
        private static /* synthetic */ MillerRabinTester[] $values() {
            return new MillerRabinTester[] { MillerRabinTester.SMALL, MillerRabinTester.LARGE };
        }
        
        static {
            $VALUES = $values();
        }
    }
}
