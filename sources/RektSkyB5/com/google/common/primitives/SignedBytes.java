/*
 * Decompiled with CFR 0.152.
 */
package com.google.common.primitives;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.util.Comparator;

@GwtCompatible
public final class SignedBytes {
    public static final byte MAX_POWER_OF_TWO = 64;

    private SignedBytes() {
    }

    public static byte checkedCast(long value) {
        byte result = (byte)value;
        if ((long)result != value) {
            throw new IllegalArgumentException("Out of range: " + value);
        }
        return result;
    }

    public static byte saturatedCast(long value) {
        if (value > 127L) {
            return 127;
        }
        if (value < -128L) {
            return -128;
        }
        return (byte)value;
    }

    public static int compare(byte a2, byte b2) {
        return a2 - b2;
    }

    public static byte min(byte ... array) {
        Preconditions.checkArgument(array.length > 0);
        byte min = array[0];
        for (int i2 = 1; i2 < array.length; ++i2) {
            if (array[i2] >= min) continue;
            min = array[i2];
        }
        return min;
    }

    public static byte max(byte ... array) {
        Preconditions.checkArgument(array.length > 0);
        byte max = array[0];
        for (int i2 = 1; i2 < array.length; ++i2) {
            if (array[i2] <= max) continue;
            max = array[i2];
        }
        return max;
    }

    public static String join(String separator, byte ... array) {
        Preconditions.checkNotNull(separator);
        if (array.length == 0) {
            return "";
        }
        StringBuilder builder = new StringBuilder(array.length * 5);
        builder.append(array[0]);
        for (int i2 = 1; i2 < array.length; ++i2) {
            builder.append(separator).append(array[i2]);
        }
        return builder.toString();
    }

    public static Comparator<byte[]> lexicographicalComparator() {
        return LexicographicalComparator.INSTANCE;
    }

    private static enum LexicographicalComparator implements Comparator<byte[]>
    {
        INSTANCE;


        @Override
        public int compare(byte[] left, byte[] right) {
            int minLength = Math.min(left.length, right.length);
            for (int i2 = 0; i2 < minLength; ++i2) {
                int result = SignedBytes.compare(left[i2], right[i2]);
                if (result == 0) continue;
                return result;
            }
            return left.length - right.length;
        }
    }
}

