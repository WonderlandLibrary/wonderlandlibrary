/*
 * Decompiled with CFR 0.152.
 */
package com.google.common.math;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.math.DoubleMath;
import com.google.common.math.DoubleUtils;
import com.google.common.math.IntMath;
import com.google.common.math.LongMath;
import com.google.common.math.MathPreconditions;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@GwtCompatible(emulated=true)
public final class BigIntegerMath {
    @VisibleForTesting
    static final int SQRT2_PRECOMPUTE_THRESHOLD = 256;
    @VisibleForTesting
    static final BigInteger SQRT2_PRECOMPUTED_BITS = new BigInteger("16a09e667f3bcc908b2fb1366ea957d3e3adec17512775099da2f590b0667322a", 16);
    private static final double LN_10 = Math.log(10.0);
    private static final double LN_2 = Math.log(2.0);

    public static boolean isPowerOfTwo(BigInteger x2) {
        Preconditions.checkNotNull(x2);
        return x2.signum() > 0 && x2.getLowestSetBit() == x2.bitLength() - 1;
    }

    public static int log2(BigInteger x2, RoundingMode mode) {
        MathPreconditions.checkPositive("x", Preconditions.checkNotNull(x2));
        int logFloor = x2.bitLength() - 1;
        switch (mode) {
            case UNNECESSARY: {
                MathPreconditions.checkRoundingUnnecessary(BigIntegerMath.isPowerOfTwo(x2));
            }
            case DOWN: 
            case FLOOR: {
                return logFloor;
            }
            case UP: 
            case CEILING: {
                return BigIntegerMath.isPowerOfTwo(x2) ? logFloor : logFloor + 1;
            }
            case HALF_DOWN: 
            case HALF_UP: 
            case HALF_EVEN: {
                if (logFloor < 256) {
                    BigInteger halfPower = SQRT2_PRECOMPUTED_BITS.shiftRight(256 - logFloor);
                    if (x2.compareTo(halfPower) <= 0) {
                        return logFloor;
                    }
                    return logFloor + 1;
                }
                BigInteger x22 = x2.pow(2);
                int logX2Floor = x22.bitLength() - 1;
                return logX2Floor < 2 * logFloor + 1 ? logFloor : logFloor + 1;
            }
        }
        throw new AssertionError();
    }

    @GwtIncompatible(value="TODO")
    public static int log10(BigInteger x2, RoundingMode mode) {
        MathPreconditions.checkPositive("x", x2);
        if (BigIntegerMath.fitsInLong(x2)) {
            return LongMath.log10(x2.longValue(), mode);
        }
        int approxLog10 = (int)((double)BigIntegerMath.log2(x2, RoundingMode.FLOOR) * LN_2 / LN_10);
        BigInteger approxPow = BigInteger.TEN.pow(approxLog10);
        int approxCmp = approxPow.compareTo(x2);
        if (approxCmp > 0) {
            do {
                --approxLog10;
            } while ((approxCmp = (approxPow = approxPow.divide(BigInteger.TEN)).compareTo(x2)) > 0);
        } else {
            BigInteger nextPow = BigInteger.TEN.multiply(approxPow);
            int nextCmp = nextPow.compareTo(x2);
            while (nextCmp <= 0) {
                ++approxLog10;
                approxPow = nextPow;
                approxCmp = nextCmp;
                nextPow = BigInteger.TEN.multiply(approxPow);
                nextCmp = nextPow.compareTo(x2);
            }
        }
        int floorLog = approxLog10;
        BigInteger floorPow = approxPow;
        int floorCmp = approxCmp;
        switch (mode) {
            case UNNECESSARY: {
                MathPreconditions.checkRoundingUnnecessary(floorCmp == 0);
            }
            case DOWN: 
            case FLOOR: {
                return floorLog;
            }
            case UP: 
            case CEILING: {
                return floorPow.equals(x2) ? floorLog : floorLog + 1;
            }
            case HALF_DOWN: 
            case HALF_UP: 
            case HALF_EVEN: {
                BigInteger x22 = x2.pow(2);
                BigInteger halfPowerSquared = floorPow.pow(2).multiply(BigInteger.TEN);
                return x22.compareTo(halfPowerSquared) <= 0 ? floorLog : floorLog + 1;
            }
        }
        throw new AssertionError();
    }

    @GwtIncompatible(value="TODO")
    public static BigInteger sqrt(BigInteger x2, RoundingMode mode) {
        MathPreconditions.checkNonNegative("x", x2);
        if (BigIntegerMath.fitsInLong(x2)) {
            return BigInteger.valueOf(LongMath.sqrt(x2.longValue(), mode));
        }
        BigInteger sqrtFloor = BigIntegerMath.sqrtFloor(x2);
        switch (mode) {
            case UNNECESSARY: {
                MathPreconditions.checkRoundingUnnecessary(sqrtFloor.pow(2).equals(x2));
            }
            case DOWN: 
            case FLOOR: {
                return sqrtFloor;
            }
            case UP: 
            case CEILING: {
                int sqrtFloorInt = sqrtFloor.intValue();
                boolean sqrtFloorIsExact = sqrtFloorInt * sqrtFloorInt == x2.intValue() && sqrtFloor.pow(2).equals(x2);
                return sqrtFloorIsExact ? sqrtFloor : sqrtFloor.add(BigInteger.ONE);
            }
            case HALF_DOWN: 
            case HALF_UP: 
            case HALF_EVEN: {
                BigInteger halfSquare = sqrtFloor.pow(2).add(sqrtFloor);
                return halfSquare.compareTo(x2) >= 0 ? sqrtFloor : sqrtFloor.add(BigInteger.ONE);
            }
        }
        throw new AssertionError();
    }

