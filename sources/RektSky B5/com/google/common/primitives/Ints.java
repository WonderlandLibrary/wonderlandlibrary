/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.CheckForNull
 */
package com.google.common.primitives;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Converter;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.RandomAccess;
import javax.annotation.CheckForNull;

@GwtCompatible(emulated=true)
public final class Ints {
    public static final int BYTES = 4;
    public static final int MAX_POWER_OF_TWO = 0x40000000;
    private static final byte[] asciiDigits;

    private Ints() {
    }

    public static int hashCode(int value) {
        return value;
    }

    public static int checkedCast(long value) {
        int result = (int)value;
        if ((long)result != value) {
            throw new IllegalArgumentException("Out of range: " + value);
        }
        return result;
    }

    public static int saturatedCast(long value) {
        if (value > Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }
        if (value < Integer.MIN_VALUE) {
            return Integer.MIN_VALUE;
        }
        return (int)value;
    }

    public static int compare(int a2, int b2) {
        return a2 < b2 ? -1 : (a2 > b2 ? 1 : 0);
    }

    public static boolean contains(int[] array, int target) {
        for (int value : array) {
            if (value != target) continue;
            return true;
        }
        return false;
    }

    public static int indexOf(int[] array, int target) {
        return Ints.indexOf(array, target, 0, array.length);
    }

    private static int indexOf(int[] array, int target, int start, int end) {
        for (int i2 = start; i2 < end; ++i2) {
            if (array[i2] != target) continue;
            return i2;
        }
        return -1;
    }

