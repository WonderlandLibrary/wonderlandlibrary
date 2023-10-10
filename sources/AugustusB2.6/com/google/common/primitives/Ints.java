// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.primitives;

import java.util.Spliterators;
import java.util.Spliterator;
import java.util.RandomAccess;
import java.util.AbstractList;
import java.io.Serializable;
import javax.annotation.CheckForNull;
import java.util.Collections;
import java.util.List;
import java.util.Collection;
import java.util.Comparator;
import java.util.Arrays;
import com.google.common.base.Converter;
import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible(emulated = true)
public final class Ints extends IntsMethodsForWeb
{
    public static final int BYTES = 4;
    public static final int MAX_POWER_OF_TWO = 1073741824;
    
    private Ints() {
    }
    
    public static int hashCode(final int value) {
        return value;
    }
    
    public static int checkedCast(final long value) {
        final int result = (int)value;
        Preconditions.checkArgument(result == value, "Out of range: %s", value);
        return result;
    }
    
    public static int saturatedCast(final long value) {
        if (value > 2147483647L) {
            return Integer.MAX_VALUE;
        }
        if (value < -2147483648L) {
            return Integer.MIN_VALUE;
        }
        return (int)value;
    }
    
    public static int compare(final int a, final int b) {
        return (a < b) ? -1 : ((a > b) ? 1 : 0);
    }
    
    public static boolean contains(final int[] array, final int target) {
        for (final int value : array) {
            if (value == target) {
                return true;
            }
        }
        return false;
    }
    
    public static int indexOf(final int[] array, final int target) {
        return indexOf(array, target, 0, array.length);
    }
    
    private static int indexOf(final int[] array, final int target, final int start, final int end) {
        for (int i = start; i < end; ++i) {
            if (array[i] == target) {
                return i;
            }
        }
        return -1;
    }
    
    public static int indexOf(final int[] array, final int[] target) {
        Preconditions.checkNotNull(array, (Object)"array");
        Preconditions.checkNotNull(target, (Object)"target");
        if (target.length == 0) {
            return 0;
        }
        int i = 0;
    Label_0023:
        while (i < array.length - target.length + 1) {
            for (int j = 0; j < target.length; ++j) {
                if (array[i + j] != target[j]) {
                    ++i;
                    continue Label_0023;
                }
            }
            return i;
        }
        return -1;
    }
    
    public static int lastIndexOf(final int[] array, final int target) {
        return lastIndexOf(array, target, 0, array.length);
    }
    
    private static int lastIndexOf(final int[] array, final int target, final int start, final int end) {
        for (int i = end - 1; i >= start; --i) {
            if (array[i] == target) {
                return i;
            }
        }
        return -1;
    }
    
    @GwtIncompatible("Available in GWT! Annotation is to avoid conflict with GWT specialization of base class.")
    public static int min(final int... array) {
        Preconditions.checkArgument(array.length > 0);
        int min = array[0];
        for (int i = 1; i < array.length; ++i) {
            if (array[i] < min) {
                min = array[i];
            }
        }
        return min;
    }
    
    @GwtIncompatible("Available in GWT! Annotation is to avoid conflict with GWT specialization of base class.")
    public static int max(final int... array) {
        Preconditions.checkArgument(array.length > 0);
        int max = array[0];
        for (int i = 1; i < array.length; ++i) {
            if (array[i] > max) {
                max = array[i];
            }
        }
        return max;
    }
    
    @Beta
    public static int constrainToRange(final int value, final int min, final int max) {
        Preconditions.checkArgument(min <= max, "min (%s) must be less than or equal to max (%s)", min, max);
        return Math.min(Math.max(value, min), max);
    }
    
    public static int[] concat(final int[]... arrays) {
        int length = 0;
        for (final int[] array : arrays) {
            length += array.length;
        }
        final int[] result = new int[length];
        int pos = 0;
        for (final int[] array2 : arrays) {
            System.arraycopy(array2, 0, result, pos, array2.length);
            pos += array2.length;
        }
        return result;
    }
    
