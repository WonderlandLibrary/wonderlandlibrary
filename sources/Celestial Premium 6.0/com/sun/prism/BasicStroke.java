/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism;

import com.sun.javafx.geom.Area;
import com.sun.javafx.geom.GeneralShapePair;
import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.PathIterator;
import com.sun.javafx.geom.RoundRectangle2D;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.prism.impl.shape.ShapeUtil;
import java.util.Arrays;

public final class BasicStroke {
    public static final int CAP_BUTT = 0;
    public static final int CAP_ROUND = 1;
    public static final int CAP_SQUARE = 2;
    public static final int JOIN_MITER = 0;
    public static final int JOIN_ROUND = 1;
    public static final int JOIN_BEVEL = 2;
    public static final int TYPE_CENTERED = 0;
    public static final int TYPE_INNER = 1;
    public static final int TYPE_OUTER = 2;
    float width;
    int type;
    int cap;
    int join;
    float miterLimit;
    float[] dash;
    float dashPhase;
    private static final int SAFE_ACCUMULATE_MASK = 91;
    private float[] tmpMiter = new float[2];
    static final float SQRT_2 = (float)Math.sqrt(2.0);

    public BasicStroke() {
        this.set(0, 1.0f, 2, 0, 10.0f);
    }

    public BasicStroke(float f, int n, int n2, float f2) {
        this.set(0, f, n, n2, f2);
    }

    public BasicStroke(int n, float f, int n2, int n3, float f2) {
        this.set(n, f, n2, n3, f2);
    }

    public BasicStroke(float f, int n, int n2, float f2, float[] arrf, float f3) {
        this.set(0, f, n, n2, f2);
        this.set(arrf, f3);
    }

    public BasicStroke(float f, int n, int n2, float f2, double[] arrd, float f3) {
        this.set(0, f, n, n2, f2);
        this.set(arrd, f3);
    }

    public BasicStroke(int n, float f, int n2, int n3, float f2, float[] arrf, float f3) {
        this.set(n, f, n2, n3, f2);
        this.set(arrf, f3);
    }

    public BasicStroke(int n, float f, int n2, int n3, float f2, double[] arrd, float f3) {
        this.set(n, f, n2, n3, f2);
        this.set(arrd, f3);
    }

    public void set(int n, float f, int n2, int n3, float f2) {
        if (n != 0 && n != 1 && n != 2) {
            throw new IllegalArgumentException("illegal type");
        }
        if (f < 0.0f) {
            throw new IllegalArgumentException("negative width");
        }
        if (n2 != 0 && n2 != 1 && n2 != 2) {
            throw new IllegalArgumentException("illegal end cap value");
        }
        if (n3 == 0) {
            if (f2 < 1.0f) {
                throw new IllegalArgumentException("miter limit < 1");
            }
        } else if (n3 != 1 && n3 != 2) {
            throw new IllegalArgumentException("illegal line join value");
        }
        this.type = n;
        this.width = f;
        this.cap = n2;
        this.join = n3;
        this.miterLimit = f2;
    }

    public void set(float[] arrf, float f) {
        if (arrf != null) {
            boolean bl = true;
            for (int i = 0; i < arrf.length; ++i) {
                float f2 = arrf[i];
                if ((double)f2 > 0.0) {
                    bl = false;
                    continue;
                }
                if (!((double)f2 < 0.0)) continue;
                throw new IllegalArgumentException("negative dash length");
            }
            if (bl) {
                throw new IllegalArgumentException("dash lengths all zero");
            }
        }
        this.dash = arrf;
        this.dashPhase = f;
    }

    public void set(double[] arrd, float f) {
        if (arrd != null) {
            float[] arrf = new float[arrd.length];
            boolean bl = true;
            for (int i = 0; i < arrd.length; ++i) {
                float f2 = (float)arrd[i];
                if ((double)f2 > 0.0) {
                    bl = false;
                } else if ((double)f2 < 0.0) {
                    throw new IllegalArgumentException("negative dash length");
                }
                arrf[i] = f2;
            }
            if (bl) {
                throw new IllegalArgumentException("dash lengths all zero");
            }
            this.dash = arrf;
        } else {
            this.dash = null;
        }
        this.dashPhase = f;
    }

