/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.geom;

import com.sun.javafx.geom.Curve;
import java.util.Enumeration;
import java.util.Vector;

public abstract class Crossings {
    public static final boolean debug = false;
    int limit = 0;
    double[] yranges = new double[10];
    double xlo;
    double ylo;
    double xhi;
    double yhi;

    public Crossings(double d, double d2, double d3, double d4) {
        this.xlo = d;
        this.ylo = d2;
        this.xhi = d3;
        this.yhi = d4;
    }

    public final double getXLo() {
        return this.xlo;
    }

    public final double getYLo() {
        return this.ylo;
    }

    public final double getXHi() {
        return this.xhi;
    }

    public final double getYHi() {
        return this.yhi;
    }

    public abstract void record(double var1, double var3, int var5);

    public void print() {
        System.out.println("Crossings [");
        System.out.println("  bounds = [" + this.ylo + ", " + this.yhi + "]");
        for (int i = 0; i < this.limit; i += 2) {
            System.out.println("  [" + this.yranges[i] + ", " + this.yranges[i + 1] + "]");
        }
        System.out.println("]");
    }

    public final boolean isEmpty() {
        return this.limit == 0;
    }

    public abstract boolean covers(double var1, double var3);

    public static Crossings findCrossings(Vector vector, double d, double d2, double d3, double d4) {
        EvenOdd evenOdd = new EvenOdd(d, d2, d3, d4);
        Enumeration enumeration = vector.elements();
        while (enumeration.hasMoreElements()) {
            Curve curve = (Curve)enumeration.nextElement();
            if (!curve.accumulateCrossings(evenOdd)) continue;
            return null;
        }
        return evenOdd;
    }

    public static final class EvenOdd
    extends Crossings {
        public EvenOdd(double d, double d2, double d3, double d4) {
            super(d, d2, d3, d4);
        }

        @Override
        public final boolean covers(double d, double d2) {
            return this.limit == 2 && this.yranges[0] <= d && this.yranges[1] >= d2;
        }

        @Override
        public void record(double d, double d2, int n) {
            int n2;
            if (d >= d2) {
                return;
            }
            for (n2 = 0; n2 < this.limit && d > this.yranges[n2 + 1]; n2 += 2) {
            }
            int n3 = n2;
            while (n2 < this.limit) {
                double d3;
                double d4;
                double d5;
                double d6;
                double d7 = this.yranges[n2++];
                double d8 = this.yranges[n2++];
                if (d2 < d7) {
                    this.yranges[n3++] = d;
                    this.yranges[n3++] = d2;
                    d = d7;
                    d2 = d8;
                    continue;
                }
                if (d < d7) {
                    d6 = d;
                    d5 = d7;
                } else {
                    d6 = d7;
                    d5 = d;
                }
                if (d2 < d8) {
                    d4 = d2;
                    d3 = d8;
                } else {
                    d4 = d8;
                    d3 = d2;
                }
                if (d5 == d4) {
                    d = d6;
                    d2 = d3;
                } else {
                    if (d5 > d4) {
                        d = d4;
                        d4 = d5;
                        d5 = d;
                    }
                    if (d6 != d5) {
                        this.yranges[n3++] = d6;
                        this.yranges[n3++] = d5;
                    }
                    d = d4;
                    d2 = d3;
                }
                if (!(d >= d2)) continue;
                break;
            }
            if (n3 < n2 && n2 < this.limit) {
                System.arraycopy(this.yranges, n2, this.yranges, n3, this.limit - n2);
            }
            n3 += this.limit - n2;
            if (d < d2) {
                if (n3 >= this.yranges.length) {
                    double[] arrd = new double[n3 + 10];
                    System.arraycopy(this.yranges, 0, arrd, 0, n3);
                    this.yranges = arrd;
                }
                this.yranges[n3++] = d;
                this.yranges[n3++] = d2;
            }
            this.limit = n3;
        }
    }
}

