// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.math;

import me.gong.mcleaks.util.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Iterator;
import me.gong.mcleaks.util.google.common.primitives.Booleans;
import me.gong.mcleaks.util.google.common.base.Preconditions;
import java.math.BigInteger;
import me.gong.mcleaks.util.google.common.annotations.GwtIncompatible;
import java.math.RoundingMode;
import me.gong.mcleaks.util.google.common.annotations.VisibleForTesting;
import me.gong.mcleaks.util.google.common.annotations.GwtCompatible;

@GwtCompatible(emulated = true)
public final class DoubleMath
{
    private static final double MIN_INT_AS_DOUBLE = -2.147483648E9;
    private static final double MAX_INT_AS_DOUBLE = 2.147483647E9;
    private static final double MIN_LONG_AS_DOUBLE = -9.223372036854776E18;
    private static final double MAX_LONG_AS_DOUBLE_PLUS_ONE = 9.223372036854776E18;
    private static final double LN_2;
    @VisibleForTesting
    static final int MAX_FACTORIAL = 170;
    @VisibleForTesting
    static final double[] everySixteenthFactorial;
    
    @GwtIncompatible
    static double roundIntermediate(final double x, final RoundingMode mode) {
        if (!DoubleUtils.isFinite(x)) {
            throw new ArithmeticException("input is infinite or NaN");
        }
        switch (mode) {
            case UNNECESSARY: {
                MathPreconditions.checkRoundingUnnecessary(isMathematicalInteger(x));
                return x;
            }
            case FLOOR: {
                if (x >= 0.0 || isMathematicalInteger(x)) {
                    return x;
                }
                return (double)((long)x - 1L);
            }
            case CEILING: {
                if (x <= 0.0 || isMathematicalInteger(x)) {
                    return x;
                }
                return (double)((long)x + 1L);
            }
            case DOWN: {
                return x;
            }
            case UP: {
                if (isMathematicalInteger(x)) {
                    return x;
                }
                return (double)((long)x + ((x > 0.0) ? 1 : -1));
            }
            case HALF_EVEN: {
                return Math.rint(x);
            }
            case HALF_UP: {
                final double z = Math.rint(x);
                if (Math.abs(x - z) == 0.5) {
                    return x + Math.copySign(0.5, x);
                }
                return z;
            }
            case HALF_DOWN: {
                final double z = Math.rint(x);
                if (Math.abs(x - z) == 0.5) {
                    return x;
                }
                return z;
            }
            default: {
                throw new AssertionError();
            }
        }
    }
    
    @GwtIncompatible
    public static int roundToInt(final double x, final RoundingMode mode) {
        final double z = roundIntermediate(x, mode);
        MathPreconditions.checkInRange(z > -2.147483649E9 & z < 2.147483648E9);
        return (int)z;
    }
    
    @GwtIncompatible
    public static long roundToLong(final double x, final RoundingMode mode) {
        final double z = roundIntermediate(x, mode);
        MathPreconditions.checkInRange(-9.223372036854776E18 - z < 1.0 & z < 9.223372036854776E18);
        return (long)z;
    }
    
    @GwtIncompatible
    public static BigInteger roundToBigInteger(double x, final RoundingMode mode) {
        x = roundIntermediate(x, mode);
        if (-9.223372036854776E18 - x < 1.0 & x < 9.223372036854776E18) {
            return BigInteger.valueOf((long)x);
        }
        final int exponent = Math.getExponent(x);
        final long significand = DoubleUtils.getSignificand(x);
        final BigInteger result = BigInteger.valueOf(significand).shiftLeft(exponent - 52);
        return (x < 0.0) ? result.negate() : result;
    }
    
    @GwtIncompatible
    public static boolean isPowerOfTwo(final double x) {
        return x > 0.0 && DoubleUtils.isFinite(x) && LongMath.isPowerOfTwo(DoubleUtils.getSignificand(x));
    }
    
    public static double log2(final double x) {
        return Math.log(x) / DoubleMath.LN_2;
    }
    
