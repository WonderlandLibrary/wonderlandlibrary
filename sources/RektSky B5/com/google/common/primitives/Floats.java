/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package com.google.common.primitives;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Converter;
import com.google.common.base.Preconditions;
import com.google.common.primitives.Doubles;
import java.io.Serializable;
import java.util.AbstractList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.RandomAccess;
import javax.annotation.Nullable;

@GwtCompatible(emulated=true)
public final class Floats {
    public static final int BYTES = 4;

    private Floats() {
    }

    public static int hashCode(float value) {
        return Float.valueOf(value).hashCode();
    }

    public static int compare(float a2, float b2) {
        return Float.compare(a2, b2);
    }

    public static boolean isFinite(float value) {
        return Float.NEGATIVE_INFINITY < value & value < Float.POSITIVE_INFINITY;
    }

    public static boolean contains(float[] array, float target) {
        for (float value : array) {
            if (value != target) continue;
            return true;
        }
        return false;
    }

    public static int indexOf(float[] array, float target) {
        return Floats.indexOf(array, target, 0, array.length);
    }

    private static int indexOf(float[] array, float target, int start, int end) {
        for (int i2 = start; i2 < end; ++i2) {
            if (array[i2] != target) continue;
            return i2;
        }
        return -1;
    }

    public static int indexOf(float[] array, float[] target) {
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

    public static int lastIndexOf(float[] array, float target) {
        return Floats.lastIndexOf(array, target, 0, array.length);
    }

    private static int lastIndexOf(float[] array, float target, int start, int end) {
        for (int i2 = end - 1; i2 >= start; --i2) {
            if (array[i2] != target) continue;
            return i2;
        }
        return -1;
    }

    public static float min(float ... array) {
        Preconditions.checkArgument(array.length > 0);
        float min = array[0];
        for (int i2 = 1; i2 < array.length; ++i2) {
            min = Math.min(min, array[i2]);
        }
        return min;
    }

    public static float max(float ... array) {
        Preconditions.checkArgument(array.length > 0);
        float max = array[0];
        for (int i2 = 1; i2 < array.length; ++i2) {
            max = Math.max(max, array[i2]);
        }
        return max;
    }

    public static float[] concat(float[] ... arrays) {
        int length = 0;
        for (float[] array : arrays) {
            length += array.length;
        }
        float[] result = new float[length];
        int pos = 0;
        for (float[] array : arrays) {
            System.arraycopy(array, 0, result, pos, array.length);
            pos += array.length;
        }
        return result;
    }

    @Beta
    public static Converter<String, Float> stringConverter() {
        return FloatConverter.INSTANCE;
    }

    public static float[] ensureCapacity(float[] array, int minLength, int padding) {
        Preconditions.checkArgument(minLength >= 0, "Invalid minLength: %s", minLength);
        Preconditions.checkArgument(padding >= 0, "Invalid padding: %s", padding);
        return array.length < minLength ? Floats.copyOf(array, minLength + padding) : array;
    }

    private static float[] copyOf(float[] original, int length) {
        float[] copy = new float[length];
        System.arraycopy(original, 0, copy, 0, Math.min(original.length, length));
        return copy;
    }

    public static String join(String separator, float ... array) {
        Preconditions.checkNotNull(separator);
        if (array.length == 0) {
            return "";
        }
        StringBuilder builder = new StringBuilder(array.length * 12);
        builder.append(array[0]);
        for (int i2 = 1; i2 < array.length; ++i2) {
            builder.append(separator).append(array[i2]);
        }
        return builder.toString();
    }

    public static Comparator<float[]> lexicographicalComparator() {
        return LexicographicalComparator.INSTANCE;
    }

    public static float[] toArray(Collection<? extends Number> collection) {
        if (collection instanceof FloatArrayAsList) {
            return ((FloatArrayAsList)collection).toFloatArray();
        }
        Object[] boxedArray = collection.toArray();
        int len = boxedArray.length;
        float[] array = new float[len];
        for (int i2 = 0; i2 < len; ++i2) {
            array[i2] = ((Number)Preconditions.checkNotNull(boxedArray[i2])).floatValue();
        }
        return array;
    }

    public static List<Float> asList(float ... backingArray) {
        if (backingArray.length == 0) {
            return Collections.emptyList();
        }
        return new FloatArrayAsList(backingArray);
    }

    @Nullable
    @GwtIncompatible(value="regular expressions")
    @Beta
    public static Float tryParse(String string) {
        if (Doubles.FLOATING_POINT_PATTERN.matcher(string).matches()) {
            try {
                return Float.valueOf(Float.parseFloat(string));
            }
            catch (NumberFormatException numberFormatException) {
                // empty catch block
            }
        }
        return null;
    }

    @GwtCompatible
    private static class FloatArrayAsList
    extends AbstractList<Float>
    implements RandomAccess,
    Serializable {
        final float[] array;
        final int start;
        final int end;
        private static final long serialVersionUID = 0L;

        FloatArrayAsList(float[] array) {
            this(array, 0, array.length);
        }

        FloatArrayAsList(float[] array, int start, int end) {
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
        public Float get(int index) {
            Preconditions.checkElementIndex(index, this.size());
            return Float.valueOf(this.array[this.start + index]);
        }

        @Override
        public boolean contains(Object target) {
            return target instanceof Float && Floats.indexOf(this.array, ((Float)target).floatValue(), this.start, this.end) != -1;
        }

        @Override
        public int indexOf(Object target) {
            int i2;
            if (target instanceof Float && (i2 = Floats.indexOf(this.array, ((Float)target).floatValue(), this.start, this.end)) >= 0) {
                return i2 - this.start;
            }
            return -1;
        }

        @Override
        public int lastIndexOf(Object target) {
            int i2;
            if (target instanceof Float && (i2 = Floats.lastIndexOf(this.array, ((Float)target).floatValue(), this.start, this.end)) >= 0) {
                return i2 - this.start;
            }
            return -1;
        }

        @Override
        public Float set(int index, Float element) {
            Preconditions.checkElementIndex(index, this.size());
            float oldValue = this.array[this.start + index];
            this.array[this.start + index] = Preconditions.checkNotNull(element).floatValue();
            return Float.valueOf(oldValue);
        }

        @Override
        public List<Float> subList(int fromIndex, int toIndex) {
            int size = this.size();
            Preconditions.checkPositionIndexes(fromIndex, toIndex, size);
            if (fromIndex == toIndex) {
                return Collections.emptyList();
            }
            return new FloatArrayAsList(this.array, this.start + fromIndex, this.start + toIndex);
        }

        @Override
        public boolean equals(Object object) {
            if (object == this) {
                return true;
            }
            if (object instanceof FloatArrayAsList) {
                FloatArrayAsList that = (FloatArrayAsList)object;
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
                result = 31 * result + Floats.hashCode(this.array[i2]);
            }
            return result;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder(this.size() * 12);
            builder.append('[').append(this.array[this.start]);
            for (int i2 = this.start + 1; i2 < this.end; ++i2) {
                builder.append(", ").append(this.array[i2]);
            }
            return builder.append(']').toString();
        }

        float[] toFloatArray() {
            int size = this.size();
            float[] result = new float[size];
            System.arraycopy(this.array, this.start, result, 0, size);
            return result;
        }
    }

    private static enum LexicographicalComparator implements Comparator<float[]>
    {
        INSTANCE;


        @Override
        public int compare(float[] left, float[] right) {
            int minLength = Math.min(left.length, right.length);
            for (int i2 = 0; i2 < minLength; ++i2) {
                int result = Floats.compare(left[i2], right[i2]);
                if (result == 0) continue;
                return result;
            }
            return left.length - right.length;
        }
    }

    private static final class FloatConverter
    extends Converter<String, Float>
    implements Serializable {
        static final FloatConverter INSTANCE = new FloatConverter();
        private static final long serialVersionUID = 1L;

        private FloatConverter() {
        }

        @Override
        protected Float doForward(String value) {
            return Float.valueOf(value);
        }

        @Override
        protected String doBackward(Float value) {
            return value.toString();
        }

        public String toString() {
            return "Floats.stringConverter()";
        }

        private Object readResolve() {
            return INSTANCE;
        }
    }
}

