/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.geom;

import com.sun.javafx.geom.LineIterator;
import com.sun.javafx.geom.PathIterator;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.BaseTransform;

public class Line2D
extends Shape {
    public float x1;
    public float y1;
    public float x2;
    public float y2;

    public Line2D() {
    }

    public Line2D(float f, float f2, float f3, float f4) {
        this.setLine(f, f2, f3, f4);
    }

    public Line2D(Point2D point2D, Point2D point2D2) {
        this.setLine(point2D, point2D2);
    }

    public void setLine(float f, float f2, float f3, float f4) {
        this.x1 = f;
        this.y1 = f2;
        this.x2 = f3;
        this.y2 = f4;
    }

    public void setLine(Point2D point2D, Point2D point2D2) {
        this.setLine(point2D.x, point2D.y, point2D2.x, point2D2.y);
    }

    public void setLine(Line2D line2D) {
        this.setLine(line2D.x1, line2D.y1, line2D.x2, line2D.y2);
    }

    @Override
    public RectBounds getBounds() {
        RectBounds rectBounds = new RectBounds();
        rectBounds.setBoundsAndSort(this.x1, this.y1, this.x2, this.y2);
        return rectBounds;
    }

    @Override
    public boolean contains(float f, float f2) {
        return false;
    }

    @Override
    public boolean contains(float f, float f2, float f3, float f4) {
        return false;
    }

    @Override
    public boolean contains(Point2D point2D) {
        return false;
    }

    @Override
    public boolean intersects(float f, float f2, float f3, float f4) {
        int n;
        int n2 = Line2D.outcode(f, f2, f3, f4, this.x2, this.y2);
        if (n2 == 0) {
            return true;
        }
        float f5 = this.x1;
        float f6 = this.y1;
        while ((n = Line2D.outcode(f, f2, f3, f4, f5, f6)) != 0) {
            if ((n & n2) != 0) {
                return false;
            }
            if ((n & 5) != 0) {
                f5 = f;
                if ((n & 4) != 0) {
                    f5 += f3;
                }
                f6 = this.y1 + (f5 - this.x1) * (this.y2 - this.y1) / (this.x2 - this.x1);
                continue;
            }
            f6 = f2;
            if ((n & 8) != 0) {
                f6 += f4;
            }
            f5 = this.x1 + (f6 - this.y1) * (this.x2 - this.x1) / (this.y2 - this.y1);
        }
        return true;
    }

    public static int relativeCCW(float f, float f2, float f3, float f4, float f5, float f6) {
        float f7 = (f5 -= f) * (f4 -= f2) - (f6 -= f2) * (f3 -= f);
        if (f7 == 0.0f && (f7 = f5 * f3 + f6 * f4) > 0.0f && (f7 = (f5 -= f3) * f3 + (f6 -= f4) * f4) < 0.0f) {
            f7 = 0.0f;
        }
        return f7 < 0.0f ? -1 : (f7 > 0.0f ? 1 : 0);
    }

    public int relativeCCW(float f, float f2) {
        return Line2D.relativeCCW(this.x1, this.y1, this.x2, this.y2, f, f2);
    }

    public int relativeCCW(Point2D point2D) {
        return Line2D.relativeCCW(this.x1, this.y1, this.x2, this.y2, point2D.x, point2D.y);
    }

    public static boolean linesIntersect(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        return Line2D.relativeCCW(f, f2, f3, f4, f5, f6) * Line2D.relativeCCW(f, f2, f3, f4, f7, f8) <= 0 && Line2D.relativeCCW(f5, f6, f7, f8, f, f2) * Line2D.relativeCCW(f5, f6, f7, f8, f3, f4) <= 0;
    }

    public boolean intersectsLine(float f, float f2, float f3, float f4) {
        return Line2D.linesIntersect(f, f2, f3, f4, this.x1, this.y1, this.x2, this.y2);
    }

    public boolean intersectsLine(Line2D line2D) {
        return Line2D.linesIntersect(line2D.x1, line2D.y1, line2D.x2, line2D.y2, this.x1, this.y1, this.x2, this.y2);
    }

    public static float ptSegDistSq(float f, float f2, float f3, float f4, float f5, float f6) {
        float f7;
        float f8;
        float f9 = (f5 -= f) * (f3 -= f) + (f6 -= f2) * (f4 -= f2);
        if ((f8 = f5 * f5 + f6 * f6 - (f7 = f9 <= 0.0f ? 0.0f : ((f9 = (f5 = f3 - f5) * f3 + (f6 = f4 - f6) * f4) <= 0.0f ? 0.0f : f9 * f9 / (f3 * f3 + f4 * f4)))) < 0.0f) {
            f8 = 0.0f;
        }
        return f8;
    }

    public static float ptSegDist(float f, float f2, float f3, float f4, float f5, float f6) {
        return (float)Math.sqrt(Line2D.ptSegDistSq(f, f2, f3, f4, f5, f6));
    }

    public float ptSegDistSq(float f, float f2) {
        return Line2D.ptSegDistSq(this.x1, this.y1, this.x2, this.y2, f, f2);
    }

    public float ptSegDistSq(Point2D point2D) {
        return Line2D.ptSegDistSq(this.x1, this.y1, this.x2, this.y2, point2D.x, point2D.y);
    }

    public double ptSegDist(float f, float f2) {
        return Line2D.ptSegDist(this.x1, this.y1, this.x2, this.y2, f, f2);
    }

    public float ptSegDist(Point2D point2D) {
        return Line2D.ptSegDist(this.x1, this.y1, this.x2, this.y2, point2D.x, point2D.y);
    }

    public static float ptLineDistSq(float f, float f2, float f3, float f4, float f5, float f6) {
        float f7;
        float f8;
        float f9;
        if ((f9 = (f5 -= f) * f5 + (f6 -= f2) * f6 - (f8 = (f7 = f5 * (f3 -= f) + f6 * (f4 -= f2)) * f7 / (f3 * f3 + f4 * f4))) < 0.0f) {
            f9 = 0.0f;
        }
        return f9;
    }

    public static float ptLineDist(float f, float f2, float f3, float f4, float f5, float f6) {
        return (float)Math.sqrt(Line2D.ptLineDistSq(f, f2, f3, f4, f5, f6));
    }

    public float ptLineDistSq(float f, float f2) {
        return Line2D.ptLineDistSq(this.x1, this.y1, this.x2, this.y2, f, f2);
    }

    public float ptLineDistSq(Point2D point2D) {
        return Line2D.ptLineDistSq(this.x1, this.y1, this.x2, this.y2, point2D.x, point2D.y);
    }

    public float ptLineDist(float f, float f2) {
        return Line2D.ptLineDist(this.x1, this.y1, this.x2, this.y2, f, f2);
    }

    public float ptLineDist(Point2D point2D) {
        return Line2D.ptLineDist(this.x1, this.y1, this.x2, this.y2, point2D.x, point2D.y);
    }

    @Override
    public PathIterator getPathIterator(BaseTransform baseTransform) {
        return new LineIterator(this, baseTransform);
    }

    @Override
    public PathIterator getPathIterator(BaseTransform baseTransform, float f) {
        return new LineIterator(this, baseTransform);
    }

    @Override
    public Line2D copy() {
        return new Line2D(this.x1, this.y1, this.x2, this.y2);
    }

    public int hashCode() {
        int n = Float.floatToIntBits(this.x1);
        n += Float.floatToIntBits(this.y1) * 37;
        n += Float.floatToIntBits(this.x2) * 43;
        return n += Float.floatToIntBits(this.y2) * 47;
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof Line2D) {
            Line2D line2D = (Line2D)object;
            return this.x1 == line2D.x1 && this.y1 == line2D.y1 && this.x2 == line2D.x2 && this.y2 == line2D.y2;
        }
        return false;
    }
}

