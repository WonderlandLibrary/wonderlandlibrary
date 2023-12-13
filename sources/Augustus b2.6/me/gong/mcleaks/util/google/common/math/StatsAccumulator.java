// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.math;

import me.gong.mcleaks.util.google.common.base.Preconditions;
import java.util.Iterator;
import me.gong.mcleaks.util.google.common.primitives.Doubles;
import me.gong.mcleaks.util.google.common.annotations.GwtIncompatible;
import me.gong.mcleaks.util.google.common.annotations.Beta;

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
    
    public void addAll(final Stats values) {
        if (values.count() == 0L) {
            return;
        }
        if (this.count == 0L) {
            this.count = values.count();
            this.mean = values.mean();
            this.sumOfSquaresOfDeltas = values.sumOfSquaresOfDeltas();
            this.min = values.min();
            this.max = values.max();
        }
        else {
            this.count += values.count();
            if (Doubles.isFinite(this.mean) && Doubles.isFinite(values.mean())) {
                final double delta = values.mean() - this.mean;
                this.mean += delta * values.count() / this.count;
                this.sumOfSquaresOfDeltas += values.sumOfSquaresOfDeltas() + delta * (values.mean() - this.mean) * values.count();
            }
            else {
                this.mean = calculateNewMeanNonFinite(this.mean, values.mean());
                this.sumOfSquaresOfDeltas = Double.NaN;
            }
            this.min = Math.min(this.min, values.min());
            this.max = Math.max(this.max, values.max());
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
