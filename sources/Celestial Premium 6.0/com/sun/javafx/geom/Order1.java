/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.geom;

import com.sun.javafx.geom.Crossings;
import com.sun.javafx.geom.Curve;
import com.sun.javafx.geom.RectBounds;

final class Order1
extends Curve {
    private double x0;
    private double y0;
    private double x1;
    private double y1;
    private double xmin;
    private double xmax;

    public Order1(double d, double d2, double d3, double d4, int n) {
        super(n);
        this.x0 = d;
        this.y0 = d2;
        this.x1 = d3;
        this.y1 = d4;
        if (d < d3) {
            this.xmin = d;
            this.xmax = d3;
        } else {
            this.xmin = d3;
            this.xmax = d;
        }
    }

    @Override
    public int getOrder() {
        return 1;
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

    @Override
    public double getX1() {
        return this.direction == -1 ? this.x0 : this.x1;
    }

    @Override
    public double getY1() {
        return this.direction == -1 ? this.y0 : this.y1;
    }

    @Override
    public double XforY(double d) {
        if (this.x0 == this.x1 || d <= this.y0) {
            return this.x0;
        }
        if (d >= this.y1) {
            return this.x1;
        }
        return this.x0 + (d - this.y0) * (this.x1 - this.x0) / (this.y1 - this.y0);
    }

    @Override
    public double TforY(double d) {
        if (d <= this.y0) {
            return 0.0;
        }
        if (d >= this.y1) {
            return 1.0;
        }
        return (d - this.y0) / (this.y1 - this.y0);
    }

    @Override
    public double XforT(double d) {
        return this.x0 + d * (this.x1 - this.x0);
    }

    @Override
    public double YforT(double d) {
        return this.y0 + d * (this.y1 - this.y0);
    }

    @Override
    public double dXforT(double d, int n) {
        switch (n) {
            case 0: {
                return this.x0 + d * (this.x1 - this.x0);
            }
            case 1: {
                return this.x1 - this.x0;
            }
        }
        return 0.0;
    }

    @Override
    public double dYforT(double d, int n) {
        switch (n) {
            case 0: {
                return this.y0 + d * (this.y1 - this.y0);
            }
            case 1: {
                return this.y1 - this.y0;
            }
        }
        return 0.0;
    }

    @Override
    public double nextVertical(double d, double d2) {
        return d2;
    }

    @Override
    public boolean accumulateCrossings(Crossings crossings) {
        double d;
        double d2;
        double d3;
        double d4;
        double d5 = crossings.getXLo();
        double d6 = crossings.getYLo();
        double d7 = crossings.getXHi();
        double d8 = crossings.getYHi();
        if (this.xmin >= d7) {
            return false;
        }
        if (this.y0 < d6) {
            if (this.y1 <= d6) {
                return false;
            }
            d4 = d6;
            d3 = this.XforY(d6);
        } else {
            if (this.y0 >= d8) {
                return false;
            }
            d4 = this.y0;
            d3 = this.x0;
        }
        if (this.y1 > d8) {
            d2 = d8;
            d = this.XforY(d8);
        } else {
            d2 = this.y1;
            d = this.x1;
        }
        if (d3 >= d7 && d >= d7) {
            return false;
        }
        if (d3 > d5 || d > d5) {
            return true;
        }
        crossings.record(d4, d2, this.direction);
        return false;
    }

    @Override
    public void enlarge(RectBounds rectBounds) {
        rectBounds.add((float)this.x0, (float)this.y0);
        rectBounds.add((float)this.x1, (float)this.y1);
    }

    @Override
    public Curve getSubCurve(double d, double d2, int n) {
        if (d == this.y0 && d2 == this.y1) {
            return this.getWithDirection(n);
        }
        if (this.x0 == this.x1) {
            return new Order1(this.x0, d, this.x1, d2, n);
        }
        double d3 = this.x0 - this.x1;
        double d4 = this.y0 - this.y1;
        double d5 = this.x0 + (d - this.y0) * d3 / d4;
        double d6 = this.x0 + (d2 - this.y0) * d3 / d4;
        return new Order1(d5, d, d6, d2, n);
    }

    @Override
    public Curve getReversedCurve() {
        return new Order1(this.x0, this.y0, this.x1, this.y1, -this.direction);
    }

    @Override
    public int compareTo(Curve curve, double[] arrd) {
        double d;
        if (!(curve instanceof Order1)) {
            return super.compareTo(curve, arrd);
        }
        Order1 order1 = (Order1)curve;
        if (arrd[1] <= arrd[0]) {
            throw new InternalError("yrange already screwed up...");
        }
        arrd[1] = Math.min(Math.min(arrd[1], this.y1), order1.y1);
        if (arrd[1] <= arrd[0]) {
            throw new InternalError("backstepping from " + arrd[0] + " to " + arrd[1]);
        }
        if (this.xmax <= order1.xmin) {
            return this.xmin == order1.xmax ? 0 : -1;
        }
        if (this.xmin >= order1.xmax) {
            return 1;
        }
        double d2 = order1.x1 - order1.x0;
        double d3 = this.y1 - this.y0;
        double d4 = this.x1 - this.x0;
        double d5 = order1.y1 - order1.y0;
        double d6 = d2 * d3 - d4 * d5;
        if (d6 != 0.0) {
            double d7 = (this.x0 - order1.x0) * d3 * d5 - this.y0 * d4 * d5 + order1.y0 * d2 * d3;
            d = d7 / d6;
            if (d <= arrd[0]) {
                d = Math.min(this.y1, order1.y1);
            } else {
                if (d < arrd[1]) {
                    arrd[1] = d;
                }
                d = Math.max(this.y0, order1.y0);
            }
        } else {
            d = Math.max(this.y0, order1.y0);
        }
        return Order1.orderof(this.XforY(d), order1.XforY(d));
    }

    @Override
    public int getSegment(float[] arrf) {
        if (this.direction == 1) {
            arrf[0] = (float)this.x1;
            arrf[1] = (float)this.y1;
        } else {
            arrf[0] = (float)this.x0;
            arrf[1] = (float)this.y0;
        }
        return 1;
    }
}

