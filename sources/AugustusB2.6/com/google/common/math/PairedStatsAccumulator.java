// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.math;

import com.google.common.base.Preconditions;
import com.google.common.primitives.Doubles;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.Beta;

@ElementTypesAreNonnullByDefault
@Beta
@GwtIncompatible
public final class PairedStatsAccumulator
{
    private final StatsAccumulator xStats;
    private final StatsAccumulator yStats;
    private double sumOfProductsOfDeltas;
    
    public PairedStatsAccumulator() {
        this.xStats = new StatsAccumulator();
        this.yStats = new StatsAccumulator();
        this.sumOfProductsOfDeltas = 0.0;
    }
    
    public void add(final double x, final double y) {
        this.xStats.add(x);
        if (Doubles.isFinite(x) && Doubles.isFinite(y)) {
            if (this.xStats.count() > 1L) {
                this.sumOfProductsOfDeltas += (x - this.xStats.mean()) * (y - this.yStats.mean());
            }
        }
        else {
            this.sumOfProductsOfDeltas = Double.NaN;
        }
        this.yStats.add(y);
    }
    
    public void addAll(final PairedStats values) {
        if (values.count() == 0L) {
            return;
        }
        this.xStats.addAll(values.xStats());
        if (this.yStats.count() == 0L) {
            this.sumOfProductsOfDeltas = values.sumOfProductsOfDeltas();
        }
        else {
            this.sumOfProductsOfDeltas += values.sumOfProductsOfDeltas() + (values.xStats().mean() - this.xStats.mean()) * (values.yStats().mean() - this.yStats.mean()) * values.count();
        }
        this.yStats.addAll(values.yStats());
    }
    
    public PairedStats snapshot() {
        return new PairedStats(this.xStats.snapshot(), this.yStats.snapshot(), this.sumOfProductsOfDeltas);
    }
    
    public long count() {
        return this.xStats.count();
    }
    
    public Stats xStats() {
        return this.xStats.snapshot();
    }
    
    public Stats yStats() {
        return this.yStats.snapshot();
    }
    
    public double populationCovariance() {
        Preconditions.checkState(this.count() != 0L);
        return this.sumOfProductsOfDeltas / this.count();
    }
    
    public final double sampleCovariance() {
        Preconditions.checkState(this.count() > 1L);
        return this.sumOfProductsOfDeltas / (this.count() - 1L);
    }
    
    public final double pearsonsCorrelationCoefficient() {
        Preconditions.checkState(this.count() > 1L);
        if (Double.isNaN(this.sumOfProductsOfDeltas)) {
            return Double.NaN;
        }
        final double xSumOfSquaresOfDeltas = this.xStats.sumOfSquaresOfDeltas();
        final double ySumOfSquaresOfDeltas = this.yStats.sumOfSquaresOfDeltas();
        Preconditions.checkState(xSumOfSquaresOfDeltas > 0.0);
        Preconditions.checkState(ySumOfSquaresOfDeltas > 0.0);
        final double productOfSumsOfSquaresOfDeltas = this.ensurePositive(xSumOfSquaresOfDeltas * ySumOfSquaresOfDeltas);
        return ensureInUnitRange(this.sumOfProductsOfDeltas / Math.sqrt(productOfSumsOfSquaresOfDeltas));
    }
    
    public final LinearTransformation leastSquaresFit() {
        Preconditions.checkState(this.count() > 1L);
        if (Double.isNaN(this.sumOfProductsOfDeltas)) {
            return LinearTransformation.forNaN();
        }
        final double xSumOfSquaresOfDeltas = this.xStats.sumOfSquaresOfDeltas();
        if (xSumOfSquaresOfDeltas <= 0.0) {
            Preconditions.checkState(this.yStats.sumOfSquaresOfDeltas() > 0.0);
            return LinearTransformation.vertical(this.xStats.mean());
        }
        if (this.yStats.sumOfSquaresOfDeltas() > 0.0) {
            return LinearTransformation.mapping(this.xStats.mean(), this.yStats.mean()).withSlope(this.sumOfProductsOfDeltas / xSumOfSquaresOfDeltas);
        }
        return LinearTransformation.horizontal(this.yStats.mean());
    }
    
    private double ensurePositive(final double value) {
        if (value > 0.0) {
            return value;
        }
        return Double.MIN_VALUE;
    }
    
    private static double ensureInUnitRange(final double value) {
        return Doubles.constrainToRange(value, -1.0, 1.0);
    }
}
