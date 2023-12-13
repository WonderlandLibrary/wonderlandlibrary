/*
 * Decompiled with CFR 0.152.
 */
package com.google.common.primitives;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;
import com.google.common.primitives.ParseRequest;
import java.util.Comparator;

@Beta
@GwtCompatible
public final class UnsignedInts {
    static final long INT_MASK = 0xFFFFFFFFL;

    private UnsignedInts() {
    }

    static int flip(int value) {
        return value ^ Integer.MIN_VALUE;
    }

    public static int compare(int a2, int b2) {
        return Ints.compare(UnsignedInts.flip(a2), UnsignedInts.flip(b2));
    }

    public static long toLong(int value) {
        return (long)value & 0xFFFFFFFFL;
    }

    public static int min(int ... array) {
        Preconditions.checkArgument(array.length > 0);
        int min = UnsignedInts.flip(array[0]);
        for (int i2 = 1; i2 < array.length; ++i2) {
            int next = UnsignedInts.flip(array[i2]);
            if (next >= min) continue;
            min = next;
        }
        return UnsignedInts.flip(min);
    }

    public static int max(int ... array) {
        Preconditions.checkArgument(array.length > 0);
        int max = UnsignedInts.flip(array[0]);
        for (int i2 = 1; i2 < array.length; ++i2) {
            int next = UnsignedInts.flip(array[i2]);
            if (next <= max) continue;
            max = next;
        }
        return UnsignedInts.flip(max);
    }

    public static String join(String separator, int ... array) {
        Preconditions.checkNotNull(separator);
        if (array.length == 0) {
            return "";
        }
        StringBuilder builder = new StringBuilder(array.length * 5);
        builder.append(UnsignedInts.toString(array[0]));
        for (int i2 = 1; i2 < array.length; ++i2) {
            builder.append(separator).append(UnsignedInts.toString(array[i2]));
        }
        return builder.toString();
    }

    public static Comparator<int[]> lexicographicalComparator() {
        return LexicographicalComparator.INSTANCE;
    }

    public static int divide(int dividend, int divisor) {
        return (int)(UnsignedInts.toLong(dividend) / UnsignedInts.toLong(divisor));
    }

    public static int remainder(int dividend, int divisor) {
        return (int)(UnsignedInts.toLong(dividend) % UnsignedInts.toLong(divisor));
    }

    public static int decode(String stringValue) {
        ParseRequest request = ParseRequest.fromString(stringValue);
        try {
            return UnsignedInts.parseUnsignedInt(request.rawValue, request.radix);
        }
        catch (NumberFormatException e2) {
            NumberFormatException decodeException = new NumberFormatException("Error parsing value: " + stringValue);
            decodeException.initCause(e2);
            throw decodeException;
        }
    }

    public static int parseUnsignedInt(String s2) {
        return UnsignedInts.parseUnsignedInt(s2, 10);
    }

    public static int parseUnsignedInt(String string, int radix) {
        Preconditions.checkNotNull(string);
        long result = Long.parseLong(string, radix);
        if ((result & 0xFFFFFFFFL) != result) {
            throw new NumberFormatException("Input " + string + " in base " + radix + " is not in the range of an unsigned integer");
        }
        return (int)result;
    }

    public static String toString(int x2) {
        return UnsignedInts.toString(x2, 10);
    }

    public static String toString(int x2, int radix) {
        long asLong = (long)x2 & 0xFFFFFFFFL;
        return Long.toString(asLong, radix);
    }

    static enum LexicographicalComparator implements Comparator<int[]>
    {
        INSTANCE;


        @Override
        public int compare(int[] left, int[] right) {
            int minLength = Math.min(left.length, right.length);
            for (int i2 = 0; i2 < minLength; ++i2) {
                if (left[i2] == right[i2]) continue;
                return UnsignedInts.compare(left[i2], right[i2]);
            }
            return left.length - right.length;
        }
    }
}

