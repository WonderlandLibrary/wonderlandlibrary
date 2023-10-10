// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.math;

import java.nio.ByteOrder;
import java.nio.ByteBuffer;
import com.google.common.primitives.Doubles;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import javax.annotation.CheckForNull;
import com.google.common.base.Preconditions;
import java.util.stream.Collector;
import java.util.stream.LongStream;
import java.util.stream.IntStream;
import java.util.stream.DoubleStream;
import java.util.Iterator;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.Beta;
import java.io.Serializable;

@ElementTypesAreNonnullByDefault
@Beta
@GwtIncompatible
public final class Stats implements Serializable
{
    private final long count;
    private final double mean;
    private final double sumOfSquaresOfDeltas;
    private final double min;
    private final double max;
    static final int BYTES = 40;
    private static final long serialVersionUID = 0L;
    
    Stats(final long count, final double mean, final double sumOfSquaresOfDeltas, final double min, final double max) {
        this.count = count;
        this.mean = mean;
        this.sumOfSquaresOfDeltas = sumOfSquaresOfDeltas;
        this.min = min;
        this.max = max;
    }
    
    public static Stats of(final Iterable<? extends Number> values) {
        final StatsAccumulator accumulator = new StatsAccumulator();
        accumulator.addAll(values);
        return accumulator.snapshot();
    }
    
    public static Stats of(final Iterator<? extends Number> values) {
        final StatsAccumulator accumulator = new StatsAccumulator();
        accumulator.addAll(values);
        return accumulator.snapshot();
    }
    
    public static Stats of(final double... values) {
        final StatsAccumulator acummulator = new StatsAccumulator();
        acummulator.addAll(values);
        return acummulator.snapshot();
    }
    
    public static Stats of(final int... values) {
        final StatsAccumulator acummulator = new StatsAccumulator();
        acummulator.addAll(values);
        return acummulator.snapshot();
    }
    
    public static Stats of(final long... values) {
        final StatsAccumulator acummulator = new StatsAccumulator();
        acummulator.addAll(values);
        return acummulator.snapshot();
    }
    
    public static Stats of(final DoubleStream values) {
        return values.collect(StatsAccumulator::new, StatsAccumulator::add, StatsAccumulator::addAll).snapshot();
    }
    
    public static Stats of(final IntStream values) {
        return values.collect(StatsAccumulator::new, StatsAccumulator::add, StatsAccumulator::addAll).snapshot();
    }
    
    public static Stats of(final LongStream values) {
        return values.collect(StatsAccumulator::new, StatsAccumulator::add, StatsAccumulator::addAll).snapshot();
    }
    
    public static Collector<Number, StatsAccumulator, Stats> toStats() {
        return Collector.of(StatsAccumulator::new, (a, x) -> a.add(x.doubleValue()), (l, r) -> {
            l.addAll(r);
            return l;
        }, StatsAccumulator::snapshot, new Collector.Characteristics[] { Collector.Characteristics.UNORDERED });
    }
    
    public long count() {
        return this.count;
    }
    
    public double mean() {
        Preconditions.checkState(this.count != 0L);
        return this.mean;
    }
    
    public double sum() {
        return this.mean * this.count;
    }
    
    public double populationVariance() {
        Preconditions.checkState(this.count > 0L);
        if (Double.isNaN(this.sumOfSquaresOfDeltas)) {
            return Double.NaN;
        }
        if (this.count == 1L) {
            return 0.0;
        }
        return DoubleUtils.ensureNonNegative(this.sumOfSquaresOfDeltas) / this.count();
    }
    
    public double populationStandardDeviation() {
        return Math.sqrt(this.populationVariance());
    }
    
    public double sampleVariance() {
        Preconditions.checkState(this.count > 1L);
        if (Double.isNaN(this.sumOfSquaresOfDeltas)) {
            return Double.NaN;
        }
        return DoubleUtils.ensureNonNegative(this.sumOfSquaresOfDeltas) / (this.count - 1L);
    }
    
    public double sampleStandardDeviation() {
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
    
    @Override
    public boolean equals(@CheckForNull final Object obj) {
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final Stats other = (Stats)obj;
        return this.count == other.count && Double.doubleToLongBits(this.mean) == Double.doubleToLongBits(other.mean) && Double.doubleToLongBits(this.sumOfSquaresOfDeltas) == Double.doubleToLongBits(other.sumOfSquaresOfDeltas) && Double.doubleToLongBits(this.min) == Double.doubleToLongBits(other.min) && Double.doubleToLongBits(this.max) == Double.doubleToLongBits(other.max);
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.count, this.mean, this.sumOfSquaresOfDeltas, this.min, this.max);
    }
    
