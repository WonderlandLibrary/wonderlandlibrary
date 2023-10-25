/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.geom;

import com.sun.javafx.geom.IllegalPathStateException;
import com.sun.javafx.geom.PathIterator;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.transform.BaseTransform;

public abstract class Shape {
    public static final int RECT_INTERSECTS = Integer.MIN_VALUE;
    public static final int OUT_LEFT = 1;
    public static final int OUT_TOP = 2;
    public static final int OUT_RIGHT = 4;
    public static final int OUT_BOTTOM = 8;

    public abstract RectBounds getBounds();

    public abstract boolean contains(float var1, float var2);

    public boolean contains(Point2D point2D) {
        return this.contains(point2D.x, point2D.y);
    }

    public abstract boolean intersects(float var1, float var2, float var3, float var4);

    public boolean intersects(RectBounds rectBounds) {
        float f = rectBounds.getMinX();
        float f2 = rectBounds.getMinY();
        float f3 = rectBounds.getMaxX() - f;
        float f4 = rectBounds.getMaxY() - f2;
        return this.intersects(f, f2, f3, f4);
    }

    public abstract boolean contains(float var1, float var2, float var3, float var4);

    public boolean contains(RectBounds rectBounds) {
        float f = rectBounds.getMinX();
        float f2 = rectBounds.getMinY();
        float f3 = rectBounds.getMaxX() - f;
        float f4 = rectBounds.getMaxY() - f2;
        return this.contains(f, f2, f3, f4);
    }

    public abstract PathIterator getPathIterator(BaseTransform var1);

    public abstract PathIterator getPathIterator(BaseTransform var1, float var2);

    public abstract Shape copy();

    public static int pointCrossingsForPath(PathIterator pathIterator, float f, float f2) {
        if (pathIterator.isDone()) {
            return 0;
        }
        float[] arrf = new float[6];
        if (pathIterator.currentSegment(arrf) != 0) {
            throw new IllegalPathStateException("missing initial moveto in path definition");
        }
        pathIterator.next();
        float f3 = arrf[0];
        float f4 = arrf[1];
        float f5 = f3;
        float f6 = f4;
        int n = 0;
        while (!pathIterator.isDone()) {
            switch (pathIterator.currentSegment(arrf)) {
                case 0: {
                    if (f6 != f4) {
                        n += Shape.pointCrossingsForLine(f, f2, f5, f6, f3, f4);
                    }
                    f3 = f5 = arrf[0];
                    f4 = f6 = arrf[1];
                    break;
                }
                case 1: {
                    float f7 = arrf[0];
                    float f8 = arrf[1];
                    n += Shape.pointCrossingsForLine(f, f2, f5, f6, f7, f8);
                    f5 = f7;
                    f6 = f8;
                    break;
                }
                case 2: {
                    float f7 = arrf[2];
                    float f8 = arrf[3];
                    n += Shape.pointCrossingsForQuad(f, f2, f5, f6, arrf[0], arrf[1], f7, f8, 0);
                    f5 = f7;
                    f6 = f8;
                    break;
                }
                case 3: {
                    float f7 = arrf[4];
                    float f8 = arrf[5];
                    n += Shape.pointCrossingsForCubic(f, f2, f5, f6, arrf[0], arrf[1], arrf[2], arrf[3], f7, f8, 0);
                    f5 = f7;
                    f6 = f8;
                    break;
                }
                case 4: {
                    if (f6 != f4) {
                        n += Shape.pointCrossingsForLine(f, f2, f5, f6, f3, f4);
                    }
                    f5 = f3;
                    f6 = f4;
                }
            }
            pathIterator.next();
        }
        if (f6 != f4) {
            n += Shape.pointCrossingsForLine(f, f2, f5, f6, f3, f4);
        }
        return n;
    }

    public static int pointCrossingsForLine(float f, float f2, float f3, float f4, float f5, float f6) {
        if (f2 < f4 && f2 < f6) {
            return 0;
        }
        if (f2 >= f4 && f2 >= f6) {
            return 0;
        }
        if (f >= f3 && f >= f5) {
            return 0;
        }
        if (f < f3 && f < f5) {
            return f4 < f6 ? 1 : -1;
        }
        float f7 = f3 + (f2 - f4) * (f5 - f3) / (f6 - f4);
        if (f >= f7) {
            return 0;
        }
        return f4 < f6 ? 1 : -1;
    }

