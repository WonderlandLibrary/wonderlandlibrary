/*
 * Decompiled with CFR 0.152.
 */
package com.google.common.primitives;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Converter;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.AbstractList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.RandomAccess;

@GwtCompatible
public final class Longs {
    public static final int BYTES = 8;
    public static final long MAX_POWER_OF_TWO = 0x4000000000000000L;

    private Longs() {
    }

    public static int hashCode(long value) {
        return (int)(value ^ value >>> 32);
    }

    public static int compare(long a2, long b2) {
        return a2 < b2 ? -1 : (a2 > b2 ? 1 : 0);
    }

    public static boolean contains(long[] array, long target) {
        for (long value : array) {
            if (value != target) continue;
            return true;
        }
        return false;
    }

    public static int indexOf(long[] array, long target) {
        return Longs.indexOf(array, target, 0, array.length);
    }

    private static int indexOf(long[] array, long target, int start, int end) {
        for (int i2 = start; i2 < end; ++i2) {
            if (array[i2] != target) continue;
            return i2;
        }
        return -1;
    }

    public static int indexOf(long[] array, long[] target) {
        Preconditions.checkNotNull(array, "array");
        Preconditions.checkNotNull(target, "target");
        if (target.length == 0) {
            return 0;
        }
        block0: for (int i2 = 0; i2 < array.length - target.length + 1; ++i2) {
            for (int j2 = 0; j2 < target.length; ++j2) {
                if (array[i2 + j2] != target[j2]) continue block0;
            }
            return i2;
        }
        return -1;
    }

    public static int lastIndexOf(long[] array, long target) {
        return Longs.lastIndexOf(array, target, 0, array.length);
    }

    private static int lastIndexOf(long[] array, long target, int start, int end) {
        for (int i2 = end - 1; i2 >= start; --i2) {
            if (array[i2] != target) continue;
            return i2;
        }
        return -1;
    }

    public static long min(long ... array) {
        Preconditions.checkArgument(array.length > 0);
        long min = array[0];
        for (int i2 = 1; i2 < array.length; ++i2) {
            if (array[i2] >= min) continue;
            min = array[i2];
        }
        return min;
    }

    public static long max(long ... array) {
        Preconditions.checkArgument(array.length > 0);
        long max = array[0];
        for (int i2 = 1; i2 < array.length; ++i2) {
            if (array[i2] <= max) continue;
            max = array[i2];
        }
        return max;
    }

    public static long[] concat(long[] ... arrays) {
        int length = 0;
        for (long[] array : arrays) {
            length += array.length;
        }
        long[] result = new long[length];
        int pos = 0;
        for (long[] array : arrays) {
            System.arraycopy(array, 0, result, pos, array.length);
            pos += array.length;
        }
        return result;
    }

    public static byte[] toByteArray(long value) {
        byte[] result = new byte[8];
        for (int i2 = 7; i2 >= 0; --i2) {
            result[i2] = (byte)(value & 0xFFL);
            value >>= 8;
        }
        return result;
    }

    public static long fromByteArray(byte[] bytes) {
        Preconditions.checkArgument(bytes.length >= 8, "array too small: %s < %s", bytes.length, 8);
        return Longs.fromBytes(bytes[0], bytes[1], bytes[2], bytes[3], bytes[4], bytes[5], bytes[6], bytes[7]);
    }

    public static long fromBytes(byte b1, byte b2, byte b3, byte b4, byte b5, byte b6, byte b7, byte b8) {
        return ((long)b1 & 0xFFL) << 56 | ((long)b2 & 0xFFL) << 48 | ((long)b3 & 0xFFL) << 40 | ((long)b4 & 0xFFL) << 32 | ((long)b5 & 0xFFL) << 24 | ((long)b6 & 0xFFL) << 16 | ((long)b7 & 0xFFL) << 8 | (long)b8 & 0xFFL;
    }

    @Beta
    public static Long tryParse(String string) {
        int digit;
        int index;
        if (Preconditions.checkNotNull(string).isEmpty()) {
            return null;
        }
        boolean negative = string.charAt(0) == '-';
        int n2 = index = negative ? 1 : 0;
        if (index == string.length()) {
            return null;
        }
        if ((digit = string.charAt(index++) - 48) < 0 || digit > 9) {
            return null;
        }
        long accum = -digit;
        while (index < string.length()) {
            if ((digit = string.charAt(index++) - 48) < 0 || digit > 9 || accum < -922337203685477580L) {
                return null;
            }
            if ((accum *= 10L) < Long.MIN_VALUE + (long)digit) {
                return null;
            }
            accum -= (long)digit;
        }
        if (negative) {
            return accum;
        }
        if (accum == Long.MIN_VALUE) {
            return null;
        }
        return -accum;
    }

    @Beta
    public static Converter<String, Long> stringConverter() {
        return LongConverter.INSTANCE;
    }

    public static long[] ensureCapacity(long[] array, int minLength, int padding) {
        Preconditions.checkArgument(minLength >= 0, "Invalid minLength: %s", minLength);
        Preconditions.checkArgument(padding >= 0, "Invalid padding: %s", padding);
        return array.length < minLength ? Longs.copyOf(array, minLength + padding) : array;
    }

