/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.geom;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.BoxBounds;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.Vec2f;
import com.sun.javafx.geom.Vec3f;

public final class RectBounds
extends BaseBounds {
    private float minX;
    private float maxX;
    private float minY;
    private float maxY;

    public RectBounds() {
        this.minY = 0.0f;
        this.minX = 0.0f;
        this.maxY = -1.0f;
        this.maxX = -1.0f;
    }

    @Override
    public BaseBounds copy() {
        return new RectBounds(this.minX, this.minY, this.maxX, this.maxY);
    }

    public RectBounds(float f, float f2, float f3, float f4) {
        this.setBounds(f, f2, f3, f4);
    }

    public RectBounds(RectBounds rectBounds) {
        this.setBounds(rectBounds);
    }

    public RectBounds(Rectangle rectangle) {
        this.setBounds(rectangle.x, rectangle.y, rectangle.x + rectangle.width, rectangle.y + rectangle.height);
    }

    @Override
    public BaseBounds.BoundsType getBoundsType() {
        return BaseBounds.BoundsType.RECTANGLE;
    }

    @Override
    public boolean is2D() {
        return true;
    }

    @Override
    public float getWidth() {
        return this.maxX - this.minX;
    }

    @Override
    public float getHeight() {
        return this.maxY - this.minY;
    }

    @Override
    public float getDepth() {
        return 0.0f;
    }

    @Override
    public float getMinX() {
        return this.minX;
    }

    public void setMinX(float f) {
        this.minX = f;
    }

    @Override
    public float getMinY() {
        return this.minY;
    }

    public void setMinY(float f) {
        this.minY = f;
    }

    @Override
    public float getMinZ() {
        return 0.0f;
    }

    @Override
    public float getMaxX() {
        return this.maxX;
    }

    public void setMaxX(float f) {
        this.maxX = f;
    }

    @Override
    public float getMaxY() {
        return this.maxY;
    }

    public void setMaxY(float f) {
        this.maxY = f;
    }

    @Override
    public float getMaxZ() {
        return 0.0f;
    }

    @Override
    public Vec2f getMin(Vec2f vec2f) {
        if (vec2f == null) {
            vec2f = new Vec2f();
        }
        vec2f.x = this.minX;
        vec2f.y = this.minY;
        return vec2f;
    }

    @Override
    public Vec2f getMax(Vec2f vec2f) {
        if (vec2f == null) {
            vec2f = new Vec2f();
        }
        vec2f.x = this.maxX;
        vec2f.y = this.maxY;
        return vec2f;
    }

    @Override
    public Vec3f getMin(Vec3f vec3f) {
        if (vec3f == null) {
            vec3f = new Vec3f();
        }
        vec3f.x = this.minX;
        vec3f.y = this.minY;
        vec3f.z = 0.0f;
        return vec3f;
    }

    @Override
    public Vec3f getMax(Vec3f vec3f) {
        if (vec3f == null) {
            vec3f = new Vec3f();
        }
        vec3f.x = this.maxX;
        vec3f.y = this.maxY;
        vec3f.z = 0.0f;
        return vec3f;
    }

    @Override
    public BaseBounds deriveWithUnion(BaseBounds baseBounds) {
        if (baseBounds.getBoundsType() != BaseBounds.BoundsType.RECTANGLE) {
            if (baseBounds.getBoundsType() == BaseBounds.BoundsType.BOX) {
                BoxBounds boxBounds = new BoxBounds((BoxBounds)baseBounds);
                boxBounds.unionWith(this);
                return boxBounds;
            }
            throw new UnsupportedOperationException("Unknown BoundsType");
        }
        RectBounds rectBounds = (RectBounds)baseBounds;
        this.unionWith(rectBounds);
        return this;
    }

    @Override
    public BaseBounds deriveWithNewBounds(Rectangle rectangle) {
        if (rectangle.width < 0 || rectangle.height < 0) {
            return this.makeEmpty();
        }
        this.setBounds(rectangle.x, rectangle.y, rectangle.x + rectangle.width, rectangle.y + rectangle.height);
        return this;
    }

    @Override
    public BaseBounds deriveWithNewBounds(BaseBounds baseBounds) {
        if (baseBounds.isEmpty()) {
            return this.makeEmpty();
        }
        if (baseBounds.getBoundsType() != BaseBounds.BoundsType.RECTANGLE) {
            if (baseBounds.getBoundsType() == BaseBounds.BoundsType.BOX) {
                return new BoxBounds((BoxBounds)baseBounds);
            }
            throw new UnsupportedOperationException("Unknown BoundsType");
        }
        RectBounds rectBounds = (RectBounds)baseBounds;
        this.minX = rectBounds.getMinX();
        this.minY = rectBounds.getMinY();
        this.maxX = rectBounds.getMaxX();
        this.maxY = rectBounds.getMaxY();
        return this;
    }

    @Override
    public BaseBounds deriveWithNewBounds(float f, float f2, float f3, float f4, float f5, float f6) {
        if (f4 < f || f5 < f2 || f6 < f3) {
            return this.makeEmpty();
        }
        if (f3 == 0.0f && f6 == 0.0f) {
            this.minX = f;
            this.minY = f2;
            this.maxX = f4;
            this.maxY = f5;
            return this;
        }
        return new BoxBounds(f, f2, f3, f4, f5, f6);
    }

    @Override
    public BaseBounds deriveWithNewBoundsAndSort(float f, float f2, float f3, float f4, float f5, float f6) {
        if (f3 == 0.0f && f6 == 0.0f) {
            this.setBoundsAndSort(f, f2, f3, f4, f5, f6);
            return this;
        }
        BoxBounds boxBounds = new BoxBounds();
        ((BaseBounds)boxBounds).setBoundsAndSort(f, f2, f3, f4, f5, f6);
        return boxBounds;
    }

    public final void setBounds(RectBounds rectBounds) {
        this.minX = rectBounds.getMinX();
        this.minY = rectBounds.getMinY();
        this.maxX = rectBounds.getMaxX();
        this.maxY = rectBounds.getMaxY();
    }

    public final void setBounds(float f, float f2, float f3, float f4) {
        this.minX = f;
        this.minY = f2;
        this.maxX = f3;
        this.maxY = f4;
    }

    public void setBoundsAndSort(float f, float f2, float f3, float f4) {
        this.setBounds(f, f2, f3, f4);
        this.sortMinMax();
    }

    @Override
    public void setBoundsAndSort(float f, float f2, float f3, float f4, float f5, float f6) {
        if (f3 != 0.0f || f6 != 0.0f) {
            throw new UnsupportedOperationException("Unknown BoundsType");
        }
        this.setBounds(f, f2, f4, f5);
        this.sortMinMax();
    }

    @Override
    public void setBoundsAndSort(Point2D point2D, Point2D point2D2) {
        this.setBoundsAndSort(point2D.x, point2D.y, point2D2.x, point2D2.y);
    }

    @Override
    public RectBounds flattenInto(RectBounds rectBounds) {
        if (rectBounds == null) {
            rectBounds = new RectBounds();
        }
        if (this.isEmpty()) {
            return rectBounds.makeEmpty();
        }
        rectBounds.setBounds(this.minX, this.minY, this.maxX, this.maxY);
        return rectBounds;
    }

    public void unionWith(RectBounds rectBounds) {
        if (rectBounds.isEmpty()) {
            return;
        }
        if (this.isEmpty()) {
            this.setBounds(rectBounds);
            return;
        }
        this.minX = Math.min(this.minX, rectBounds.getMinX());
        this.minY = Math.min(this.minY, rectBounds.getMinY());
        this.maxX = Math.max(this.maxX, rectBounds.getMaxX());
        this.maxY = Math.max(this.maxY, rectBounds.getMaxY());
    }

    public void unionWith(float f, float f2, float f3, float f4) {
        if (f3 < f || f4 < f2) {
            return;
        }
        if (this.isEmpty()) {
            this.setBounds(f, f2, f3, f4);
            return;
        }
        this.minX = Math.min(this.minX, f);
        this.minY = Math.min(this.minY, f2);
        this.maxX = Math.max(this.maxX, f3);
        this.maxY = Math.max(this.maxY, f4);
    }

    @Override
    public void add(float f, float f2, float f3) {
        if (f3 != 0.0f) {
            throw new UnsupportedOperationException("Unknown BoundsType");
        }
        this.unionWith(f, f2, f, f2);
    }

    public void add(float f, float f2) {
        this.unionWith(f, f2, f, f2);
    }

    @Override
    public void add(Point2D point2D) {
        this.add(point2D.x, point2D.y);
    }

    @Override
    public void intersectWith(BaseBounds baseBounds) {
        if (this.isEmpty()) {
            return;
        }
        if (baseBounds.isEmpty()) {
            this.makeEmpty();
            return;
        }
        this.minX = Math.max(this.minX, baseBounds.getMinX());
        this.minY = Math.max(this.minY, baseBounds.getMinY());
        this.maxX = Math.min(this.maxX, baseBounds.getMaxX());
        this.maxY = Math.min(this.maxY, baseBounds.getMaxY());
    }

    @Override
    public void intersectWith(Rectangle rectangle) {
        float f = rectangle.x;
        float f2 = rectangle.y;
        this.intersectWith(f, f2, f + (float)rectangle.width, f2 + (float)rectangle.height);
    }

    public void intersectWith(float f, float f2, float f3, float f4) {
        if (this.isEmpty()) {
            return;
        }
        if (f3 < f || f4 < f2) {
            this.makeEmpty();
            return;
        }
        this.minX = Math.max(this.minX, f);
        this.minY = Math.max(this.minY, f2);
        this.maxX = Math.min(this.maxX, f3);
        this.maxY = Math.min(this.maxY, f4);
    }

    @Override
    public void intersectWith(float f, float f2, float f3, float f4, float f5, float f6) {
        if (this.isEmpty()) {
            return;
        }
        if (f4 < f || f5 < f2 || f6 < f3) {
            this.makeEmpty();
            return;
        }
        this.minX = Math.max(this.minX, f);
        this.minY = Math.max(this.minY, f2);
        this.maxX = Math.min(this.maxX, f4);
        this.maxY = Math.min(this.maxY, f5);
    }

    @Override
    public boolean contains(Point2D point2D) {
        if (point2D == null || this.isEmpty()) {
            return false;
        }
        return point2D.x >= this.minX && point2D.x <= this.maxX && point2D.y >= this.minY && point2D.y <= this.maxY;
    }

    @Override
    public boolean contains(float f, float f2) {
        if (this.isEmpty()) {
            return false;
        }
        return f >= this.minX && f <= this.maxX && f2 >= this.minY && f2 <= this.maxY;
    }

    public boolean contains(RectBounds rectBounds) {
        if (this.isEmpty() || rectBounds.isEmpty()) {
            return false;
        }
        return this.minX <= rectBounds.minX && this.maxX >= rectBounds.maxX && this.minY <= rectBounds.minY && this.maxY >= rectBounds.maxY;
    }

    @Override
    public boolean intersects(float f, float f2, float f3, float f4) {
        if (this.isEmpty()) {
            return false;
        }
        return f + f3 >= this.minX && f2 + f4 >= this.minY && f <= this.maxX && f2 <= this.maxY;
    }

    public boolean intersects(BaseBounds baseBounds) {
        if (baseBounds == null || baseBounds.isEmpty() || this.isEmpty()) {
            return false;
        }
        return baseBounds.getMaxX() >= this.minX && baseBounds.getMaxY() >= this.minY && baseBounds.getMaxZ() >= this.getMinZ() && baseBounds.getMinX() <= this.maxX && baseBounds.getMinY() <= this.maxY && baseBounds.getMinZ() <= this.getMaxZ();
    }

    @Override
    public boolean disjoint(float f, float f2, float f3, float f4) {
        if (this.isEmpty()) {
            return true;
        }
        return f + f3 < this.minX || f2 + f4 < this.minY || f > this.maxX || f2 > this.maxY;
    }

    public boolean disjoint(RectBounds rectBounds) {
        if (rectBounds == null || rectBounds.isEmpty() || this.isEmpty()) {
            return true;
        }
        return rectBounds.getMaxX() < this.minX || rectBounds.getMaxY() < this.minY || rectBounds.getMinX() > this.maxX || rectBounds.getMinY() > this.maxY;
    }

    @Override
    public boolean isEmpty() {
        return !(this.maxX >= this.minX) || !(this.maxY >= this.minY);
    }

    @Override
    public void roundOut() {
        this.minX = (float)Math.floor(this.minX);
        this.minY = (float)Math.floor(this.minY);
        this.maxX = (float)Math.ceil(this.maxX);
        this.maxY = (float)Math.ceil(this.maxY);
    }

    public void grow(float f, float f2) {
        this.minX -= f;
        this.maxX += f;
        this.minY -= f2;
        this.maxY += f2;
    }

    @Override
    public BaseBounds deriveWithPadding(float f, float f2, float f3) {
        if (f3 == 0.0f) {
            this.grow(f, f2);
            return this;
        }
        BoxBounds boxBounds = new BoxBounds(this.minX, this.minY, 0.0f, this.maxX, this.maxY, 0.0f);
        boxBounds.grow(f, f2, f3);
        return boxBounds;
    }

    @Override
    public RectBounds makeEmpty() {
        this.minY = 0.0f;
        this.minX = 0.0f;
        this.maxY = -1.0f;
        this.maxX = -1.0f;
        return this;
    }

    @Override
    protected void sortMinMax() {
        float f;
        if (this.minX > this.maxX) {
            f = this.maxX;
            this.maxX = this.minX;
            this.minX = f;
        }
        if (this.minY > this.maxY) {
            f = this.maxY;
            this.maxY = this.minY;
            this.minY = f;
        }
    }

    @Override
    public void translate(float f, float f2, float f3) {
        this.setMinX(this.getMinX() + f);
        this.setMinY(this.getMinY() + f2);
        this.setMaxX(this.getMaxX() + f);
        this.setMaxY(this.getMaxY() + f2);
    }

    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (this.getClass() != object.getClass()) {
            return false;
        }
        RectBounds rectBounds = (RectBounds)object;
        if (this.minX != rectBounds.getMinX()) {
            return false;
        }
        if (this.minY != rectBounds.getMinY()) {
            return false;
        }
        if (this.maxX != rectBounds.getMaxX()) {
            return false;
        }
        return this.maxY == rectBounds.getMaxY();
    }

    public int hashCode() {
        int n = 7;
        n = 79 * n + Float.floatToIntBits(this.minX);
        n = 79 * n + Float.floatToIntBits(this.minY);
        n = 79 * n + Float.floatToIntBits(this.maxX);
        n = 79 * n + Float.floatToIntBits(this.maxY);
        return n;
    }

    public String toString() {
        return "RectBounds { minX:" + this.minX + ", minY:" + this.minY + ", maxX:" + this.maxX + ", maxY:" + this.maxY + "} (w:" + (this.maxX - this.minX) + ", h:" + (this.maxY - this.minY) + ")";
    }
}

