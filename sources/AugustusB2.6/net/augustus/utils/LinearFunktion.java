// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.utils;

public class LinearFunktion
{
    double m;
    double b;
    
    public LinearFunktion(final double[] point1, final double[] point2) {
        final double x1 = point1[0];
        final double x2 = point2[0];
        final double y1 = point1[0];
        final double y2 = point2[0];
        this.b = (x2 * y1 + -y2 * x1) / (x2 - x1);
        this.m = (-this.b + y1) / x1;
    }
    
    public LinearFunktion(final float[] point1, final float[] point2) {
        final double x1 = point1[0];
        final double x2 = point2[0];
        final double y1 = point1[0];
        final double y2 = point2[0];
        this.b = (x2 * y1 + -y2 * x1) / (x2 - x1);
        this.m = (-this.b + y1) / x1;
    }
    
    public double getX(final double y) {
        return (-this.b + y) / this.m;
    }
    
    public double getY(final double x) {
        return this.m * x + this.b;
    }
}