    public static int pointCrossingsForQuad(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, int n) {
        if (f2 < f4 && f2 < f6 && f2 < f8) {
            return 0;
        }
        if (f2 >= f4 && f2 >= f6 && f2 >= f8) {
            return 0;
        }
        if (f >= f3 && f >= f5 && f >= f7) {
            return 0;
        }
        if (f < f3 && f < f5 && f < f7) {
            if (f2 >= f4) {
                if (f2 < f8) {
                    return 1;
                }
            } else if (f2 >= f8) {
                return -1;
            }
            return 0;
        }
        if (n > 52) {
            return Shape.pointCrossingsForLine(f, f2, f3, f4, f7, f8);
        }
        float f9 = (f3 + f5) / 2.0f;
        float f10 = (f4 + f6) / 2.0f;
        float f11 = (f5 + f7) / 2.0f;
        float f12 = (f6 + f8) / 2.0f;
        f5 = (f9 + f11) / 2.0f;
        f6 = (f10 + f12) / 2.0f;
        if (Float.isNaN(f5) || Float.isNaN(f6)) {
            return 0;
        }
        return Shape.pointCrossingsForQuad(f, f2, f3, f4, f9, f10, f5, f6, n + 1) + Shape.pointCrossingsForQuad(f, f2, f5, f6, f11, f12, f7, f8, n + 1);
    }

    public static int pointCrossingsForCubic(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, int n) {
        if (f2 < f4 && f2 < f6 && f2 < f8 && f2 < f10) {
            return 0;
        }
        if (f2 >= f4 && f2 >= f6 && f2 >= f8 && f2 >= f10) {
            return 0;
        }
        if (f >= f3 && f >= f5 && f >= f7 && f >= f9) {
            return 0;
        }
        if (f < f3 && f < f5 && f < f7 && f < f9) {
            if (f2 >= f4) {
                if (f2 < f10) {
                    return 1;
                }
            } else if (f2 >= f10) {
                return -1;
            }
            return 0;
        }
        if (n > 52) {
            return Shape.pointCrossingsForLine(f, f2, f3, f4, f9, f10);
        }
        float f11 = (f5 + f7) / 2.0f;
        float f12 = (f6 + f8) / 2.0f;
        f5 = (f3 + f5) / 2.0f;
        f6 = (f4 + f6) / 2.0f;
        f7 = (f7 + f9) / 2.0f;
        f8 = (f8 + f10) / 2.0f;
        float f13 = (f5 + f11) / 2.0f;
        float f14 = (f6 + f12) / 2.0f;
        float f15 = (f11 + f7) / 2.0f;
        float f16 = (f12 + f8) / 2.0f;
        f11 = (f13 + f15) / 2.0f;
        f12 = (f14 + f16) / 2.0f;
        if (Float.isNaN(f11) || Float.isNaN(f12)) {
            return 0;
        }
        return Shape.pointCrossingsForCubic(f, f2, f3, f4, f5, f6, f13, f14, f11, f12, n + 1) + Shape.pointCrossingsForCubic(f, f2, f11, f12, f15, f16, f7, f8, f9, f10, n + 1);
    }

    public static int rectCrossingsForPath(PathIterator pathIterator, float f, float f2, float f3, float f4) {
        float f5;
        float f6;
        if (f3 <= f || f4 <= f2) {
            return 0;
        }
        if (pathIterator.isDone()) {
            return 0;
        }
        float[] arrf = new float[6];
        if (pathIterator.currentSegment(arrf) != 0) {
            throw new IllegalPathStateException("missing initial moveto in path definition");
        }
        pathIterator.next();
        float f7 = f6 = arrf[0];
        float f8 = f5 = arrf[1];
        int n = 0;
        while (n != Integer.MIN_VALUE && !pathIterator.isDone()) {
            switch (pathIterator.currentSegment(arrf)) {
                case 0: {
                    if (f7 != f6 || f8 != f5) {
                        n = Shape.rectCrossingsForLine(n, f, f2, f3, f4, f7, f8, f6, f5);
                    }
                    f6 = f7 = arrf[0];
                    f5 = f8 = arrf[1];
                    break;
                }
                case 1: {
                    float f9 = arrf[0];
                    float f10 = arrf[1];
                    n = Shape.rectCrossingsForLine(n, f, f2, f3, f4, f7, f8, f9, f10);
                    f7 = f9;
                    f8 = f10;
                    break;
                }
                case 2: {
                    float f9 = arrf[2];
                    float f10 = arrf[3];
                    n = Shape.rectCrossingsForQuad(n, f, f2, f3, f4, f7, f8, arrf[0], arrf[1], f9, f10, 0);
                    f7 = f9;
                    f8 = f10;
                    break;
                }
                case 3: {
                    float f9 = arrf[4];
                    float f10 = arrf[5];
                    n = Shape.rectCrossingsForCubic(n, f, f2, f3, f4, f7, f8, arrf[0], arrf[1], arrf[2], arrf[3], f9, f10, 0);
                    f7 = f9;
                    f8 = f10;
                    break;
                }
                case 4: {
                    if (f7 != f6 || f8 != f5) {
                        n = Shape.rectCrossingsForLine(n, f, f2, f3, f4, f7, f8, f6, f5);
                    }
                    f7 = f6;
                    f8 = f5;
                }
            }
            pathIterator.next();
        }
        if (n != Integer.MIN_VALUE && (f7 != f6 || f8 != f5)) {
            n = Shape.rectCrossingsForLine(n, f, f2, f3, f4, f7, f8, f6, f5);
        }
        return n;
    }

