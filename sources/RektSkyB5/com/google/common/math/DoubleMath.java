/*
 * Decompiled with CFR 0.152.
 */
package com.google.common.math;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.math.DoubleUtils;
import com.google.common.math.LongMath;
import com.google.common.math.MathPreconditions;
import com.google.common.primitives.Booleans;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Iterator;

@GwtCompatible(emulated=true)
public final class DoubleMath {
    private static final double MIN_INT_AS_DOUBLE = -2.147483648E9;
    private static final double MAX_INT_AS_DOUBLE = 2.147483647E9;
    private static final double MIN_LONG_AS_DOUBLE = -9.223372036854776E18;
    private static final double MAX_LONG_AS_DOUBLE_PLUS_ONE = 9.223372036854776E18;
    private static final double LN_2 = Math.log(2.0);
    @VisibleForTesting
    static final int MAX_FACTORIAL = 170;
    @VisibleForTesting
    static final double[] everySixteenthFactorial = new double[]{1.0, 2.0922789888E13, 2.631308369336935E35, 1.2413915592536073E61, 1.2688693218588417E89, 7.156945704626381E118, 9.916779348709496E149, 1.974506857221074E182, 3.856204823625804E215, 5.5502938327393044E249, 4.7147236359920616E284};

    @GwtIncompatible(value="#isMathematicalInteger, com.google.common.math.DoubleUtils")
    static double roundIntermediate(double x2, RoundingMode mode) {
        if (!DoubleUtils.isFinite(x2)) {
            throw new ArithmeticException("input is infinite or NaN");
        }
        switch (mode) {
            case UNNECESSARY: {
                MathPreconditions.checkRoundingUnnecessary(DoubleMath.isMathematicalInteger(x2));
                return x2;
            }
            case FLOOR: {
                if (x2 >= 0.0 || DoubleMath.isMathematicalInteger(x2)) {
                    return x2;
                }
                return x2 - 1.0;
            }
            case CEILING: {
                if (x2 <= 0.0 || DoubleMath.isMathematicalInteger(x2)) {
                    return x2;
                }
                return x2 + 1.0;
            }
            case DOWN: {
                return x2;
            }
            case UP: {
                if (DoubleMath.isMathematicalInteger(x2)) {
                    return x2;
                }
                return x2 + Math.copySign(1.0, x2);
            }
            case HALF_EVEN: {
                return Math.rint(x2);
            }
            case HALF_UP: {
                double z2 = Math.rint(x2);
                if (Math.abs(x2 - z2) == 0.5) {
                    return x2 + Math.copySign(0.5, x2);
                }
                return z2;
            }
            case HALF_DOWN: {
                double z3 = Math.rint(x2);
                if (Math.abs(x2 - z3) == 0.5) {
                    return x2;
                }
                return z3;
            }
        }
        throw new AssertionError();
    }

    @GwtIncompatible(value="#roundIntermediate")
    public static int roundToInt(double x2, RoundingMode mode) {
        double z2 = DoubleMath.roundIntermediate(x2, mode);
        MathPreconditions.checkInRange(z2 > -2.147483649E9 & z2 < 2.147483648E9);
        return (int)z2;
    }

    @GwtIncompatible(value="#roundIntermediate")
    public static long roundToLong(double x2, RoundingMode mode) {
        double z2 = DoubleMath.roundIntermediate(x2, mode);
        MathPreconditions.checkInRange(-9.223372036854776E18 - z2 < 1.0 & z2 < 9.223372036854776E18);
        return (long)z2;
    }

    @GwtIncompatible(value="#roundIntermediate, java.lang.Math.getExponent, com.google.common.math.DoubleUtils")
    public static BigInteger roundToBigInteger(double x2, RoundingMode mode) {
        if (-9.223372036854776E18 - (x2 = DoubleMath.roundIntermediate(x2, mode)) < 1.0 & x2 < 9.223372036854776E18) {
            return BigInteger.valueOf((long)x2);
        }
        int exponent = Math.getExponent(x2);
        long significand = DoubleUtils.getSignificand(x2);
        BigInteger result = BigInteger.valueOf(significand).shiftLeft(exponent - 52);
        return x2 < 0.0 ? result.negate() : result;
    }

    @GwtIncompatible(value="com.google.common.math.DoubleUtils")
    public static boolean isPowerOfTwo(double x2) {
        return x2 > 0.0 && DoubleUtils.isFinite(x2) && LongMath.isPowerOfTwo(DoubleUtils.getSignificand(x2));
    }

    public static double log2(double x2) {
        return Math.log(x2) / LN_2;
    }