    public static byte[] toByteArray(final int value) {
        return new byte[] { (byte)(value >> 24), (byte)(value >> 16), (byte)(value >> 8), (byte)value };
    }
    
    public static int fromByteArray(final byte[] bytes) {
        Preconditions.checkArgument(bytes.length >= 4, "array too small: %s < %s", bytes.length, 4);
        return fromBytes(bytes[0], bytes[1], bytes[2], bytes[3]);
    }
    
    public static int fromBytes(final byte b1, final byte b2, final byte b3, final byte b4) {
        return b1 << 24 | (b2 & 0xFF) << 16 | (b3 & 0xFF) << 8 | (b4 & 0xFF);
    }
    
    @Beta
    public static Converter<String, Integer> stringConverter() {
        return IntConverter.INSTANCE;
    }
    
    public static int[] ensureCapacity(final int[] array, final int minLength, final int padding) {
        Preconditions.checkArgument(minLength >= 0, "Invalid minLength: %s", minLength);
        Preconditions.checkArgument(padding >= 0, "Invalid padding: %s", padding);
        return (array.length < minLength) ? Arrays.copyOf(array, minLength + padding) : array;
    }
    
    public static String join(final String separator, final int... array) {
        Preconditions.checkNotNull(separator);
        if (array.length == 0) {
            return "";
        }
        final StringBuilder builder = new StringBuilder(array.length * 5);
        builder.append(array[0]);
        for (int i = 1; i < array.length; ++i) {
            builder.append(separator).append(array[i]);
        }
        return builder.toString();
    }
    
    public static Comparator<int[]> lexicographicalComparator() {
        return LexicographicalComparator.INSTANCE;
    }
    
    public static void sortDescending(final int[] array) {
        Preconditions.checkNotNull(array);
        sortDescending(array, 0, array.length);
    }
    
    public static void sortDescending(final int[] array, final int fromIndex, final int toIndex) {
        Preconditions.checkNotNull(array);
        Preconditions.checkPositionIndexes(fromIndex, toIndex, array.length);
        Arrays.sort(array, fromIndex, toIndex);
        reverse(array, fromIndex, toIndex);
    }
    
    public static void reverse(final int[] array) {
        Preconditions.checkNotNull(array);
        reverse(array, 0, array.length);
    }
    
    public static void reverse(final int[] array, final int fromIndex, final int toIndex) {
        Preconditions.checkNotNull(array);
        Preconditions.checkPositionIndexes(fromIndex, toIndex, array.length);
        for (int i = fromIndex, j = toIndex - 1; i < j; ++i, --j) {
            final int tmp = array[i];
            array[i] = array[j];
            array[j] = tmp;
        }
    }
    
    public static int[] toArray(final Collection<? extends Number> collection) {
        if (collection instanceof IntArrayAsList) {
            return ((IntArrayAsList)collection).toIntArray();
        }
        final Object[] boxedArray = collection.toArray();
        final int len = boxedArray.length;
        final int[] array = new int[len];
        for (int i = 0; i < len; ++i) {
            array[i] = Preconditions.checkNotNull(boxedArray[i]).intValue();
        }
        return array;
    }
    
    public static List<Integer> asList(final int... backingArray) {
        if (backingArray.length == 0) {
            return Collections.emptyList();
        }
        return new IntArrayAsList(backingArray);
    }
    
    @CheckForNull
    @Beta
    public static Integer tryParse(final String string) {
        return tryParse(string, 10);
    }
    
    @CheckForNull
    @Beta
    public static Integer tryParse(final String string, final int radix) {
        final Long result = Longs.tryParse(string, radix);
        if (result == null || result != result.intValue()) {
            return null;
        }
        return result.intValue();
    }
    
    private static final class IntConverter extends Converter<String, Integer> implements Serializable
    {
        static final IntConverter INSTANCE;
        private static final long serialVersionUID = 1L;
        
        @Override
        protected Integer doForward(final String value) {
            return Integer.decode(value);
        }
        
        @Override
        protected String doBackward(final Integer value) {
            return value.toString();
        }
        
        @Override
        public String toString() {
            return "Ints.stringConverter()";
        }
        