    public int getType() {
        return this.type;
    }

    public float getLineWidth() {
        return this.width;
    }

    public int getEndCap() {
        return this.cap;
    }

    public int getLineJoin() {
        return this.join;
    }

    public float getMiterLimit() {
        return this.miterLimit;
    }

    public boolean isDashed() {
        return this.dash != null;
    }

    public float[] getDashArray() {
        return this.dash;
    }

    public float getDashPhase() {
        return this.dashPhase;
    }

    public Shape createStrokedShape(Shape shape) {
        Shape shape2 = shape instanceof RoundRectangle2D ? this.strokeRoundRectangle((RoundRectangle2D)shape) : null;
        if (shape2 != null) {
            return shape2;
        }
        shape2 = this.createCenteredStrokedShape(shape);
        if (this.type == 1) {
            shape2 = this.makeIntersectedShape(shape2, shape);
        } else if (this.type == 2) {
            shape2 = this.makeSubtractedShape(shape2, shape);
        }
        return shape2;
    }

    private boolean isCW(float f, float f2, float f3, float f4) {
        return f * f4 <= f2 * f3;
    }

    private void computeOffset(float f, float f2, float f3, float[] arrf, int n) {
        float f4 = (float)Math.sqrt(f * f + f2 * f2);
        if (f4 == 0.0f) {
            arrf[n + 1] = 0.0f;
            arrf[n + 0] = 0.0f;
        } else {
            arrf[n + 0] = f2 * f3 / f4;
            arrf[n + 1] = -(f * f3) / f4;
        }
    }

    private void computeMiter(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float[] arrf, int n) {
        float f9 = f3 - f;
        float f10 = f4 - f2;
        float f11 = f7 - f5;
        float f12 = f8 - f6;
        float f13 = f9 * f12 - f11 * f10;
        float f14 = f11 * (f2 - f6) - f12 * (f - f5);
        arrf[n++] = f + (f14 /= f13) * f9;
        arrf[n] = f2 + f14 * f10;
    }

    private void accumulateQuad(float[] arrf, int n, float f, float f2, float f3, float f4) {
        float f5;
        float f6 = f - f2;
        float f7 = f3 - f2 + f6;
        if (f7 != 0.0f && (f5 = f6 / f7) > 0.0f && f5 < 1.0f) {
            float f8 = 1.0f - f5;
            float f9 = f * f8 * f8 + 2.0f * f2 * f5 * f8 + f3 * f5 * f5;
            if (arrf[n] > f9 - f4) {
                arrf[n] = f9 - f4;
            }
            if (arrf[n + 2] < f9 + f4) {
                arrf[n + 2] = f9 + f4;
            }
        }
    }

    private void accumulateCubic(float[] arrf, int n, float f, float f2, float f3, float f4, float f5, float f6) {
        if (f > 0.0f && f < 1.0f) {
            float f7 = 1.0f - f;
            float f8 = f2 * f7 * f7 * f7 + 3.0f * f3 * f * f7 * f7 + 3.0f * f4 * f * f * f7 + f5 * f * f * f;
            if (arrf[n] > f8 - f6) {
                arrf[n] = f8 - f6;
            }
            if (arrf[n + 2] < f8 + f6) {
                arrf[n + 2] = f8 + f6;
            }
        }
    }

    private void accumulateCubic(float[] arrf, int n, float f, float f2, float f3, float f4, float f5) {
        float f6 = f2 - f;
        float f7 = 2.0f * (f3 - f2 - f6);
        float f8 = f4 - f3 - f7 - f6;
        if (f8 == 0.0f) {
            if (f7 == 0.0f) {
                return;
            }
            this.accumulateCubic(arrf, n, -f6 / f7, f, f2, f3, f4, f5);
        } else {
            float f9 = f7 * f7 - 4.0f * f8 * f6;
            if (f9 < 0.0f) {
                return;
            }
            f9 = (float)Math.sqrt(f9);
            if (f7 < 0.0f) {
                f9 = -f9;
            }
            float f10 = (f7 + f9) / -2.0f;
            this.accumulateCubic(arrf, n, f10 / f8, f, f2, f3, f4, f5);
            if (f10 != 0.0f) {
                this.accumulateCubic(arrf, n, f6 / f10, f, f2, f3, f4, f5);
            }
        }
    }

