/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.geom;

import com.sun.javafx.geom.Curve;
import com.sun.javafx.geom.RectBounds;
import java.util.Vector;

final class Order2
extends Curve {
    private double x0;
    private double y0;
    private double cx0;
    private double cy0;
    private double x1;
    private double y1;
    private double xmin;
    private double xmax;
    private double xcoeff0;
    private double xcoeff1;
    private double xcoeff2;
    private double ycoeff0;
    private double ycoeff1;
    private double ycoeff2;

    public static void insert(Vector vector, double[] arrd, double d, double d2, double d3, double d4, double d5, double d6, int n) {
        int n2 = Order2.getHorizontalParams(d2, d4, d6, arrd);
        if (n2 == 0) {
            Order2.addInstance(vector, d, d2, d3, d4, d5, d6, n);
            return;
        }
        double d7 = arrd[0];
        arrd[0] = d;
        arrd[1] = d2;
        arrd[2] = d3;
        arrd[3] = d4;
        arrd[4] = d5;
        arrd[5] = d6;
        Order2.split(arrd, 0, d7);
        int n3 = n == 1 ? 0 : 4;
        int n4 = 4 - n3;
        Order2.addInstance(vector, arrd[n3], arrd[n3 + 1], arrd[n3 + 2], arrd[n3 + 3], arrd[n3 + 4], arrd[n3 + 5], n);
        Order2.addInstance(vector, arrd[n4], arrd[n4 + 1], arrd[n4 + 2], arrd[n4 + 3], arrd[n4 + 4], arrd[n4 + 5], n);
    }

    public static void addInstance(Vector vector, double d, double d2, double d3, double d4, double d5, double d6, int n) {
        if (d2 > d6) {
            vector.add(new Order2(d5, d6, d3, d4, d, d2, -n));
        } else if (d6 > d2) {
            vector.add(new Order2(d, d2, d3, d4, d5, d6, n));
        }
    }

    public static int getHorizontalParams(double d, double d2, double d3, double[] arrd) {
        if (d <= d2 && d2 <= d3) {
            return 0;
        }
        double d4 = (d -= d2) + (d3 -= d2);
        if (d4 == 0.0) {
            return 0;
        }
        double d5 = d / d4;
        if (d5 <= 0.0 || d5 >= 1.0) {
            return 0;
        }
        arrd[0] = d5;
        return 1;
    }

    public static void split(double[] arrd, int n, double d) {
        double d2;
        double d3;
        arrd[n + 8] = d3 = arrd[n + 4];
        arrd[n + 9] = d2 = arrd[n + 5];
        double d4 = arrd[n + 2];
        double d5 = arrd[n + 3];
        d3 = d4 + (d3 - d4) * d;
        d2 = d5 + (d2 - d5) * d;
        double d6 = arrd[n + 0];
        double d7 = arrd[n + 1];
        d6 += (d4 - d6) * d;
        d7 += (d5 - d7) * d;
        d4 = d6 + (d3 - d6) * d;
        d5 = d7 + (d2 - d7) * d;
        arrd[n + 2] = d6;
        arrd[n + 3] = d7;
        arrd[n + 4] = d4;
        arrd[n + 5] = d5;
        arrd[n + 6] = d3;
        arrd[n + 7] = d2;
    }

    public Order2(double d, double d2, double d3, double d4, double d5, double d6, int n) {
        super(n);
        if (d4 < d2) {
            d4 = d2;
        } else if (d4 > d6) {
            d4 = d6;
        }
        this.x0 = d;
        this.y0 = d2;
        this.cx0 = d3;
        this.cy0 = d4;
        this.x1 = d5;
        this.y1 = d6;
        this.xmin = Math.min(Math.min(d, d5), d3);
        this.xmax = Math.max(Math.max(d, d5), d3);
        this.xcoeff0 = d;
        this.xcoeff1 = d3 + d3 - d - d;
        this.xcoeff2 = d - d3 - d3 + d5;
        this.ycoeff0 = d2;
        this.ycoeff1 = d4 + d4 - d2 - d2;
        this.ycoeff2 = d2 - d4 - d4 + d6;
    }

    @Override
    public int getOrder() {
        return 2;
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
        return this.cx0;
    }

    public double getCY0() {
        return this.cy0;
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
        if (d <= this.y0) {
            return this.x0;
        }
        if (d >= this.y1) {
            return this.x1;
        }
        return this.XforT(this.TforY(d));
    }

    @Override
    public double TforY(double d) {
        if (d <= this.y0) {
            return 0.0;
        }
        if (d >= this.y1) {
            return 1.0;
        }
        return Order2.TforY(d, this.ycoeff0, this.ycoeff1, this.ycoeff2);
    }

    public static double TforY(double d, double d2, double d3, double d4) {
        double d5;
        double d6;
        d2 -= d;
        if (d4 == 0.0) {
            d6 = -d2 / d3;
            if (d6 >= 0.0 && d6 <= 1.0) {
                return d6;
            }
        } else {
            d6 = d3 * d3 - 4.0 * d4 * d2;
            if (d6 >= 0.0) {
                double d7;
                d6 = Math.sqrt(d6);
                if (d3 < 0.0) {
                    d6 = -d6;
                }
                if ((d7 = (d5 = (d3 + d6) / -2.0) / d4) >= 0.0 && d7 <= 1.0) {
                    return d7;
                }
                if (d5 != 0.0 && (d7 = d2 / d5) >= 0.0 && d7 <= 1.0) {
                    return d7;
                }
            }
        }
        return 0.0 < ((d6 = d2) + (d5 = d2 + d3 + d4)) / 2.0 ? 0.0 : 1.0;
    }

    @Override
    public double XforT(double d) {
        return (this.xcoeff2 * d + this.xcoeff1) * d + this.xcoeff0;
    }

    @Override
    public double YforT(double d) {
        return (this.ycoeff2 * d + this.ycoeff1) * d + this.ycoeff0;
    }

    @Override
    public double dXforT(double d, int n) {
        switch (n) {
            case 0: {
                return (this.xcoeff2 * d + this.xcoeff1) * d + this.xcoeff0;
            }
            case 1: {
                return 2.0 * this.xcoeff2 * d + this.xcoeff1;
            }
            case 2: {
                return 2.0 * this.xcoeff2;
            }
        }
        return 0.0;
    }

    @Override
    public double dYforT(double d, int n) {
        switch (n) {
            case 0: {
                return (this.ycoeff2 * d + this.ycoeff1) * d + this.ycoeff0;
            }
            case 1: {
                return 2.0 * this.ycoeff2 * d + this.ycoeff1;
            }
            case 2: {
                return 2.0 * this.ycoeff2;
            }
        }
        return 0.0;
    }

    @Override
    public double nextVertical(double d, double d2) {
        double d3 = -this.xcoeff1 / (2.0 * this.xcoeff2);
        if (d3 > d && d3 < d2) {
            return d3;
        }
        return d2;
    }

    @Override
    public void enlarge(RectBounds rectBounds) {
        rectBounds.add((float)this.x0, (float)this.y0);
        double d = -this.xcoeff1 / (2.0 * this.xcoeff2);
        if (d > 0.0 && d < 1.0) {
            rectBounds.add((float)this.XforT(d), (float)this.YforT(d));
        }
        rectBounds.add((float)this.x1, (float)this.y1);
    }

    @Override
    public Curve getSubCurve(double d, double d2, int n) {
        int n2;
        double d3;
        if (d <= this.y0) {
            if (d2 >= this.y1) {
                return this.getWithDirection(n);
            }
            d3 = 0.0;
        } else {
            d3 = Order2.TforY(d, this.ycoeff0, this.ycoeff1, this.ycoeff2);
        }
        double d4 = d2 >= this.y1 ? 1.0 : Order2.TforY(d2, this.ycoeff0, this.ycoeff1, this.ycoeff2);
        double[] arrd = new double[10];
        arrd[0] = this.x0;
        arrd[1] = this.y0;
        arrd[2] = this.cx0;
        arrd[3] = this.cy0;
        arrd[4] = this.x1;
        arrd[5] = this.y1;
        if (d4 < 1.0) {
            Order2.split(arrd, 0, d4);
        }
        if (d3 <= 0.0) {
            n2 = 0;
        } else {
            Order2.split(arrd, 0, d3 / d4);
            n2 = 4;
        }
        return new Order2(arrd[n2 + 0], d, arrd[n2 + 2], arrd[n2 + 3], arrd[n2 + 4], d2, n);
    }

    @Override
    public Curve getReversedCurve() {
        return new Order2(this.x0, this.y0, this.cx0, this.cy0, this.x1, this.y1, -this.direction);
    }

    @Override
    public int getSegment(float[] arrf) {
        arrf[0] = (float)this.cx0;
        arrf[1] = (float)this.cy0;
        if (this.direction == 1) {
            arrf[2] = (float)this.x1;
            arrf[3] = (float)this.y1;
        } else {
            arrf[2] = (float)this.x0;
            arrf[3] = (float)this.y0;
        }
        return 2;
    }

    @Override
    public String controlPointString() {
        return "(" + Order2.round(this.cx0) + ", " + Order2.round(this.cy0) + "), ";
    }
}

