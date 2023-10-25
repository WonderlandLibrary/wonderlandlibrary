/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.geom;

import com.sun.javafx.geom.Crossings;
import com.sun.javafx.geom.Order0;
import com.sun.javafx.geom.Order1;
import com.sun.javafx.geom.Order2;
import com.sun.javafx.geom.Order3;
import com.sun.javafx.geom.RectBounds;
import java.util.Vector;

public abstract class Curve {
    public static final int INCREASING = 1;
    public static final int DECREASING = -1;
    protected int direction;
    public static final double TMIN = 0.001;

    public static void insertMove(Vector vector, double d, double d2) {
        vector.add(new Order0(d, d2));
    }

    public static void insertLine(Vector vector, double d, double d2, double d3, double d4) {
        if (d2 < d4) {
            vector.add(new Order1(d, d2, d3, d4, 1));
        } else if (d2 > d4) {
            vector.add(new Order1(d3, d4, d, d2, -1));
        }
    }

    public static void insertQuad(Vector vector, double[] arrd, double d, double d2, double d3, double d4, double d5, double d6) {
        if (d2 > d6) {
            Order2.insert(vector, arrd, d5, d6, d3, d4, d, d2, -1);
        } else {
            if (d2 == d6 && d2 == d4) {
                return;
            }
            Order2.insert(vector, arrd, d, d2, d3, d4, d5, d6, 1);
        }
    }

    public static void insertCubic(Vector vector, double[] arrd, double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8) {
        if (d2 > d8) {
            Order3.insert(vector, arrd, d7, d8, d5, d6, d3, d4, d, d2, -1);
        } else {
            if (d2 == d8 && d2 == d4 && d2 == d6) {
                return;
            }
            Order3.insert(vector, arrd, d, d2, d3, d4, d5, d6, d7, d8, 1);
        }
    }

    public Curve(int n) {
        this.direction = n;
    }

    public final int getDirection() {
        return this.direction;
    }

    public final Curve getWithDirection(int n) {
        return this.direction == n ? this : this.getReversedCurve();
    }

    public static double round(double d) {
        return d;
    }

    public static int orderof(double d, double d2) {
        if (d < d2) {
            return -1;
        }
        if (d > d2) {
            return 1;
        }
        return 0;
    }

    public static long signeddiffbits(double d, double d2) {
        return Double.doubleToLongBits(d) - Double.doubleToLongBits(d2);
    }

    public static long diffbits(double d, double d2) {
        return Math.abs(Double.doubleToLongBits(d) - Double.doubleToLongBits(d2));
    }

    public static double prev(double d) {
        return Double.longBitsToDouble(Double.doubleToLongBits(d) - 1L);
    }

    public static double next(double d) {
        return Double.longBitsToDouble(Double.doubleToLongBits(d) + 1L);
    }

    public String toString() {
        return "Curve[" + this.getOrder() + ", (" + Curve.round(this.getX0()) + ", " + Curve.round(this.getY0()) + "), " + this.controlPointString() + "(" + Curve.round(this.getX1()) + ", " + Curve.round(this.getY1()) + "), " + (this.direction == 1 ? "D" : "U") + "]";
    }

    public String controlPointString() {
        return "";
    }

    public abstract int getOrder();

    public abstract double getXTop();

    public abstract double getYTop();

    public abstract double getXBot();

    public abstract double getYBot();

    public abstract double getXMin();

    public abstract double getXMax();

    public abstract double getX0();

    public abstract double getY0();

    public abstract double getX1();

    public abstract double getY1();

    public abstract double XforY(double var1);

    public abstract double TforY(double var1);

    public abstract double XforT(double var1);

    public abstract double YforT(double var1);

    public abstract double dXforT(double var1, int var3);

    public abstract double dYforT(double var1, int var3);

    public abstract double nextVertical(double var1, double var3);

    public int crossingsFor(double d, double d2) {
        if (d2 >= this.getYTop() && d2 < this.getYBot() && d < this.getXMax() && (d < this.getXMin() || d < this.XforY(d2))) {
            return 1;
        }
        return 0;
    }

    public boolean accumulateCrossings(Crossings crossings) {
        double d;
        double d2;
        double d3;
        double d4;
        double d5 = crossings.getXHi();
        if (this.getXMin() >= d5) {
            return false;
        }
        double d6 = crossings.getXLo();
        double d7 = crossings.getYLo();
        double d8 = crossings.getYHi();
        double d9 = this.getYTop();
        double d10 = this.getYBot();
        if (d9 < d7) {
            if (d10 <= d7) {
                return false;
            }
            d4 = d7;
            d3 = this.TforY(d7);
        } else {
            if (d9 >= d8) {
                return false;
            }
            d4 = d9;
            d3 = 0.0;
        }
        if (d10 > d8) {
            d2 = d8;
            d = this.TforY(d8);
        } else {
            d2 = d10;
            d = 1.0;
        }
        boolean bl = false;
        boolean bl2 = false;
        while (true) {
            double d11;
            if ((d11 = this.XforT(d3)) < d5) {
                if (bl2 || d11 > d6) {
                    return true;
                }
                bl = true;
            } else {
                if (bl) {
                    return true;
                }
                bl2 = true;
            }
            if (d3 >= d) break;
            d3 = this.nextVertical(d3, d);
        }
        if (bl) {
            crossings.record(d4, d2, this.direction);
        }
        return false;
    }

