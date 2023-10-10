// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.math;

import java.nio.ByteOrder;
import java.nio.ByteBuffer;
import me.gong.mcleaks.util.google.common.base.MoreObjects;
import me.gong.mcleaks.util.google.common.base.Objects;
import javax.annotation.Nullable;
import me.gong.mcleaks.util.google.common.base.Preconditions;
import me.gong.mcleaks.util.google.common.annotations.GwtIncompatible;
import me.gong.mcleaks.util.google.common.annotations.Beta;
import java.io.Serializable;

@Beta
@GwtIncompatible
public final class PairedStats implements Serializable
{
    private final Stats xStats;
    private final Stats yStats;
    private final double sumOfProductsOfDeltas;
    private static final int BYTES = 88;
    private static final long serialVersionUID = 0L;
    
    PairedStats(final Stats xStats, final Stats yStats, final double sumOfProductsOfDeltas) {
        this.xStats = xStats;
        this.yStats = yStats;
        this.sumOfProductsOfDeltas = sumOfProductsOfDeltas;
    }
    
    public long count() {
        return this.xStats.count();
    }
    
    public Stats xStats() {
        return this.xStats;
    }
    
    public Stats yStats() {
        return this.yStats;
    }
    
    public double populationCovariance() {
        Preconditions.checkState(this.count() != 0L);
        return this.sumOfProductsOfDeltas / this.count();
    }
    
    public double sampleCovariance() {
        Preconditions.checkState(this.count() > 1L);
        return this.sumOfProductsOfDeltas / (this.count() - 1L);
    }
    
    public double pearsonsCorrelationCoefficient() {
        Preconditions.checkState(this.count() > 1L);
        if (Double.isNaN(this.sumOfProductsOfDeltas)) {
            return Double.NaN;
        }
        final double xSumOfSquaresOfDeltas = this.xStats().sumOfSquaresOfDeltas();
        final double ySumOfSquaresOfDeltas = this.yStats().sumOfSquaresOfDeltas();
        Preconditions.checkState(xSumOfSquaresOfDeltas > 0.0);
        Preconditions.checkState(ySumOfSquaresOfDeltas > 0.0);
        final double productOfSumsOfSquaresOfDeltas = ensurePositive(xSumOfSquaresOfDeltas * ySumOfSquaresOfDeltas);
        return ensureInUnitRange(this.sumOfProductsOfDeltas / Math.sqrt(productOfSumsOfSquaresOfDeltas));
    }
    
    public LinearTransformation leastSquaresFit() {
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
    
    @Override
    public boolean equals(@Nullable final Object obj) {
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final PairedStats other = (PairedStats)obj;
        return this.xStats.equals(other.xStats) && this.yStats.equals(other.yStats) && Double.doubleToLongBits(this.sumOfProductsOfDeltas) == Double.doubleToLongBits(other.sumOfProductsOfDeltas);
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.xStats, this.yStats, this.sumOfProductsOfDeltas);
    }
    
    @Override
    public String toString() {
        if (this.count() > 0L) {
            return MoreObjects.toStringHelper(this).add("xStats", this.xStats).add("yStats", this.yStats).add("populationCovariance", this.populationCovariance()).toString();
        }
        return MoreObjects.toStringHelper(this).add("xStats", this.xStats).add("yStats", this.yStats).toString();
    }
    
    double sumOfProductsOfDeltas() {
        return this.sumOfProductsOfDeltas;
    }
    
    private static double ensurePositive(final double value) {
        if (value > 0.0) {
            return value;
        }
        return Double.MIN_VALUE;
    }
    
    private static double ensureInUnitRange(final double value) {
        if (value >= 1.0) {
            return 1.0;
        }
        if (value <= -1.0) {
            return -1.0;
        }
        return value;
    }
    
    public byte[] toByteArray() {
        final ByteBuffer buffer = ByteBuffer.allocate(88).order(ByteOrder.LITTLE_ENDIAN);
        this.xStats.writeTo(buffer);
        this.yStats.writeTo(buffer);
        buffer.putDouble(this.sumOfProductsOfDeltas);
        return buffer.array();
    }
    
    public static PairedStats fromByteArray(final byte[] byteArray) {
        Preconditions.checkNotNull(byteArray);
        Preconditions.checkArgument(byteArray.length == 88, "Expected PairedStats.BYTES = %s, got %s", 88, byteArray.length);
        final ByteBuffer buffer = ByteBuffer.wrap(byteArray).order(ByteOrder.LITTLE_ENDIAN);
        final Stats xStats = Stats.readFrom(buffer);
        final Stats yStats = Stats.readFrom(buffer);
        final double sumOfProductsOfDeltas = buffer.getDouble();
        return new PairedStats(xStats, yStats, sumOfProductsOfDeltas);
    }
}
