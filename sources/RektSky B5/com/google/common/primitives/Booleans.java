/*
 * Decompiled with CFR 0.152.
 */
package com.google.common.primitives;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.AbstractList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.RandomAccess;

@GwtCompatible
public final class Booleans {
    private Booleans() {
    }

    public static int hashCode(boolean value) {
        return value ? 1231 : 1237;
    }

    public static int compare(boolean a2, boolean b2) {
        return a2 == b2 ? 0 : (a2 ? 1 : -1);
    }

    public static boolean contains(boolean[] array, boolean target) {
        for (boolean value : array) {
            if (value != target) continue;
            return true;
        }
        return false;
    }

    public static int indexOf(boolean[] array, boolean target) {
        return Booleans.indexOf(array, target, 0, array.length);
    }

    private static int indexOf(boolean[] array, boolean target, int start, int end) {
        for (int i2 = start; i2 < end; ++i2) {
            if (array[i2] != target) continue;
            return i2;
        }
        return -1;
    }

    public static int indexOf(boolean[] array, boolean[] target) {
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

    public static int lastIndexOf(boolean[] array, boolean target) {
        return Booleans.lastIndexOf(array, target, 0, array.length);
    }

    private static int lastIndexOf(boolean[] array, boolean target, int start, int end) {
        for (int i2 = end - 1; i2 >= start; --i2) {
            if (array[i2] != target) continue;
            return i2;
        }
        return -1;
    }

    public static boolean[] concat(boolean[] ... arrays) {
        int length = 0;
        for (boolean[] array : arrays) {
            length += array.length;
        }
        boolean[] result = new boolean[length];
        int pos = 0;
        for (boolean[] array : arrays) {
            System.arraycopy(array, 0, result, pos, array.length);
            pos += array.length;
        }
        return result;
    }

    public static boolean[] ensureCapacity(boolean[] array, int minLength, int padding) {
        Preconditions.checkArgument(minLength >= 0, "Invalid minLength: %s", minLength);
        Preconditions.checkArgument(padding >= 0, "Invalid padding: %s", padding);
        return array.length < minLength ? Booleans.copyOf(array, minLength + padding) : array;
    }

    private static boolean[] copyOf(boolean[] original, int length) {
        boolean[] copy = new boolean[length];
        System.arraycopy(original, 0, copy, 0, Math.min(original.length, length));
        return copy;
    }

    public static String join(String separator, boolean ... array) {
        Preconditions.checkNotNull(separator);
        if (array.length == 0) {
            return "";
        }
        StringBuilder builder = new StringBuilder(array.length * 7);
        builder.append(array[0]);
        for (int i2 = 1; i2 < array.length; ++i2) {
            builder.append(separator).append(array[i2]);
        }
        return builder.toString();
    }

    public static Comparator<boolean[]> lexicographicalComparator() {
        return LexicographicalComparator.INSTANCE;
    }

    public static boolean[] toArray(Collection<Boolean> collection) {
        if (collection instanceof BooleanArrayAsList) {
            return ((BooleanArrayAsList)collection).toBooleanArray();
        }
        Object[] boxedArray = collection.toArray();
        int len = boxedArray.length;
        boolean[] array = new boolean[len];
        for (int i2 = 0; i2 < len; ++i2) {
            array[i2] = (Boolean)Preconditions.checkNotNull(boxedArray[i2]);
        }
        return array;
    }

    public static List<Boolean> asList(boolean ... backingArray) {
        if (backingArray.length == 0) {
            return Collections.emptyList();
        }
        return new BooleanArrayAsList(backingArray);
    }

    @Beta
    public static int countTrue(boolean ... values) {
        int count = 0;
        for (boolean value : values) {
            if (!value) continue;
            ++count;
        }
        return count;
    }

    @GwtCompatible
    private static class BooleanArrayAsList
    extends AbstractList<Boolean>
    implements RandomAccess,
    Serializable {
        final boolean[] array;
        final int start;
        final int end;
        private static final long serialVersionUID = 0L;

        BooleanArrayAsList(boolean[] array) {
            this(array, 0, array.length);
        }

        BooleanArrayAsList(boolean[] array, int start, int end) {
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
        public Boolean get(int index) {
            Preconditions.checkElementIndex(index, this.size());
            return this.array[this.start + index];
        }

        @Override
        public boolean contains(Object target) {
            return target instanceof Boolean && Booleans.indexOf(this.array, (Boolean)target, this.start, this.end) != -1;
        }

        @Override
        public int indexOf(Object target) {
            int i2;
            if (target instanceof Boolean && (i2 = Booleans.indexOf(this.array, (Boolean)target, this.start, this.end)) >= 0) {
                return i2 - this.start;
            }
            return -1;
        }

        @Override
        public int lastIndexOf(Object target) {
            int i2;
            if (target instanceof Boolean && (i2 = Booleans.lastIndexOf(this.array, (Boolean)target, this.start, this.end)) >= 0) {
                return i2 - this.start;
            }
            return -1;
        }

        @Override
        public Boolean set(int index, Boolean element) {
            Preconditions.checkElementIndex(index, this.size());
            boolean oldValue = this.array[this.start + index];
            this.array[this.start + index] = Preconditions.checkNotNull(element);
            return oldValue;
        }

        @Override
        public List<Boolean> subList(int fromIndex, int toIndex) {
            int size = this.size();
            Preconditions.checkPositionIndexes(fromIndex, toIndex, size);
            if (fromIndex == toIndex) {
                return Collections.emptyList();
            }
            return new BooleanArrayAsList(this.array, this.start + fromIndex, this.start + toIndex);
        }

        @Override
        public boolean equals(Object object) {
            if (object == this) {
                return true;
            }
            if (object instanceof BooleanArrayAsList) {
                BooleanArrayAsList that = (BooleanArrayAsList)object;
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
                result = 31 * result + Booleans.hashCode(this.array[i2]);
            }
            return result;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder(this.size() * 7);
            builder.append(this.array[this.start] ? "[true" : "[false");
            for (int i2 = this.start + 1; i2 < this.end; ++i2) {
                builder.append(this.array[i2] ? ", true" : ", false");
            }
            return builder.append(']').toString();
        }

        boolean[] toBooleanArray() {
            int size = this.size();
            boolean[] result = new boolean[size];
            System.arraycopy(this.array, this.start, result, 0, size);
            return result;
        }
    }

    private static enum LexicographicalComparator implements Comparator<boolean[]>
    {
        INSTANCE;


        @Override
        public int compare(boolean[] left, boolean[] right) {
            int minLength = Math.min(left.length, right.length);
            for (int i2 = 0; i2 < minLength; ++i2) {
                int result = Booleans.compare(left[i2], right[i2]);
                if (result == 0) continue;
                return result;
            }
            return left.length - right.length;
        }
    }
}