    public abstract void enlarge(RectBounds var1);

    public Curve getSubCurve(double d, double d2) {
        return this.getSubCurve(d, d2, this.direction);
    }

    public abstract Curve getReversedCurve();

    public abstract Curve getSubCurve(double var1, double var3, int var5);

    public int compareTo(Curve curve, double[] arrd) {
        double d;
        double d2;
        double d3;
        double d4;
        double d5;
        double d6;
        double d7;
        double d8;
        double d9 = arrd[0];
        double d10 = arrd[1];
        if ((d10 = Math.min(Math.min(d10, this.getYBot()), curve.getYBot())) <= arrd[0]) {
            System.err.println("this == " + this);
            System.err.println("that == " + curve);
            System.out.println("target range = " + arrd[0] + "=>" + arrd[1]);
            throw new InternalError("backstepping from " + arrd[0] + " to " + d10);
        }
        arrd[1] = d10;
        if (this.getXMax() <= curve.getXMin()) {
            if (this.getXMin() == curve.getXMax()) {
                return 0;
            }
            return -1;
        }
        if (this.getXMin() >= curve.getXMax()) {
            return 1;
        }
        double d11 = this.TforY(d9);
        double d12 = this.YforT(d11);
        if (d12 < d9) {
            d11 = this.refineTforY(d11, d9);
            d12 = this.YforT(d11);
        }
        if (this.YforT(d8 = this.TforY(d10)) < d9) {
            d8 = this.refineTforY(d8, d9);
        }
        if ((d7 = curve.YforT(d6 = curve.TforY(d9))) < d9) {
            d6 = curve.refineTforY(d6, d9);
            d7 = curve.YforT(d6);
        }
        if (curve.YforT(d5 = curve.TforY(d10)) < d9) {
            d5 = curve.refineTforY(d5, d9);
        }
        double d13 = this.XforT(d11);
        double d14 = curve.XforT(d6);
        double d15 = Math.max(Math.abs(d9), Math.abs(d10));
        double d16 = Math.max(d15 * 1.0E-14, 1.0E-300);
        if (this.fairlyClose(d13, d14)) {
            d4 = d16;
            d3 = Math.min(d16 * 1.0E13, (d10 - d9) * 0.1);
            for (d2 = d9 + d4; d2 <= d10; d2 += d4) {
                if (this.fairlyClose(this.XforY(d2), curve.XforY(d2))) {
                    double d17;
                    d4 *= 2.0;
                    if (!(d17 > d3)) continue;
                    d4 = d3;
                    continue;
                }
                d2 -= d4;
                while (!((d = d2 + (d4 /= 2.0)) <= d2)) {
                    if (!this.fairlyClose(this.XforY(d), curve.XforY(d))) continue;
                    d2 = d;
                }
                break;
            }
            if (d2 > d9) {
                if (d2 < d10) {
                    arrd[1] = d2;
                }
                return 0;
            }
        }
        if (d16 <= 0.0) {
            System.out.println("ymin = " + d16);
        }
        while (d11 < d8 && d6 < d5) {
            d4 = this.nextVertical(d11, d8);
            d3 = this.XforT(d4);
            d2 = this.YforT(d4);
            d = curve.nextVertical(d6, d5);
            double d18 = curve.XforT(d);
            double d19 = curve.YforT(d);
            try {
                if (this.findIntersect(curve, arrd, d16, 0, 0, d11, d13, d12, d4, d3, d2, d6, d14, d7, d, d18, d19)) {
                    break;
                }
            }
            catch (Throwable throwable) {
                System.err.println("Error: " + throwable);
                System.err.println("y range was " + arrd[0] + "=>" + arrd[1]);
                System.err.println("s y range is " + d12 + "=>" + d2);
                System.err.println("t y range is " + d7 + "=>" + d19);
                System.err.println("ymin is " + d16);
                return 0;
            }
            if (d2 < d19) {
                if (d2 > arrd[0]) {
                    if (!(d2 < arrd[1])) break;
                    arrd[1] = d2;
                    break;
                }
                d11 = d4;
                d13 = d3;
                d12 = d2;
                continue;
            }
            if (d19 > arrd[0]) {
                if (!(d19 < arrd[1])) break;
                arrd[1] = d19;
                break;
            }
            d6 = d;
            d14 = d18;
            d7 = d19;
        }
        d4 = (arrd[0] + arrd[1]) / 2.0;
        return Curve.orderof(this.XforY(d4), curve.XforY(d4));
    }