    public void accumulateShapeBounds(float[] arrf, Shape shape, BaseTransform baseTransform) {
        if (this.type == 1) {
            Shape.accumulate(arrf, shape, baseTransform);
            return;
        }
        if ((baseTransform.getType() & 0xFFFFFFA4) != 0) {
            Shape.accumulate(arrf, this.createStrokedShape(shape), baseTransform);
            return;
        }
        PathIterator pathIterator = shape.getPathIterator(baseTransform);
        boolean bl = true;
        float[] arrf2 = new float[6];
        float f = this.type == 0 ? this.getLineWidth() / 2.0f : this.getLineWidth();
        f = (float)((double)f * Math.hypot(baseTransform.getMxx(), baseTransform.getMyx()));
        float f2 = 0.0f;
        float f3 = 0.0f;
        float f4 = 0.0f;
        float f5 = 0.0f;
        float f6 = 0.0f;
        float f7 = 0.0f;
        float f8 = 0.0f;
        float f9 = 0.0f;
        float[] arrf3 = new float[4];
        float f10 = 0.0f;
        float f11 = 0.0f;
        float f12 = 0.0f;
        float f13 = 0.0f;
        while (!pathIterator.isDone()) {
            int n = pathIterator.currentSegment(arrf2);
            switch (n) {
                case 0: {
                    if (!bl) {
                        this.accumulateCap(f8, f9, f4, f5, f10, f11, arrf, f);
                        this.accumulateCap(-f6, -f7, f2, f3, -f12, -f13, arrf, f);
                    }
                    f4 = f2 = arrf2[0];
                    f5 = f3 = arrf2[1];
                    break;
                }
                case 1: {
                    float f14 = arrf2[0];
                    float f15 = arrf2[1];
                    float f16 = f14 - f4;
                    float f17 = f15 - f5;
                    if (f16 == 0.0f && f17 == 0.0f) {
                        f16 = 1.0f;
                    }
                    this.computeOffset(f16, f17, f, arrf3, 0);
                    if (!bl) {
                        this.accumulateJoin(f8, f9, f16, f17, f4, f5, f10, f11, arrf3[0], arrf3[1], arrf, f);
                    }
                    f4 = f14;
                    f5 = f15;
                    f8 = f16;
                    f9 = f17;
                    f10 = arrf3[0];
                    f11 = arrf3[1];
                    if (!bl) break;
                    f6 = f8;
                    f7 = f9;
                    f12 = f10;
                    f13 = f11;
                    break;
                }
                case 2: {
                    float f14 = arrf2[2];
                    float f15 = arrf2[3];
                    float f16 = arrf2[0] - f4;
                    float f17 = arrf2[1] - f5;
                    this.computeOffset(f16, f17, f, arrf3, 0);
                    if (!bl) {
                        this.accumulateJoin(f8, f9, f16, f17, f4, f5, f10, f11, arrf3[0], arrf3[1], arrf, f);
                    }
                    if (arrf[0] > arrf2[0] - f || arrf[2] < arrf2[0] + f) {
                        this.accumulateQuad(arrf, 0, f4, arrf2[0], f14, f);
                    }
                    if (arrf[1] > arrf2[1] - f || arrf[3] < arrf2[1] + f) {
                        this.accumulateQuad(arrf, 1, f5, arrf2[1], f15, f);
                    }
                    f4 = f14;
                    f5 = f15;
                    if (bl) {
                        f6 = f16;
                        f7 = f17;
                        f12 = arrf3[0];
                        f13 = arrf3[1];
                    }
                    f8 = f14 - arrf2[0];
                    f9 = f15 - arrf2[1];
                    this.computeOffset(f8, f9, f, arrf3, 0);
                    f10 = arrf3[0];
                    f11 = arrf3[1];
                    break;
                }
                case 3: {
                    float f14 = arrf2[4];
                    float f15 = arrf2[5];
                    float f16 = arrf2[0] - f4;
                    float f17 = arrf2[1] - f5;
                    this.computeOffset(f16, f17, f, arrf3, 0);
                    if (!bl) {
                        this.accumulateJoin(f8, f9, f16, f17, f4, f5, f10, f11, arrf3[0], arrf3[1], arrf, f);
                    }
                    if (arrf[0] > arrf2[0] - f || arrf[2] < arrf2[0] + f || arrf[0] > arrf2[2] - f || arrf[2] < arrf2[2] + f) {
                        this.accumulateCubic(arrf, 0, f4, arrf2[0], arrf2[2], f14, f);
                    }
                    if (arrf[1] > arrf2[1] - f || arrf[3] < arrf2[1] + f || arrf[1] > arrf2[3] - f || arrf[3] < arrf2[3] + f) {
                        this.accumulateCubic(arrf, 1, f5, arrf2[1], arrf2[3], f15, f);
                    }
                    f4 = f14;
                    f5 = f15;
                    if (bl) {
                        f6 = f16;
                        f7 = f17;
                        f12 = arrf3[0];
                        f13 = arrf3[1];
                    }
                    f8 = f14 - arrf2[2];
                    f9 = f15 - arrf2[3];
                    this.computeOffset(f8, f9, f, arrf3, 0);
                    f10 = arrf3[0];
                    f11 = arrf3[1];
                    break;
                }
                case 4: {
                    float f16 = f2 - f4;
                    float f17 = f3 - f5;
                    float f14 = f2;
                    float f15 = f3;
                    if (!bl) {
                        this.computeOffset(f6, f7, f, arrf3, 2);
                        if (f16 == 0.0f && f17 == 0.0f) {
                            this.accumulateJoin(f8, f9, f6, f7, f2, f3, f10, f11, arrf3[2], arrf3[3], arrf, f);
                        } else {
                            this.computeOffset(f16, f17, f, arrf3, 0);
                            this.accumulateJoin(f8, f9, f16, f17, f4, f5, f10, f11, arrf3[0], arrf3[1], arrf, f);
                            this.accumulateJoin(f16, f17, f6, f7, f14, f15, arrf3[0], arrf3[1], arrf3[2], arrf3[3], arrf, f);
                        }
                    }
                    f4 = f14;
                    f5 = f15;
                }
            }
            bl = n == 0 || n == 4;
            pathIterator.next();
        }
        if (!bl) {
            this.accumulateCap(f8, f9, f4, f5, f10, f11, arrf, f);
            this.accumulateCap(-f6, -f7, f2, f3, -f12, -f13, arrf, f);
        }
    }