    public static int rectCrossingsForLine(int n, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        if (f6 >= f4 && f8 >= f4) {
            return n;
        }
        if (f6 <= f2 && f8 <= f2) {
            return n;
        }
        if (f5 <= f && f7 <= f) {
            return n;
        }
        if (f5 >= f3 && f7 >= f3) {
            if (f6 < f8) {
                if (f6 <= f2) {
                    ++n;
                }
                if (f8 >= f4) {
                    ++n;
                }
            } else if (f8 < f6) {
                if (f8 <= f2) {
                    --n;
                }
                if (f6 >= f4) {
                    --n;
                }
            }
            return n;
        }
        if (f5 > f && f5 < f3 && f6 > f2 && f6 < f4 || f7 > f && f7 < f3 && f8 > f2 && f8 < f4) {
            return Integer.MIN_VALUE;
        }
        float f9 = f5;
        if (f6 < f2) {
            f9 += (f2 - f6) * (f7 - f5) / (f8 - f6);
        } else if (f6 > f4) {
            f9 += (f4 - f6) * (f7 - f5) / (f8 - f6);
        }
        float f10 = f7;
        if (f8 < f2) {
            f10 += (f2 - f8) * (f5 - f7) / (f6 - f8);
        } else if (f8 > f4) {
            f10 += (f4 - f8) * (f5 - f7) / (f6 - f8);
        }
        if (f9 <= f && f10 <= f) {
            return n;
        }
        if (f9 >= f3 && f10 >= f3) {
            if (f6 < f8) {
                if (f6 <= f2) {
                    ++n;
                }
                if (f8 >= f4) {
                    ++n;
                }
            } else if (f8 < f6) {
                if (f8 <= f2) {
                    --n;
                }
                if (f6 >= f4) {
                    --n;
                }
            }
            return n;
        }
        return Integer.MIN_VALUE;
    }

    public static int rectCrossingsForQuad(int n, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, int n2) {
        if (f6 >= f4 && f8 >= f4 && f10 >= f4) {
            return n;
        }
        if (f6 <= f2 && f8 <= f2 && f10 <= f2) {
            return n;
        }
        if (f5 <= f && f7 <= f && f9 <= f) {
            return n;
        }
        if (f5 >= f3 && f7 >= f3 && f9 >= f3) {
            if (f6 < f10) {
                if (f6 <= f2 && f10 > f2) {
                    ++n;
                }
                if (f6 < f4 && f10 >= f4) {
                    ++n;
                }
            } else if (f10 < f6) {
                if (f10 <= f2 && f6 > f2) {
                    --n;
                }
                if (f10 < f4 && f6 >= f4) {
                    --n;
                }
            }
            return n;
        }
        if (f5 < f3 && f5 > f && f6 < f4 && f6 > f2 || f9 < f3 && f9 > f && f10 < f4 && f10 > f2) {
            return Integer.MIN_VALUE;
        }
        if (n2 > 52) {
            return Shape.rectCrossingsForLine(n, f, f2, f3, f4, f5, f6, f9, f10);
        }
        float f11 = (f5 + f7) / 2.0f;
        float f12 = (f6 + f8) / 2.0f;
        float f13 = (f7 + f9) / 2.0f;
        float f14 = (f8 + f10) / 2.0f;
        f7 = (f11 + f13) / 2.0f;
        f8 = (f12 + f14) / 2.0f;
        if (Float.isNaN(f7) || Float.isNaN(f8)) {
            return 0;
        }
        if ((n = Shape.rectCrossingsForQuad(n, f, f2, f3, f4, f5, f6, f11, f12, f7, f8, n2 + 1)) != Integer.MIN_VALUE) {
            n = Shape.rectCrossingsForQuad(n, f, f2, f3, f4, f7, f8, f13, f14, f9, f10, n2 + 1);
        }
        return n;
    }