    public boolean findIntersect(Curve curve, double[] arrd, double d, int n, int n2, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, double d10, double d11, double d12, double d13) {
        if (d4 > d13 || d10 > d7) {
            return false;
        }
        if (Math.min(d3, d6) > Math.max(d9, d12) || Math.max(d3, d6) < Math.min(d9, d12)) {
            return false;
        }
        if (d5 - d2 > 0.001) {
            double d14 = (d2 + d5) / 2.0;
            double d15 = this.XforT(d14);
            double d16 = this.YforT(d14);
            if (d14 == d2 || d14 == d5) {
                System.out.println("s0 = " + d2);
                System.out.println("s1 = " + d5);
                throw new InternalError("no s progress!");
            }
            if (d11 - d8 > 0.001) {
                double d17 = (d8 + d11) / 2.0;
                double d18 = curve.XforT(d17);
                double d19 = curve.YforT(d17);
                if (d17 == d8 || d17 == d11) {
                    System.out.println("t0 = " + d8);
                    System.out.println("t1 = " + d11);
                    throw new InternalError("no t progress!");
                }
                if (d16 >= d10 && d19 >= d4 && this.findIntersect(curve, arrd, d, n + 1, n2 + 1, d2, d3, d4, d14, d15, d16, d8, d9, d10, d17, d18, d19)) {
                    return true;
                }
                if (d16 >= d19 && this.findIntersect(curve, arrd, d, n + 1, n2 + 1, d2, d3, d4, d14, d15, d16, d17, d18, d19, d11, d12, d13)) {
                    return true;
                }
                if (d19 >= d16 && this.findIntersect(curve, arrd, d, n + 1, n2 + 1, d14, d15, d16, d5, d6, d7, d8, d9, d10, d17, d18, d19)) {
                    return true;
                }
                if (d7 >= d19 && d13 >= d16 && this.findIntersect(curve, arrd, d, n + 1, n2 + 1, d14, d15, d16, d5, d6, d7, d17, d18, d19, d11, d12, d13)) {
                    return true;
                }
            } else {
                if (d16 >= d10 && this.findIntersect(curve, arrd, d, n + 1, n2, d2, d3, d4, d14, d15, d16, d8, d9, d10, d11, d12, d13)) {
                    return true;
                }
                if (d13 >= d16 && this.findIntersect(curve, arrd, d, n + 1, n2, d14, d15, d16, d5, d6, d7, d8, d9, d10, d11, d12, d13)) {
                    return true;
                }
            }
        } else if (d11 - d8 > 0.001) {
            double d20 = (d8 + d11) / 2.0;
            double d21 = curve.XforT(d20);
            double d22 = curve.YforT(d20);
            if (d20 == d8 || d20 == d11) {
                System.out.println("t0 = " + d8);
                System.out.println("t1 = " + d11);
                throw new InternalError("no t progress!");
            }
            if (d22 >= d4 && this.findIntersect(curve, arrd, d, n, n2 + 1, d2, d3, d4, d5, d6, d7, d8, d9, d10, d20, d21, d22)) {
                return true;
            }
            if (d7 >= d22 && this.findIntersect(curve, arrd, d, n, n2 + 1, d2, d3, d4, d5, d6, d7, d20, d21, d22, d11, d12, d13)) {
                return true;
            }
        } else {
            double d23 = d6 - d3;
            double d24 = d7 - d4;
            double d25 = d12 - d9;
            double d26 = d13 - d10;
            double d27 = d9 - d3;
            double d28 = d10 - d4;
            double d29 = d25 * d24 - d26 * d23;
            if (d29 != 0.0) {
                double d30 = 1.0 / d29;
                double d31 = (d25 * d28 - d26 * d27) * d30;
                double d32 = (d23 * d28 - d24 * d27) * d30;
                if (d31 >= 0.0 && d31 <= 1.0 && d32 >= 0.0 && d32 <= 1.0) {
                    double d33;
                    d31 = d2 + d31 * (d5 - d2);
                    d32 = d8 + d32 * (d11 - d8);
                    if (d31 < 0.0 || d31 > 1.0 || d32 < 0.0 || d32 > 1.0) {
                        System.out.println("Uh oh!");
                    }
                    if ((d33 = (this.YforT(d31) + curve.YforT(d32)) / 2.0) <= arrd[1] && d33 > arrd[0]) {
                        arrd[1] = d33;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public double refineTforY(double d, double d2) {
        double d3 = 1.0;
        while (true) {
            double d4;
            if ((d4 = (d + d3) / 2.0) == d || d4 == d3) {
                return d3;
            }
            double d5 = this.YforT(d4);
            if (d5 < d2) {
                d = d4;
                continue;
            }
            if (!(d5 > d2)) break;
            d3 = d4;
        }
        return d3;
    }

    public boolean fairlyClose(double d, double d2) {
        return Math.abs(d - d2) < Math.max(Math.abs(d), Math.abs(d2)) * 1.0E-10;
    }

    public abstract int getSegment(float[] var1);
}

