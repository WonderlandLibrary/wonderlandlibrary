// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.math;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.math.RoundingMode;
import com.google.common.primitives.Doubles;
import com.google.common.primitives.Ints;
import java.util.Collection;
import com.google.common.base.Preconditions;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.Beta;

@ElementTypesAreNonnullByDefault
@Beta
@GwtIncompatible
public final class Quantiles
{
    public static ScaleAndIndex median() {
        return scale(2).index(1);
    }
    
    public static Scale quartiles() {
        return scale(4);
    }
    
    public static Scale percentiles() {
        return scale(100);
    }
    
    public static Scale scale(final int scale) {
        return new Scale(scale);
    }
    
    private static boolean containsNaN(final double... dataset) {
        for (final double value : dataset) {
            if (Double.isNaN(value)) {
                return true;
            }
        }
        return false;
    }
    
    private static double interpolate(final double lower, final double upper, final double remainder, final double scale) {
        if (lower == Double.NEGATIVE_INFINITY) {
            if (upper == Double.POSITIVE_INFINITY) {
                return Double.NaN;
            }
            return Double.NEGATIVE_INFINITY;
        }
        else {
            if (upper == Double.POSITIVE_INFINITY) {
                return Double.POSITIVE_INFINITY;
            }
            return lower + (upper - lower) * remainder / scale;
        }
    }
    
    private static void checkIndex(final int index, final int scale) {
        if (index < 0 || index > scale) {
            throw new IllegalArgumentException(new StringBuilder(70).append("Quantile indexes must be between 0 and the scale, which is ").append(scale).toString());
        }
    }
    
    private static double[] longsToDoubles(final long[] longs) {
        final int len = longs.length;
        final double[] doubles = new double[len];
        for (int i = 0; i < len; ++i) {
            doubles[i] = (double)longs[i];
        }
        return doubles;
    }
    
    private static double[] intsToDoubles(final int[] ints) {
        final int len = ints.length;
        final double[] doubles = new double[len];
        for (int i = 0; i < len; ++i) {
            doubles[i] = ints[i];
        }
        return doubles;
    }
    
    private static void selectInPlace(final int required, final double[] array, int from, int to) {
        if (required == from) {
            int min = from;
            for (int index = from + 1; index <= to; ++index) {
                if (array[min] > array[index]) {
                    min = index;
                }
            }
            if (min != from) {
                swap(array, min, from);
            }
            return;
        }
        while (to > from) {
            final int partitionPoint = partition(array, from, to);
            if (partitionPoint >= required) {
                to = partitionPoint - 1;
            }
            if (partitionPoint <= required) {
                from = partitionPoint + 1;
            }
        }
    }
    
    private static int partition(final double[] array, final int from, final int to) {
        movePivotToStartOfSlice(array, from, to);
        final double pivot = array[from];
        int partitionPoint = to;
        for (int i = to; i > from; --i) {
            if (array[i] > pivot) {
                swap(array, partitionPoint, i);
                --partitionPoint;
            }
        }
        swap(array, from, partitionPoint);
        return partitionPoint;
    }
    
    private static void movePivotToStartOfSlice(final double[] array, final int from, final int to) {
        final int mid = from + to >>> 1;
        final boolean toLessThanMid = array[to] < array[mid];
        final boolean midLessThanFrom = array[mid] < array[from];
        final boolean toLessThanFrom = array[to] < array[from];
        if (toLessThanMid == midLessThanFrom) {
            swap(array, mid, from);
        }
        else if (toLessThanMid != toLessThanFrom) {
            swap(array, from, to);
        }
    }
    
    private static void selectAllInPlace(final int[] allRequired, final int requiredFrom, final int requiredTo, final double[] array, final int from, final int to) {
        final int requiredChosen = chooseNextSelection(allRequired, requiredFrom, requiredTo, from, to);
        final int required = allRequired[requiredChosen];
        selectInPlace(required, array, from, to);
        int requiredBelow;
        for (requiredBelow = requiredChosen - 1; requiredBelow >= requiredFrom && allRequired[requiredBelow] == required; --requiredBelow) {}
        if (requiredBelow >= requiredFrom) {
            selectAllInPlace(allRequired, requiredFrom, requiredBelow, array, from, required - 1);
        }
        int requiredAbove;
        for (requiredAbove = requiredChosen + 1; requiredAbove <= requiredTo && allRequired[requiredAbove] == required; ++requiredAbove) {}
        if (requiredAbove <= requiredTo) {
            selectAllInPlace(allRequired, requiredAbove, requiredTo, array, required + 1, to);
        }
    }
    
    private static int chooseNextSelection(final int[] allRequired, final int requiredFrom, final int requiredTo, final int from, final int to) {
        if (requiredFrom == requiredTo) {
            return requiredFrom;
        }
        final int centerFloor = from + to >>> 1;
        int low = requiredFrom;
        int high = requiredTo;
        while (high > low + 1) {
            final int mid = low + high >>> 1;
            if (allRequired[mid] > centerFloor) {
                high = mid;
            }
            else {
                if (allRequired[mid] >= centerFloor) {
                    return mid;
                }
                low = mid;
            }
        }
        if (from + to - allRequired[low] - allRequired[high] > 0) {
            return high;
        }
        return low;
    }
    