    public static int rectCrossingsForCubic(int n, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, int n2) {
        if (f6 >= f4 && f8 >= f4 && f10 >= f4 && f12 >= f4) {
            return n;
        }
        if (f6 <= f2 && f8 <= f2 && f10 <= f2 && f12 <= f2) {
            return n;
        }
        if (f5 <= f && f7 <= f && f9 <= f && f11 <= f) {
            return n;
        }
        if (f5 >= f3 && f7 >= f3 && f9 >= f3 && f11 >= f3) {
            if (f6 < f12) {
                if (f6 <= f2 && f12 > f2) {
                    ++n;
                }
                if (f6 < f4 && f12 >= f4) {
                    ++n;
                }
            } else if (f12 < f6) {
                if (f12 <= f2 && f6 > f2) {
                    --n;
                }
                if (f12 < f4 && f6 >= f4) {
                    --n;
                }
            }
            return n;
        }
        if (f5 > f && f5 < f3 && f6 > f2 && f6 < f4 || f11 > f && f11 < f3 && f12 > f2 && f12 < f4) {
            return Integer.MIN_VALUE;
        }
        if (n2 > 52) {
            return Shape.rectCrossingsForLine(n, f, f2, f3, f4, f5, f6, f11, f12);
        }
        float f13 = (f7 + f9) / 2.0f;
        float f14 = (f8 + f10) / 2.0f;
        f7 = (f5 + f7) / 2.0f;
        f8 = (f6 + f8) / 2.0f;
        f9 = (f9 + f11) / 2.0f;
        f10 = (f10 + f12) / 2.0f;
        float f15 = (f7 + f13) / 2.0f;
        float f16 = (f8 + f14) / 2.0f;
        float f17 = (f13 + f9) / 2.0f;
        float f18 = (f14 + f10) / 2.0f;
        f13 = (f15 + f17) / 2.0f;
        f14 = (f16 + f18) / 2.0f;
        if (Float.isNaN(f13) || Float.isNaN(f14)) {
            return 0;
        }
        if ((n = Shape.rectCrossingsForCubic(n, f, f2, f3, f4, f5, f6, f7, f8, f15, f16, f13, f14, n2 + 1)) != Integer.MIN_VALUE) {
            n = Shape.rectCrossingsForCubic(n, f, f2, f3, f4, f13, f14, f17, f18, f9, f10, f11, f12, n2 + 1);
        }
        return n;
    }

    static boolean intersectsLine(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        int n;
        int n2 = Shape.outcode(f, f2, f3, f4, f7, f8);
        if (n2 == 0) {
            return true;
        }
        while ((n = Shape.outcode(f, f2, f3, f4, f5, f6)) != 0) {
            if ((n & n2) != 0) {
                return false;
            }
            if ((n & 5) != 0) {
                if ((n & 4) != 0) {
                    f += f3;
                }
                f6 += (f - f5) * (f8 - f6) / (f7 - f5);
                f5 = f;
                continue;
            }
            if ((n & 8) != 0) {
                f2 += f4;
            }
            f5 += (f2 - f6) * (f7 - f5) / (f8 - f6);
            f6 = f2;
        }
        return true;
    }

    static int outcode(float f, float f2, float f3, float f4, float f5, float f6) {
        int n = 0;
        if (f3 <= 0.0f) {
            n |= 5;
        } else if (f5 < f) {
            n |= 1;
        } else if ((double)f5 > (double)f + (double)f3) {
            n |= 4;
        }
        if (f4 <= 0.0f) {
            n |= 0xA;
        } else if (f6 < f2) {
            n |= 2;
        } else if ((double)f6 > (double)f2 + (double)f4) {
            n |= 8;
        }
        return n;
    }