    private void accumulate(float f, float f2, float f3, float f4, float[] arrf) {
        if (f <= f3) {
            if (f < arrf[0]) {
                arrf[0] = f;
            }
            if (f3 > arrf[2]) {
                arrf[2] = f3;
            }
        } else {
            if (f3 < arrf[0]) {
                arrf[0] = f3;
            }
            if (f > arrf[2]) {
                arrf[2] = f;
            }
        }
        if (f2 <= f4) {
            if (f2 < arrf[1]) {
                arrf[1] = f2;
            }
            if (f4 > arrf[3]) {
                arrf[3] = f4;
            }
        } else {
            if (f4 < arrf[1]) {
                arrf[1] = f4;
            }
            if (f2 > arrf[3]) {
                arrf[3] = f2;
            }
        }
    }

    private void accumulateOrdered(float f, float f2, float f3, float f4, float[] arrf) {
        if (f < arrf[0]) {
            arrf[0] = f;
        }
        if (f3 > arrf[2]) {
            arrf[2] = f3;
        }
        if (f2 < arrf[1]) {
            arrf[1] = f2;
        }
        if (f4 > arrf[3]) {
            arrf[3] = f4;
        }
    }

    private void accumulateJoin(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float[] arrf, float f11) {
        if (this.join == 2) {
            this.accumulateBevel(f5, f6, f7, f8, f9, f10, arrf);
        } else if (this.join == 0) {
            this.accumulateMiter(f, f2, f3, f4, f7, f8, f9, f10, f5, f6, arrf, f11);
        } else {
            this.accumulateOrdered(f5 - f11, f6 - f11, f5 + f11, f6 + f11, arrf);
        }
    }