    private static void swap(final double[] array, final int i, final int j) {
        final double temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
    
    public static final class Scale
    {
        private final int scale;
        
        private Scale(final int scale) {
            Preconditions.checkArgument(scale > 0, (Object)"Quantile scale must be positive");
            this.scale = scale;
        }
        
        public ScaleAndIndex index(final int index) {
            return new ScaleAndIndex(this.scale, index);
        }
        
        public ScaleAndIndexes indexes(final int... indexes) {
            return new ScaleAndIndexes(this.scale, (int[])indexes.clone());
        }
        
        public ScaleAndIndexes indexes(final Collection<Integer> indexes) {
            return new ScaleAndIndexes(this.scale, Ints.toArray(indexes));
        }
    }
    
    public static final class ScaleAndIndex
    {
        private final int scale;
        private final int index;
        
        private ScaleAndIndex(final int scale, final int index) {
            checkIndex(index, scale);
            this.scale = scale;
            this.index = index;
        }
        
        public double compute(final Collection<? extends Number> dataset) {
            return this.computeInPlace(Doubles.toArray(dataset));
        }
        
        public double compute(final double... dataset) {
            return this.computeInPlace((double[])dataset.clone());
        }
        
        public double compute(final long... dataset) {
            return this.computeInPlace(longsToDoubles(dataset));
        }
        
        public double compute(final int... dataset) {
            return this.computeInPlace(intsToDoubles(dataset));
        }
        
        public double computeInPlace(final double... dataset) {
            Preconditions.checkArgument(dataset.length > 0, (Object)"Cannot calculate quantiles of an empty dataset");
            if (containsNaN(dataset)) {
                return Double.NaN;
            }
            final long numerator = this.index * (long)(dataset.length - 1);
            final int quotient = (int)LongMath.divide(numerator, this.scale, RoundingMode.DOWN);
            final int remainder = (int)(numerator - quotient * (long)this.scale);
            selectInPlace(quotient, dataset, 0, dataset.length - 1);
            if (remainder == 0) {
                return dataset[quotient];
            }
            selectInPlace(quotient + 1, dataset, quotient + 1, dataset.length - 1);
            return interpolate(dataset[quotient], dataset[quotient + 1], remainder, this.scale);
        }
    }
    
    public static final class ScaleAndIndexes
    {
        private final int scale;
        private final int[] indexes;
        
        private ScaleAndIndexes(final int scale, final int[] indexes) {
            for (final int index : indexes) {
                checkIndex(index, scale);
            }
            Preconditions.checkArgument(indexes.length > 0, (Object)"Indexes must be a non empty array");
            this.scale = scale;
            this.indexes = indexes;
        }
        
        public Map<Integer, Double> compute(final Collection<? extends Number> dataset) {
            return this.computeInPlace(Doubles.toArray(dataset));
        }
        
        public Map<Integer, Double> compute(final double... dataset) {
            return this.computeInPlace((double[])dataset.clone());
        }
        
        public Map<Integer, Double> compute(final long... dataset) {
            return this.computeInPlace(longsToDoubles(dataset));
        }
        
        public Map<Integer, Double> compute(final int... dataset) {
            return this.computeInPlace(intsToDoubles(dataset));
        }
        
        public Map<Integer, Double> computeInPlace(final double... dataset) {
            Preconditions.checkArgument(dataset.length > 0, (Object)"Cannot calculate quantiles of an empty dataset");
            if (containsNaN(dataset)) {
                final Map<Integer, Double> nanMap = new LinkedHashMap<Integer, Double>();
                for (final int index : this.indexes) {
                    nanMap.put(index, Double.NaN);
                }
                return Collections.unmodifiableMap((Map<? extends Integer, ? extends Double>)nanMap);
            }
            final int[] quotients = new int[this.indexes.length];
            final int[] remainders = new int[this.indexes.length];
            final int[] requiredSelections = new int[this.indexes.length * 2];
            int requiredSelectionsCount = 0;
            for (int i = 0; i < this.indexes.length; ++i) {
                final long numerator = this.indexes[i] * (long)(dataset.length - 1);
                final int quotient = (int)LongMath.divide(numerator, this.scale, RoundingMode.DOWN);
                final int remainder = (int)(numerator - quotient * (long)this.scale);
                quotients[i] = quotient;
                remainders[i] = remainder;
                requiredSelections[requiredSelectionsCount] = quotient;
                ++requiredSelectionsCount;
                if (remainder != 0) {
                    requiredSelections[requiredSelectionsCount] = quotient + 1;
                    ++requiredSelectionsCount;
                }
            }
            Arrays.sort(requiredSelections, 0, requiredSelectionsCount);
            selectAllInPlace(requiredSelections, 0, requiredSelectionsCount - 1, dataset, 0, dataset.length - 1);
            final Map<Integer, Double> ret = new LinkedHashMap<Integer, Double>();
            for (int j = 0; j < this.indexes.length; ++j) {
                final int quotient2 = quotients[j];
                final int remainder2 = remainders[j];
                if (remainder2 == 0) {
                    ret.put(this.indexes[j], dataset[quotient2]);
                }
                else {
                    ret.put(this.indexes[j], interpolate(dataset[quotient2], dataset[quotient2 + 1], remainder2, this.scale));
                }
            }
            return Collections.unmodifiableMap((Map<? extends Integer, ? extends Double>)ret);
        }
    }
}
