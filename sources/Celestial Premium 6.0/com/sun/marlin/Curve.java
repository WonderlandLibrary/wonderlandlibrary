/*
 * Decompiled with CFR 0.150.
 */
package com.sun.marlin;

import com.sun.marlin.Helpers;

final class Curve {
    double ax;
    double ay;
    double bx;
    double by;
    double cx;
    double cy;
    double dx;
    double dy;
    double dax;
    double day;
    double dbx;
    double dby;

    Curve() {
    }

    void set(double[] arrd, int n) {
        if (n == 8) {
            this.set(arrd[0], arrd[1], arrd[2], arrd[3], arrd[4], arrd[5], arrd[6], arrd[7]);
        } else if (n == 4) {
            this.set(arrd[0], arrd[1], arrd[2], arrd[3]);
        } else {
            this.set(arrd[0], arrd[1], arrd[2], arrd[3], arrd[4], arrd[5]);
        }
    }

    void set(double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8) {
        double d9 = 3.0 * (d5 - d3);
        double d10 = 3.0 * (d6 - d4);
        double d11 = 3.0 * (d3 - d);
        double d12 = 3.0 * (d4 - d2);
        this.ax = d7 - d - d9;
        this.ay = d8 - d2 - d10;
        this.bx = d9 - d11;
        this.by = d10 - d12;
        this.cx = d11;
        this.cy = d12;
        this.dx = d;
        this.dy = d2;
        this.dax = 3.0 * this.ax;
        this.day = 3.0 * this.ay;
        this.dbx = 2.0 * this.bx;
        this.dby = 2.0 * this.by;
    }

    void set(double d, double d2, double d3, double d4, double d5, double d6) {
        double d7 = d3 - d;
        double d8 = d4 - d2;
        this.ax = 0.0;
        this.ay = 0.0;
        this.bx = d5 - d3 - d7;
        this.by = d6 - d4 - d8;
        this.cx = 2.0 * d7;
        this.cy = 2.0 * d8;
        this.dx = d;
        this.dy = d2;
        this.dax = 0.0;
        this.day = 0.0;
        this.dbx = 2.0 * this.bx;
        this.dby = 2.0 * this.by;
    }

    void set(double d, double d2, double d3, double d4) {
        double d5 = d3 - d;
        double d6 = d4 - d2;
        this.ax = 0.0;
        this.ay = 0.0;
        this.bx = 0.0;
        this.by = 0.0;
        this.cx = d5;
        this.cy = d6;
        this.dx = d;
        this.dy = d2;
        this.dax = 0.0;
        this.day = 0.0;
        this.dbx = 0.0;
        this.dby = 0.0;
    }

    int dxRoots(double[] arrd, int n) {
        return Helpers.quadraticRoots(this.dax, this.dbx, this.cx, arrd, n);
    }

    int dyRoots(double[] arrd, int n) {
        return Helpers.quadraticRoots(this.day, this.dby, this.cy, arrd, n);
    }

    int infPoints(double[] arrd, int n) {
        double d = this.dax * this.dby - this.dbx * this.day;
        double d2 = 2.0 * (this.cy * this.dax - this.day * this.cx);
        double d3 = this.cy * this.dbx - this.cx * this.dby;
        return Helpers.quadraticRoots(d, d2, d3, arrd, n);
    }

    int xPoints(double[] arrd, int n, double d) {
        return Helpers.cubicRootsInAB(this.ax, this.bx, this.cx, this.dx - d, arrd, n, 0.0, 1.0);
    }

    int yPoints(double[] arrd, int n, double d) {
        return Helpers.cubicRootsInAB(this.ay, this.by, this.cy, this.dy - d, arrd, n, 0.0, 1.0);
    }

    private int perpendiculardfddf(double[] arrd, int n) {
        assert (arrd.length >= n + 4);
        double d = 2.0 * (this.dax * this.dax + this.day * this.day);
        double d2 = 3.0 * (this.dax * this.dbx + this.day * this.dby);
        double d3 = 2.0 * (this.dax * this.cx + this.day * this.cy) + this.dbx * this.dbx + this.dby * this.dby;
        double d4 = this.dbx * this.cx + this.dby * this.cy;
        return Helpers.cubicRootsInAB(d, d2, d3, d4, arrd, n, 0.0, 1.0);
    }

    int rootsOfROCMinusW(double[] arrd, int n, double d, double d2) {
        assert (n <= 6 && arrd.length >= 10);
        int n2 = n;
        int n3 = n + this.perpendiculardfddf(arrd, n);
        arrd[n3] = 1.0;
        double d3 = 0.0;
        double d4 = this.ROCsq(d3) - d;
        for (int i = n; i <= n3; ++i) {
            double d5 = arrd[i];
            double d6 = this.ROCsq(d5) - d;
            if (d4 == 0.0) {
                arrd[n2++] = d3;
            } else if (d6 * d4 < 0.0) {
                arrd[n2++] = this.falsePositionROCsqMinusX(d3, d5, d, d2);
            }
            d3 = d5;
            d4 = d6;
        }
        return n2 - n;
    }

    private static double eliminateInf(double d) {
        return d == Double.POSITIVE_INFINITY ? Double.MAX_VALUE : (d == Double.NEGATIVE_INFINITY ? Double.MIN_VALUE : d);
    }

    private double falsePositionROCsqMinusX(double d, double d2, double d3, double d4) {
        int n = 0;
        double d5 = d2;
        double d6 = Curve.eliminateInf(this.ROCsq(d5) - d3);
        double d7 = d;
        double d8 = Curve.eliminateInf(this.ROCsq(d7) - d3);
        double d9 = d7;
        for (int i = 0; i < 100 && Math.abs(d5 - d7) > d4 * Math.abs(d5 + d7); ++i) {
            d9 = (d8 * d5 - d6 * d7) / (d8 - d6);
            double d10 = this.ROCsq(d9) - d3;
            if (Curve.sameSign(d10, d6)) {
                d6 = d10;
                d5 = d9;
                if (n < 0) {
                    d8 /= (double)(1 << -n);
                    --n;
                    continue;
                }
                n = -1;
                continue;
            }
            if (!(d10 * d8 > 0.0)) break;
            d8 = d10;
            d7 = d9;
            if (n > 0) {
                d6 /= (double)(1 << n);
                ++n;
                continue;
            }
            n = 1;
        }
        return d9;
    }

    private static boolean sameSign(double d, double d2) {
        return d < 0.0 && d2 < 0.0 || d > 0.0 && d2 > 0.0;
    }

    private double ROCsq(double d) {
        double d2 = d * (d * this.dax + this.dbx) + this.cx;
        double d3 = d * (d * this.day + this.dby) + this.cy;
        double d4 = 2.0 * this.dax * d + this.dbx;
        double d5 = 2.0 * this.day * d + this.dby;
        double d6 = d2 * d2 + d3 * d3;
        double d7 = d4 * d4 + d5 * d5;
        double d8 = d4 * d2 + d5 * d3;
        return d6 * (d6 * d6 / (d6 * d7 - d8 * d8));
    }
}