    private static long[] copyOf(long[] original, int length) {
        long[] copy = new long[length];
        System.arraycopy(original, 0, copy, 0, Math.min(original.length, length));
        return copy;
    }

    public static String join(String separator, long ... array) {
        Preconditions.checkNotNull(separator);
        if (array.length == 0) {
            return "";
        }
        StringBuilder builder = new StringBuilder(array.length * 10);
        builder.append(array[0]);
        for (int i2 = 1; i2 < array.length; ++i2) {
            builder.append(separator).append(array[i2]);
        }
        return builder.toString();
    }

    public static Comparator<long[]> lexicographicalComparator() {
        return LexicographicalComparator.INSTANCE;
    }

    public static long[] toArray(Collection<? extends Number> collection) {
        if (collection instanceof LongArrayAsList) {
            return ((LongArrayAsList)collection).toLongArray();
        }
        Object[] boxedArray = collection.toArray();
        int len = boxedArray.length;
        long[] array = new long[len];
        for (int i2 = 0; i2 < len; ++i2) {
            array[i2] = ((Number)Preconditions.checkNotNull(boxedArray[i2])).longValue();
        }
        return array;
    }

    public static List<Long> asList(long ... backingArray) {
        if (backingArray.length == 0) {
            return Collections.emptyList();
        }
        return new LongArrayAsList(backingArray);
    }

    @GwtCompatible
    private static class LongArrayAsList
    extends AbstractList<Long>
    implements RandomAccess,
    Serializable {
        final long[] array;
        final int start;
        final int end;
        private static final long serialVersionUID = 0L;

        LongArrayAsList(long[] array) {
            this(array, 0, array.length);
        }

        LongArrayAsList(long[] array, int start, int end) {
            this.array = array;
            this.start = start;
            this.end = end;
        }

        @Override
        public int size() {
            return this.end - this.start;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public Long get(int index) {
            Preconditions.checkElementIndex(index, this.size());
            return this.array[this.start + index];
        }

        @Override
        public boolean contains(Object target) {
            return target instanceof Long && Longs.indexOf(this.array, (Long)target, this.start, this.end) != -1;
        }

        @Override
        public int indexOf(Object target) {
            int i2;
            if (target instanceof Long && (i2 = Longs.indexOf(this.array, (Long)target, this.start, this.end)) >= 0) {
                return i2 - this.start;
            }
            return -1;
        }

        @Override
        public int lastIndexOf(Object target) {
            int i2;
            if (target instanceof Long && (i2 = Longs.lastIndexOf(this.array, (Long)target, this.start, this.end)) >= 0) {
                return i2 - this.start;
            }
            return -1;
        }

        @Override
        public Long set(int index, Long element) {
            Preconditions.checkElementIndex(index, this.size());
            long oldValue = this.array[this.start + index];
            this.array[this.start + index] = Preconditions.checkNotNull(element);
            return oldValue;
        }

        @Override
        public List<Long> subList(int fromIndex, int toIndex) {
            int size = this.size();
            Preconditions.checkPositionIndexes(fromIndex, toIndex, size);
            if (fromIndex == toIndex) {
                return Collections.emptyList();
            }
            return new LongArrayAsList(this.array, this.start + fromIndex, this.start + toIndex);
        }

        @Override
        public boolean equals(Object object) {
            if (object == this) {
                return true;
            }
            if (object instanceof LongArrayAsList) {
                LongArrayAsList that = (LongArrayAsList)object;
                int size = this.size();
                if (that.size() != size) {
                    return false;
                }
                for (int i2 = 0; i2 < size; ++i2) {
                    if (this.array[this.start + i2] == that.array[that.start + i2]) continue;
                    return false;
                }
                return true;
            }
            return super.equals(object);
        }

        @Override
        public int hashCode() {
            int result = 1;
            for (int i2 = this.start; i2 < this.end; ++i2) {
                result = 31 * result + Longs.hashCode(this.array[i2]);
            }
            return result;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder(this.size() * 10);
            builder.append('[').append(this.array[this.start]);
            for (int i2 = this.start + 1; i2 < this.end; ++i2) {
                builder.append(", ").append(this.array[i2]);
            }
            return builder.append(']').toString();
        }

        long[] toLongArray() {
            int size = this.size();
            long[] result = new long[size];
            System.arraycopy(this.array, this.start, result, 0, size);
            return result;
        }
    }

    private static enum LexicographicalComparator implements Comparator<long[]>
    {
        INSTANCE;


        @Override
        public int compare(long[] left, long[] right) {
            int minLength = Math.min(left.length, right.length);
            for (int i2 = 0; i2 < minLength; ++i2) {
                int result = Longs.compare(left[i2], right[i2]);
                if (result == 0) continue;
                return result;
            }
            return left.length - right.length;
        }
    }

    private static final class LongConverter
    extends Converter<String, Long>
    implements Serializable {
        static final LongConverter INSTANCE = new LongConverter();
        private static final long serialVersionUID = 1L;

        private LongConverter() {
        }

        @Override
        protected Long doForward(String value) {
            return Long.decode(value);
        }

        @Override
        protected String doBackward(Long value) {
            return value.toString();
        }

        public String toString() {
            return "Longs.stringConverter()";
        }

        private Object readResolve() {
            return INSTANCE;
        }
    }
}