    private void accumulateCap(float f, float f2, float f3, float f4, float f5, float f6, float[] arrf, float f7) {
        if (this.cap == 2) {
            this.accumulate(f3 + f5 - f6, f4 + f6 + f5, f3 - f5 - f6, f4 - f6 + f5, arrf);
        } else if (this.cap == 0) {
            this.accumulate(f3 + f5, f4 + f6, f3 - f5, f4 - f6, arrf);
        } else {
            this.accumulateOrdered(f3 - f7, f4 - f7, f3 + f7, f4 + f7, arrf);
        }
    }

    private void accumulateMiter(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float[] arrf, float f11) {
        this.accumulateBevel(f9, f10, f5, f6, f7, f8, arrf);
        boolean bl = this.isCW(f, f2, f3, f4);
        if (bl) {
            f5 = -f5;
            f6 = -f6;
            f7 = -f7;
            f8 = -f8;
        }
        this.computeMiter(f9 - f + f5, f10 - f2 + f6, f9 + f5, f10 + f6, f9 + f3 + f7, f10 + f4 + f8, f9 + f7, f10 + f8, this.tmpMiter, 0);
        float f12 = (this.tmpMiter[0] - f9) * (this.tmpMiter[0] - f9) + (this.tmpMiter[1] - f10) * (this.tmpMiter[1] - f10);
        float f13 = this.miterLimit * f11;
        if (f12 < f13 * f13) {
            this.accumulateOrdered(this.tmpMiter[0], this.tmpMiter[1], this.tmpMiter[0], this.tmpMiter[1], arrf);
        }
    }

    private void accumulateBevel(float f, float f2, float f3, float f4, float f5, float f6, float[] arrf) {
        this.accumulate(f + f3, f2 + f4, f - f3, f2 - f4, arrf);
        this.accumulate(f + f5, f2 + f6, f - f5, f2 - f6, arrf);
    }

    public Shape createCenteredStrokedShape(Shape shape) {
        return ShapeUtil.createCenteredStrokedShape(shape, this);
    }

    Shape strokeRoundRectangle(RoundRectangle2D roundRectangle2D) {
        Shape shape;
        float f;
        float f2;
        int n;
        if (roundRectangle2D.width < 0.0f || roundRectangle2D.height < 0.0f) {
            return new Path2D();
        }
        if (this.isDashed()) {
            return null;
        }
        float f3 = roundRectangle2D.arcWidth;
        float f4 = roundRectangle2D.arcHeight;
        if (f3 <= 0.0f || f4 <= 0.0f) {
            f4 = 0.0f;
            f3 = 0.0f;
            if (this.type == 1) {
                n = 0;
            } else {
                n = this.join;
                if (n == 0 && this.miterLimit < SQRT_2) {
                    n = 2;
                }
            }
        } else {
            if (f3 < f4 * 0.9f || f4 < f3 * 0.9f) {
                return null;
            }
            n = 1;
        }
        if (this.type == 1) {
            f2 = 0.0f;
            f = this.width;
        } else if (this.type == 2) {
            f2 = this.width;
            f = 0.0f;
        } else {
            f2 = f = this.width / 2.0f;
        }
        switch (n) {
            case 0: {
                shape = new RoundRectangle2D(roundRectangle2D.x - f2, roundRectangle2D.y - f2, roundRectangle2D.width + f2 * 2.0f, roundRectangle2D.height + f2 * 2.0f, 0.0f, 0.0f);
                break;
            }
            case 2: {
                shape = BasicStroke.makeBeveledRect(roundRectangle2D.x, roundRectangle2D.y, roundRectangle2D.width, roundRectangle2D.height, f2);
                break;
            }
            case 1: {
                shape = new RoundRectangle2D(roundRectangle2D.x - f2, roundRectangle2D.y - f2, roundRectangle2D.width + f2 * 2.0f, roundRectangle2D.height + f2 * 2.0f, f3 + f2 * 2.0f, f4 + f2 * 2.0f);
                break;
            }
            default: {
                throw new InternalError("Unrecognized line join style");
            }
        }
        if (roundRectangle2D.width <= f * 2.0f || roundRectangle2D.height <= f * 2.0f) {
            return shape;
        }
        f4 -= f * 2.0f;
        if ((f3 -= f * 2.0f) <= 0.0f || f4 <= 0.0f) {
            f4 = 0.0f;
            f3 = 0.0f;
        }
        RoundRectangle2D roundRectangle2D2 = new RoundRectangle2D(roundRectangle2D.x + f, roundRectangle2D.y + f, roundRectangle2D.width - f * 2.0f, roundRectangle2D.height - f * 2.0f, f3, f4);
        Path2D path2D = shape instanceof Path2D ? (Path2D)shape : new Path2D(shape);
        path2D.setWindingRule(0);
        path2D.append(roundRectangle2D2, false);
        return path2D;
    }