    public static void accumulate(float[] arrf, Shape shape, BaseTransform baseTransform) {
        PathIterator pathIterator = shape.getPathIterator(baseTransform);
        float[] arrf2 = new float[6];
        float f = 0.0f;
        float f2 = 0.0f;
        float f3 = 0.0f;
        float f4 = 0.0f;
        while (!pathIterator.isDone()) {
            switch (pathIterator.currentSegment(arrf2)) {
                case 0: {
                    f = arrf2[0];
                    f2 = arrf2[1];
                }
                case 1: {
                    f3 = arrf2[0];
                    f4 = arrf2[1];
                    if (arrf[0] > f3) {
                        arrf[0] = f3;
                    }
                    if (arrf[1] > f4) {
                        arrf[1] = f4;
                    }
                    if (arrf[2] < f3) {
                        arrf[2] = f3;
                    }
                    if (!(arrf[3] < f4)) break;
                    arrf[3] = f4;
                    break;
                }
                case 2: {
                    float f5 = arrf2[2];
                    float f6 = arrf2[3];
                    if (arrf[0] > f5) {
                        arrf[0] = f5;
                    }
                    if (arrf[1] > f6) {
                        arrf[1] = f6;
                    }
                    if (arrf[2] < f5) {
                        arrf[2] = f5;
                    }
                    if (arrf[3] < f6) {
                        arrf[3] = f6;
                    }
                    if (arrf[0] > arrf2[0] || arrf[2] < arrf2[0]) {
                        Shape.accumulateQuad(arrf, 0, f3, arrf2[0], f5);
                    }
                    if (arrf[1] > arrf2[1] || arrf[3] < arrf2[1]) {
                        Shape.accumulateQuad(arrf, 1, f4, arrf2[1], f6);
                    }
                    f3 = f5;
                    f4 = f6;
                    break;
                }
                case 3: {
                    float f5 = arrf2[4];
                    float f6 = arrf2[5];
                    if (arrf[0] > f5) {
                        arrf[0] = f5;
                    }
                    if (arrf[1] > f6) {
                        arrf[1] = f6;
                    }
                    if (arrf[2] < f5) {
                        arrf[2] = f5;
                    }
                    if (arrf[3] < f6) {
                        arrf[3] = f6;
                    }
                    if (arrf[0] > arrf2[0] || arrf[2] < arrf2[0] || arrf[0] > arrf2[2] || arrf[2] < arrf2[2]) {
                        Shape.accumulateCubic(arrf, 0, f3, arrf2[0], arrf2[2], f5);
                    }
                    if (arrf[1] > arrf2[1] || arrf[3] < arrf2[1] || arrf[1] > arrf2[3] || arrf[3] < arrf2[3]) {
                        Shape.accumulateCubic(arrf, 1, f4, arrf2[1], arrf2[3], f6);
                    }
                    f3 = f5;
                    f4 = f6;
                    break;
                }
                case 4: {
                    f3 = f;
                    f4 = f2;
                }
            }
            pathIterator.next();
        }
    }

    public static void accumulateQuad(float[] arrf, int n, float f, float f2, float f3) {
        float f4;
        float f5 = f - f2;
        float f6 = f3 - f2 + f5;
        if (f6 != 0.0f && (f4 = f5 / f6) > 0.0f && f4 < 1.0f) {
            float f7 = 1.0f - f4;
            float f8 = f * f7 * f7 + 2.0f * f2 * f4 * f7 + f3 * f4 * f4;
            if (arrf[n] > f8) {
                arrf[n] = f8;
            }
            if (arrf[n + 2] < f8) {
                arrf[n + 2] = f8;
            }
        }
    }

    public static void accumulateCubic(float[] arrf, int n, float f, float f2, float f3, float f4) {
        float f5 = f2 - f;
        float f6 = 2.0f * (f3 - f2 - f5);
        float f7 = f4 - f3 - f6 - f5;
        if (f7 == 0.0f) {
            if (f6 == 0.0f) {
                return;
            }
            Shape.accumulateCubic(arrf, n, -f5 / f6, f, f2, f3, f4);
        } else {
            float f8 = f6 * f6 - 4.0f * f7 * f5;
            if (f8 < 0.0f) {
                return;
            }
            f8 = (float)Math.sqrt(f8);
            if (f6 < 0.0f) {
                f8 = -f8;
            }
            float f9 = (f6 + f8) / -2.0f;
            Shape.accumulateCubic(arrf, n, f9 / f7, f, f2, f3, f4);
            if (f9 != 0.0f) {
                Shape.accumulateCubic(arrf, n, f5 / f9, f, f2, f3, f4);
            }
        }
    }

    public static void accumulateCubic(float[] arrf, int n, float f, float f2, float f3, float f4, float f5) {
        if (f > 0.0f && f < 1.0f) {
            float f6 = 1.0f - f;
            float f7 = f2 * f6 * f6 * f6 + 3.0f * f3 * f * f6 * f6 + 3.0f * f4 * f * f * f6 + f5 * f * f * f;
            if (arrf[n] > f7) {
                arrf[n] = f7;
            }
            if (arrf[n + 2] < f7) {
                arrf[n + 2] = f7;
            }
        }
    }
}

