// 
// Decompiled by Procyon v0.6.0
// 

package com.google.common.primitives;

import java.util.Comparator;
import com.google.common.base.Preconditions;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.Beta;

@Beta
@GwtCompatible
public final class UnsignedInts
{
    static final long INT_MASK = 4294967295L;
    
    private UnsignedInts() {
    }
    
    static int flip(final int value) {
        return value ^ Integer.MIN_VALUE;
    }
    
    public static int compare(final int a, final int b) {
        return Ints.compare(flip(a), flip(b));
    }
    
    public static long toLong(final int value) {
        return (long)value & 0xFFFFFFFFL;
    }
    
    public static int min(final int... array) {
        Preconditions.checkArgument(array.length > 0);
        int min = flip(array[0]);
        for (int i = 1; i < array.length; ++i) {
            final int next = flip(array[i]);
            if (next < min) {
                min = next;
            }
        }
        return flip(min);
    }
    
    public static int max(final int... array) {
        Preconditions.checkArgument(array.length > 0);
        int max = flip(array[0]);
        for (int i = 1; i < array.length; ++i) {
            final int next = flip(array[i]);
            if (next > max) {
                max = next;
            }
        }
        return flip(max);
    }
    
    public static String join(final String separator, final int... array) {
        Preconditions.checkNotNull(separator);
        if (array.length == 0) {
            return "";
        }
        final StringBuilder builder = new StringBuilder(array.length * 5);
        builder.append(toString(array[0]));
        for (int i = 1; i < array.length; ++i) {
            builder.append(separator).append(toString(array[i]));
        }
        return builder.toString();
    }
    
    public static Comparator<int[]> lexicographicalComparator() {
        return LexicographicalComparator.INSTANCE;
    }
    
    public static int divide(final int dividend, final int divisor) {
        return (int)(toLong(dividend) / toLong(divisor));
    }
    
    public static int remainder(final int dividend, final int divisor) {
        return (int)(toLong(dividend) % toLong(divisor));
    }
    
    public static int decode(final String stringValue) {
        final ParseRequest request = ParseRequest.fromString(stringValue);
        try {
            return parseUnsignedInt(request.rawValue, request.radix);
        }
        catch (final NumberFormatException e) {
            final String s = "Error parsing value: ";
            final String value = String.valueOf(stringValue);
            final NumberFormatException decodeException = new NumberFormatException((value.length() != 0) ? s.concat(value) : new String(s));
            decodeException.initCause(e);
            throw decodeException;
        }
    }
    
    public static int parseUnsignedInt(final String s) {
        return parseUnsignedInt(s, 10);
    }
    
    public static int parseUnsignedInt(final String string, final int radix) {
        Preconditions.checkNotNull(string);
        final long result = Long.parseLong(string, radix);
        if ((result & 0xFFFFFFFFL) != result) {
            final String value = String.valueOf(String.valueOf(string));
            throw new NumberFormatException(new StringBuilder(69 + value.length()).append("Input ").append(value).append(" in base ").append(radix).append(" is not in the range of an unsigned integer").toString());
        }
        return (int)result;
    }
    
    public static String toString(final int x) {
        return toString(x, 10);
    }
    
    public static String toString(final int x, final int radix) {
        final long asLong = (long)x & 0xFFFFFFFFL;
        return Long.toString(asLong, radix);
    }
    
    enum LexicographicalComparator implements Comparator<int[]>
    {
        INSTANCE;
        
        @Override
        public int compare(final int[] left, final int[] right) {
            for (int minLength = Math.min(left.length, right.length), i = 0; i < minLength; ++i) {
                if (left[i] != right[i]) {
                    return UnsignedInts.compare(left[i], right[i]);
                }
            }
            return left.length - right.length;
        }
    }
}