    public static int indexOf(int[] array, int[] target) {
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

    public static int lastIndexOf(int[] array, int target) {
        return Ints.lastIndexOf(array, target, 0, array.length);
    }

    private static int lastIndexOf(int[] array, int target, int start, int end) {
        for (int i2 = end - 1; i2 >= start; --i2) {
            if (array[i2] != target) continue;
            return i2;
        }
        return -1;
    }

    public static int min(int ... array) {
        Preconditions.checkArgument(array.length > 0);
        int min = array[0];
        for (int i2 = 1; i2 < array.length; ++i2) {
            if (array[i2] >= min) continue;
            min = array[i2];
        }
        return min;
    }

    public static int max(int ... array) {
        Preconditions.checkArgument(array.length > 0);
        int max = array[0];
        for (int i2 = 1; i2 < array.length; ++i2) {
            if (array[i2] <= max) continue;
            max = array[i2];
        }
        return max;
    }

    public static int[] concat(int[] ... arrays) {
        int length = 0;
        for (int[] array : arrays) {
            length += array.length;
        }
        int[] result = new int[length];
        int pos = 0;
        for (int[] array : arrays) {
            System.arraycopy(array, 0, result, pos, array.length);
            pos += array.length;
        }
        return result;
    }

    @GwtIncompatible(value="doesn't work")
    public static byte[] toByteArray(int value) {
        return new byte[]{(byte)(value >> 24), (byte)(value >> 16), (byte)(value >> 8), (byte)value};
    }

    @GwtIncompatible(value="doesn't work")
    public static int fromByteArray(byte[] bytes) {
        Preconditions.checkArgument(bytes.length >= 4, "array too small: %s < %s", bytes.length, 4);
        return Ints.fromBytes(bytes[0], bytes[1], bytes[2], bytes[3]);
    }

    @GwtIncompatible(value="doesn't work")
    public static int fromBytes(byte b1, byte b2, byte b3, byte b4) {
        return b1 << 24 | (b2 & 0xFF) << 16 | (b3 & 0xFF) << 8 | b4 & 0xFF;
    }

    @Beta
    public static Converter<String, Integer> stringConverter() {
        return IntConverter.INSTANCE;
    }

    public static int[] ensureCapacity(int[] array, int minLength, int padding) {
        Preconditions.checkArgument(minLength >= 0, "Invalid minLength: %s", minLength);
        Preconditions.checkArgument(padding >= 0, "Invalid padding: %s", padding);
        return array.length < minLength ? Ints.copyOf(array, minLength + padding) : array;
    }

    private static int[] copyOf(int[] original, int length) {
        int[] copy = new int[length];
        System.arraycopy(original, 0, copy, 0, Math.min(original.length, length));
        return copy;
    }

    public static String join(String separator, int ... array) {
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

    public static Comparator<int[]> lexicographicalComparator() {
        return LexicographicalComparator.INSTANCE;
    }

    public static int[] toArray(Collection<? extends Number> collection) {
        if (collection instanceof IntArrayAsList) {
            return ((IntArrayAsList)collection).toIntArray();
        }
        Object[] boxedArray = collection.toArray();
        int len = boxedArray.length;
        int[] array = new int[len];
        for (int i2 = 0; i2 < len; ++i2) {
            array[i2] = ((Number)Preconditions.checkNotNull(boxedArray[i2])).intValue();
        }
        return array;
    }

    public static List<Integer> asList(int ... backingArray) {
        if (backingArray.length == 0) {
            return Collections.emptyList();
        }
        return new IntArrayAsList(backingArray);
    }

    private static int digit(char c2) {
        return c2 < '\u0080' ? asciiDigits[c2] : -1;
    }

    @CheckForNull
    @Beta
    @GwtIncompatible(value="TODO")
    public static Integer tryParse(String string) {
        return Ints.tryParse(string, 10);
    }

    @CheckForNull
    @GwtIncompatible(value="TODO")
    static Integer tryParse(String string, int radix) {
        int digit;
        int index;
        if (Preconditions.checkNotNull(string).isEmpty()) {
            return null;
        }
        if (radix < 2 || radix > 36) {
            throw new IllegalArgumentException("radix must be between MIN_RADIX and MAX_RADIX but was " + radix);
        }
        boolean negative = string.charAt(0) == '-';
        int n2 = index = negative ? 1 : 0;
        if (index == string.length()) {
            return null;
        }
        if ((digit = Ints.digit(string.charAt(index++))) < 0 || digit >= radix) {
            return null;
        }
        int accum = -digit;
        int cap = Integer.MIN_VALUE / radix;
        while (index < string.length()) {
            if ((digit = Ints.digit(string.charAt(index++))) < 0 || digit >= radix || accum < cap) {
                return null;
            }
            if ((accum *= radix) < Integer.MIN_VALUE + digit) {
                return null;
            }
            accum -= digit;
        }
        if (negative) {
            return accum;
        }
        if (accum == Integer.MIN_VALUE) {
            return null;
        }
        return -accum;
    }

    static {
        int i2;
        asciiDigits = new byte[128];
        Arrays.fill(asciiDigits, (byte)-1);
        for (i2 = 0; i2 <= 9; ++i2) {
            Ints.asciiDigits[48 + i2] = (byte)i2;
        }
        for (i2 = 0; i2 <= 26; ++i2) {
            Ints.asciiDigits[65 + i2] = (byte)(10 + i2);
            Ints.asciiDigits[97 + i2] = (byte)(10 + i2);
        }
    }

    @GwtCompatible
    private static class IntArrayAsList
    extends AbstractList<Integer>
    implements RandomAccess,
    Serializable {
        final int[] array;
        final int start;
        final int end;
        private static final long serialVersionUID = 0L;

        IntArrayAsList(int[] array) {
            this(array, 0, array.length);
        }

        IntArrayAsList(int[] array, int start, int end) {
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
        public Integer get(int index) {
            Preconditions.checkElementIndex(index, this.size());
            return this.array[this.start + index];
        }

        @Override
        public boolean contains(Object target) {
            return target instanceof Integer && Ints.indexOf(this.array, (Integer)target, this.start, this.end) != -1;
        }

        @Override
        public int indexOf(Object target) {
            int i2;
            if (target instanceof Integer && (i2 = Ints.indexOf(this.array, (Integer)target, this.start, this.end)) >= 0) {
                return i2 - this.start;
            }
            return -1;
        }

        @Override
        public int lastIndexOf(Object target) {
            int i2;
            if (target instanceof Integer && (i2 = Ints.lastIndexOf(this.array, (Integer)target, this.start, this.end)) >= 0) {
                return i2 - this.start;
            }
            return -1;
        }

        @Override
        public Integer set(int index, Integer element) {
            Preconditions.checkElementIndex(index, this.size());
            int oldValue = this.array[this.start + index];
            this.array[this.start + index] = Preconditions.checkNotNull(element);
            return oldValue;
        }

        @Override
        public List<Integer> subList(int fromIndex, int toIndex) {
            int size = this.size();
            Preconditions.checkPositionIndexes(fromIndex, toIndex, size);
            if (fromIndex == toIndex) {
                return Collections.emptyList();
            }
            return new IntArrayAsList(this.array, this.start + fromIndex, this.start + toIndex);
        }

        @Override
        public boolean equals(Object object) {
            if (object == this) {
                return true;
            }
            if (object instanceof IntArrayAsList) {
                IntArrayAsList that = (IntArrayAsList)object;
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
                result = 31 * result + Ints.hashCode(this.array[i2]);
            }
            return result;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder(this.size() * 5);
            builder.append('[').append(this.array[this.start]);
            for (int i2 = this.start + 1; i2 < this.end; ++i2) {
                builder.append(", ").append(this.array[i2]);
            }
            return builder.append(']').toString();
        }

        int[] toIntArray() {
            int size = this.size();
            int[] result = new int[size];
            System.arraycopy(this.array, this.start, result, 0, size);
            return result;
        }
    }

    private static enum LexicographicalComparator implements Comparator<int[]>
    {
        INSTANCE;


        @Override
        public int compare(int[] left, int[] right) {
            int minLength = Math.min(left.length, right.length);
            for (int i2 = 0; i2 < minLength; ++i2) {
                int result = Ints.compare(left[i2], right[i2]);
                if (result == 0) continue;
                return result;
            }
            return left.length - right.length;
        }
    }

    private static final class IntConverter
    extends Converter<String, Integer>
    implements Serializable {
        static final IntConverter INSTANCE = new IntConverter();
        private static final long serialVersionUID = 1L;

        private IntConverter() {
        }

        @Override
        protected Integer doForward(String value) {
            return Integer.decode(value);
        }

        @Override
        protected String doBackward(Integer value) {
            return value.toString();
        }

        public String toString() {
            return "Ints.stringConverter()";
        }

        private Object readResolve() {
            return INSTANCE;
        }
    }
}