        private Object readResolve() {
            return IntConverter.INSTANCE;
        }
        
        static {
            INSTANCE = new IntConverter();
        }
    }
    
    private enum LexicographicalComparator implements Comparator<int[]>
    {
        INSTANCE;
        
        @Override
        public int compare(final int[] left, final int[] right) {
            for (int minLength = Math.min(left.length, right.length), i = 0; i < minLength; ++i) {
                final int result = Ints.compare(left[i], right[i]);
                if (result != 0) {
                    return result;
                }
            }
            return left.length - right.length;
        }
        
        @Override
        public String toString() {
            return "Ints.lexicographicalComparator()";
        }
        
        private static /* synthetic */ LexicographicalComparator[] $values() {
            return new LexicographicalComparator[] { LexicographicalComparator.INSTANCE };
        }
        
        static {
            $VALUES = $values();
        }
    }
    
    @GwtCompatible
    private static class IntArrayAsList extends AbstractList<Integer> implements RandomAccess, Serializable
    {
        final int[] array;
        final int start;
        final int end;
        private static final long serialVersionUID = 0L;
        
        IntArrayAsList(final int[] array) {
            this(array, 0, array.length);
        }
        
        IntArrayAsList(final int[] array, final int start, final int end) {
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
        public Integer get(final int index) {
            Preconditions.checkElementIndex(index, this.size());
            return this.array[this.start + index];
        }
        
        @Override
        public Spliterator.OfInt spliterator() {
            return Spliterators.spliterator(this.array, this.start, this.end, 0);
        }
        
        @Override
        public boolean contains(@CheckForNull final Object target) {
            return target instanceof Integer && indexOf(this.array, (int)target, this.start, this.end) != -1;
        }
        
        @Override
        public int indexOf(@CheckForNull final Object target) {
            if (target instanceof Integer) {
                final int i = indexOf(this.array, (int)target, this.start, this.end);
                if (i >= 0) {
                    return i - this.start;
                }
            }
            return -1;
        }
        
        @Override
        public int lastIndexOf(@CheckForNull final Object target) {
            if (target instanceof Integer) {
                final int i = lastIndexOf(this.array, (int)target, this.start, this.end);
                if (i >= 0) {
                    return i - this.start;
                }
            }
            return -1;
        }
        
        @Override
        public Integer set(final int index, final Integer element) {
            Preconditions.checkElementIndex(index, this.size());
            final int oldValue = this.array[this.start + index];
            this.array[this.start + index] = Preconditions.checkNotNull(element);
            return oldValue;
        }
        
        @Override
        public List<Integer> subList(final int fromIndex, final int toIndex) {
            final int size = this.size();
            Preconditions.checkPositionIndexes(fromIndex, toIndex, size);
            if (fromIndex == toIndex) {
                return Collections.emptyList();
            }
            return new IntArrayAsList(this.array, this.start + fromIndex, this.start + toIndex);
        }
        
        @Override
        public boolean equals(@CheckForNull final Object object) {
            if (object == this) {
                return true;
            }
            if (!(object instanceof IntArrayAsList)) {
                return super.equals(object);
            }
            final IntArrayAsList that = (IntArrayAsList)object;
            final int size = this.size();
            if (that.size() != size) {
                return false;
            }
            for (int i = 0; i < size; ++i) {
                if (this.array[this.start + i] != that.array[that.start + i]) {
                    return false;
                }
            }
            return true;
        }
        
        @Override
        public int hashCode() {
            int result = 1;
            for (int i = this.start; i < this.end; ++i) {
                result = 31 * result + Ints.hashCode(this.array[i]);
            }
            return result;
        }
        
        @Override
        public String toString() {
            final StringBuilder builder = new StringBuilder(this.size() * 5);
            builder.append('[').append(this.array[this.start]);
            for (int i = this.start + 1; i < this.end; ++i) {
                builder.append(", ").append(this.array[i]);
            }
            return builder.append(']').toString();
        }
        
        int[] toIntArray() {
            return Arrays.copyOfRange(this.array, this.start, this.end);
        }
    }
}
