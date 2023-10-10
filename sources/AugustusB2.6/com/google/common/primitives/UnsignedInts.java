// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.primitives;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Arrays;
import java.util.Comparator;
import com.google.common.base.Preconditions;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.Beta;

@ElementTypesAreNonnullByDefault
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
    
    public static int checkedCast(final long value) {
        Preconditions.checkArgument(value >> 32 == 0L, "out of range: %s", value);
        return (int)value;
    }
    
    public static int saturatedCast(final long value) {
        if (value <= 0L) {
            return 0;
        }
        if (value >= 4294967296L) {
            return -1;
        }
        return (int)value;
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
    
    public static void sort(final int[] array) {
        Preconditions.checkNotNull(array);
        sort(array, 0, array.length);
    }
    
    public static void sort(final int[] array, final int fromIndex, final int toIndex) {
        Preconditions.checkNotNull(array);
        Preconditions.checkPositionIndexes(fromIndex, toIndex, array.length);
        for (int i = fromIndex; i < toIndex; ++i) {
            array[i] = flip(array[i]);
        }
        Arrays.sort(array, fromIndex, toIndex);
        for (int i = fromIndex; i < toIndex; ++i) {
            array[i] = flip(array[i]);
        }
    }
    
    public static void sortDescending(final int[] array) {
        Preconditions.checkNotNull(array);
        sortDescending(array, 0, array.length);
    }
    
    public static void sortDescending(final int[] array, final int fromIndex, final int toIndex) {
        Preconditions.checkNotNull(array);
        Preconditions.checkPositionIndexes(fromIndex, toIndex, array.length);
        for (int i = fromIndex; i < toIndex; ++i) {
            final int n = i;
            array[n] ^= Integer.MAX_VALUE;
        }
        Arrays.sort(array, fromIndex, toIndex);
        for (int i = fromIndex; i < toIndex; ++i) {
            final int n2 = i;
            array[n2] ^= Integer.MAX_VALUE;
        }
    }
    
    public static int divide(final int dividend, final int divisor) {
        return (int)(toLong(dividend) / toLong(divisor));
    }
    
    public static int remainder(final int dividend, final int divisor) {
        return (int)(toLong(dividend) % toLong(divisor));
    }
    
    @CanIgnoreReturnValue
    public static int decode(final String stringValue) {
        final ParseRequest request = ParseRequest.fromString(stringValue);
        try {
            return parseUnsignedInt(request.rawValue, request.radix);
        }
        catch (NumberFormatException e) {
            final String original = "Error parsing value: ";
            final String value = String.valueOf(stringValue);
            final NumberFormatException decodeException = new NumberFormatException((value.length() != 0) ? original.concat(value) : new String(original));
            decodeException.initCause(e);
            throw decodeException;
        }
    }
    
    @CanIgnoreReturnValue
    public static int parseUnsignedInt(final String s) {
        return parseUnsignedInt(s, 10);
    }
    
    @CanIgnoreReturnValue
    public static int parseUnsignedInt(final String string, final int radix) {
        Preconditions.checkNotNull(string);
        final long result = Long.parseLong(string, radix);
        if ((result & 0xFFFFFFFFL) != result) {
            throw new NumberFormatException(new StringBuilder(69 + String.valueOf(string).length()).append("Input ").append(string).append(" in base ").append(radix).append(" is not in the range of an unsigned integer").toString());
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
        
        @Override
        public String toString() {
            return "UnsignedInts.lexicographicalComparator()";
        }
        
        private static /* synthetic */ LexicographicalComparator[] $values() {
            return new LexicographicalComparator[] { LexicographicalComparator.INSTANCE };
        }
        
        static {
            $VALUES = $values();
        }
    }
}
