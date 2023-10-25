/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.geom;

import com.sun.javafx.geom.FlatteningPathIterator;
import com.sun.javafx.geom.IllegalPathStateException;
import com.sun.javafx.geom.PathConsumer2D;
import com.sun.javafx.geom.PathIterator;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.BaseTransform;
import java.util.Arrays;

public class Path2D
extends Shape
implements PathConsumer2D {
    static final int[] curvecoords = new int[]{2, 2, 4, 6, 0};
    public static final int WIND_EVEN_ODD = 0;
    public static final int WIND_NON_ZERO = 1;
    private static final byte SEG_MOVETO = 0;
    private static final byte SEG_LINETO = 1;
    private static final byte SEG_QUADTO = 2;
    private static final byte SEG_CUBICTO = 3;
    private static final byte SEG_CLOSE = 4;
    byte[] pointTypes;
    int numTypes;
    int numCoords;
    int windingRule;
    static final int INIT_SIZE = 20;
    static final int EXPAND_MAX = 500;
    static final int EXPAND_MAX_COORDS = 1000;
    float[] floatCoords;
    float moveX;
    float moveY;
    float prevX;
    float prevY;
    float currX;
    float currY;

    public Path2D() {
        this(1, 20);
    }

    public Path2D(int n) {
        this(n, 20);
    }

    public Path2D(int n, int n2) {
        this.setWindingRule(n);
        this.pointTypes = new byte[n2];
        this.floatCoords = new float[n2 * 2];
    }

    public Path2D(Shape shape) {
        this(shape, null);
    }

    public Path2D(Shape shape, BaseTransform baseTransform) {
        if (shape instanceof Path2D) {
            Path2D path2D = (Path2D)shape;
            this.setWindingRule(path2D.windingRule);
            this.numTypes = path2D.numTypes;
            this.pointTypes = Arrays.copyOf(path2D.pointTypes, this.numTypes);
            this.numCoords = path2D.numCoords;
            if (baseTransform == null || baseTransform.isIdentity()) {
                this.floatCoords = Arrays.copyOf(path2D.floatCoords, this.numCoords);
                this.moveX = path2D.moveX;
                this.moveY = path2D.moveY;
                this.prevX = path2D.prevX;
                this.prevY = path2D.prevY;
                this.currX = path2D.currX;
                this.currY = path2D.currY;
            } else {
                this.floatCoords = new float[this.numCoords + 6];
                baseTransform.transform(path2D.floatCoords, 0, this.floatCoords, 0, this.numCoords / 2);
                this.floatCoords[this.numCoords + 0] = this.moveX;
                this.floatCoords[this.numCoords + 1] = this.moveY;
                this.floatCoords[this.numCoords + 2] = this.prevX;
                this.floatCoords[this.numCoords + 3] = this.prevY;
                this.floatCoords[this.numCoords + 4] = this.currX;
                this.floatCoords[this.numCoords + 5] = this.currY;
                baseTransform.transform(this.floatCoords, this.numCoords, this.floatCoords, this.numCoords, 3);
                this.moveX = this.floatCoords[this.numCoords + 0];
                this.moveY = this.floatCoords[this.numCoords + 1];
                this.prevX = this.floatCoords[this.numCoords + 2];
                this.prevY = this.floatCoords[this.numCoords + 3];
                this.currX = this.floatCoords[this.numCoords + 4];
                this.currY = this.floatCoords[this.numCoords + 5];
            }
        } else {
            PathIterator pathIterator = shape.getPathIterator(baseTransform);
            this.setWindingRule(pathIterator.getWindingRule());
            this.pointTypes = new byte[20];
            this.floatCoords = new float[40];
            this.append(pathIterator, false);
        }
    }

    public Path2D(int n, byte[] arrby, int n2, float[] arrf, int n3) {
        this.windingRule = n;
        this.pointTypes = arrby;
        this.numTypes = n2;
        this.floatCoords = arrf;
        this.numCoords = n3;
    }

    Point2D getPoint(int n) {
        return new Point2D(this.floatCoords[n], this.floatCoords[n + 1]);
    }

    private boolean close(int n, float f, float f2) {
        return Math.abs((float)n - f) <= f2;
    }

    public boolean checkAndGetIntRect(Rectangle rectangle, float f) {
        if (this.numTypes == 5) {
            if (this.pointTypes[4] != 1 && this.pointTypes[4] != 4) {
                return false;
            }
        } else if (this.numTypes == 6) {
            if (this.pointTypes[4] != 1) {
                return false;
            }
            if (this.pointTypes[5] != 4) {
                return false;
            }
        } else if (this.numTypes != 4) {
            return false;
        }
        if (this.pointTypes[0] != 0) {
            return false;
        }
        if (this.pointTypes[1] != 1) {
            return false;
        }
        if (this.pointTypes[2] != 1) {
            return false;
        }
        if (this.pointTypes[3] != 1) {
            return false;
        }
        int n = (int)(this.floatCoords[0] + 0.5f);
        int n2 = (int)(this.floatCoords[1] + 0.5f);
        if (!this.close(n, this.floatCoords[0], f)) {
            return false;
        }
        if (!this.close(n2, this.floatCoords[1], f)) {
            return false;
        }
        int n3 = (int)(this.floatCoords[2] + 0.5f);
        int n4 = (int)(this.floatCoords[3] + 0.5f);
        if (!this.close(n3, this.floatCoords[2], f)) {
            return false;
        }
        if (!this.close(n4, this.floatCoords[3], f)) {
            return false;
        }
        int n5 = (int)(this.floatCoords[4] + 0.5f);
        int n6 = (int)(this.floatCoords[5] + 0.5f);
        if (!this.close(n5, this.floatCoords[4], f)) {
            return false;
        }
        if (!this.close(n6, this.floatCoords[5], f)) {
            return false;
        }
        int n7 = (int)(this.floatCoords[6] + 0.5f);
        int n8 = (int)(this.floatCoords[7] + 0.5f);
        if (!this.close(n7, this.floatCoords[6], f)) {
            return false;
        }
        if (!this.close(n8, this.floatCoords[7], f)) {
            return false;
        }
        if (this.numTypes > 4 && this.pointTypes[4] == 1) {
            if (!this.close(n, this.floatCoords[8], f)) {
                return false;
            }
            if (!this.close(n2, this.floatCoords[9], f)) {
                return false;
            }
        }
        if (n == n3 && n5 == n7 && n2 == n8 && n4 == n6 || n2 == n4 && n6 == n8 && n == n7 && n3 == n5) {
            int n9;
            int n10;
            int n11;
            int n12;
            if (n5 < n) {
                n12 = n5;
                n11 = n - n5;
            } else {
                n12 = n;
                n11 = n5 - n;
            }
            if (n6 < n2) {
                n10 = n6;
                n9 = n2 - n6;
            } else {
                n10 = n2;
                n9 = n6 - n2;
            }
            if (n11 < 0) {
                return false;
            }
            if (n9 < 0) {
                return false;
            }
            if (rectangle != null) {
                rectangle.setBounds(n12, n10, n11, n9);
            }
            return true;
        }
        return false;
    }

    void needRoom(boolean bl, int n) {
        if (bl && this.numTypes == 0) {
            throw new IllegalPathStateException("missing initial moveto in path definition");
        }
        int n2 = this.pointTypes.length;
        if (n2 == 0) {
            this.pointTypes = new byte[2];
        } else if (this.numTypes >= n2) {
            this.pointTypes = Path2D.expandPointTypes(this.pointTypes, 1);
        }
        n2 = this.floatCoords.length;
        if (this.numCoords > this.floatCoords.length - n) {
            this.floatCoords = Path2D.expandCoords(this.floatCoords, n);
        }
    }

    static byte[] expandPointTypes(byte[] arrby, int n) {
        int n2 = arrby.length;
        int n3 = n2 + n;
        if (n3 < n2) {
            throw new ArrayIndexOutOfBoundsException("pointTypes exceeds maximum capacity !");
        }
        int n4 = n2;
        if (n4 > 500) {
            n4 = Math.max(500, n2 >> 3);
        } else if (n4 < 20) {
            n4 = 20;
        }
        assert (n4 > 0);
        int n5 = n2 + n4;
        if (n5 < n3) {
            n5 = Integer.MAX_VALUE;
        }
        while (true) {
            try {
                return Arrays.copyOf(arrby, n5);
            }
            catch (OutOfMemoryError outOfMemoryError) {
                if (n5 == n3) {
                    throw outOfMemoryError;
                }
                n5 = n3 + (n5 - n3) / 2;
                continue;
            }
            break;
        }
    }

    static float[] expandCoords(float[] arrf, int n) {
        int n2 = arrf.length;
        int n3 = n2 + n;
        if (n3 < n2) {
            throw new ArrayIndexOutOfBoundsException("coords exceeds maximum capacity !");
        }
        int n4 = n2;
        if (n4 > 1000) {
            n4 = Math.max(1000, n2 >> 3);
        } else if (n4 < 20) {
            n4 = 20;
        }
        assert (n4 > n);
        int n5 = n2 + n4;
        if (n5 < n3) {
            n5 = Integer.MAX_VALUE;
        }
        while (true) {
            try {
                return Arrays.copyOf(arrf, n5);
            }
            catch (OutOfMemoryError outOfMemoryError) {
                if (n5 == n3) {
                    throw outOfMemoryError;
                }
                n5 = n3 + (n5 - n3) / 2;
                continue;
            }
            break;
        }
    }

    @Override
    public final void moveTo(float f, float f2) {
        if (this.numTypes > 0 && this.pointTypes[this.numTypes - 1] == 0) {
            this.prevX = this.currX = f;
            this.moveX = this.currX;
            this.floatCoords[this.numCoords - 2] = this.currX;
            this.prevY = this.currY = f2;
            this.moveY = this.currY;
            this.floatCoords[this.numCoords - 1] = this.currY;
        } else {
            this.needRoom(false, 2);
            this.pointTypes[this.numTypes++] = 0;
            this.prevX = this.currX = f;
            this.moveX = this.currX;
            this.floatCoords[this.numCoords++] = this.currX;
            this.prevY = this.currY = f2;
            this.moveY = this.currY;
            this.floatCoords[this.numCoords++] = this.currY;
        }
    }

    public final void moveToRel(float f, float f2) {
        if (this.numTypes > 0 && this.pointTypes[this.numTypes - 1] == 0) {
            this.prevX = this.currX += f;
            this.moveX = this.currX;
            this.floatCoords[this.numCoords - 2] = this.currX;
            this.prevY = this.currY += f2;
            this.moveY = this.currY;
            this.floatCoords[this.numCoords - 1] = this.currY;
        } else {
            this.needRoom(true, 2);
            this.pointTypes[this.numTypes++] = 0;
            this.prevX = this.currX += f;
            this.moveX = this.currX;
            this.floatCoords[this.numCoords++] = this.currX;
            this.prevY = this.currY += f2;
            this.moveY = this.currY;
            this.floatCoords[this.numCoords++] = this.currY;
        }
    }

    @Override
    public final void lineTo(float f, float f2) {
        this.needRoom(true, 2);
        this.pointTypes[this.numTypes++] = 1;
        this.prevX = this.currX = f;
        this.floatCoords[this.numCoords++] = this.currX;
        this.prevY = this.currY = f2;
        this.floatCoords[this.numCoords++] = this.currY;
    }

    public final void lineToRel(float f, float f2) {
        this.needRoom(true, 2);
        this.pointTypes[this.numTypes++] = 1;
        this.prevX = this.currX += f;
        this.floatCoords[this.numCoords++] = this.currX;
        this.prevY = this.currY += f2;
        this.floatCoords[this.numCoords++] = this.currY;
    }

    @Override
    public final void quadTo(float f, float f2, float f3, float f4) {
        this.needRoom(true, 4);
        this.pointTypes[this.numTypes++] = 2;
        this.floatCoords[this.numCoords++] = this.prevX = f;
        this.floatCoords[this.numCoords++] = this.prevY = f2;
        this.floatCoords[this.numCoords++] = this.currX = f3;
        this.floatCoords[this.numCoords++] = this.currY = f4;
    }

    public final void quadToRel(float f, float f2, float f3, float f4) {
        this.needRoom(true, 4);
        this.pointTypes[this.numTypes++] = 2;
        this.floatCoords[this.numCoords++] = this.prevX = this.currX + f;
        this.floatCoords[this.numCoords++] = this.prevY = this.currY + f2;
        this.floatCoords[this.numCoords++] = this.currX += f3;
        this.floatCoords[this.numCoords++] = this.currY += f4;
    }

    public final void quadToSmooth(float f, float f2) {
        this.needRoom(true, 4);
        this.pointTypes[this.numTypes++] = 2;
        this.floatCoords[this.numCoords++] = this.prevX = this.currX * 2.0f - this.prevX;
        this.floatCoords[this.numCoords++] = this.prevY = this.currY * 2.0f - this.prevY;
        this.floatCoords[this.numCoords++] = this.currX = f;
        this.floatCoords[this.numCoords++] = this.currY = f2;
    }

    public final void quadToSmoothRel(float f, float f2) {
        this.needRoom(true, 4);
        this.pointTypes[this.numTypes++] = 2;
        this.floatCoords[this.numCoords++] = this.prevX = this.currX * 2.0f - this.prevX;
        this.floatCoords[this.numCoords++] = this.prevY = this.currY * 2.0f - this.prevY;
        this.floatCoords[this.numCoords++] = this.currX += f;
        this.floatCoords[this.numCoords++] = this.currY += f2;
    }

    @Override
    public final void curveTo(float f, float f2, float f3, float f4, float f5, float f6) {
        this.needRoom(true, 6);
        this.pointTypes[this.numTypes++] = 3;
        this.floatCoords[this.numCoords++] = f;
        this.floatCoords[this.numCoords++] = f2;
        this.floatCoords[this.numCoords++] = this.prevX = f3;
        this.floatCoords[this.numCoords++] = this.prevY = f4;
        this.floatCoords[this.numCoords++] = this.currX = f5;
        this.floatCoords[this.numCoords++] = this.currY = f6;
    }

    public final void curveToRel(float f, float f2, float f3, float f4, float f5, float f6) {
        this.needRoom(true, 6);
        this.pointTypes[this.numTypes++] = 3;
        this.floatCoords[this.numCoords++] = this.currX + f;
        this.floatCoords[this.numCoords++] = this.currY + f2;
        this.floatCoords[this.numCoords++] = this.prevX = this.currX + f3;
        this.floatCoords[this.numCoords++] = this.prevY = this.currY + f4;
        this.floatCoords[this.numCoords++] = this.currX += f5;
        this.floatCoords[this.numCoords++] = this.currY += f6;
    }

    public final void curveToSmooth(float f, float f2, float f3, float f4) {
        this.needRoom(true, 6);
        this.pointTypes[this.numTypes++] = 3;
        this.floatCoords[this.numCoords++] = this.currX * 2.0f - this.prevX;
        this.floatCoords[this.numCoords++] = this.currY * 2.0f - this.prevY;
        this.floatCoords[this.numCoords++] = this.prevX = f;
        this.floatCoords[this.numCoords++] = this.prevY = f2;
        this.floatCoords[this.numCoords++] = this.currX = f3;
        this.floatCoords[this.numCoords++] = this.currY = f4;
    }

    public final void curveToSmoothRel(float f, float f2, float f3, float f4) {
        this.needRoom(true, 6);
        this.pointTypes[this.numTypes++] = 3;
        this.floatCoords[this.numCoords++] = this.currX * 2.0f - this.prevX;
        this.floatCoords[this.numCoords++] = this.currY * 2.0f - this.prevY;
        this.floatCoords[this.numCoords++] = this.prevX = this.currX + f;
        this.floatCoords[this.numCoords++] = this.prevY = this.currY + f2;
        this.floatCoords[this.numCoords++] = this.currX += f3;
        this.floatCoords[this.numCoords++] = this.currY += f4;
    }

    public final void ovalQuadrantTo(float f, float f2, float f3, float f4, float f5, float f6) {
        if (this.numTypes < 1) {
            throw new IllegalPathStateException("missing initial moveto in path definition");
        }
        this.appendOvalQuadrant(this.currX, this.currY, f, f2, f3, f4, f5, f6, CornerPrefix.CORNER_ONLY);
    }

    public final void appendOvalQuadrant(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, CornerPrefix cornerPrefix) {
        if (!(f7 >= 0.0f && f7 <= f8 && f8 <= 1.0f)) {
            throw new IllegalArgumentException("0 <= tfrom <= tto <= 1 required");
        }
        float f9 = (float)((double)f + (double)(f3 - f) * 0.5522847498307933);
        float f10 = (float)((double)f2 + (double)(f4 - f2) * 0.5522847498307933);
        float f11 = (float)((double)f5 + (double)(f3 - f5) * 0.5522847498307933);
        float f12 = (float)((double)f6 + (double)(f4 - f6) * 0.5522847498307933);
        if (f8 < 1.0f) {
            float f13 = 1.0f - f8;
            f5 += (f11 - f5) * f13;
            f6 += (f12 - f6) * f13;
            f11 += (f9 - f11) * f13;
            f12 += (f10 - f12) * f13;
            f9 += (f - f9) * f13;
            f10 += (f2 - f10) * f13;
            f5 += (f11 - f5) * f13;
            f6 += (f12 - f6) * f13;
            f11 += (f9 - f11) * f13;
            f12 += (f10 - f12) * f13;
            f5 += (f11 - f5) * f13;
            f6 += (f12 - f6) * f13;
        }
        if (f7 > 0.0f) {
            if (f8 < 1.0f) {
                f7 /= f8;
            }
            f += (f9 - f) * f7;
            f2 += (f10 - f2) * f7;
            f9 += (f11 - f9) * f7;
            f10 += (f12 - f10) * f7;
            f11 += (f5 - f11) * f7;
            f12 += (f6 - f12) * f7;
            f += (f9 - f) * f7;
            f2 += (f10 - f2) * f7;
            f9 += (f11 - f9) * f7;
            f10 += (f12 - f10) * f7;
            f += (f9 - f) * f7;
            f2 += (f10 - f2) * f7;
        }
        if (cornerPrefix == CornerPrefix.MOVE_THEN_CORNER) {
            this.moveTo(f, f2);
        } else if (cornerPrefix == CornerPrefix.LINE_THEN_CORNER && (this.numTypes == 1 || f != this.currX || f2 != this.currY)) {
            this.lineTo(f, f2);
        }
        if (f7 == f8 || f == f9 && f9 == f11 && f11 == f5 && f2 == f10 && f10 == f12 && f12 == f6) {
            if (cornerPrefix != CornerPrefix.LINE_THEN_CORNER) {
                this.lineTo(f5, f6);
            }
        } else {
            this.curveTo(f9, f10, f11, f12, f5, f6);
        }
    }

    public void arcTo(float f, float f2, float f3, boolean bl, boolean bl2, float f4, float f5) {
        double d;
        double d2;
        if (this.numTypes < 1) {
            throw new IllegalPathStateException("missing initial moveto in path definition");
        }
        double d3 = Math.abs(f);
        double d4 = Math.abs(f2);
        if (d3 == 0.0 || d4 == 0.0) {
            this.lineTo(f4, f5);
            return;
        }
        double d5 = this.currX;
        double d6 = this.currY;
        double d7 = f4;
        double d8 = f5;
        if (d5 == d7 && d6 == d8) {
            return;
        }
        if ((double)f3 == 0.0) {
            d2 = 1.0;
            d = 0.0;
        } else {
            d2 = Math.cos(f3);
            d = Math.sin(f3);
        }
        double d9 = (d5 + d7) / 2.0;
        double d10 = (d6 + d8) / 2.0;
        double d11 = d5 - d9;
        double d12 = d6 - d10;
        double d13 = (d2 * d11 + d * d12) / d3;
        double d14 = (d2 * d12 - d * d11) / d4;
        double d15 = d13 * d13 + d14 * d14;
        if (d15 >= 1.0) {
            double d16 = d14 * d3;
            double d17 = d13 * d4;
            if (bl2) {
                d16 = -d16;
            } else {
                d17 = -d17;
            }
            double d18 = d2 * d16 - d * d17;
            double d19 = d2 * d17 + d * d16;
            double d20 = d9 + d18;
            double d21 = d10 + d19;
            double d22 = d5 + d18;
            double d23 = d6 + d19;
            this.appendOvalQuadrant((float)d5, (float)d6, (float)d22, (float)d23, (float)d20, (float)d21, 0.0f, 1.0f, CornerPrefix.CORNER_ONLY);
            d22 = d7 + d18;
            d23 = d8 + d19;
            this.appendOvalQuadrant((float)d20, (float)d21, (float)d22, (float)d23, (float)d7, (float)d8, 0.0f, 1.0f, CornerPrefix.CORNER_ONLY);
            return;
        }
        double d24 = Math.sqrt((1.0 - d15) / d15);
        double d25 = d24 * d14;
        double d26 = d24 * d13;
        if (bl == bl2) {
            d25 = -d25;
        } else {
            d26 = -d26;
        }
        d9 += d2 * d25 * d3 - d * d26 * d4;
        d10 += d2 * d26 * d4 + d * d25 * d3;
        double d27 = d13 - d25;
        double d28 = d14 - d26;
        double d29 = -(d13 + d25);
        double d30 = -(d14 + d26);
        boolean bl3 = false;
        float f6 = 1.0f;
        boolean bl4 = false;
        do {
            double d31;
            double d32 = d28;
            double d33 = d27;
            if (bl2) {
                d32 = -d32;
            } else {
                d33 = -d33;
            }
            if (d32 * d29 + d33 * d30 > 0.0) {
                d31 = d27 * d29 + d28 * d30;
                if (d31 >= 0.0) {
                    f6 = (float)(Math.acos(d31) / 1.5707963267948966);
                    bl3 = true;
                }
                bl4 = true;
            } else if (bl4) break;
            d31 = d2 * d32 * d3 - d * d33 * d4;
            double d34 = d2 * d33 * d4 + d * d32 * d3;
            double d35 = d9 + d31;
            double d36 = d10 + d34;
            double d37 = d5 + d31;
            double d38 = d6 + d34;
            this.appendOvalQuadrant((float)d5, (float)d6, (float)d37, (float)d38, (float)d35, (float)d36, 0.0f, f6, CornerPrefix.CORNER_ONLY);
            d5 = d35;
            d6 = d36;
            d27 = d32;
            d28 = d33;
        } while (!bl3);
    }

    public void arcToRel(float f, float f2, float f3, boolean bl, boolean bl2, float f4, float f5) {
        this.arcTo(f, f2, f3, bl, bl2, this.currX + f4, this.currY + f5);
    }

    int pointCrossings(float f, float f2) {
        float f3;
        float f4;
        float[] arrf = this.floatCoords;
        float f5 = f4 = arrf[0];
        float f6 = f3 = arrf[1];
        int n = 0;
        int n2 = 2;
        block7: for (int i = 1; i < this.numTypes; ++i) {
            switch (this.pointTypes[i]) {
                case 0: {
                    if (f6 != f3) {
                        n += Shape.pointCrossingsForLine(f, f2, f5, f6, f4, f3);
                    }
                    f4 = f5 = arrf[n2++];
                    f3 = f6 = arrf[n2++];
                    continue block7;
                }
                case 1: {
                    float f7 = arrf[n2++];
                    float f8 = arrf[n2++];
                    n += Shape.pointCrossingsForLine(f, f2, f5, f6, f7, f8);
                    f5 = f7;
                    f6 = f8;
                    continue block7;
                }
                case 2: {
                    int n3 = n2++;
                    int n4 = n2++;
                    float f7 = arrf[n2++];
                    float f8 = arrf[n2++];
                    n += Shape.pointCrossingsForQuad(f, f2, f5, f6, arrf[n3], arrf[n4], f7, f8, 0);
                    f5 = f7;
                    f6 = f8;
                    continue block7;
                }
                case 3: {
                    int n5 = n2++;
                    int n6 = n2++;
                    int n7 = n2++;
                    int n8 = n2++;
                    float f7 = arrf[n2++];
                    float f8 = arrf[n2++];
                    n += Shape.pointCrossingsForCubic(f, f2, f5, f6, arrf[n5], arrf[n6], arrf[n7], arrf[n8], f7, f8, 0);
                    f5 = f7;
                    f6 = f8;
                    continue block7;
                }
                case 4: {
                    if (f6 != f3) {
                        n += Shape.pointCrossingsForLine(f, f2, f5, f6, f4, f3);
                    }
                    f5 = f4;
                    f6 = f3;
                }
            }
        }
        if (f6 != f3) {
            n += Shape.pointCrossingsForLine(f, f2, f5, f6, f4, f3);
        }
        return n;
    }

    int rectCrossings(float f, float f2, float f3, float f4) {
        float f5;
        float f6;
        float[] arrf = this.floatCoords;
        float f7 = f6 = arrf[0];
        float f8 = f5 = arrf[1];
        int n = 0;
        int n2 = 2;
        block7: for (int i = 1; n != Integer.MIN_VALUE && i < this.numTypes; ++i) {
            switch (this.pointTypes[i]) {
                case 0: {
                    if (f7 != f6 || f8 != f5) {
                        n = Shape.rectCrossingsForLine(n, f, f2, f3, f4, f7, f8, f6, f5);
                    }
                    f6 = f7 = arrf[n2++];
                    f5 = f8 = arrf[n2++];
                    continue block7;
                }
                case 1: {
                    float f9 = arrf[n2++];
                    float f10 = arrf[n2++];
                    n = Shape.rectCrossingsForLine(n, f, f2, f3, f4, f7, f8, f9, f10);
                    f7 = f9;
                    f8 = f10;
                    continue block7;
                }
                case 2: {
                    int n3 = n2++;
                    int n4 = n2++;
                    float f9 = arrf[n2++];
                    float f10 = arrf[n2++];
                    n = Shape.rectCrossingsForQuad(n, f, f2, f3, f4, f7, f8, arrf[n3], arrf[n4], f9, f10, 0);
                    f7 = f9;
                    f8 = f10;
                    continue block7;
                }
                case 3: {
                    int n5 = n2++;
                    int n6 = n2++;
                    int n7 = n2++;
                    int n8 = n2++;
                    float f9 = arrf[n2++];
                    float f10 = arrf[n2++];
                    n = Shape.rectCrossingsForCubic(n, f, f2, f3, f4, f7, f8, arrf[n5], arrf[n6], arrf[n7], arrf[n8], f9, f10, 0);
                    f7 = f9;
                    f8 = f10;
                    continue block7;
                }
                case 4: {
                    if (f7 != f6 || f8 != f5) {
                        n = Shape.rectCrossingsForLine(n, f, f2, f3, f4, f7, f8, f6, f5);
                    }
                    f7 = f6;
                    f8 = f5;
                }
            }
        }
        if (n != Integer.MIN_VALUE && (f7 != f6 || f8 != f5)) {
            n = Shape.rectCrossingsForLine(n, f, f2, f3, f4, f7, f8, f6, f5);
        }
        return n;
    }

    public final void append(PathIterator pathIterator, boolean bl) {
        float[] arrf = new float[6];
        while (!pathIterator.isDone()) {
            switch (pathIterator.currentSegment(arrf)) {
                case 0: {
                    if (!bl || this.numTypes < 1 || this.numCoords < 1) {
                        this.moveTo(arrf[0], arrf[1]);
                        break;
                    }
                    if (this.pointTypes[this.numTypes - 1] != 4 && this.floatCoords[this.numCoords - 2] == arrf[0] && this.floatCoords[this.numCoords - 1] == arrf[1]) break;
                }
                case 1: {
                    this.lineTo(arrf[0], arrf[1]);
                    break;
                }
                case 2: {
                    this.quadTo(arrf[0], arrf[1], arrf[2], arrf[3]);
                    break;
                }
                case 3: {
                    this.curveTo(arrf[0], arrf[1], arrf[2], arrf[3], arrf[4], arrf[5]);
                    break;
                }
                case 4: {
                    this.closePath();
                }
            }
            pathIterator.next();
            bl = false;
        }
    }

    public final void transform(BaseTransform baseTransform) {
        if (this.numCoords == 0) {
            return;
        }
        this.needRoom(false, 6);
        this.floatCoords[this.numCoords + 0] = this.moveX;
        this.floatCoords[this.numCoords + 1] = this.moveY;
        this.floatCoords[this.numCoords + 2] = this.prevX;
        this.floatCoords[this.numCoords + 3] = this.prevY;
        this.floatCoords[this.numCoords + 4] = this.currX;
        this.floatCoords[this.numCoords + 5] = this.currY;
        baseTransform.transform(this.floatCoords, 0, this.floatCoords, 0, this.numCoords / 2 + 3);
        this.moveX = this.floatCoords[this.numCoords + 0];
        this.moveY = this.floatCoords[this.numCoords + 1];
        this.prevX = this.floatCoords[this.numCoords + 2];
        this.prevY = this.floatCoords[this.numCoords + 3];
        this.currX = this.floatCoords[this.numCoords + 4];
        this.currY = this.floatCoords[this.numCoords + 5];
    }

    @Override
    public final RectBounds getBounds() {
        float f;
        float f2;
        float f3;
        float f4;
        int n = this.numCoords;
        if (n > 0) {
            f3 = f4 = this.floatCoords[--n];
            f = f2 = this.floatCoords[--n];
            while (n > 0) {
                float f5;
                float f6 = this.floatCoords[--n];
                if ((f5 = this.floatCoords[--n]) < f) {
                    f = f5;
                }
                if (f6 < f3) {
                    f3 = f6;
                }
                if (f5 > f2) {
                    f2 = f5;
                }
                if (!(f6 > f4)) continue;
                f4 = f6;
            }
        } else {
            f4 = 0.0f;
            f2 = 0.0f;
            f3 = 0.0f;
            f = 0.0f;
        }
        return new RectBounds(f, f3, f2, f4);
    }

    public final int getNumCommands() {
        return this.numTypes;
    }

    public final byte[] getCommandsNoClone() {
        return this.pointTypes;
    }

    public final float[] getFloatCoordsNoClone() {
        return this.floatCoords;
    }

    @Override
    public PathIterator getPathIterator(BaseTransform baseTransform) {
        if (baseTransform == null) {
            return new CopyIterator(this);
        }
        return new TxIterator(this, baseTransform);
    }

    @Override
    public final void closePath() {
        if (this.numTypes == 0 || this.pointTypes[this.numTypes - 1] != 4) {
            this.needRoom(true, 0);
            this.pointTypes[this.numTypes++] = 4;
            this.prevX = this.currX = this.moveX;
            this.prevY = this.currY = this.moveY;
        }
    }

    @Override
    public void pathDone() {
    }

    public final void append(Shape shape, boolean bl) {
        this.append(shape.getPathIterator(null), bl);
    }

    public final void appendSVGPath(String string) {
        SVGParser sVGParser = new SVGParser(string);
        sVGParser.allowcomma = false;
        while (!sVGParser.isDone()) {
            sVGParser.allowcomma = false;
            char c = sVGParser.getChar();
            switch (c) {
                case 'M': {
                    this.moveTo(sVGParser.f(), sVGParser.f());
                    while (sVGParser.nextIsNumber()) {
                        this.lineTo(sVGParser.f(), sVGParser.f());
                    }
                    break;
                }
                case 'm': {
                    if (this.numTypes > 0) {
                        this.moveToRel(sVGParser.f(), sVGParser.f());
                    } else {
                        this.moveTo(sVGParser.f(), sVGParser.f());
                    }
                    while (sVGParser.nextIsNumber()) {
                        this.lineToRel(sVGParser.f(), sVGParser.f());
                    }
                    break;
                }
                case 'L': {
                    do {
                        this.lineTo(sVGParser.f(), sVGParser.f());
                    } while (sVGParser.nextIsNumber());
                    break;
                }
                case 'l': {
                    do {
                        this.lineToRel(sVGParser.f(), sVGParser.f());
                    } while (sVGParser.nextIsNumber());
                    break;
                }
                case 'H': {
                    do {
                        this.lineTo(sVGParser.f(), this.currY);
                    } while (sVGParser.nextIsNumber());
                    break;
                }
                case 'h': {
                    do {
                        this.lineToRel(sVGParser.f(), 0.0f);
                    } while (sVGParser.nextIsNumber());
                    break;
                }
                case 'V': {
                    do {
                        this.lineTo(this.currX, sVGParser.f());
                    } while (sVGParser.nextIsNumber());
                    break;
                }
                case 'v': {
                    do {
                        this.lineToRel(0.0f, sVGParser.f());
                    } while (sVGParser.nextIsNumber());
                    break;
                }
                case 'Q': {
                    do {
                        this.quadTo(sVGParser.f(), sVGParser.f(), sVGParser.f(), sVGParser.f());
                    } while (sVGParser.nextIsNumber());
                    break;
                }
                case 'q': {
                    do {
                        this.quadToRel(sVGParser.f(), sVGParser.f(), sVGParser.f(), sVGParser.f());
                    } while (sVGParser.nextIsNumber());
                    break;
                }
                case 'T': {
                    do {
                        this.quadToSmooth(sVGParser.f(), sVGParser.f());
                    } while (sVGParser.nextIsNumber());
                    break;
                }
                case 't': {
                    do {
                        this.quadToSmoothRel(sVGParser.f(), sVGParser.f());
                    } while (sVGParser.nextIsNumber());
                    break;
                }
                case 'C': {
                    do {
                        this.curveTo(sVGParser.f(), sVGParser.f(), sVGParser.f(), sVGParser.f(), sVGParser.f(), sVGParser.f());
                    } while (sVGParser.nextIsNumber());
                    break;
                }
                case 'c': {
                    do {
                        this.curveToRel(sVGParser.f(), sVGParser.f(), sVGParser.f(), sVGParser.f(), sVGParser.f(), sVGParser.f());
                    } while (sVGParser.nextIsNumber());
                    break;
                }
                case 'S': {
                    do {
                        this.curveToSmooth(sVGParser.f(), sVGParser.f(), sVGParser.f(), sVGParser.f());
                    } while (sVGParser.nextIsNumber());
                    break;
                }
                case 's': {
                    do {
                        this.curveToSmoothRel(sVGParser.f(), sVGParser.f(), sVGParser.f(), sVGParser.f());
                    } while (sVGParser.nextIsNumber());
                    break;
                }
                case 'A': {
                    do {
                        this.arcTo(sVGParser.f(), sVGParser.f(), sVGParser.a(), sVGParser.b(), sVGParser.b(), sVGParser.f(), sVGParser.f());
                    } while (sVGParser.nextIsNumber());
                    break;
                }
                case 'a': {
                    do {
                        this.arcToRel(sVGParser.f(), sVGParser.f(), sVGParser.a(), sVGParser.b(), sVGParser.b(), sVGParser.f(), sVGParser.f());
                    } while (sVGParser.nextIsNumber());
                    break;
                }
                case 'Z': 
                case 'z': {
                    this.closePath();
                    break;
                }
                default: {
                    throw new IllegalArgumentException("invalid command (" + c + ") in SVG path at pos=" + sVGParser.pos);
                }
            }
            sVGParser.allowcomma = false;
        }
    }

    public final int getWindingRule() {
        return this.windingRule;
    }

    public final void setWindingRule(int n) {
        if (n != 0 && n != 1) {
            throw new IllegalArgumentException("winding rule must be WIND_EVEN_ODD or WIND_NON_ZERO");
        }
        this.windingRule = n;
    }

    public final Point2D getCurrentPoint() {
        if (this.numTypes < 1) {
            return null;
        }
        return new Point2D(this.currX, this.currY);
    }

    public final float getCurrentX() {
        if (this.numTypes < 1) {
            throw new IllegalPathStateException("no current point in empty path");
        }
        return this.currX;
    }

    public final float getCurrentY() {
        if (this.numTypes < 1) {
            throw new IllegalPathStateException("no current point in empty path");
        }
        return this.currY;
    }

    public final void reset() {
        this.numCoords = 0;
        this.numTypes = 0;
        this.currY = 0.0f;
        this.currX = 0.0f;
        this.prevY = 0.0f;
        this.prevX = 0.0f;
        this.moveY = 0.0f;
        this.moveX = 0.0f;
    }

    public final Shape createTransformedShape(BaseTransform baseTransform) {
        return new Path2D(this, baseTransform);
    }

    @Override
    public Path2D copy() {
        return new Path2D(this);
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof Path2D) {
            Path2D path2D = (Path2D)object;
            if (path2D.numTypes == this.numTypes && path2D.numCoords == this.numCoords && path2D.windingRule == this.windingRule) {
                int n;
                for (n = 0; n < this.numTypes; ++n) {
                    if (path2D.pointTypes[n] == this.pointTypes[n]) continue;
                    return false;
                }
                for (n = 0; n < this.numCoords; ++n) {
                    if (path2D.floatCoords[n] == this.floatCoords[n]) continue;
                    return false;
                }
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        int n;
        int n2 = 7;
        n2 = 11 * n2 + this.numTypes;
        n2 = 11 * n2 + this.numCoords;
        n2 = 11 * n2 + this.windingRule;
        for (n = 0; n < this.numTypes; ++n) {
            n2 = 11 * n2 + this.pointTypes[n];
        }
        for (n = 0; n < this.numCoords; ++n) {
            n2 = 11 * n2 + Float.floatToIntBits(this.floatCoords[n]);
        }
        return n2;
    }

    public static boolean contains(PathIterator pathIterator, float f, float f2) {
        if (f * 0.0f + f2 * 0.0f == 0.0f) {
            int n = pathIterator.getWindingRule() == 1 ? -1 : 1;
            int n2 = Shape.pointCrossingsForPath(pathIterator, f, f2);
            return (n2 & n) != 0;
        }
        return false;
    }

    public static boolean contains(PathIterator pathIterator, Point2D point2D) {
        return Path2D.contains(pathIterator, point2D.x, point2D.y);
    }

    @Override
    public final boolean contains(float f, float f2) {
        if (f * 0.0f + f2 * 0.0f == 0.0f) {
            if (this.numTypes < 2) {
                return false;
            }
            int n = this.windingRule == 1 ? -1 : 1;
            return (this.pointCrossings(f, f2) & n) != 0;
        }
        return false;
    }

    @Override
    public final boolean contains(Point2D point2D) {
        return this.contains(point2D.x, point2D.y);
    }

    public static boolean contains(PathIterator pathIterator, float f, float f2, float f3, float f4) {
        if (Float.isNaN(f + f3) || Float.isNaN(f2 + f4)) {
            return false;
        }
        if (f3 <= 0.0f || f4 <= 0.0f) {
            return false;
        }
        int n = pathIterator.getWindingRule() == 1 ? -1 : 2;
        int n2 = Shape.rectCrossingsForPath(pathIterator, f, f2, f + f3, f2 + f4);
        return n2 != Integer.MIN_VALUE && (n2 & n) != 0;
    }

    @Override
    public final boolean contains(float f, float f2, float f3, float f4) {
        if (Float.isNaN(f + f3) || Float.isNaN(f2 + f4)) {
            return false;
        }
        if (f3 <= 0.0f || f4 <= 0.0f) {
            return false;
        }
        int n = this.windingRule == 1 ? -1 : 2;
        int n2 = this.rectCrossings(f, f2, f + f3, f2 + f4);
        return n2 != Integer.MIN_VALUE && (n2 & n) != 0;
    }

    public static boolean intersects(PathIterator pathIterator, float f, float f2, float f3, float f4) {
        if (Float.isNaN(f + f3) || Float.isNaN(f2 + f4)) {
            return false;
        }
        if (f3 <= 0.0f || f4 <= 0.0f) {
            return false;
        }
        int n = pathIterator.getWindingRule() == 1 ? -1 : 2;
        int n2 = Shape.rectCrossingsForPath(pathIterator, f, f2, f + f3, f2 + f4);
        return n2 == Integer.MIN_VALUE || (n2 & n) != 0;
    }

    @Override
    public final boolean intersects(float f, float f2, float f3, float f4) {
        if (Float.isNaN(f + f3) || Float.isNaN(f2 + f4)) {
            return false;
        }
        if (f3 <= 0.0f || f4 <= 0.0f) {
            return false;
        }
        int n = this.windingRule == 1 ? -1 : 2;
        int n2 = this.rectCrossings(f, f2, f + f3, f2 + f4);
        return n2 == Integer.MIN_VALUE || (n2 & n) != 0;
    }

    @Override
    public PathIterator getPathIterator(BaseTransform baseTransform, float f) {
        return new FlatteningPathIterator(this.getPathIterator(baseTransform), f);
    }

    public void setTo(Path2D path2D) {
        this.numTypes = path2D.numTypes;
        this.numCoords = path2D.numCoords;
        if (this.numTypes > this.pointTypes.length) {
            this.pointTypes = new byte[this.numTypes];
        }
        System.arraycopy(path2D.pointTypes, 0, this.pointTypes, 0, this.numTypes);
        if (this.numCoords > this.floatCoords.length) {
            this.floatCoords = new float[this.numCoords];
        }
        System.arraycopy(path2D.floatCoords, 0, this.floatCoords, 0, this.numCoords);
        this.windingRule = path2D.windingRule;
        this.moveX = path2D.moveX;
        this.moveY = path2D.moveY;
        this.prevX = path2D.prevX;
        this.prevY = path2D.prevY;
        this.currX = path2D.currX;
        this.currY = path2D.currY;
    }

    public static final class CornerPrefix
    extends Enum<CornerPrefix> {
        public static final /* enum */ CornerPrefix CORNER_ONLY = new CornerPrefix();
        public static final /* enum */ CornerPrefix MOVE_THEN_CORNER = new CornerPrefix();
        public static final /* enum */ CornerPrefix LINE_THEN_CORNER = new CornerPrefix();
        private static final /* synthetic */ CornerPrefix[] $VALUES;

        public static CornerPrefix[] values() {
            return (CornerPrefix[])$VALUES.clone();
        }

        public static CornerPrefix valueOf(String string) {
            return Enum.valueOf(CornerPrefix.class, string);
        }

        private static /* synthetic */ CornerPrefix[] $values() {
            return new CornerPrefix[]{CORNER_ONLY, MOVE_THEN_CORNER, LINE_THEN_CORNER};
        }

        static {
            $VALUES = CornerPrefix.$values();
        }
    }

    static class CopyIterator
    extends Iterator {
        float[] floatCoords;

        CopyIterator(Path2D path2D) {
            super(path2D);
            this.floatCoords = path2D.floatCoords;
        }

        @Override
        public int currentSegment(float[] arrf) {
            byte by = this.path.pointTypes[this.typeIdx];
            int n = curvecoords[by];
            if (n > 0) {
                System.arraycopy(this.floatCoords, this.pointIdx, arrf, 0, n);
            }
            return by;
        }

        public int currentSegment(double[] arrd) {
            byte by = this.path.pointTypes[this.typeIdx];
            int n = curvecoords[by];
            if (n > 0) {
                for (int i = 0; i < n; ++i) {
                    arrd[i] = this.floatCoords[this.pointIdx + i];
                }
            }
            return by;
        }
    }

    static class TxIterator
    extends Iterator {
        float[] floatCoords;
        BaseTransform transform;

        TxIterator(Path2D path2D, BaseTransform baseTransform) {
            super(path2D);
            this.floatCoords = path2D.floatCoords;
            this.transform = baseTransform;
        }

        @Override
        public int currentSegment(float[] arrf) {
            byte by = this.path.pointTypes[this.typeIdx];
            int n = curvecoords[by];
            if (n > 0) {
                this.transform.transform(this.floatCoords, this.pointIdx, arrf, 0, n / 2);
            }
            return by;
        }

        public int currentSegment(double[] arrd) {
            byte by = this.path.pointTypes[this.typeIdx];
            int n = curvecoords[by];
            if (n > 0) {
                this.transform.transform(this.floatCoords, this.pointIdx, arrd, 0, n / 2);
            }
            return by;
        }
    }

    static class SVGParser {
        final String svgpath;
        final int len;
        int pos;
        boolean allowcomma;

        public SVGParser(String string) {
            this.svgpath = string;
            this.len = string.length();
        }

        public boolean isDone() {
            return this.toNextNonWsp() >= this.len;
        }

        public char getChar() {
            return this.svgpath.charAt(this.pos++);
        }

        public boolean nextIsNumber() {
            if (this.toNextNonWsp() < this.len) {
                switch (this.svgpath.charAt(this.pos)) {
                    case '+': 
                    case '-': 
                    case '.': 
                    case '0': 
                    case '1': 
                    case '2': 
                    case '3': 
                    case '4': 
                    case '5': 
                    case '6': 
                    case '7': 
                    case '8': 
                    case '9': {
                        return true;
                    }
                }
            }
            return false;
        }

        public float f() {
            return this.getFloat();
        }

        public float a() {
            return (float)Math.toRadians(this.getFloat());
        }

        public float getFloat() {
            int n = this.toNextNonWsp();
            this.allowcomma = true;
            int n2 = this.toNumberEnd();
            if (n < n2) {
                String string = this.svgpath.substring(n, n2);
                try {
                    return Float.parseFloat(string);
                }
                catch (NumberFormatException numberFormatException) {
                    throw new IllegalArgumentException("invalid float (" + string + ") in path at pos=" + n);
                }
            }
            throw new IllegalArgumentException("end of path looking for float");
        }

        public boolean b() {
            this.toNextNonWsp();
            this.allowcomma = true;
            if (this.pos < this.len) {
                char c = this.svgpath.charAt(this.pos);
                switch (c) {
                    case '0': {
                        ++this.pos;
                        return false;
                    }
                    case '1': {
                        ++this.pos;
                        return true;
                    }
                }
                throw new IllegalArgumentException("invalid boolean flag (" + c + ") in path at pos=" + this.pos);
            }
            throw new IllegalArgumentException("end of path looking for boolean");
        }

        private int toNextNonWsp() {
            boolean bl = this.allowcomma;
            while (this.pos < this.len) {
                switch (this.svgpath.charAt(this.pos)) {
                    case ',': {
                        if (!bl) {
                            return this.pos;
                        }
                        bl = false;
                        break;
                    }
                    case '\t': 
                    case '\n': 
                    case '\r': 
                    case ' ': {
                        break;
                    }
                    default: {
                        return this.pos;
                    }
                }
                ++this.pos;
            }
            return this.pos;
        }

        private int toNumberEnd() {
            boolean bl = true;
            boolean bl2 = false;
            boolean bl3 = false;
            while (this.pos < this.len) {
                switch (this.svgpath.charAt(this.pos)) {
                    case '+': 
                    case '-': {
                        if (!bl) {
                            return this.pos;
                        }
                        bl = false;
                        break;
                    }
                    case '0': 
                    case '1': 
                    case '2': 
                    case '3': 
                    case '4': 
                    case '5': 
                    case '6': 
                    case '7': 
                    case '8': 
                    case '9': {
                        bl = false;
                        break;
                    }
                    case 'E': 
                    case 'e': {
                        if (bl2) {
                            return this.pos;
                        }
                        bl = true;
                        bl2 = true;
                        break;
                    }
                    case '.': {
                        if (bl2 || bl3) {
                            return this.pos;
                        }
                        bl3 = true;
                        bl = false;
                        break;
                    }
                    default: {
                        return this.pos;
                    }
                }
                ++this.pos;
            }
            return this.pos;
        }
    }

    static abstract class Iterator
    implements PathIterator {
        int typeIdx;
        int pointIdx;
        Path2D path;

        Iterator(Path2D path2D) {
            this.path = path2D;
        }

        @Override
        public int getWindingRule() {
            return this.path.getWindingRule();
        }

        @Override
        public boolean isDone() {
            return this.typeIdx >= this.path.numTypes;
        }

        @Override
        public void next() {
            byte by = this.path.pointTypes[this.typeIdx++];
            this.pointIdx += curvecoords[by];
        }
    }
}