    @Override
    public String toString() {
        if (this.count() > 0L) {
            return MoreObjects.toStringHelper(this).add("count", this.count).add("mean", this.mean).add("populationStandardDeviation", this.populationStandardDeviation()).add("min", this.min).add("max", this.max).toString();
        }
        return MoreObjects.toStringHelper(this).add("count", this.count).toString();
    }
    
    double sumOfSquaresOfDeltas() {
        return this.sumOfSquaresOfDeltas;
    }
    
    public static double meanOf(final Iterable<? extends Number> values) {
        return meanOf(values.iterator());
    }
    
    public static double meanOf(final Iterator<? extends Number> values) {
        Preconditions.checkArgument(values.hasNext());
        long count = 1L;
        double mean = ((Number)values.next()).doubleValue();
        while (values.hasNext()) {
            final double value = ((Number)values.next()).doubleValue();
            ++count;
            if (Doubles.isFinite(value) && Doubles.isFinite(mean)) {
                mean += (value - mean) / count;
            }
            else {
                mean = StatsAccumulator.calculateNewMeanNonFinite(mean, value);
            }
        }
        return mean;
    }
    
    public static double meanOf(final double... values) {
        Preconditions.checkArgument(values.length > 0);
        double mean = values[0];
        for (int index = 1; index < values.length; ++index) {
            final double value = values[index];
            if (Doubles.isFinite(value) && Doubles.isFinite(mean)) {
                mean += (value - mean) / (index + 1);
            }
            else {
                mean = StatsAccumulator.calculateNewMeanNonFinite(mean, value);
            }
        }
        return mean;
    }
    
    public static double meanOf(final int... values) {
        Preconditions.checkArgument(values.length > 0);
        double mean = values[0];
        for (int index = 1; index < values.length; ++index) {
            final double value = values[index];
            if (Doubles.isFinite(value) && Doubles.isFinite(mean)) {
                mean += (value - mean) / (index + 1);
            }
            else {
                mean = StatsAccumulator.calculateNewMeanNonFinite(mean, value);
            }
        }
        return mean;
    }
    
    public static double meanOf(final long... values) {
        Preconditions.checkArgument(values.length > 0);
        double mean = (double)values[0];
        for (int index = 1; index < values.length; ++index) {
            final double value = (double)values[index];
            if (Doubles.isFinite(value) && Doubles.isFinite(mean)) {
                mean += (value - mean) / (index + 1);
            }
            else {
                mean = StatsAccumulator.calculateNewMeanNonFinite(mean, value);
            }
        }
        return mean;
    }
    
    public byte[] toByteArray() {
        final ByteBuffer buff = ByteBuffer.allocate(40).order(ByteOrder.LITTLE_ENDIAN);
        this.writeTo(buff);
        return buff.array();
    }
    
    void writeTo(final ByteBuffer buffer) {
        Preconditions.checkNotNull(buffer);
        Preconditions.checkArgument(buffer.remaining() >= 40, "Expected at least Stats.BYTES = %s remaining , got %s", 40, buffer.remaining());
        buffer.putLong(this.count).putDouble(this.mean).putDouble(this.sumOfSquaresOfDeltas).putDouble(this.min).putDouble(this.max);
    }
    
    public static Stats fromByteArray(final byte[] byteArray) {
        Preconditions.checkNotNull(byteArray);
        Preconditions.checkArgument(byteArray.length == 40, "Expected Stats.BYTES = %s remaining , got %s", 40, byteArray.length);
        return readFrom(ByteBuffer.wrap(byteArray).order(ByteOrder.LITTLE_ENDIAN));
    }
    
    static Stats readFrom(final ByteBuffer buffer) {
        Preconditions.checkNotNull(buffer);
        Preconditions.checkArgument(buffer.remaining() >= 40, "Expected at least Stats.BYTES = %s remaining , got %s", 40, buffer.remaining());
        return new Stats(buffer.getLong(), buffer.getDouble(), buffer.getDouble(), buffer.getDouble(), buffer.getDouble());
    }
}
