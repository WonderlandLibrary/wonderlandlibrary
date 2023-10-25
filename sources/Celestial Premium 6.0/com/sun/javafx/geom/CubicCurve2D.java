/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.geom;

import com.sun.javafx.geom.CubicIterator;
import com.sun.javafx.geom.FlatteningPathIterator;
import com.sun.javafx.geom.Line2D;
import com.sun.javafx.geom.PathIterator;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.QuadCurve2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.BaseTransform;
import java.util.Arrays;

public class CubicCurve2D
extends Shape {
    public float x1;
    public float y1;
    public float ctrlx1;
    public float ctrly1;
    public float ctrlx2;
    public float ctrly2;
    public float x2;
    public float y2;
    private static final int BELOW = -2;
    private static final int LOWEDGE = -1;
    private static final int INSIDE = 0;
    private static final int HIGHEDGE = 1;
    private static final int ABOVE = 2;

    public CubicCurve2D() {
    }

    public CubicCurve2D(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        this.setCurve(f, f2, f3, f4, f5, f6, f7, f8);
    }

    public void setCurve(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        this.x1 = f;
        this.y1 = f2;
        this.ctrlx1 = f3;
        this.ctrly1 = f4;
        this.ctrlx2 = f5;
        this.ctrly2 = f6;
        this.x2 = f7;
        this.y2 = f8;
    }

    @Override
    public RectBounds getBounds() {
        float f = Math.min(Math.min(this.x1, this.x2), Math.min(this.ctrlx1, this.ctrlx2));
        float f2 = Math.min(Math.min(this.y1, this.y2), Math.min(this.ctrly1, this.ctrly2));
        float f3 = Math.max(Math.max(this.x1, this.x2), Math.max(this.ctrlx1, this.ctrlx2));
        float f4 = Math.max(Math.max(this.y1, this.y2), Math.max(this.ctrly1, this.ctrly2));
        return new RectBounds(f, f2, f3, f4);
    }

    public Point2D eval(float f) {
        Point2D point2D = new Point2D();
        this.eval(f, point2D);
        return point2D;
    }

    public void eval(float f, Point2D point2D) {
        point2D.setLocation(this.calcX(f), this.calcY(f));
    }

    public Point2D evalDt(float f) {
        Point2D point2D = new Point2D();
        this.evalDt(f, point2D);
        return point2D;
    }

    public void evalDt(float f, Point2D point2D) {
        float f2 = f;
        float f3 = 1.0f - f2;
        float f4 = 3.0f * ((this.ctrlx1 - this.x1) * f3 * f3 + 2.0f * (this.ctrlx2 - this.ctrlx1) * f3 * f2 + (this.x2 - this.ctrlx2) * f2 * f2);
        float f5 = 3.0f * ((this.ctrly1 - this.y1) * f3 * f3 + 2.0f * (this.ctrly2 - this.ctrly1) * f3 * f2 + (this.y2 - this.ctrly2) * f2 * f2);
        point2D.setLocation(f4, f5);
    }

    public void setCurve(float[] arrf, int n) {
        this.setCurve(arrf[n + 0], arrf[n + 1], arrf[n + 2], arrf[n + 3], arrf[n + 4], arrf[n + 5], arrf[n + 6], arrf[n + 7]);
    }

    public void setCurve(Point2D point2D, Point2D point2D2, Point2D point2D3, Point2D point2D4) {
        this.setCurve(point2D.x, point2D.y, point2D2.x, point2D2.y, point2D3.x, point2D3.y, point2D4.x, point2D4.y);
    }

    public void setCurve(Point2D[] arrpoint2D, int n) {
        this.setCurve(arrpoint2D[n + 0].x, arrpoint2D[n + 0].y, arrpoint2D[n + 1].x, arrpoint2D[n + 1].y, arrpoint2D[n + 2].x, arrpoint2D[n + 2].y, arrpoint2D[n + 3].x, arrpoint2D[n + 3].y);
    }

    public void setCurve(CubicCurve2D cubicCurve2D) {
        this.setCurve(cubicCurve2D.x1, cubicCurve2D.y1, cubicCurve2D.ctrlx1, cubicCurve2D.ctrly1, cubicCurve2D.ctrlx2, cubicCurve2D.ctrly2, cubicCurve2D.x2, cubicCurve2D.y2);
    }

    public static float getFlatnessSq(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        return Math.max(Line2D.ptSegDistSq(f, f2, f7, f8, f3, f4), Line2D.ptSegDistSq(f, f2, f7, f8, f5, f6));
    }

    public static float getFlatness(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        return (float)Math.sqrt(CubicCurve2D.getFlatnessSq(f, f2, f3, f4, f5, f6, f7, f8));
    }

    public static float getFlatnessSq(float[] arrf, int n) {
        return CubicCurve2D.getFlatnessSq(arrf[n + 0], arrf[n + 1], arrf[n + 2], arrf[n + 3], arrf[n + 4], arrf[n + 5], arrf[n + 6], arrf[n + 7]);
    }

    public static float getFlatness(float[] arrf, int n) {
        return CubicCurve2D.getFlatness(arrf[n + 0], arrf[n + 1], arrf[n + 2], arrf[n + 3], arrf[n + 4], arrf[n + 5], arrf[n + 6], arrf[n + 7]);
    }

    public float getFlatnessSq() {
        return CubicCurve2D.getFlatnessSq(this.x1, this.y1, this.ctrlx1, this.ctrly1, this.ctrlx2, this.ctrly2, this.x2, this.y2);
    }

    public float getFlatness() {
        return CubicCurve2D.getFlatness(this.x1, this.y1, this.ctrlx1, this.ctrly1, this.ctrlx2, this.ctrly2, this.x2, this.y2);
    }

    public void subdivide(float f, CubicCurve2D cubicCurve2D, CubicCurve2D cubicCurve2D2) {
        float f2;
        float f3;
        float f4;
        float f5;
        float f6;
        float f7;
        float f8;
        float f9;
        if (cubicCurve2D == null && cubicCurve2D2 == null) {
            return;
        }
        float f10 = this.calcX(f);
        float f11 = this.calcY(f);
        float f12 = this.x1;
        float f13 = this.y1;
        float f14 = this.ctrlx1;
        float f15 = this.ctrly1;
        float f16 = this.ctrlx2;
        float f17 = this.ctrly2;
        float f18 = this.x2;
        float f19 = this.y2;
        float f20 = 1.0f - f;
        float f21 = f20 * f14 + f * f16;
        float f22 = f20 * f15 + f * f17;
        if (cubicCurve2D != null) {
            f9 = f12;
            f8 = f13;
            f7 = f20 * f12 + f * f14;
            f6 = f20 * f13 + f * f15;
            f5 = f20 * f7 + f * f21;
            f4 = f20 * f6 + f * f22;
            f3 = f10;
            f2 = f11;
            cubicCurve2D.setCurve(f9, f8, f7, f6, f5, f4, f3, f2);
        }
        if (cubicCurve2D2 != null) {
            f9 = f10;
            f8 = f11;
            f7 = f20 * f16 + f * f18;
            f6 = f20 * f17 + f * f19;
            f5 = f20 * f21 + f * f7;
            f4 = f20 * f22 + f * f6;
            f3 = f18;
            f2 = f19;
            cubicCurve2D2.setCurve(f9, f8, f5, f4, f7, f6, f3, f2);
        }
    }

    public void subdivide(CubicCurve2D cubicCurve2D, CubicCurve2D cubicCurve2D2) {
        CubicCurve2D.subdivide(this, cubicCurve2D, cubicCurve2D2);
    }

    public static void subdivide(CubicCurve2D cubicCurve2D, CubicCurve2D cubicCurve2D2, CubicCurve2D cubicCurve2D3) {
        float f = cubicCurve2D.x1;
        float f2 = cubicCurve2D.y1;
        float f3 = cubicCurve2D.ctrlx1;
        float f4 = cubicCurve2D.ctrly1;
        float f5 = cubicCurve2D.ctrlx2;
        float f6 = cubicCurve2D.ctrly2;
        float f7 = cubicCurve2D.x2;
        float f8 = cubicCurve2D.y2;
        float f9 = (f3 + f5) / 2.0f;
        float f10 = (f4 + f6) / 2.0f;
        f3 = (f + f3) / 2.0f;
        f4 = (f2 + f4) / 2.0f;
        f5 = (f7 + f5) / 2.0f;
        f6 = (f8 + f6) / 2.0f;
        float f11 = (f3 + f9) / 2.0f;
        float f12 = (f4 + f10) / 2.0f;
        float f13 = (f5 + f9) / 2.0f;
        float f14 = (f6 + f10) / 2.0f;
        f9 = (f11 + f13) / 2.0f;
        f10 = (f12 + f14) / 2.0f;
        if (cubicCurve2D2 != null) {
            cubicCurve2D2.setCurve(f, f2, f3, f4, f11, f12, f9, f10);
        }
        if (cubicCurve2D3 != null) {
            cubicCurve2D3.setCurve(f9, f10, f13, f14, f5, f6, f7, f8);
        }
    }

    public static void subdivide(float[] arrf, int n, float[] arrf2, int n2, float[] arrf3, int n3) {
        float f = arrf[n + 0];
        float f2 = arrf[n + 1];
        float f3 = arrf[n + 2];
        float f4 = arrf[n + 3];
        float f5 = arrf[n + 4];
        float f6 = arrf[n + 5];
        float f7 = arrf[n + 6];
        float f8 = arrf[n + 7];
        if (arrf2 != null) {
            arrf2[n2 + 0] = f;
            arrf2[n2 + 1] = f2;
        }
        if (arrf3 != null) {
            arrf3[n3 + 6] = f7;
            arrf3[n3 + 7] = f8;
        }
        f = (f + f3) / 2.0f;
        f2 = (f2 + f4) / 2.0f;
        f7 = (f7 + f5) / 2.0f;
        f8 = (f8 + f6) / 2.0f;
        float f9 = (f3 + f5) / 2.0f;
        float f10 = (f4 + f6) / 2.0f;
        f3 = (f + f9) / 2.0f;
        f4 = (f2 + f10) / 2.0f;
        f5 = (f7 + f9) / 2.0f;
        f6 = (f8 + f10) / 2.0f;
        f9 = (f3 + f5) / 2.0f;
        f10 = (f4 + f6) / 2.0f;
        if (arrf2 != null) {
            arrf2[n2 + 2] = f;
            arrf2[n2 + 3] = f2;
            arrf2[n2 + 4] = f3;
            arrf2[n2 + 5] = f4;
            arrf2[n2 + 6] = f9;
            arrf2[n2 + 7] = f10;
        }
        if (arrf3 != null) {
            arrf3[n3 + 0] = f9;
            arrf3[n3 + 1] = f10;
            arrf3[n3 + 2] = f5;
            arrf3[n3 + 3] = f6;
            arrf3[n3 + 4] = f7;
            arrf3[n3 + 5] = f8;
        }
    }

    public static int solveCubic(float[] arrf) {
        return CubicCurve2D.solveCubic(arrf, arrf);
    }

    public static int solveCubic(float[] arrf, float[] arrf2) {
        float f = arrf[3];
        if (f == 0.0f) {
            return QuadCurve2D.solveQuadratic(arrf, arrf2);
        }
        float f2 = arrf[2] / f;
        float f3 = arrf[1] / f;
        float f4 = arrf[0] / f;
        int n = 0;
        float f5 = (f2 * f2 - 3.0f * f3) / 9.0f;
        float f6 = (2.0f * f2 * f2 * f2 - 9.0f * f2 * f3 + 27.0f * f4) / 54.0f;
        float f7 = f6 * f6;
        float f8 = f5 * f5 * f5;
        f2 /= 3.0f;
        if (f7 < f8) {
            float f9 = (float)Math.acos((double)f6 / Math.sqrt(f8));
            f5 = (float)(-2.0 * Math.sqrt(f5));
            if (arrf2 == arrf) {
                arrf = new float[4];
                System.arraycopy(arrf2, 0, arrf, 0, 4);
            }
            arrf2[n++] = (float)((double)f5 * Math.cos(f9 / 3.0f) - (double)f2);
            arrf2[n++] = (float)((double)f5 * Math.cos(((double)f9 + Math.PI * 2) / 3.0) - (double)f2);
            arrf2[n++] = (float)((double)f5 * Math.cos(((double)f9 - Math.PI * 2) / 3.0) - (double)f2);
            CubicCurve2D.fixRoots(arrf2, arrf);
        } else {
            boolean bl = f6 < 0.0f;
            float f10 = (float)Math.sqrt(f7 - f8);
            if (bl) {
                f6 = -f6;
            }
            float f11 = (float)Math.pow(f6 + f10, 0.3333333432674408);
            if (!bl) {
                f11 = -f11;
            }
            float f12 = f11 == 0.0f ? 0.0f : f5 / f11;
            arrf2[n++] = f11 + f12 - f2;
        }
        return n;
    }

    private static void fixRoots(float[] arrf, float[] arrf2) {
        for (int i = 0; i < 3; ++i) {
            float f = arrf[i];
            if (Math.abs(f) < 1.0E-5f) {
                arrf[i] = CubicCurve2D.findZero(f, 0.0f, arrf2);
                continue;
            }
            if (!(Math.abs(f - 1.0f) < 1.0E-5f)) continue;
            arrf[i] = CubicCurve2D.findZero(f, 1.0f, arrf2);
        }
    }

    private static float solveEqn(float[] arrf, int n, float f) {
        float f2 = arrf[n];
        while (--n >= 0) {
            f2 = f2 * f + arrf[n];
        }
        return f2;
    }

    private static float findZero(float f, float f2, float[] arrf) {
        float[] arrf2 = new float[]{arrf[1], 2.0f * arrf[2], 3.0f * arrf[3]};
        float f3 = 0.0f;
        float f4 = f;
        float f5;
        while ((f5 = CubicCurve2D.solveEqn(arrf2, 2, f)) != 0.0f) {
            float f6;
            float f7 = CubicCurve2D.solveEqn(arrf, 3, f);
            if (f7 == 0.0f) {
                return f;
            }
            float f8 = -(f7 / f5);
            if (f3 == 0.0f) {
                f3 = f8;
            }
            if (f < f2) {
                if (f8 < 0.0f) {
                    return f;
                }
            } else if (f > f2) {
                if (f8 > 0.0f) {
                    return f;
                }
            } else {
                return f8 > 0.0f ? f2 + Float.MIN_VALUE : f2 - Float.MIN_VALUE;
            }
            if (f == (f6 = f + f8)) {
                return f;
            }
            if (f8 * f3 < 0.0f) {
                int n;
                int n2 = n = f4 < f ? CubicCurve2D.getTag(f2, f4, f) : CubicCurve2D.getTag(f2, f, f4);
                if (n != 0) {
                    return (f4 + f) / 2.0f;
                }
                f = f2;
                continue;
            }
            f = f6;
        }
        return f;
    }

    @Override
    public boolean contains(float f, float f2) {
        if (f * 0.0f + f2 * 0.0f != 0.0f) {
            return false;
        }
        int n = Shape.pointCrossingsForLine(f, f2, this.x1, this.y1, this.x2, this.y2) + Shape.pointCrossingsForCubic(f, f2, this.x1, this.y1, this.ctrlx1, this.ctrly1, this.ctrlx2, this.ctrly2, this.x2, this.y2, 0);
        return (n & 1) == 1;
    }

    @Override
    public boolean contains(Point2D point2D) {
        return this.contains(point2D.x, point2D.y);
    }

    private static void fillEqn(float[] arrf, float f, float f2, float f3, float f4, float f5) {
        arrf[0] = f2 - f;
        arrf[1] = (f3 - f2) * 3.0f;
        arrf[2] = (f4 - f3 - f3 + f2) * 3.0f;
        arrf[3] = f5 + (f3 - f4) * 3.0f - f2;
    }

    private static int evalCubic(float[] arrf, int n, boolean bl, boolean bl2, float[] arrf2, float f, float f2, float f3, float f4) {
        int n2 = 0;
        for (int i = 0; i < n; ++i) {
            float f5 = arrf[i];
            if (!(bl ? f5 >= 0.0f : f5 > 0.0f) || !(bl2 ? f5 <= 1.0f : f5 < 1.0f) || arrf2 != null && arrf2[1] + (2.0f * arrf2[2] + 3.0f * arrf2[3] * f5) * f5 == 0.0f) continue;
            float f6 = 1.0f - f5;
            arrf[n2++] = f * f6 * f6 * f6 + 3.0f * f2 * f5 * f6 * f6 + 3.0f * f3 * f5 * f5 * f6 + f4 * f5 * f5 * f5;
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
        int n2 = CubicCurve2D.getTag(f5, f, f + f3);
        int n3 = CubicCurve2D.getTag(f6, f2, f2 + f4);
        if (n2 == 0 && n3 == 0) {
            return true;
        }
        float f7 = this.x2;
        float f8 = this.y2;
        int n4 = CubicCurve2D.getTag(f7, f, f + f3);
        int n5 = CubicCurve2D.getTag(f8, f2, f2 + f4);
        if (n4 == 0 && n5 == 0) {
            return true;
        }
        float f9 = this.ctrlx1;
        float f10 = this.ctrly1;
        float f11 = this.ctrlx2;
        float f12 = this.ctrly2;
        int n6 = CubicCurve2D.getTag(f9, f, f + f3);
        int n7 = CubicCurve2D.getTag(f10, f2, f2 + f4);
        int n8 = CubicCurve2D.getTag(f11, f, f + f3);
        int n9 = CubicCurve2D.getTag(f12, f2, f2 + f4);
        if (n2 < 0 && n4 < 0 && n6 < 0 && n8 < 0) {
            return false;
        }
        if (n3 < 0 && n5 < 0 && n7 < 0 && n9 < 0) {
            return false;
        }
        if (n2 > 0 && n4 > 0 && n6 > 0 && n8 > 0) {
            return false;
        }
        if (n3 > 0 && n5 > 0 && n7 > 0 && n9 > 0) {
            return false;
        }
        if (CubicCurve2D.inwards(n2, n4, n6) && CubicCurve2D.inwards(n3, n5, n7)) {
            return true;
        }
        if (CubicCurve2D.inwards(n4, n2, n8) && CubicCurve2D.inwards(n5, n3, n9)) {
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
        float[] arrf = new float[4];
        float[] arrf2 = new float[4];
        if (!bl) {
            CubicCurve2D.fillEqn(arrf, n3 < 0 ? f2 : f2 + f4, f6, f10, f12, f8);
            int n10 = CubicCurve2D.solveCubic(arrf, arrf2);
            n10 = CubicCurve2D.evalCubic(arrf2, n10, true, true, null, f5, f9, f11, f7);
            return n10 == 2 && CubicCurve2D.getTag(arrf2[0], f, f + f3) * CubicCurve2D.getTag(arrf2[1], f, f + f3) <= 0;
        }
        if (!bl2) {
            CubicCurve2D.fillEqn(arrf, n2 < 0 ? f : f + f3, f5, f9, f11, f7);
            int n11 = CubicCurve2D.solveCubic(arrf, arrf2);
            n11 = CubicCurve2D.evalCubic(arrf2, n11, true, true, null, f6, f10, f12, f8);
            return n11 == 2 && CubicCurve2D.getTag(arrf2[0], f2, f2 + f4) * CubicCurve2D.getTag(arrf2[1], f2, f2 + f4) <= 0;
        }
        float f13 = f7 - f5;
        float f14 = f8 - f6;
        float f15 = f8 * f5 - f7 * f6;
        int n12 = n3 == 0 ? n2 : CubicCurve2D.getTag((f15 + f13 * (n3 < 0 ? f2 : f2 + f4)) / f14, f, f + f3);
        if (n12 * (n = n5 == 0 ? n4 : CubicCurve2D.getTag((f15 + f13 * (n5 < 0 ? f2 : f2 + f4)) / f14, f, f + f3)) <= 0) {
            return true;
        }
        n12 = n12 * n2 <= 0 ? n3 : n5;
        CubicCurve2D.fillEqn(arrf, n < 0 ? f : f + f3, f5, f9, f11, f7);
        int n13 = CubicCurve2D.solveCubic(arrf, arrf2);
        n13 = CubicCurve2D.evalCubic(arrf2, n13, true, true, null, f6, f10, f12, f8);
        int[] arrn = new int[n13 + 1];
        for (int i = 0; i < n13; ++i) {
            arrn[i] = CubicCurve2D.getTag(arrf2[i], f2, f2 + f4);
        }
        arrn[n13] = n12;
        Arrays.sort(arrn);
        return n13 >= 1 && arrn[0] * arrn[1] <= 0 || n13 >= 3 && arrn[2] * arrn[3] <= 0;
    }

    @Override
    public boolean contains(float f, float f2, float f3, float f4) {
        if (f3 <= 0.0f || f4 <= 0.0f) {
            return false;
        }
        if (!(this.contains(f, f2) && this.contains(f + f3, f2) && this.contains(f + f3, f2 + f4) && this.contains(f, f2 + f4))) {
            return false;
        }
        return !Shape.intersectsLine(f, f2, f3, f4, this.x1, this.y1, this.x2, this.y2);
    }

    @Override
    public PathIterator getPathIterator(BaseTransform baseTransform) {
        return new CubicIterator(this, baseTransform);
    }

    @Override
    public PathIterator getPathIterator(BaseTransform baseTransform, float f) {
        return new FlatteningPathIterator(this.getPathIterator(baseTransform), f);
    }

    @Override
    public CubicCurve2D copy() {
        return new CubicCurve2D(this.x1, this.y1, this.ctrlx1, this.ctrly1, this.ctrlx2, this.ctrly2, this.x2, this.y2);
    }

    public int hashCode() {
        int n = Float.floatToIntBits(this.x1);
        n += Float.floatToIntBits(this.y1) * 37;
        n += Float.floatToIntBits(this.x2) * 43;
        n += Float.floatToIntBits(this.y2) * 47;
        n += Float.floatToIntBits(this.ctrlx1) * 53;
        n += Float.floatToIntBits(this.ctrly1) * 59;
        n += Float.floatToIntBits(this.ctrlx2) * 61;
        return n += Float.floatToIntBits(this.ctrly2) * 101;
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof CubicCurve2D) {
            CubicCurve2D cubicCurve2D = (CubicCurve2D)object;
            return this.x1 == cubicCurve2D.x1 && this.y1 == cubicCurve2D.y1 && this.x2 == cubicCurve2D.x2 && this.y2 == cubicCurve2D.y2 && this.ctrlx1 == cubicCurve2D.ctrlx1 && this.ctrly1 == cubicCurve2D.ctrly1 && this.ctrlx2 == cubicCurve2D.ctrlx2 && this.ctrly2 == cubicCurve2D.ctrly2;
        }
        return false;
    }

    private float calcX(float f) {
        float f2 = 1.0f - f;
        return f2 * f2 * f2 * this.x1 + 3.0f * (f * f2 * f2 * this.ctrlx1 + f * f * f2 * this.ctrlx2) + f * f * f * this.x2;
    }

    private float calcY(float f) {
        float f2 = 1.0f - f;
        return f2 * f2 * f2 * this.y1 + 3.0f * (f * f2 * f2 * this.ctrly1 + f * f * f2 * this.ctrly2) + f * f * f * this.y2;
    }
}