    @GwtIncompatible
    public static int log2(final double x, final RoundingMode mode) {
        Preconditions.checkArgument(x > 0.0 && DoubleUtils.isFinite(x), (Object)"x must be positive and finite");
        final int exponent = Math.getExponent(x);
        if (!DoubleUtils.isNormal(x)) {
            return log2(x * 4.503599627370496E15, mode) - 52;
        }
        boolean increment = false;
        switch (mode) {
            case UNNECESSARY: {
                MathPreconditions.checkRoundingUnnecessary(isPowerOfTwo(x));
            }
            case FLOOR: {
                increment = false;
                break;
            }
            case CEILING: {
                increment = !isPowerOfTwo(x);
                break;
            }
            case DOWN: {
                increment = (exponent < 0 & !isPowerOfTwo(x));
                break;
            }
            case UP: {
                increment = (exponent >= 0 & !isPowerOfTwo(x));
                break;
            }
            case HALF_EVEN:
            case HALF_UP:
            case HALF_DOWN: {
                final double xScaled = DoubleUtils.scaleNormalize(x);
                increment = (xScaled * xScaled > 2.0);
                break;
            }
            default: {
                throw new AssertionError();
            }
        }
        return increment ? (exponent + 1) : exponent;
    }
    
    @GwtIncompatible
    public static boolean isMathematicalInteger(final double x) {
        return DoubleUtils.isFinite(x) && (x == 0.0 || 52 - Long.numberOfTrailingZeros(DoubleUtils.getSignificand(x)) <= Math.getExponent(x));
    }
    
    public static double factorial(final int n) {
        MathPreconditions.checkNonNegative("n", n);
        if (n > 170) {
            return Double.POSITIVE_INFINITY;
        }
        double accum = 1.0;
        for (int i = 1 + (n & 0xFFFFFFF0); i <= n; ++i) {
            accum *= i;
        }
        return accum * DoubleMath.everySixteenthFactorial[n >> 4];
    }
    
    public static boolean fuzzyEquals(final double a, final double b, final double tolerance) {
        MathPreconditions.checkNonNegative("tolerance", tolerance);
        return Math.copySign(a - b, 1.0) <= tolerance || a == b || (Double.isNaN(a) && Double.isNaN(b));
    }
    
    public static int fuzzyCompare(final double a, final double b, final double tolerance) {
        if (fuzzyEquals(a, b, tolerance)) {
            return 0;
        }
        if (a < b) {
            return -1;
        }
        if (a > b) {
            return 1;
        }
        return Booleans.compare(Double.isNaN(a), Double.isNaN(b));
    }
    
    @Deprecated
    @GwtIncompatible
    public static double mean(final double... values) {
        Preconditions.checkArgument(values.length > 0, (Object)"Cannot take mean of 0 values");
        long count = 1L;
        double mean = checkFinite(values[0]);
        for (int index = 1; index < values.length; ++index) {
            checkFinite(values[index]);
            ++count;
            mean += (values[index] - mean) / count;
        }
        return mean;
    }
    
    @Deprecated
    public static double mean(final int... values) {
        Preconditions.checkArgument(values.length > 0, (Object)"Cannot take mean of 0 values");
        long sum = 0L;
        for (int index = 0; index < values.length; ++index) {
            sum += values[index];
        }
        return sum / (double)values.length;
    }
    
    @Deprecated
    public static double mean(final long... values) {
        Preconditions.checkArgument(values.length > 0, (Object)"Cannot take mean of 0 values");
        long count = 1L;
        double mean = (double)values[0];
        for (int index = 1; index < values.length; ++index) {
            ++count;
            mean += (values[index] - mean) / count;
        }
        return mean;
    }
    
    @Deprecated
    @GwtIncompatible
    public static double mean(final Iterable<? extends Number> values) {
        return mean(values.iterator());
    }
    
    @Deprecated
    @GwtIncompatible
    public static double mean(final Iterator<? extends Number> values) {
        Preconditions.checkArgument(values.hasNext(), (Object)"Cannot take mean of 0 values");
        long count = 1L;
        double mean = checkFinite(((Number)values.next()).doubleValue());
        while (values.hasNext()) {
            final double value = checkFinite(((Number)values.next()).doubleValue());
            ++count;
            mean += (value - mean) / count;
        }
        return mean;
    }
    
    @GwtIncompatible
    @CanIgnoreReturnValue
    private static double checkFinite(final double argument) {
        Preconditions.checkArgument(DoubleUtils.isFinite(argument));
        return argument;
    }
    
    private DoubleMath() {
    }
    
    static {
        LN_2 = Math.log(2.0);
        everySixteenthFactorial = new double[] { 1.0, 2.0922789888E13, 2.631308369336935E35, 1.2413915592536073E61, 1.2688693218588417E89, 7.156945704626381E118, 9.916779348709496E149, 1.974506857221074E182, 3.856204823625804E215, 5.5502938327393044E249, 4.7147236359920616E284 };
    }
}
