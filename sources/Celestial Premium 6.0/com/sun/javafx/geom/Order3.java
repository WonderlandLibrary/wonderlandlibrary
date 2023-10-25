/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.geom;

import com.sun.javafx.geom.Curve;
import com.sun.javafx.geom.Order2;
import com.sun.javafx.geom.RectBounds;
import java.util.Vector;

final class Order3
extends Curve {
    private double x0;
    private double y0;
    private double cx0;
    private double cy0;
    private double cx1;
    private double cy1;
    private double x1;
    private double y1;
    private double xmin;
    private double xmax;
    private double xcoeff0;
    private double xcoeff1;
    private double xcoeff2;
    private double xcoeff3;
    private double ycoeff0;
    private double ycoeff1;
    private double ycoeff2;
    private double ycoeff3;
    private double TforY1;
    private double YforT1;
    private double TforY2;
    private double YforT2;
    private double TforY3;
    private double YforT3;

    public static void insert(Vector vector, double[] arrd, double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8, int n) {
        int n2 = Order3.getHorizontalParams(d2, d4, d6, d8, arrd);
        if (n2 == 0) {
            Order3.addInstance(vector, d, d2, d3, d4, d5, d6, d7, d8, n);
            return;
        }
        arrd[3] = d;
        arrd[4] = d2;
        arrd[5] = d3;
        arrd[6] = d4;
        arrd[7] = d5;
        arrd[8] = d6;
        arrd[9] = d7;
        arrd[10] = d8;
        double d9 = arrd[0];
        if (n2 > 1 && d9 > arrd[1]) {
            arrd[0] = arrd[1];
            arrd[1] = d9;
            d9 = arrd[0];
        }
        Order3.split(arrd, 3, d9);
        if (n2 > 1) {
            d9 = (arrd[1] - d9) / (1.0 - d9);
            Order3.split(arrd, 9, d9);
        }
        int n3 = 3;
        if (n == -1) {
            n3 += n2 * 6;
        }
        while (n2 >= 0) {
            Order3.addInstance(vector, arrd[n3 + 0], arrd[n3 + 1], arrd[n3 + 2], arrd[n3 + 3], arrd[n3 + 4], arrd[n3 + 5], arrd[n3 + 6], arrd[n3 + 7], n);
            --n2;
            if (n == 1) {
                n3 += 6;
                continue;
            }
            n3 -= 6;
        }
    }

    public static void addInstance(Vector vector, double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8, int n) {
        if (d2 > d8) {
            vector.add(new Order3(d7, d8, d5, d6, d3, d4, d, d2, -n));
        } else if (d8 > d2) {
            vector.add(new Order3(d, d2, d3, d4, d5, d6, d7, d8, n));
        }
    }

    public static int solveQuadratic(double[] arrd, double[] arrd2) {
        double d = arrd[2];
        double d2 = arrd[1];
        double d3 = arrd[0];
        int n = 0;
        if (d == 0.0) {
            if (d2 == 0.0) {
                return -1;
            }
            arrd2[n++] = -d3 / d2;
        } else {
            double d4 = d2 * d2 - 4.0 * d * d3;
            if (d4 < 0.0) {
                return 0;
            }
            d4 = Math.sqrt(d4);
            if (d2 < 0.0) {
                d4 = -d4;
            }
            double d5 = (d2 + d4) / -2.0;
            arrd2[n++] = d5 / d;
            if (d5 != 0.0) {
                arrd2[n++] = d3 / d5;
            }
        }
        return n;
    }

    public static int getHorizontalParams(double d, double d2, double d3, double d4, double[] arrd) {
        if (d <= d2 && d2 <= d3 && d3 <= d4) {
            return 0;
        }
        d4 -= d3;
        d3 -= d2;
        arrd[0] = d2 -= d;
        arrd[1] = (d3 - d2) * 2.0;
        arrd[2] = d4 - d3 - d3 + d2;
        int n = Order3.solveQuadratic(arrd, arrd);
        int n2 = 0;
        for (int i = 0; i < n; ++i) {
            double d5 = arrd[i];
            if (!(d5 > 0.0) || !(d5 < 1.0)) continue;
            if (n2 < i) {
                arrd[n2] = d5;
            }
            ++n2;
        }
        return n2;
    }

    public static void split(double[] arrd, int n, double d) {
        double d2;
        double d3;
        arrd[n + 12] = d3 = arrd[n + 6];
        arrd[n + 13] = d2 = arrd[n + 7];
        double d4 = arrd[n + 4];
        double d5 = arrd[n + 5];
        d3 = d4 + (d3 - d4) * d;
        d2 = d5 + (d2 - d5) * d;
        double d6 = arrd[n + 0];
        double d7 = arrd[n + 1];
        double d8 = arrd[n + 2];
        double d9 = arrd[n + 3];
        d6 += (d8 - d6) * d;
        d7 += (d9 - d7) * d;
        d8 += (d4 - d8) * d;
        d9 += (d5 - d9) * d;
        d4 = d8 + (d3 - d8) * d;
        d5 = d9 + (d2 - d9) * d;
        d8 = d6 + (d8 - d6) * d;
        d9 = d7 + (d9 - d7) * d;
        arrd[n + 2] = d6;
        arrd[n + 3] = d7;
        arrd[n + 4] = d8;
        arrd[n + 5] = d9;
        arrd[n + 6] = d8 + (d4 - d8) * d;
        arrd[n + 7] = d9 + (d5 - d9) * d;
        arrd[n + 8] = d4;
        arrd[n + 9] = d5;
        arrd[n + 10] = d3;
        arrd[n + 11] = d2;
    }

    public Order3(double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8, int n) {
        super(n);
        if (d4 < d2) {
            d4 = d2;
        }
        if (d6 > d8) {
            d6 = d8;
        }
        this.x0 = d;
        this.y0 = d2;
        this.cx0 = d3;
        this.cy0 = d4;
        this.cx1 = d5;
        this.cy1 = d6;
        this.x1 = d7;
        this.y1 = d8;
        this.xmin = Math.min(Math.min(d, d7), Math.min(d3, d5));
        this.xmax = Math.max(Math.max(d, d7), Math.max(d3, d5));
        this.xcoeff0 = d;
        this.xcoeff1 = (d3 - d) * 3.0;
        this.xcoeff2 = (d5 - d3 - d3 + d) * 3.0;
        this.xcoeff3 = d7 - (d5 - d3) * 3.0 - d;
        this.ycoeff0 = d2;
        this.ycoeff1 = (d4 - d2) * 3.0;
        this.ycoeff2 = (d6 - d4 - d4 + d2) * 3.0;
        this.ycoeff3 = d8 - (d6 - d4) * 3.0 - d2;
        this.YforT2 = this.YforT3 = d2;
        this.YforT1 = this.YforT3;
    }

    @Override
    public int getOrder() {
        return 3;
    }

    @Override
    public double getXTop() {
        return this.x0;
    }

    @Override
    public double getYTop() {
        return this.y0;
    }

    @Override
    public double getXBot() {
        return this.x1;
    }

    @Override
    public double getYBot() {
        return this.y1;
    }

    @Override
    public double getXMin() {
        return this.xmin;
    }

    @Override
    public double getXMax() {
        return this.xmax;
    }

    @Override
    public double getX0() {
        return this.direction == 1 ? this.x0 : this.x1;
    }

    @Override
    public double getY0() {
        return this.direction == 1 ? this.y0 : this.y1;
    }

    public double getCX0() {
        return this.direction == 1 ? this.cx0 : this.cx1;
    }

    public double getCY0() {
        return this.direction == 1 ? this.cy0 : this.cy1;
    }

    public double getCX1() {
        return this.direction == -1 ? this.cx0 : this.cx1;
    }

    public double getCY1() {
        return this.direction == -1 ? this.cy0 : this.cy1;
    }

    @Override
    public double getX1() {
        return this.direction == -1 ? this.x0 : this.x1;
    }

    @Override
    public double getY1() {
        return this.direction == -1 ? this.y0 : this.y1;
    }

    @Override
    public double TforY(double d) {
        double d2;
        double d3;
        if (d <= this.y0) {
            return 0.0;
        }
        if (d >= this.y1) {
            return 1.0;
        }
        if (d == this.YforT1) {
            return this.TforY1;
        }
        if (d == this.YforT2) {
            return this.TforY2;
        }
        if (d == this.YforT3) {
            return this.TforY3;
        }
        if (this.ycoeff3 == 0.0) {
            return Order2.TforY(d, this.ycoeff0, this.ycoeff1, this.ycoeff2);
        }
        double d4 = this.ycoeff2 / this.ycoeff3;
        double d5 = this.ycoeff1 / this.ycoeff3;
        double d6 = (this.ycoeff0 - d) / this.ycoeff3;
        boolean bl = false;
        double d7 = (d4 * d4 - 3.0 * d5) / 9.0;
        double d8 = (2.0 * d4 * d4 * d4 - 9.0 * d4 * d5 + 27.0 * d6) / 54.0;
        double d9 = d8 * d8;
        double d10 = d7 * d7 * d7;
        double d11 = d4 / 3.0;
        if (d9 < d10) {
            d3 = Math.acos(d8 / Math.sqrt(d10));
            d2 = this.refine(d4, d5, d6, d, (d7 = -2.0 * Math.sqrt(d7)) * Math.cos(d3 / 3.0) - d11);
            if (d2 < 0.0) {
                d2 = this.refine(d4, d5, d6, d, d7 * Math.cos((d3 + Math.PI * 2) / 3.0) - d11);
            }
            if (d2 < 0.0) {
                d2 = this.refine(d4, d5, d6, d, d7 * Math.cos((d3 - Math.PI * 2) / 3.0) - d11);
            }
        } else {
            boolean bl2 = d8 < 0.0;
            double d12 = Math.sqrt(d9 - d10);
            if (bl2) {
                d8 = -d8;
            }
            double d13 = Math.pow(d8 + d12, 0.3333333333333333);
            if (!bl2) {
                d13 = -d13;
            }
            double d14 = d13 == 0.0 ? 0.0 : d7 / d13;
            d2 = this.refine(d4, d5, d6, d, d13 + d14 - d11);
        }
        if (d2 < 0.0) {
            d3 = 0.0;
            double d15 = 1.0;
            while ((d2 = (d3 + d15) / 2.0) != d3 && d2 != d15) {
                double d16 = this.YforT(d2);
                if (d16 < d) {
                    d3 = d2;
                    continue;
                }
                if (!(d16 > d)) break;
                d15 = d2;
            }
        }
        if (d2 >= 0.0) {
            this.TforY3 = this.TforY2;
            this.YforT3 = this.YforT2;
            this.TforY2 = this.TforY1;
            this.YforT2 = this.YforT1;
            this.TforY1 = d2;
            this.YforT1 = d;
        }
        return d2;
    }

    public double refine(double d, double d2, double d3, double d4, double d5) {
        double d6;
        double d7;
        if (d5 < -0.1 || d5 > 1.1) {
            return -1.0;
        }
        double d8 = this.YforT(d5);
        if (d8 < d4) {
            d7 = d5;
            d6 = 1.0;
        } else {
            d7 = 0.0;
            d6 = d5;
        }
        double d9 = d5;
        double d10 = d8;
        boolean bl = true;
        while (d8 != d4) {
            double d11;
            if (!bl) {
                d11 = (d7 + d6) / 2.0;
                if (d11 == d7 || d11 == d6) break;
                d5 = d11;
            } else {
                d11 = this.dYforT(d5, 1);
                if (d11 == 0.0) {
                    bl = false;
                    continue;
                }
                double d12 = d5 + (d4 - d8) / d11;
                if (d12 == d5 || d12 <= d7 || d12 >= d6) {
                    bl = false;
                    continue;
                }
                d5 = d12;
            }
            d8 = this.YforT(d5);
            if (d8 < d4) {
                d7 = d5;
                continue;
            }
            if (!(d8 > d4)) break;
            d6 = d5;
        }
        boolean bl2 = false;
        return d5 > 1.0 ? -1.0 : d5;
    }

    @Override
    public double XforY(double d) {
        if (d <= this.y0) {
            return this.x0;
        }
        if (d >= this.y1) {
            return this.x1;
        }
        return this.XforT(this.TforY(d));
    }

    @Override
    public double XforT(double d) {
        return ((this.xcoeff3 * d + this.xcoeff2) * d + this.xcoeff1) * d + this.xcoeff0;
    }

    @Override
    public double YforT(double d) {
        return ((this.ycoeff3 * d + this.ycoeff2) * d + this.ycoeff1) * d + this.ycoeff0;
    }

    @Override
    public double dXforT(double d, int n) {
        switch (n) {
            case 0: {
                return ((this.xcoeff3 * d + this.xcoeff2) * d + this.xcoeff1) * d + this.xcoeff0;
            }
            case 1: {
                return (3.0 * this.xcoeff3 * d + 2.0 * this.xcoeff2) * d + this.xcoeff1;
            }
            case 2: {
                return 6.0 * this.xcoeff3 * d + 2.0 * this.xcoeff2;
            }
            case 3: {
                return 6.0 * this.xcoeff3;
            }
        }
        return 0.0;
    }

    @Override
    public double dYforT(double d, int n) {
        switch (n) {
            case 0: {
                return ((this.ycoeff3 * d + this.ycoeff2) * d + this.ycoeff1) * d + this.ycoeff0;
            }
            case 1: {
                return (3.0 * this.ycoeff3 * d + 2.0 * this.ycoeff2) * d + this.ycoeff1;
            }
            case 2: {
                return 6.0 * this.ycoeff3 * d + 2.0 * this.ycoeff2;
            }
            case 3: {
                return 6.0 * this.ycoeff3;
            }
        }
        return 0.0;
    }

    @Override
    public double nextVertical(double d, double d2) {
        double[] arrd = new double[]{this.xcoeff1, 2.0 * this.xcoeff2, 3.0 * this.xcoeff3};
        int n = Order3.solveQuadratic(arrd, arrd);
        for (int i = 0; i < n; ++i) {
            if (!(arrd[i] > d) || !(arrd[i] < d2)) continue;
            d2 = arrd[i];
        }
        return d2;
    }

    @Override
    public void enlarge(RectBounds rectBounds) {
        rectBounds.add((float)this.x0, (float)this.y0);
        double[] arrd = new double[]{this.xcoeff1, 2.0 * this.xcoeff2, 3.0 * this.xcoeff3};
        int n = Order3.solveQuadratic(arrd, arrd);
        for (int i = 0; i < n; ++i) {
            double d = arrd[i];
            if (!(d > 0.0) || !(d < 1.0)) continue;
            rectBounds.add((float)this.XforT(d), (float)this.YforT(d));
        }
        rectBounds.add((float)this.x1, (float)this.y1);
    }

    @Override
    public Curve getSubCurve(double d, double d2, int n) {
        int n2;
        if (d <= this.y0 && d2 >= this.y1) {
            return this.getWithDirection(n);
        }
        double[] arrd = new double[14];
        double d3 = this.TforY(d);
        double d4 = this.TforY(d2);
        arrd[0] = this.x0;
        arrd[1] = this.y0;
        arrd[2] = this.cx0;
        arrd[3] = this.cy0;
        arrd[4] = this.cx1;
        arrd[5] = this.cy1;
        arrd[6] = this.x1;
        arrd[7] = this.y1;
        if (d3 > d4) {
            double d5 = d3;
            d3 = d4;
            d4 = d5;
        }
        if (d4 < 1.0) {
            Order3.split(arrd, 0, d4);
        }
        if (d3 <= 0.0) {
            n2 = 0;
        } else {
            Order3.split(arrd, 0, d3 / d4);
            n2 = 6;
        }
        return new Order3(arrd[n2 + 0], d, arrd[n2 + 2], arrd[n2 + 3], arrd[n2 + 4], arrd[n2 + 5], arrd[n2 + 6], d2, n);
    }

    @Override
    public Curve getReversedCurve() {
        return new Order3(this.x0, this.y0, this.cx0, this.cy0, this.cx1, this.cy1, this.x1, this.y1, -this.direction);
    }

    @Override
    public int getSegment(float[] arrf) {
        if (this.direction == 1) {
            arrf[0] = (float)this.cx0;
            arrf[1] = (float)this.cy0;
            arrf[2] = (float)this.cx1;
            arrf[3] = (float)this.cy1;
            arrf[4] = (float)this.x1;
            arrf[5] = (float)this.y1;
        } else {
            arrf[0] = (float)this.cx1;
            arrf[1] = (float)this.cy1;
            arrf[2] = (float)this.cx0;
            arrf[3] = (float)this.cy0;
            arrf[4] = (float)this.x0;
            arrf[5] = (float)this.y0;
        }
        return 3;
    }

    @Override
    public String controlPointString() {
        return "(" + Order3.round(this.getCX0()) + ", " + Order3.round(this.getCY0()) + "), (" + Order3.round(this.getCX1()) + ", " + Order3.round(this.getCY1()) + "), ";
    }
}