    @GwtIncompatible(value="java.lang.Math.getExponent, com.google.common.math.DoubleUtils")
    public static int log2(double x2, RoundingMode mode) {
        boolean increment;
        Preconditions.checkArgument(x2 > 0.0 && DoubleUtils.isFinite(x2), "x must be positive and finite");
        int exponent = Math.getExponent(x2);
        if (!DoubleUtils.isNormal(x2)) {
            return DoubleMath.log2(x2 * 4.503599627370496E15, mode) - 52;
        }
        switch (mode) {
            case UNNECESSARY: {
                MathPreconditions.checkRoundingUnnecessary(DoubleMath.isPowerOfTwo(x2));
            }
            case FLOOR: {
                increment = false;
                break;
            }
            case CEILING: {
                increment = !DoubleMath.isPowerOfTwo(x2);
                break;
            }
            case DOWN: {
                increment = exponent < 0 & !DoubleMath.isPowerOfTwo(x2);
                break;
            }
            case UP: {
                increment = exponent >= 0 & !DoubleMath.isPowerOfTwo(x2);
                break;
            }
            case HALF_EVEN: 
            case HALF_UP: 
            case HALF_DOWN: {
                double xScaled = DoubleUtils.scaleNormalize(x2);
                increment = xScaled * xScaled > 2.0;
                break;
            }
            default: {
                throw new AssertionError();
            }
        }
        return increment ? exponent + 1 : exponent;
    }

    @GwtIncompatible(value="java.lang.Math.getExponent, com.google.common.math.DoubleUtils")
    public static boolean isMathematicalInteger(double x2) {
        return DoubleUtils.isFinite(x2) && (x2 == 0.0 || 52 - Long.numberOfTrailingZeros(DoubleUtils.getSignificand(x2)) <= Math.getExponent(x2));
    }

    public static double factorial(int n2) {
        MathPreconditions.checkNonNegative("n", n2);
        if (n2 > 170) {
            return Double.POSITIVE_INFINITY;
        }
        double accum = 1.0;
        for (int i2 = 1 + (n2 & 0xFFFFFFF0); i2 <= n2; ++i2) {
            accum *= (double)i2;
        }
        return accum * everySixteenthFactorial[n2 >> 4];
    }

    public static boolean fuzzyEquals(double a2, double b2, double tolerance) {
        MathPreconditions.checkNonNegative("tolerance", tolerance);
        return Math.copySign(a2 - b2, 1.0) <= tolerance || a2 == b2 || Double.isNaN(a2) && Double.isNaN(b2);
    }

    public static int fuzzyCompare(double a2, double b2, double tolerance) {
        if (DoubleMath.fuzzyEquals(a2, b2, tolerance)) {
            return 0;
        }
        if (a2 < b2) {
            return -1;
        }
        if (a2 > b2) {
            return 1;
        }
        return Booleans.compare(Double.isNaN(a2), Double.isNaN(b2));
    }

    @GwtIncompatible(value="MeanAccumulator")
    public static double mean(double ... values) {
        MeanAccumulator accumulator = new MeanAccumulator();
        for (double value : values) {
            accumulator.add(value);
        }
        return accumulator.mean();
    }

    @GwtIncompatible(value="MeanAccumulator")
    public static double mean(int ... values) {
        MeanAccumulator accumulator = new MeanAccumulator();
        for (int value : values) {
            accumulator.add(value);
        }
        return accumulator.mean();
    }

    @GwtIncompatible(value="MeanAccumulator")
    public static double mean(long ... values) {
        MeanAccumulator accumulator = new MeanAccumulator();
        for (long value : values) {
            accumulator.add(value);
        }
        return accumulator.mean();
    }

    @GwtIncompatible(value="MeanAccumulator")
    public static double mean(Iterable<? extends Number> values) {
        MeanAccumulator accumulator = new MeanAccumulator();
        for (Number number : values) {
            accumulator.add(number.doubleValue());
        }
        return accumulator.mean();
    }

    @GwtIncompatible(value="MeanAccumulator")
    public static double mean(Iterator<? extends Number> values) {
        MeanAccumulator accumulator = new MeanAccumulator();
        while (values.hasNext()) {
            accumulator.add(values.next().doubleValue());
        }
        return accumulator.mean();
    }

    private DoubleMath() {
    }

    @GwtIncompatible(value="com.google.common.math.DoubleUtils")
    private static final class MeanAccumulator {
        private long count = 0L;
        private double mean = 0.0;

        private MeanAccumulator() {
        }

        void add(double value) {
            Preconditions.checkArgument(DoubleUtils.isFinite(value));
            ++this.count;
            this.mean += (value - this.mean) / (double)this.count;
        }

        double mean() {
            Preconditions.checkArgument(this.count > 0L, "Cannot take mean of 0 values");
            return this.mean;
        }
    }
}

