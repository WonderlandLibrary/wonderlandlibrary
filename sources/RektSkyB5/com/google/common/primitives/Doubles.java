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
import java.io.Serializable;
import java.util.AbstractList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.RandomAccess;
import java.util.regex.Pattern;
import javax.annotation.Nullable;

@GwtCompatible(emulated=true)
public final class Doubles {
    public static final int BYTES = 8;
    @GwtIncompatible(value="regular expressions")
    static final Pattern FLOATING_POINT_PATTERN = Doubles.fpPattern();

    private Doubles() {
    }

    public static int hashCode(double value) {
        return Double.valueOf(value).hashCode();
    }

    public static int compare(double a2, double b2) {
        return Double.compare(a2, b2);
    }

    public static boolean isFinite(double value) {
        return Double.NEGATIVE_INFINITY < value & value < Double.POSITIVE_INFINITY;
    }

    public static boolean contains(double[] array, double target) {
        for (double value : array) {
            if (value != target) continue;
            return true;
        }
        return false;
    }

    public static int indexOf(double[] array, double target) {
        return Doubles.indexOf(array, target, 0, array.length);
    }

    private static int indexOf(double[] array, double target, int start, int end) {
        for (int i2 = start; i2 < end; ++i2) {
            if (array[i2] != target) continue;
            return i2;
        }
        return -1;
    }

