/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.geom;

import com.sun.javafx.geom.EllipseIterator;
import com.sun.javafx.geom.PathIterator;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.RectangularShape;
import com.sun.javafx.geom.transform.BaseTransform;

public class Ellipse2D
extends RectangularShape {
    public float x;
    public float y;
    public float width;
    public float height;

    public Ellipse2D() {
    }

    public Ellipse2D(float f, float f2, float f3, float f4) {
        this.setFrame(f, f2, f3, f4);
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

    @Override
    public void setFrame(float f, float f2, float f3, float f4) {
        this.x = f;
        this.y = f2;
        this.width = f3;
        this.height = f4;
    }

    @Override
    public RectBounds getBounds() {
        return new RectBounds(this.x, this.y, this.x + this.width, this.y + this.height);
    }

    @Override
    public boolean contains(float f, float f2) {
        float f3 = this.width;
        if (f3 <= 0.0f) {
            return false;
        }
        float f4 = (f - this.x) / f3 - 0.5f;
        float f5 = this.height;
        if (f5 <= 0.0f) {
            return false;
        }
        float f6 = (f2 - this.y) / f5 - 0.5f;
        return f4 * f4 + f6 * f6 < 0.25f;
    }

    @Override
    public boolean intersects(float f, float f2, float f3, float f4) {
        if (f3 <= 0.0f || f4 <= 0.0f) {
            return false;
        }
        float f5 = this.width;
        if (f5 <= 0.0f) {
            return false;
        }
        float f6 = (f - this.x) / f5 - 0.5f;
        float f7 = f6 + f3 / f5;
        float f8 = this.height;
        if (f8 <= 0.0f) {
            return false;
        }
        float f9 = (f2 - this.y) / f8 - 0.5f;
        float f10 = f9 + f4 / f8;
        float f11 = f6 > 0.0f ? f6 : (f7 < 0.0f ? f7 : 0.0f);
        float f12 = f9 > 0.0f ? f9 : (f10 < 0.0f ? f10 : 0.0f);
        return f11 * f11 + f12 * f12 < 0.25f;
    }

    @Override
    public boolean contains(float f, float f2, float f3, float f4) {
        return this.contains(f, f2) && this.contains(f + f3, f2) && this.contains(f, f2 + f4) && this.contains(f + f3, f2 + f4);
    }

    @Override
    public PathIterator getPathIterator(BaseTransform baseTransform) {
        return new EllipseIterator(this, baseTransform);
    }

    @Override
    public Ellipse2D copy() {
        return new Ellipse2D(this.x, this.y, this.width, this.height);
    }

    public int hashCode() {
        int n = Float.floatToIntBits(this.x);
        n += Float.floatToIntBits(this.y) * 37;
        n += Float.floatToIntBits(this.width) * 43;
        return n += Float.floatToIntBits(this.height) * 47;
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof Ellipse2D) {
            Ellipse2D ellipse2D = (Ellipse2D)object;
            return this.x == ellipse2D.x && this.y == ellipse2D.y && this.width == ellipse2D.width && this.height == ellipse2D.height;
        }
        return false;
    }
}

