// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.math;

import me.gong.mcleaks.util.google.errorprone.annotations.concurrent.LazyInit;
import me.gong.mcleaks.util.google.common.base.Preconditions;
import me.gong.mcleaks.util.google.common.annotations.GwtIncompatible;
import me.gong.mcleaks.util.google.common.annotations.Beta;

@Beta
@GwtIncompatible
public abstract class LinearTransformation
{
    public static LinearTransformationBuilder mapping(final double x1, final double y1) {
        Preconditions.checkArgument(DoubleUtils.isFinite(x1) && DoubleUtils.isFinite(y1));
        return new LinearTransformationBuilder(x1, y1);
    }
    
    public static LinearTransformation vertical(final double x) {
        Preconditions.checkArgument(DoubleUtils.isFinite(x));
        return new VerticalLinearTransformation(x);
    }
    
    public static LinearTransformation horizontal(final double y) {
        Preconditions.checkArgument(DoubleUtils.isFinite(y));
        final double slope = 0.0;
        return new RegularLinearTransformation(slope, y);
    }
    
    public static LinearTransformation forNaN() {
        return NaNLinearTransformation.INSTANCE;
    }
    
    public abstract boolean isVertical();
    
    public abstract boolean isHorizontal();
    
    public abstract double slope();
    
    public abstract double transform(final double p0);
    
    public abstract LinearTransformation inverse();
    
    public static final class LinearTransformationBuilder
    {
        private final double x1;
        private final double y1;
        
        private LinearTransformationBuilder(final double x1, final double y1) {
            this.x1 = x1;
            this.y1 = y1;
        }
        
        public LinearTransformation and(final double x2, final double y2) {
            Preconditions.checkArgument(DoubleUtils.isFinite(x2) && DoubleUtils.isFinite(y2));
            if (x2 == this.x1) {
                Preconditions.checkArgument(y2 != this.y1);
                return new VerticalLinearTransformation(this.x1);
            }
            return this.withSlope((y2 - this.y1) / (x2 - this.x1));
        }
        
        public LinearTransformation withSlope(final double slope) {
            Preconditions.checkArgument(!Double.isNaN(slope));
            if (DoubleUtils.isFinite(slope)) {
                final double yIntercept = this.y1 - this.x1 * slope;
                return new RegularLinearTransformation(slope, yIntercept);
            }
            return new VerticalLinearTransformation(this.x1);
        }
    }
    
    private static final class RegularLinearTransformation extends LinearTransformation
    {
        final double slope;
        final double yIntercept;
        @LazyInit
        LinearTransformation inverse;
        
        RegularLinearTransformation(final double slope, final double yIntercept) {
            this.slope = slope;
            this.yIntercept = yIntercept;
            this.inverse = null;
        }
        
        RegularLinearTransformation(final double slope, final double yIntercept, final LinearTransformation inverse) {
            this.slope = slope;
            this.yIntercept = yIntercept;
            this.inverse = inverse;
        }
        
        @Override
        public boolean isVertical() {
            return false;
        }
        
        @Override
        public boolean isHorizontal() {
            return this.slope == 0.0;
        }
        
        @Override
        public double slope() {
            return this.slope;
        }
        
        @Override
        public double transform(final double x) {
            return x * this.slope + this.yIntercept;
        }
        
        @Override
        public LinearTransformation inverse() {
            final LinearTransformation result = this.inverse;
            return (result == null) ? (this.inverse = this.createInverse()) : result;
        }
        
        @Override
        public String toString() {
            return String.format("y = %g * x + %g", this.slope, this.yIntercept);
        }
        
        private LinearTransformation createInverse() {
            if (this.slope != 0.0) {
                return new RegularLinearTransformation(1.0 / this.slope, -1.0 * this.yIntercept / this.slope, this);
            }
            return new VerticalLinearTransformation(this.yIntercept, this);
        }
    }
    
    private static final class VerticalLinearTransformation extends LinearTransformation
    {
        final double x;
        @LazyInit
        LinearTransformation inverse;
        
        VerticalLinearTransformation(final double x) {
            this.x = x;
            this.inverse = null;
        }
        
        VerticalLinearTransformation(final double x, final LinearTransformation inverse) {
            this.x = x;
            this.inverse = inverse;
        }
        
        @Override
        public boolean isVertical() {
            return true;
        }
        
        @Override
        public boolean isHorizontal() {
            return false;
        }
        
        @Override
        public double slope() {
            throw new IllegalStateException();
        }
        
        @Override
        public double transform(final double x) {
            throw new IllegalStateException();
        }
        
        @Override
        public LinearTransformation inverse() {
            final LinearTransformation result = this.inverse;
            return (result == null) ? (this.inverse = this.createInverse()) : result;
        }
        
        @Override
        public String toString() {
            return String.format("x = %g", this.x);
        }
        
        private LinearTransformation createInverse() {
            return new RegularLinearTransformation(0.0, this.x, this);
        }
    }
    
    private static final class NaNLinearTransformation extends LinearTransformation
    {
        static final NaNLinearTransformation INSTANCE;
        
        @Override
        public boolean isVertical() {
            return false;
        }
        
        @Override
        public boolean isHorizontal() {
            return false;
        }
        
        @Override
        public double slope() {
            return Double.NaN;
        }
        
        @Override
        public double transform(final double x) {
            return Double.NaN;
        }
        
        @Override
        public LinearTransformation inverse() {
            return this;
        }
        
        @Override
        public String toString() {
            return "NaN";
        }
        
        static {
            INSTANCE = new NaNLinearTransformation();
        }
    }
}
