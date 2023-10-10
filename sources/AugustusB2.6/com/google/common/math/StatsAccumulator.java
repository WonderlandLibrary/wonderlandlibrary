// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.math;

import com.google.common.base.Preconditions;
import java.util.stream.LongStream;
import java.util.stream.IntStream;
import java.util.stream.DoubleStream;
import java.util.Iterator;
import com.google.common.primitives.Doubles;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.Beta;

@ElementTypesAreNonnullByDefault
@Beta
@GwtIncompatible
public final class StatsAccumulator
{
    private long count;
    private double mean;
    private double sumOfSquaresOfDeltas;
    private double min;
    private double max;
    
    public StatsAccumulator() {
        this.count = 0L;
        this.mean = 0.0;
        this.sumOfSquaresOfDeltas = 0.0;
        this.min = Double.NaN;
        this.max = Double.NaN;
    }
    
    public void add(final double value) {
        if (this.count == 0L) {
            this.count = 1L;
            this.mean = value;
            this.min = value;
            this.max = value;
            if (!Doubles.isFinite(value)) {
                this.sumOfSquaresOfDeltas = Double.NaN;
            }
        }
        else {
            ++this.count;
            if (Doubles.isFinite(value) && Doubles.isFinite(this.mean)) {
                final double delta = value - this.mean;
                this.mean += delta / this.count;
                this.sumOfSquaresOfDeltas += delta * (value - this.mean);
            }
            else {
                this.mean = calculateNewMeanNonFinite(this.mean, value);
                this.sumOfSquaresOfDeltas = Double.NaN;
            }
            this.min = Math.min(this.min, value);
            this.max = Math.max(this.max, value);
        }
    }
    
    public void addAll(final Iterable<? extends Number> values) {
        for (final Number value : values) {
            this.add(value.doubleValue());
        }
    }
    
    public void addAll(final Iterator<? extends Number> values) {
        while (values.hasNext()) {
            this.add(((Number)values.next()).doubleValue());
        }
    }
    
    public void addAll(final double... values) {
        for (final double value : values) {
            this.add(value);
        }
    }
    
    public void addAll(final int... values) {
        for (final int value : values) {
            this.add(value);
        }
    }
    
    public void addAll(final long... values) {
        for (final long value : values) {
            this.add((double)value);
        }
    }
    
    public void addAll(final DoubleStream values) {
        this.addAll(values.collect(StatsAccumulator::new, StatsAccumulator::add, StatsAccumulator::addAll));
    }
    
    public void addAll(final IntStream values) {
        this.addAll(values.collect(StatsAccumulator::new, StatsAccumulator::add, StatsAccumulator::addAll));
    }
    
    public void addAll(final LongStream values) {
        this.addAll(values.collect(StatsAccumulator::new, StatsAccumulator::add, StatsAccumulator::addAll));
    }
    
    public void addAll(final Stats values) {
        if (values.count() == 0L) {
            return;
        }
        this.merge(values.count(), values.mean(), values.sumOfSquaresOfDeltas(), values.min(), values.max());
    }
    
    public void addAll(final StatsAccumulator values) {
        if (values.count() == 0L) {
            return;
        }
        this.merge(values.count(), values.mean(), values.sumOfSquaresOfDeltas(), values.min(), values.max());
    }
    
    private void merge(final long otherCount, final double otherMean, final double otherSumOfSquaresOfDeltas, final double otherMin, final double otherMax) {
        if (this.count == 0L) {
            this.count = otherCount;
            this.mean = otherMean;
            this.sumOfSquaresOfDeltas = otherSumOfSquaresOfDeltas;
            this.min = otherMin;
            this.max = otherMax;
        }
        else {
            this.count += otherCount;
            if (Doubles.isFinite(this.mean) && Doubles.isFinite(otherMean)) {
                final double delta = otherMean - this.mean;
                this.mean += delta * otherCount / this.count;
                this.sumOfSquaresOfDeltas += otherSumOfSquaresOfDeltas + delta * (otherMean - this.mean) * otherCount;
            }
            else {
                this.mean = calculateNewMeanNonFinite(this.mean, otherMean);
                this.sumOfSquaresOfDeltas = Double.NaN;
            }
            this.min = Math.min(this.min, otherMin);
            this.max = Math.max(this.max, otherMax);
        }
    }
    
    public Stats snapshot() {
        return new Stats(this.count, this.mean, this.sumOfSquaresOfDeltas, this.min, this.max);
    }
    
    public long count() {
        return this.count;
    }
    
    public double mean() {
        Preconditions.checkState(this.count != 0L);
        return this.mean;
    }
    
    public final double sum() {
        return this.mean * this.count;
    }
    
    public final double populationVariance() {
        Preconditions.checkState(this.count != 0L);
        if (Double.isNaN(this.sumOfSquaresOfDeltas)) {
            return Double.NaN;
        }
        if (this.count == 1L) {
            return 0.0;
        }
        return DoubleUtils.ensureNonNegative(this.sumOfSquaresOfDeltas) / this.count;
    }
    
    public final double populationStandardDeviation() {
        return Math.sqrt(this.populationVariance());
    }
    
    public final double sampleVariance() {
        Preconditions.checkState(this.count > 1L);
        if (Double.isNaN(this.sumOfSquaresOfDeltas)) {
            return Double.NaN;
        }
        return DoubleUtils.ensureNonNegative(this.sumOfSquaresOfDeltas) / (this.count - 1L);
    }
    
    public final double sampleStandardDeviation() {
        return Math.sqrt(this.sampleVariance());
    }
    
    public double min() {
        Preconditions.checkState(this.count != 0L);
        return this.min;
    }
    
    public double max() {
        Preconditions.checkState(this.count != 0L);
        return this.max;
    }
    
    double sumOfSquaresOfDeltas() {
        return this.sumOfSquaresOfDeltas;
    }
    
    static double calculateNewMeanNonFinite(final double previousMean, final double value) {
        if (Doubles.isFinite(previousMean)) {
            return value;
        }
        if (Doubles.isFinite(value) || previousMean == value) {
            return previousMean;
        }
        return Double.NaN;
    }
}