    public static int indexOf(double[] array, double[] target) {
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

    public static int lastIndexOf(double[] array, double target) {
        return Doubles.lastIndexOf(array, target, 0, array.length);
    }

    private static int lastIndexOf(double[] array, double target, int start, int end) {
        for (int i2 = end - 1; i2 >= start; --i2) {
            if (array[i2] != target) continue;
            return i2;
        }
        return -1;
    }

    public static double min(double ... array) {
        Preconditions.checkArgument(array.length > 0);
        double min = array[0];
        for (int i2 = 1; i2 < array.length; ++i2) {
            min = Math.min(min, array[i2]);
        }
        return min;
    }

    public static double max(double ... array) {
        Preconditions.checkArgument(array.length > 0);
        double max = array[0];
        for (int i2 = 1; i2 < array.length; ++i2) {
            max = Math.max(max, array[i2]);
        }
        return max;
    }

    public static double[] concat(double[] ... arrays) {
        int length = 0;
        for (double[] array : arrays) {
            length += array.length;
        }
        double[] result = new double[length];
        int pos = 0;
        for (double[] array : arrays) {
            System.arraycopy(array, 0, result, pos, array.length);
            pos += array.length;
        }
        return result;
    }

    @Beta
    public static Converter<String, Double> stringConverter() {
        return DoubleConverter.INSTANCE;
    }

    public static double[] ensureCapacity(double[] array, int minLength, int padding) {
        Preconditions.checkArgument(minLength >= 0, "Invalid minLength: %s", minLength);
        Preconditions.checkArgument(padding >= 0, "Invalid padding: %s", padding);
        return array.length < minLength ? Doubles.copyOf(array, minLength + padding) : array;
    }

    private static double[] copyOf(double[] original, int length) {
        double[] copy = new double[length];
        System.arraycopy(original, 0, copy, 0, Math.min(original.length, length));
        return copy;
    }

    public static String join(String separator, double ... array) {
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

    public static Comparator<double[]> lexicographicalComparator() {
        return LexicographicalComparator.INSTANCE;
    }

    public static double[] toArray(Collection<? extends Number> collection) {
        if (collection instanceof DoubleArrayAsList) {
            return ((DoubleArrayAsList)collection).toDoubleArray();
        }
        Object[] boxedArray = collection.toArray();
        int len = boxedArray.length;
        double[] array = new double[len];
        for (int i2 = 0; i2 < len; ++i2) {
            array[i2] = ((Number)Preconditions.checkNotNull(boxedArray[i2])).doubleValue();
        }
        return array;
    }

    public static List<Double> asList(double ... backingArray) {
        if (backingArray.length == 0) {
            return Collections.emptyList();
        }
        return new DoubleArrayAsList(backingArray);
    }

    @GwtIncompatible(value="regular expressions")
    private static Pattern fpPattern() {
        String decimal = "(?:\\d++(?:\\.\\d*+)?|\\.\\d++)";
        String completeDec = decimal + "(?:[eE][+-]?\\d++)?[fFdD]?";
        String hex = "(?:\\p{XDigit}++(?:\\.\\p{XDigit}*+)?|\\.\\p{XDigit}++)";
        String completeHex = "0[xX]" + hex + "[pP][+-]?\\d++[fFdD]?";
        String fpPattern = "[+-]?(?:NaN|Infinity|" + completeDec + "|" + completeHex + ")";
        return Pattern.compile(fpPattern);
    }

    @Nullable
    @GwtIncompatible(value="regular expressions")
    @Beta
    public static Double tryParse(String string) {
        if (FLOATING_POINT_PATTERN.matcher(string).matches()) {
            try {
                return Double.parseDouble(string);
            }
            catch (NumberFormatException numberFormatException) {
                // empty catch block
            }
        }
        return null;
    }

    @GwtCompatible
    private static class DoubleArrayAsList
    extends AbstractList<Double>
    implements RandomAccess,
    Serializable {
        final double[] array;
        final int start;
        final int end;
        private static final long serialVersionUID = 0L;

        DoubleArrayAsList(double[] array) {
            this(array, 0, array.length);
        }

        DoubleArrayAsList(double[] array, int start, int end) {
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
        public Double get(int index) {
            Preconditions.checkElementIndex(index, this.size());
            return this.array[this.start + index];
        }

        @Override
        public boolean contains(Object target) {
            return target instanceof Double && Doubles.indexOf(this.array, (Double)target, this.start, this.end) != -1;
        }

        @Override
        public int indexOf(Object target) {
            int i2;
            if (target instanceof Double && (i2 = Doubles.indexOf(this.array, (Double)target, this.start, this.end)) >= 0) {
                return i2 - this.start;
            }
            return -1;
        }

        @Override
        public int lastIndexOf(Object target) {
            int i2;
            if (target instanceof Double && (i2 = Doubles.lastIndexOf(this.array, (Double)target, this.start, this.end)) >= 0) {
                return i2 - this.start;
            }
            return -1;
        }

        @Override
        public Double set(int index, Double element) {
            Preconditions.checkElementIndex(index, this.size());
            double oldValue = this.array[this.start + index];
            this.array[this.start + index] = Preconditions.checkNotNull(element);
            return oldValue;
        }

        @Override
        public List<Double> subList(int fromIndex, int toIndex) {
            int size = this.size();
            Preconditions.checkPositionIndexes(fromIndex, toIndex, size);
            if (fromIndex == toIndex) {
                return Collections.emptyList();
            }
            return new DoubleArrayAsList(this.array, this.start + fromIndex, this.start + toIndex);
        }

        @Override
        public boolean equals(Object object) {
            if (object == this) {
                return true;
            }
            if (object instanceof DoubleArrayAsList) {
                DoubleArrayAsList that = (DoubleArrayAsList)object;
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
                result = 31 * result + Doubles.hashCode(this.array[i2]);
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

        double[] toDoubleArray() {
            int size = this.size();
            double[] result = new double[size];
            System.arraycopy(this.array, this.start, result, 0, size);
            return result;
        }
    }

    private static enum LexicographicalComparator implements Comparator<double[]>
    {
        INSTANCE;


        @Override
        public int compare(double[] left, double[] right) {
            int minLength = Math.min(left.length, right.length);
            for (int i2 = 0; i2 < minLength; ++i2) {
                int result = Doubles.compare(left[i2], right[i2]);
                if (result == 0) continue;
                return result;
            }
            return left.length - right.length;
        }
    }

    private static final class DoubleConverter
    extends Converter<String, Double>
    implements Serializable {
        static final DoubleConverter INSTANCE = new DoubleConverter();
        private static final long serialVersionUID = 1L;

        private DoubleConverter() {
        }

        @Override
        protected Double doForward(String value) {
            return Double.valueOf(value);
        }

        @Override
        protected String doBackward(Double value) {
            return value.toString();
        }

        public String toString() {
            return "Doubles.stringConverter()";
        }

        private Object readResolve() {
            return INSTANCE;
        }
    }
}