    @GwtIncompatible(value="TODO")
    private static BigInteger sqrtFloor(BigInteger x2) {
        BigInteger sqrt0;
        int log2 = BigIntegerMath.log2(x2, RoundingMode.FLOOR);
        if (log2 < 1023) {
            sqrt0 = BigIntegerMath.sqrtApproxWithDoubles(x2);
        } else {
            int shift = log2 - 52 & 0xFFFFFFFE;
            sqrt0 = BigIntegerMath.sqrtApproxWithDoubles(x2.shiftRight(shift)).shiftLeft(shift >> 1);
        }
        BigInteger sqrt1 = sqrt0.add(x2.divide(sqrt0)).shiftRight(1);
        if (sqrt0.equals(sqrt1)) {
            return sqrt0;
        }
        while ((sqrt1 = (sqrt0 = sqrt1).add(x2.divide(sqrt0)).shiftRight(1)).compareTo(sqrt0) < 0) {
        }
        return sqrt0;
    }

    @GwtIncompatible(value="TODO")
    private static BigInteger sqrtApproxWithDoubles(BigInteger x2) {
        return DoubleMath.roundToBigInteger(Math.sqrt(DoubleUtils.bigToDouble(x2)), RoundingMode.HALF_EVEN);
    }

    @GwtIncompatible(value="TODO")
    public static BigInteger divide(BigInteger p2, BigInteger q2, RoundingMode mode) {
        BigDecimal pDec = new BigDecimal(p2);
        BigDecimal qDec = new BigDecimal(q2);
        return pDec.divide(qDec, 0, mode).toBigIntegerExact();
    }

    public static BigInteger factorial(int n2) {
        MathPreconditions.checkNonNegative("n", n2);
        if (n2 < LongMath.factorials.length) {
            return BigInteger.valueOf(LongMath.factorials[n2]);
        }
        int approxSize = IntMath.divide(n2 * IntMath.log2(n2, RoundingMode.CEILING), 64, RoundingMode.CEILING);
        ArrayList<BigInteger> bignums = new ArrayList<BigInteger>(approxSize);
        int startingNumber = LongMath.factorials.length;
        long product = LongMath.factorials[startingNumber - 1];
        int shift = Long.numberOfTrailingZeros(product);
        int productBits = LongMath.log2(product >>= shift, RoundingMode.FLOOR) + 1;
        int bits = LongMath.log2(startingNumber, RoundingMode.FLOOR) + 1;
        int nextPowerOfTwo = 1 << bits - 1;
        for (long num = (long)startingNumber; num <= (long)n2; ++num) {
            if ((num & (long)nextPowerOfTwo) != 0L) {
                nextPowerOfTwo <<= 1;
                ++bits;
            }
            int tz = Long.numberOfTrailingZeros(num);
            long normalizedNum = num >> tz;
            shift += tz;
            int normalizedBits = bits - tz;
            if (normalizedBits + productBits >= 64) {
                bignums.add(BigInteger.valueOf(product));
                product = 1L;
                productBits = 0;
            }
            productBits = LongMath.log2(product *= normalizedNum, RoundingMode.FLOOR) + 1;
        }
        if (product > 1L) {
            bignums.add(BigInteger.valueOf(product));
        }
        return BigIntegerMath.listProduct(bignums).shiftLeft(shift);
    }

    static BigInteger listProduct(List<BigInteger> nums) {
        return BigIntegerMath.listProduct(nums, 0, nums.size());
    }

    static BigInteger listProduct(List<BigInteger> nums, int start, int end) {
        switch (end - start) {
            case 0: {
                return BigInteger.ONE;
            }
            case 1: {
                return nums.get(start);
            }
            case 2: {
                return nums.get(start).multiply(nums.get(start + 1));
            }
            case 3: {
                return nums.get(start).multiply(nums.get(start + 1)).multiply(nums.get(start + 2));
            }
        }
        int m2 = end + start >>> 1;
        return BigIntegerMath.listProduct(nums, start, m2).multiply(BigIntegerMath.listProduct(nums, m2, end));
    }

    public static BigInteger binomial(int n2, int k2) {
        int bits;
        MathPreconditions.checkNonNegative("n", n2);
        MathPreconditions.checkNonNegative("k", k2);
        Preconditions.checkArgument(k2 <= n2, "k (%s) > n (%s)", k2, n2);
        if (k2 > n2 >> 1) {
            k2 = n2 - k2;
        }
        if (k2 < LongMath.biggestBinomials.length && n2 <= LongMath.biggestBinomials[k2]) {
            return BigInteger.valueOf(LongMath.binomial(n2, k2));
        }
        BigInteger accum = BigInteger.ONE;
        long numeratorAccum = n2;
        long denominatorAccum = 1L;
        int numeratorBits = bits = LongMath.log2(n2, RoundingMode.CEILING);
        for (int i2 = 1; i2 < k2; ++i2) {
            int p2 = n2 - i2;
            int q2 = i2 + 1;
            if (numeratorBits + bits >= 63) {
                accum = accum.multiply(BigInteger.valueOf(numeratorAccum)).divide(BigInteger.valueOf(denominatorAccum));
                numeratorAccum = p2;
                denominatorAccum = q2;
                numeratorBits = bits;
                continue;
            }
            numeratorAccum *= (long)p2;
            denominatorAccum *= (long)q2;
            numeratorBits += bits;
        }
        return accum.multiply(BigInteger.valueOf(numeratorAccum)).divide(BigInteger.valueOf(denominatorAccum));
    }

    @GwtIncompatible(value="TODO")
    static boolean fitsInLong(BigInteger x2) {
        return x2.bitLength() <= 63;
    }

    private BigIntegerMath() {
    }
}