    static Shape makeBeveledRect(float f, float f2, float f3, float f4, float f5) {
        float f6 = f;
        float f7 = f2;
        float f8 = f + f3;
        float f9 = f2 + f4;
        Path2D path2D = new Path2D();
        path2D.moveTo(f6, f7 - f5);
        path2D.lineTo(f8, f7 - f5);
        path2D.lineTo(f8 + f5, f7);
        path2D.lineTo(f8 + f5, f9);
        path2D.lineTo(f8, f9 + f5);
        path2D.lineTo(f6, f9 + f5);
        path2D.lineTo(f6 - f5, f9);
        path2D.lineTo(f6 - f5, f7);
        path2D.closePath();
        return path2D;
    }

    protected Shape makeIntersectedShape(Shape shape, Shape shape2) {
        return new CAGShapePair(shape, shape2, 4);
    }

    protected Shape makeSubtractedShape(Shape shape, Shape shape2) {
        return new CAGShapePair(shape, shape2, 1);
    }

    public int hashCode() {
        int n = Float.floatToIntBits(this.width);
        n = n * 31 + this.join;
        n = n * 31 + this.cap;
        n = n * 31 + Float.floatToIntBits(this.miterLimit);
        if (this.dash != null) {
            n = n * 31 + Float.floatToIntBits(this.dashPhase);
            for (int i = 0; i < this.dash.length; ++i) {
                n = n * 31 + Float.floatToIntBits(this.dash[i]);
            }
        }
        return n;
    }

    public boolean equals(Object object) {
        if (!(object instanceof BasicStroke)) {
            return false;
        }
        BasicStroke basicStroke = (BasicStroke)object;
        if (this.width != basicStroke.width) {
            return false;
        }
        if (this.join != basicStroke.join) {
            return false;
        }
        if (this.cap != basicStroke.cap) {
            return false;
        }
        if (this.miterLimit != basicStroke.miterLimit) {
            return false;
        }
        if (this.dash != null) {
            if (this.dashPhase != basicStroke.dashPhase) {
                return false;
            }
            if (!Arrays.equals(this.dash, basicStroke.dash)) {
                return false;
            }
        } else if (basicStroke.dash != null) {
            return false;
        }
        return true;
    }

    public BasicStroke copy() {
        return new BasicStroke(this.type, this.width, this.cap, this.join, this.miterLimit, this.dash, this.dashPhase);
    }

    static class CAGShapePair
    extends GeneralShapePair {
        private Shape cagshape;

        public CAGShapePair(Shape shape, Shape shape2, int n) {
            super(shape, shape2, n);
        }

        @Override
        public PathIterator getPathIterator(BaseTransform baseTransform) {
            if (this.cagshape == null) {
                Area area = new Area(this.getOuterShape());
                Area area2 = new Area(this.getInnerShape());
                if (this.getCombinationType() == 4) {
                    area.intersect(area2);
                } else {
                    area.subtract(area2);
                }
                this.cagshape = area;
            }
            return this.cagshape.getPathIterator(baseTransform);
        }
    }
}

