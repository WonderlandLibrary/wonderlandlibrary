/*
 * Decompiled with CFR 0.152.
 */
package com.google.common.primitives;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.primitives.Longs;
import com.google.common.primitives.ParseRequest;
import java.math.BigInteger;
import java.util.Comparator;

@Beta
@GwtCompatible
public final class UnsignedLongs {
    public static final long MAX_VALUE = -1L;
    private static final long[] maxValueDivs = new long[37];
    private static final int[] maxValueMods = new int[37];
    private static final int[] maxSafeDigits = new int[37];

    private UnsignedLongs() {
    }

    private static long flip(long a2) {
        return a2 ^ Long.MIN_VALUE;
    }

    public static int compare(long a2, long b2) {
        return Longs.compare(UnsignedLongs.flip(a2), UnsignedLongs.flip(b2));
    }

    public static long min(long ... array) {
        Preconditions.checkArgument(array.length > 0);
        long min = UnsignedLongs.flip(array[0]);
        for (int i2 = 1; i2 < array.length; ++i2) {
            long next = UnsignedLongs.flip(array[i2]);
            if (next >= min) continue;
            min = next;
        }
        return UnsignedLongs.flip(min);
    }

    public static long max(long ... array) {
        Preconditions.checkArgument(array.length > 0);
        long max = UnsignedLongs.flip(array[0]);
        for (int i2 = 1; i2 < array.length; ++i2) {
            long next = UnsignedLongs.flip(array[i2]);
            if (next <= max) continue;
            max = next;
        }
        return UnsignedLongs.flip(max);
    }

    public static String join(String separator, long ... array) {
        Preconditions.checkNotNull(separator);
        if (array.length == 0) {
            return "";
        }
        StringBuilder builder = new StringBuilder(array.length * 5);
        builder.append(UnsignedLongs.toString(array[0]));
        for (int i2 = 1; i2 < array.length; ++i2) {
            builder.append(separator).append(UnsignedLongs.toString(array[i2]));
        }
        return builder.toString();
    }

    public static Comparator<long[]> lexicographicalComparator() {
        return LexicographicalComparator.INSTANCE;
    }

    public static long divide(long dividend, long divisor) {
        long quotient;
        if (divisor < 0L) {
            if (UnsignedLongs.compare(dividend, divisor) < 0) {
                return 0L;
            }
            return 1L;
        }
        if (dividend >= 0L) {
            return dividend / divisor;
        }
        long rem = dividend - (quotient = (dividend >>> 1) / divisor << 1) * divisor;
        return quotient + (long)(UnsignedLongs.compare(rem, divisor) >= 0 ? 1 : 0);
    }

    public static long remainder(long dividend, long divisor) {
        long rem;
        if (divisor < 0L) {
            if (UnsignedLongs.compare(dividend, divisor) < 0) {
                return dividend;
            }
            return dividend - divisor;
        }
        if (dividend >= 0L) {
            return dividend % divisor;
        }
        long quotient = (dividend >>> 1) / divisor << 1;
        return rem - (UnsignedLongs.compare(rem = dividend - quotient * divisor, divisor) >= 0 ? divisor : 0L);
    }

    public static long parseUnsignedLong(String s2) {
        return UnsignedLongs.parseUnsignedLong(s2, 10);
    }

    public static long decode(String stringValue) {
        ParseRequest request = ParseRequest.fromString(stringValue);
        try {
            return UnsignedLongs.parseUnsignedLong(request.rawValue, request.radix);
        }
        catch (NumberFormatException e2) {
            NumberFormatException decodeException = new NumberFormatException("Error parsing value: " + stringValue);
            decodeException.initCause(e2);
            throw decodeException;
        }
    }

    public static long parseUnsignedLong(String s2, int radix) {
        Preconditions.checkNotNull(s2);
        if (s2.length() == 0) {
            throw new NumberFormatException("empty string");
        }
        if (radix < 2 || radix > 36) {
            throw new NumberFormatException("illegal radix: " + radix);
        }
        int max_safe_pos = maxSafeDigits[radix] - 1;
        long value = 0L;
        for (int pos = 0; pos < s2.length(); ++pos) {
            int digit = Character.digit(s2.charAt(pos), radix);
            if (digit == -1) {
                throw new NumberFormatException(s2);
            }
            if (pos > max_safe_pos && UnsignedLongs.overflowInParse(value, digit, radix)) {
                throw new NumberFormatException("Too large for unsigned long: " + s2);
            }
            value = value * (long)radix + (long)digit;
        }
        return value;
    }

    private static boolean overflowInParse(long current, int digit, int radix) {
        if (current >= 0L) {
            if (current < maxValueDivs[radix]) {
                return false;
            }
            if (current > maxValueDivs[radix]) {
                return true;
            }
            return digit > maxValueMods[radix];
        }
        return true;
    }

    public static String toString(long x2) {
        return UnsignedLongs.toString(x2, 10);
    }

    public static String toString(long x2, int radix) {
        Preconditions.checkArgument(radix >= 2 && radix <= 36, "radix (%s) must be between Character.MIN_RADIX and Character.MAX_RADIX", radix);
        if (x2 == 0L) {
            return "0";
        }
        char[] buf = new char[64];
        int i2 = buf.length;
        if (x2 < 0L) {
            long quotient = UnsignedLongs.divide(x2, radix);
            long rem = x2 - quotient * (long)radix;
            buf[--i2] = Character.forDigit((int)rem, radix);
            x2 = quotient;
        }
        while (x2 > 0L) {
            buf[--i2] = Character.forDigit((int)(x2 % (long)radix), radix);
            x2 /= (long)radix;
        }
        return new String(buf, i2, buf.length - i2);
    }

    static {
        BigInteger overflow = new BigInteger("10000000000000000", 16);
        for (int i2 = 2; i2 <= 36; ++i2) {
            UnsignedLongs.maxValueDivs[i2] = UnsignedLongs.divide(-1L, i2);
            UnsignedLongs.maxValueMods[i2] = (int)UnsignedLongs.remainder(-1L, i2);
            UnsignedLongs.maxSafeDigits[i2] = overflow.toString(i2).length() - 1;
        }
    }

    static enum LexicographicalComparator implements Comparator<long[]>
    {
        INSTANCE;


        @Override
        public int compare(long[] left, long[] right) {
            int minLength = Math.min(left.length, right.length);
            for (int i2 = 0; i2 < minLength; ++i2) {
                if (left[i2] == right[i2]) continue;
                return UnsignedLongs.compare(left[i2], right[i2]);
            }
            return left.length - right.length;
        }
    }
}

