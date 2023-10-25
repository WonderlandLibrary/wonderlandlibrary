/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.geom;

import com.sun.javafx.geom.ArcIterator;
import com.sun.javafx.geom.Dimension2D;
import com.sun.javafx.geom.Line2D;
import com.sun.javafx.geom.PathIterator;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.RectangularShape;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.BaseTransform;

public class Arc2D
extends RectangularShape {
    public static final int OPEN = 0;
    public static final int CHORD = 1;
    public static final int PIE = 2;
    private int type;
    public float x;
    public float y;
    public float width;
    public float height;
    public float start;
    public float extent;

    public Arc2D() {
        this(0);
    }

    public Arc2D(int n) {
        this.setArcType(n);
    }

    public Arc2D(float f, float f2, float f3, float f4, float f5, float f6, int n) {
        this(n);
        this.x = f;
        this.y = f2;
        this.width = f3;
        this.height = f4;
        this.start = f5;
        this.extent = f6;
    }

    @Override
    public float getX() {
        return this.x;
    }

    @Override
    public float getY() {
        return this.y;
    }

    @Override
    public float getWidth() {
        return this.width;
    }

    @Override
    public float getHeight() {
        return this.height;
    }

    @Override
    public boolean isEmpty() {
        return this.width <= 0.0f || this.height <= 0.0f;
    }

    public void setArc(float f, float f2, float f3, float f4, float f5, float f6, int n) {
        this.setArcType(n);
        this.x = f;
        this.y = f2;
        this.width = f3;
        this.height = f4;
        this.start = f5;
        this.extent = f6;
    }

    public int getArcType() {
        return this.type;
    }

    public Point2D getStartPoint() {
        double d = Math.toRadians(-this.start);
        double d2 = (double)this.x + (Math.cos(d) * 0.5 + 0.5) * (double)this.width;
        double d3 = (double)this.y + (Math.sin(d) * 0.5 + 0.5) * (double)this.height;
        return new Point2D((float)d2, (float)d3);
    }

    public Point2D getEndPoint() {
        double d = Math.toRadians(-this.start - this.extent);
        double d2 = (double)this.x + (Math.cos(d) * 0.5 + 0.5) * (double)this.width;
        double d3 = (double)this.y + (Math.sin(d) * 0.5 + 0.5) * (double)this.height;
        return new Point2D((float)d2, (float)d3);
    }

    public void setArc(Point2D point2D, Dimension2D dimension2D, float f, float f2, int n) {
        this.setArc(point2D.x, point2D.y, dimension2D.width, dimension2D.height, f, f2, n);
    }

    public void setArc(Arc2D arc2D) {
        this.setArc(arc2D.x, arc2D.y, arc2D.width, arc2D.height, arc2D.start, arc2D.extent, arc2D.type);
    }

    public void setArcByCenter(float f, float f2, float f3, float f4, float f5, int n) {
        this.setArc(f - f3, f2 - f3, f3 * 2.0f, f3 * 2.0f, f4, f5, n);
    }

    public void setArcByTangent(Point2D point2D, Point2D point2D2, Point2D point2D3, float f) {
        double d = Math.atan2(point2D.y - point2D2.y, point2D.x - point2D2.x);
        double d2 = Math.atan2(point2D3.y - point2D2.y, point2D3.x - point2D2.x);
        double d3 = d2 - d;
        if (d3 > Math.PI) {
            d2 -= Math.PI * 2;
        } else if (d3 < -Math.PI) {
            d2 += Math.PI * 2;
        }
        double d4 = (d + d2) / 2.0;
        double d5 = Math.abs(d2 - d4);
        double d6 = (double)f / Math.sin(d5);
        double d7 = (double)point2D2.x + d6 * Math.cos(d4);
        double d8 = (double)point2D2.y + d6 * Math.sin(d4);
        if (d < d2) {
            d -= 1.5707963267948966;
            d2 += 1.5707963267948966;
        } else {
            d += 1.5707963267948966;
            d2 -= 1.5707963267948966;
        }
        d = Math.toDegrees(-d);
        d2 = Math.toDegrees(-d2);
        d3 = d2 - d;
        d3 = d3 < 0.0 ? (d3 += 360.0) : (d3 -= 360.0);
        this.setArcByCenter((float)d7, (float)d8, f, (float)d, (float)d3, this.type);
    }

    public void setAngleStart(Point2D point2D) {
        double d = this.height * (point2D.x - this.getCenterX());
        double d2 = this.width * (point2D.y - this.getCenterY());
        this.start = (float)(-Math.toDegrees(Math.atan2(d2, d)));
    }

    public void setAngles(float f, float f2, float f3, float f4) {
        double d = this.getCenterX();
        double d2 = this.getCenterY();
        double d3 = this.width;
        double d4 = this.height;
        double d5 = Math.atan2(d3 * (d2 - (double)f2), d4 * ((double)f - d));
        double d6 = Math.atan2(d3 * (d2 - (double)f4), d4 * ((double)f3 - d));
        if ((d6 -= d5) <= 0.0) {
            d6 += Math.PI * 2;
        }
        this.start = (float)Math.toDegrees(d5);
        this.extent = (float)Math.toDegrees(d6);
    }

    public void setAngles(Point2D point2D, Point2D point2D2) {
        this.setAngles(point2D.x, point2D.y, point2D2.x, point2D2.y);
    }

    public void setArcType(int n) {
        if (n < 0 || n > 2) {
            throw new IllegalArgumentException("invalid type for Arc: " + n);
        }
        this.type = n;
    }

    @Override
    public void setFrame(float f, float f2, float f3, float f4) {
        this.setArc(f, f2, f3, f4, this.start, this.extent, this.type);
    }

    @Override
    public RectBounds getBounds() {
        double d;
        double d2;
        double d3;
        double d4;
        if (this.isEmpty()) {
            return new RectBounds(this.x, this.y, this.x + this.width, this.y + this.height);
        }
        if (this.getArcType() == 2) {
            d4 = 0.0;
            d3 = 0.0;
            d2 = 0.0;
            d = 0.0;
        } else {
            d2 = 1.0;
            d = 1.0;
            d4 = -1.0;
            d3 = -1.0;
        }
        double d5 = 0.0;
        for (int i = 0; i < 6; ++i) {
            if (i < 4) {
                if (!this.containsAngle((float)(d5 += 90.0))) {
                    continue;
                }
            } else {
                d5 = i == 4 ? (double)this.start : (d5 += (double)this.extent);
            }
            double d6 = Math.toRadians(-d5);
            double d7 = Math.cos(d6);
            double d8 = Math.sin(d6);
            d = Math.min(d, d7);
            d2 = Math.min(d2, d8);
            d3 = Math.max(d3, d7);
            d4 = Math.max(d4, d8);
        }
        double d9 = this.width;
        double d10 = this.height;
        d3 = (double)this.x + (d3 * 0.5 + 0.5) * d9;
        d4 = (double)this.y + (d4 * 0.5 + 0.5) * d10;
        d = (double)this.x + (d * 0.5 + 0.5) * d9;
        d2 = (double)this.y + (d2 * 0.5 + 0.5) * d10;
        return new RectBounds((float)d, (float)d2, (float)d3, (float)d4);
    }

    static float normalizeDegrees(double d) {
        if (d > 180.0) {
            if (d <= 540.0) {
                d -= 360.0;
            } else if ((d = Math.IEEEremainder(d, 360.0)) == -180.0) {
                d = 180.0;
            }
        } else if (d <= -180.0) {
            if (d > -540.0) {
                d += 360.0;
            } else if ((d = Math.IEEEremainder(d, 360.0)) == -180.0) {
                d = 180.0;
            }
        }
        return (float)d;
    }

    public boolean containsAngle(float f) {
        boolean bl;
        double d = this.extent;
        boolean bl2 = bl = d < 0.0;
        if (bl) {
            d = -d;
        }
        if (d >= 360.0) {
            return true;
        }
        f = Arc2D.normalizeDegrees(f) - Arc2D.normalizeDegrees(this.start);
        if (bl) {
            f = -f;
        }
        if ((double)f < 0.0) {
            f = (float)((double)f + 360.0);
        }
        return (double)f >= 0.0 && (double)f < d;
    }

    @Override
    public boolean contains(float f, float f2) {
        double d;
        double d2;
        double d3;
        boolean bl;
        double d4 = this.width;
        if (d4 <= 0.0) {
            return false;
        }
        double d5 = (double)(f - this.x) / d4 - 0.5;
        double d6 = this.height;
        if (d6 <= 0.0) {
            return false;
        }
        double d7 = (double)(f2 - this.y) / d6 - 0.5;
        double d8 = d5 * d5 + d7 * d7;
        if (d8 >= 0.25) {
            return false;
        }
        double d9 = Math.abs(this.extent);
        if (d9 >= 360.0) {
            return true;
        }
        boolean bl2 = this.containsAngle((float)(-Math.toDegrees(Math.atan2(d7, d5))));
        if (this.type == 2) {
            return bl2;
        }
        if (bl2) {
            if (d9 >= 180.0) {
                return true;
            }
        } else if (d9 <= 180.0) {
            return false;
        }
        double d10 = Math.toRadians(-this.start);
        double d11 = Math.cos(d10);
        boolean bl3 = bl = Line2D.relativeCCW((float)d11, (float)(d3 = Math.sin(d10)), (float)(d2 = Math.cos(d10 += Math.toRadians(-this.extent))), (float)(d = Math.sin(d10)), (float)(2.0 * d5), (float)(2.0 * d7)) * Line2D.relativeCCW((float)d11, (float)d3, (float)d2, (float)d, 0.0f, 0.0f) >= 0;
        return bl2 ? !bl : bl;
    }

    @Override
    public boolean intersects(float f, float f2, float f3, float f4) {
        float f5 = this.width;
        float f6 = this.height;
        if (f3 <= 0.0f || f4 <= 0.0f || f5 <= 0.0f || f6 <= 0.0f) {
            return false;
        }
        float f7 = this.extent;
        if (f7 == 0.0f) {
            return false;
        }
        float f8 = this.x;
        float f9 = this.y;
        float f10 = f8 + f5;
        float f11 = f9 + f6;
        float f12 = f + f3;
        float f13 = f2 + f4;
        if (f >= f10 || f2 >= f11 || f12 <= f8 || f13 <= f9) {
            return false;
        }
        float f14 = this.getCenterX();
        float f15 = this.getCenterY();
        double d = Math.toRadians(-this.start);
        float f16 = (float)((double)this.x + (Math.cos(d) * 0.5 + 0.5) * (double)this.width);
        float f17 = (float)((double)this.y + (Math.sin(d) * 0.5 + 0.5) * (double)this.height);
        double d2 = Math.toRadians(-this.start - this.extent);
        float f18 = (float)((double)this.x + (Math.cos(d2) * 0.5 + 0.5) * (double)this.width);
        float f19 = (float)((double)this.y + (Math.sin(d2) * 0.5 + 0.5) * (double)this.height);
        if (f15 >= f2 && f15 <= f13 && (f16 < f12 && f18 < f12 && f14 < f12 && f10 > f && this.containsAngle(0.0f) || f16 > f && f18 > f && f14 > f && f8 < f12 && this.containsAngle(180.0f))) {
            return true;
        }
        if (f14 >= f && f14 <= f12 && (f17 > f2 && f19 > f2 && f15 > f2 && f9 < f13 && this.containsAngle(90.0f) || f17 < f13 && f19 < f13 && f15 < f13 && f11 > f2 && this.containsAngle(270.0f))) {
            return true;
        }
        if (this.type == 2 || Math.abs(f7) > 180.0f ? Shape.intersectsLine(f, f2, f3, f4, f14, f15, f16, f17) || Shape.intersectsLine(f, f2, f3, f4, f14, f15, f18, f19) : Shape.intersectsLine(f, f2, f3, f4, f16, f17, f18, f19)) {
            return true;
        }
        return this.contains(f, f2) || this.contains(f + f3, f2) || this.contains(f, f2 + f4) || this.contains(f + f3, f2 + f4);
    }

    @Override
    public boolean contains(float f, float f2, float f3, float f4) {
        float f5;
        float f6;
        float f7;
        float f8;
        float f9;
        if (!(this.contains(f, f2) && this.contains(f + f3, f2) && this.contains(f, f2 + f4) && this.contains(f + f3, f2 + f4))) {
            return false;
        }
        if (this.type != 2 || (double)Math.abs(this.extent) <= 180.0) {
            return true;
        }
        float f10 = this.getWidth() / 2.0f;
        float f11 = f + f10;
        if (Shape.intersectsLine(f, f2, f3, f4, f11, f9 = f2 + (f8 = this.getHeight() / 2.0f), f7 = (float)((double)f11 + (double)f10 * Math.cos(f6 = (float)Math.toRadians(-this.start))), f5 = (float)((double)f9 + (double)f8 * Math.sin(f6)))) {
            return false;
        }
        f7 = (float)((double)f11 + (double)f10 * Math.cos(f6 += (float)Math.toRadians(-this.extent)));
        return !Shape.intersectsLine(f, f2, f3, f4, f11, f9, f7, f5 = (float)((double)f9 + (double)f8 * Math.sin(f6)));
    }

    @Override
    public PathIterator getPathIterator(BaseTransform baseTransform) {
        return new ArcIterator(this, baseTransform);
    }

    @Override
    public Arc2D copy() {
        return new Arc2D(this.x, this.y, this.width, this.height, this.start, this.extent, this.type);
    }

    public int hashCode() {
        int n = Float.floatToIntBits(this.x);
        n += Float.floatToIntBits(this.y) * 37;
        n += Float.floatToIntBits(this.width) * 43;
        n += Float.floatToIntBits(this.height) * 47;
        n += Float.floatToIntBits(this.start) * 53;
        n += Float.floatToIntBits(this.extent) * 59;
        return n += this.getArcType() * 61;
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof Arc2D) {
            Arc2D arc2D = (Arc2D)object;
            return this.x == arc2D.x && this.y == arc2D.y && this.width == arc2D.width && this.height == arc2D.height && this.start == arc2D.start && this.extent == arc2D.extent && this.type == arc2D.type;
        }
        return false;
    }
}

