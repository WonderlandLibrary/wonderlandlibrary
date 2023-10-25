/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.geom;

import com.sun.javafx.geom.CubicCurve2D;
import com.sun.javafx.geom.FlatteningPathIterator;
import com.sun.javafx.geom.Line2D;
import com.sun.javafx.geom.PathIterator;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.QuadIterator;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.BaseTransform;

public class QuadCurve2D
extends Shape {
    public float x1;
    public float y1;
    public float ctrlx;
    public float ctrly;
    public float x2;
    public float y2;
    private static final int BELOW = -2;
    private static final int LOWEDGE = -1;
    private static final int INSIDE = 0;
    private static final int HIGHEDGE = 1;
    private static final int ABOVE = 2;

    public QuadCurve2D() {
    }

    public QuadCurve2D(float f, float f2, float f3, float f4, float f5, float f6) {
        this.setCurve(f, f2, f3, f4, f5, f6);
    }

    public void setCurve(float f, float f2, float f3, float f4, float f5, float f6) {
        this.x1 = f;
        this.y1 = f2;
        this.ctrlx = f3;
        this.ctrly = f4;
        this.x2 = f5;
        this.y2 = f6;
    }

    @Override
    public RectBounds getBounds() {
        float f = Math.min(Math.min(this.x1, this.x2), this.ctrlx);
        float f2 = Math.min(Math.min(this.y1, this.y2), this.ctrly);
        float f3 = Math.max(Math.max(this.x1, this.x2), this.ctrlx);
        float f4 = Math.max(Math.max(this.y1, this.y2), this.ctrly);
        return new RectBounds(f, f2, f3, f4);
    }

    public CubicCurve2D toCubic() {
        return new CubicCurve2D(this.x1, this.y1, (this.x1 + 2.0f * this.ctrlx) / 3.0f, (this.y1 + 2.0f * this.ctrly) / 3.0f, (2.0f * this.ctrlx + this.x2) / 3.0f, (2.0f * this.ctrly + this.y2) / 3.0f, this.x2, this.y2);
    }

    public void setCurve(float[] arrf, int n) {
        this.setCurve(arrf[n + 0], arrf[n + 1], arrf[n + 2], arrf[n + 3], arrf[n + 4], arrf[n + 5]);
    }

    public void setCurve(Point2D point2D, Point2D point2D2, Point2D point2D3) {
        this.setCurve(point2D.x, point2D.y, point2D2.x, point2D2.y, point2D3.x, point2D3.y);
    }

    public void setCurve(Point2D[] arrpoint2D, int n) {
        this.setCurve(arrpoint2D[n + 0].x, arrpoint2D[n + 0].y, arrpoint2D[n + 1].x, arrpoint2D[n + 1].y, arrpoint2D[n + 2].x, arrpoint2D[n + 2].y);
    }

    public void setCurve(QuadCurve2D quadCurve2D) {
        this.setCurve(quadCurve2D.x1, quadCurve2D.y1, quadCurve2D.ctrlx, quadCurve2D.ctrly, quadCurve2D.x2, quadCurve2D.y2);
    }

    public static float getFlatnessSq(float f, float f2, float f3, float f4, float f5, float f6) {
        return Line2D.ptSegDistSq(f, f2, f5, f6, f3, f4);
    }

    public static float getFlatness(float f, float f2, float f3, float f4, float f5, float f6) {
        return Line2D.ptSegDist(f, f2, f5, f6, f3, f4);
    }

    public static float getFlatnessSq(float[] arrf, int n) {
        return Line2D.ptSegDistSq(arrf[n + 0], arrf[n + 1], arrf[n + 4], arrf[n + 5], arrf[n + 2], arrf[n + 3]);
    }

    public static float getFlatness(float[] arrf, int n) {
        return Line2D.ptSegDist(arrf[n + 0], arrf[n + 1], arrf[n + 4], arrf[n + 5], arrf[n + 2], arrf[n + 3]);
    }

    public float getFlatnessSq() {
        return Line2D.ptSegDistSq(this.x1, this.y1, this.x2, this.y2, this.ctrlx, this.ctrly);
    }

    public float getFlatness() {
        return Line2D.ptSegDist(this.x1, this.y1, this.x2, this.y2, this.ctrlx, this.ctrly);
    }

    public void subdivide(QuadCurve2D quadCurve2D, QuadCurve2D quadCurve2D2) {
        QuadCurve2D.subdivide(this, quadCurve2D, quadCurve2D2);
    }

    public static void subdivide(QuadCurve2D quadCurve2D, QuadCurve2D quadCurve2D2, QuadCurve2D quadCurve2D3) {
        float f = quadCurve2D.x1;
        float f2 = quadCurve2D.y1;
        float f3 = quadCurve2D.ctrlx;
        float f4 = quadCurve2D.ctrly;
        float f5 = quadCurve2D.x2;
        float f6 = quadCurve2D.y2;
        float f7 = (f + f3) / 2.0f;
        float f8 = (f2 + f4) / 2.0f;
        float f9 = (f5 + f3) / 2.0f;
        float f10 = (f6 + f4) / 2.0f;
        f3 = (f7 + f9) / 2.0f;
        f4 = (f8 + f10) / 2.0f;
        if (quadCurve2D2 != null) {
            quadCurve2D2.setCurve(f, f2, f7, f8, f3, f4);
        }
        if (quadCurve2D3 != null) {
            quadCurve2D3.setCurve(f3, f4, f9, f10, f5, f6);
        }
    }

    public static void subdivide(float[] arrf, int n, float[] arrf2, int n2, float[] arrf3, int n3) {
        float f = arrf[n + 0];
        float f2 = arrf[n + 1];
        float f3 = arrf[n + 2];
        float f4 = arrf[n + 3];
        float f5 = arrf[n + 4];
        float f6 = arrf[n + 5];
        if (arrf2 != null) {
            arrf2[n2 + 0] = f;
            arrf2[n2 + 1] = f2;
        }
        if (arrf3 != null) {
            arrf3[n3 + 4] = f5;
            arrf3[n3 + 5] = f6;
        }
        f = (f + f3) / 2.0f;
        f2 = (f2 + f4) / 2.0f;
        f5 = (f5 + f3) / 2.0f;
        f6 = (f6 + f4) / 2.0f;
        f3 = (f + f5) / 2.0f;
        f4 = (f2 + f6) / 2.0f;
        if (arrf2 != null) {
            arrf2[n2 + 2] = f;
            arrf2[n2 + 3] = f2;
            arrf2[n2 + 4] = f3;
            arrf2[n2 + 5] = f4;
        }
        if (arrf3 != null) {
            arrf3[n3 + 0] = f3;
            arrf3[n3 + 1] = f4;
            arrf3[n3 + 2] = f5;
            arrf3[n3 + 3] = f6;
        }
    }

    public static int solveQuadratic(float[] arrf) {
        return QuadCurve2D.solveQuadratic(arrf, arrf);
    }

    public static int solveQuadratic(float[] arrf, float[] arrf2) {
        float f = arrf[2];
        float f2 = arrf[1];
        float f3 = arrf[0];
        int n = 0;
        if (f == 0.0f) {
            if (f2 == 0.0f) {
                return -1;
            }
            arrf2[n++] = -f3 / f2;
        } else {
            float f4 = f2 * f2 - 4.0f * f * f3;
            if (f4 < 0.0f) {
                return 0;
            }
            f4 = (float)Math.sqrt(f4);
            if (f2 < 0.0f) {
                f4 = -f4;
            }
            float f5 = (f2 + f4) / -2.0f;
            arrf2[n++] = f5 / f;
            if (f5 != 0.0f) {
                arrf2[n++] = f3 / f5;
            }
        }
        return n;
    }

    @Override
    public boolean contains(float f, float f2) {
        float f3 = this.x1;
        float f4 = f - f3;
        float f5 = this.y1;
        float f6 = this.ctrly;
        float f7 = this.y2;
        float f8 = f5 - 2.0f * f6 + f7;
        float f9 = f2 - f5;
        float f10 = this.ctrlx;
        float f11 = this.x2;
        float f12 = f3 - 2.0f * f10 + f11;
        float f13 = f11 - f3;
        float f14 = f7 - f5;
        float f15 = (f4 * f8 - f9 * f12) / (f13 * f8 - f14 * f12);
        if (f15 < 0.0f || f15 > 1.0f || f15 != f15) {
            return false;
        }
        float f16 = f12 * f15 * f15 + 2.0f * (f10 - f3) * f15 + f3;
        float f17 = f8 * f15 * f15 + 2.0f * (f6 - f5) * f15 + f5;
        float f18 = f13 * f15 + f3;
        float f19 = f14 * f15 + f5;
        return f >= f16 && f < f18 || f >= f18 && f < f16 || f2 >= f17 && f2 < f19 || f2 >= f19 && f2 < f17;
    }

    @Override
    public boolean contains(Point2D point2D) {
        return this.contains(point2D.x, point2D.y);
    }

    private static void fillEqn(float[] arrf, float f, float f2, float f3, float f4) {
        arrf[0] = f2 - f;
        arrf[1] = f3 + f3 - f2 - f2;
        arrf[2] = f2 - f3 - f3 + f4;
    }

    private static int evalQuadratic(float[] arrf, int n, boolean bl, boolean bl2, float[] arrf2, float f, float f2, float f3) {
        int n2 = 0;
        for (int i = 0; i < n; ++i) {
            float f4 = arrf[i];
            if (!(bl ? f4 >= 0.0f : f4 > 0.0f) || !(bl2 ? f4 <= 1.0f : f4 < 1.0f) || arrf2 != null && arrf2[1] + 2.0f * arrf2[2] * f4 == 0.0f) continue;
            float f5 = 1.0f - f4;
            arrf[n2++] = f * f5 * f5 + 2.0f * f2 * f4 * f5 + f3 * f4 * f4;
        }
        return n2;
    }

    private static int getTag(float f, float f2, float f3) {
        if (f <= f2) {
            return f < f2 ? -2 : -1;
        }
        if (f >= f3) {
            return f > f3 ? 2 : 1;
        }
        return 0;
    }

    private static boolean inwards(int n, int n2, int n3) {
        switch (n) {
            default: {
                return false;
            }
            case -1: {
                return n2 >= 0 || n3 >= 0;
            }
            case 0: {
                return true;
            }
            case 1: 
        }
        return n2 <= 0 || n3 <= 0;
    }

    @Override
    public boolean intersects(float f, float f2, float f3, float f4) {
        int n;
        boolean bl;
        if (f3 <= 0.0f || f4 <= 0.0f) {
            return false;
        }
        float f5 = this.x1;
        float f6 = this.y1;
        int n2 = QuadCurve2D.getTag(f5, f, f + f3);
        int n3 = QuadCurve2D.getTag(f6, f2, f2 + f4);
        if (n2 == 0 && n3 == 0) {
            return true;
        }
        float f7 = this.x2;
        float f8 = this.y2;
        int n4 = QuadCurve2D.getTag(f7, f, f + f3);
        int n5 = QuadCurve2D.getTag(f8, f2, f2 + f4);
        if (n4 == 0 && n5 == 0) {
            return true;
        }
        float f9 = this.ctrlx;
        float f10 = this.ctrly;
        int n6 = QuadCurve2D.getTag(f9, f, f + f3);
        int n7 = QuadCurve2D.getTag(f10, f2, f2 + f4);
        if (n2 < 0 && n4 < 0 && n6 < 0) {
            return false;
        }
        if (n3 < 0 && n5 < 0 && n7 < 0) {
            return false;
        }
        if (n2 > 0 && n4 > 0 && n6 > 0) {
            return false;
        }
        if (n3 > 0 && n5 > 0 && n7 > 0) {
            return false;
        }
        if (QuadCurve2D.inwards(n2, n4, n6) && QuadCurve2D.inwards(n3, n5, n7)) {
            return true;
        }
        if (QuadCurve2D.inwards(n4, n2, n6) && QuadCurve2D.inwards(n5, n3, n7)) {
            return true;
        }
        boolean bl2 = n2 * n4 <= 0;
        boolean bl3 = bl = n3 * n5 <= 0;
        if (n2 == 0 && n4 == 0 && bl) {
            return true;
        }
        if (n3 == 0 && n5 == 0 && bl2) {
            return true;
        }
        float[] arrf = new float[3];
        float[] arrf2 = new float[3];
        if (!bl) {
            QuadCurve2D.fillEqn(arrf, n3 < 0 ? f2 : f2 + f4, f6, f10, f8);
            return QuadCurve2D.solveQuadratic(arrf, arrf2) == 2 && QuadCurve2D.evalQuadratic(arrf2, 2, true, true, null, f5, f9, f7) == 2 && QuadCurve2D.getTag(arrf2[0], f, f + f3) * QuadCurve2D.getTag(arrf2[1], f, f + f3) <= 0;
        }
        if (!bl2) {
            QuadCurve2D.fillEqn(arrf, n2 < 0 ? f : f + f3, f5, f9, f7);
            return QuadCurve2D.solveQuadratic(arrf, arrf2) == 2 && QuadCurve2D.evalQuadratic(arrf2, 2, true, true, null, f6, f10, f8) == 2 && QuadCurve2D.getTag(arrf2[0], f2, f2 + f4) * QuadCurve2D.getTag(arrf2[1], f2, f2 + f4) <= 0;
        }
        float f11 = f7 - f5;
        float f12 = f8 - f6;
        float f13 = f8 * f5 - f7 * f6;
        int n8 = n3 == 0 ? n2 : QuadCurve2D.getTag((f13 + f11 * (n3 < 0 ? f2 : f2 + f4)) / f12, f, f + f3);
        if (n8 * (n = n5 == 0 ? n4 : QuadCurve2D.getTag((f13 + f11 * (n5 < 0 ? f2 : f2 + f4)) / f12, f, f + f3)) <= 0) {
            return true;
        }
        n8 = n8 * n2 <= 0 ? n3 : n5;
        QuadCurve2D.fillEqn(arrf, n < 0 ? f : f + f3, f5, f9, f7);
        int n9 = QuadCurve2D.solveQuadratic(arrf, arrf2);
        QuadCurve2D.evalQuadratic(arrf2, n9, true, true, null, f6, f10, f8);
        n = QuadCurve2D.getTag(arrf2[0], f2, f2 + f4);
        return n8 * n <= 0;
    }

    @Override
    public boolean contains(float f, float f2, float f3, float f4) {
        if (f3 <= 0.0f || f4 <= 0.0f) {
            return false;
        }
        return this.contains(f, f2) && this.contains(f + f3, f2) && this.contains(f + f3, f2 + f4) && this.contains(f, f2 + f4);
    }

    @Override
    public PathIterator getPathIterator(BaseTransform baseTransform) {
        return new QuadIterator(this, baseTransform);
    }

    @Override
    public PathIterator getPathIterator(BaseTransform baseTransform, float f) {
        return new FlatteningPathIterator(this.getPathIterator(baseTransform), f);
    }

    @Override
    public QuadCurve2D copy() {
        return new QuadCurve2D(this.x1, this.y1, this.ctrlx, this.ctrly, this.x2, this.y2);
    }

    public int hashCode() {
        int n = Float.floatToIntBits(this.x1);
        n += Float.floatToIntBits(this.y1) * 37;
        n += Float.floatToIntBits(this.x2) * 43;
        n += Float.floatToIntBits(this.y2) * 47;
        n += Float.floatToIntBits(this.ctrlx) * 53;
        return n += Float.floatToIntBits(this.ctrly) * 59;
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof QuadCurve2D) {
            QuadCurve2D quadCurve2D = (QuadCurve2D)object;
            return this.x1 == quadCurve2D.x1 && this.y1 == quadCurve2D.y1 && this.x2 == quadCurve2D.x2 && this.y2 == quadCurve2D.y2 && this.ctrlx == quadCurve2D.ctrlx && this.ctrly == quadCurve2D.ctrly;
        }
        return false;
    }
}

