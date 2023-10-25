/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.geom;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.Vec2f;
import com.sun.javafx.geom.Vec3f;
import com.sun.javafx.geom.transform.Affine3D;

public class BoxBounds
extends BaseBounds {
    private float minX;
    private float maxX;
    private float minY;
    private float maxY;
    private float minZ;
    private float maxZ;

    public BoxBounds() {
        this.minZ = 0.0f;
        this.minY = 0.0f;
        this.minX = 0.0f;
        this.maxZ = -1.0f;
        this.maxY = -1.0f;
        this.maxX = -1.0f;
    }

    @Override
    public BaseBounds copy() {
        return new BoxBounds(this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ);
    }

    public BoxBounds(float f, float f2, float f3, float f4, float f5, float f6) {
        this.setBounds(f, f2, f3, f4, f5, f6);
    }

    public BoxBounds(BoxBounds boxBounds) {
        this.setBounds(boxBounds);
    }

    @Override
    public BaseBounds.BoundsType getBoundsType() {
        return BaseBounds.BoundsType.BOX;
    }

    @Override
    public boolean is2D() {
        return Affine3D.almostZero(this.minZ) && Affine3D.almostZero(this.maxZ);
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
        return this.maxZ - this.minZ;
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
        return this.minZ;
    }

    public void setMinZ(float f) {
        this.minZ = f;
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
        return this.maxZ;
    }

    public void setMaxZ(float f) {
        this.maxZ = f;
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
        vec3f.z = this.minZ;
        return vec3f;
    }

    @Override
    public Vec3f getMax(Vec3f vec3f) {
        if (vec3f == null) {
            vec3f = new Vec3f();
        }
        vec3f.x = this.maxX;
        vec3f.y = this.maxY;
        vec3f.z = this.maxZ;
        return vec3f;
    }

    @Override
    public BaseBounds deriveWithUnion(BaseBounds baseBounds) {
        if (baseBounds.getBoundsType() != BaseBounds.BoundsType.RECTANGLE && baseBounds.getBoundsType() != BaseBounds.BoundsType.BOX) {
            throw new UnsupportedOperationException("Unknown BoundsType");
        }
        this.unionWith(baseBounds);
        return this;
    }

    @Override
    public BaseBounds deriveWithNewBounds(Rectangle rectangle) {
        if (rectangle.width < 0 || rectangle.height < 0) {
            return this.makeEmpty();
        }
        this.setBounds(rectangle.x, rectangle.y, 0.0f, rectangle.x + rectangle.width, rectangle.y + rectangle.height, 0.0f);
        return this;
    }

    @Override
    public BaseBounds deriveWithNewBounds(BaseBounds baseBounds) {
        if (baseBounds.isEmpty()) {
            return this.makeEmpty();
        }
        if (baseBounds.getBoundsType() != BaseBounds.BoundsType.RECTANGLE && baseBounds.getBoundsType() != BaseBounds.BoundsType.BOX) {
            throw new UnsupportedOperationException("Unknown BoundsType");
        }
        this.minX = baseBounds.getMinX();
        this.minY = baseBounds.getMinY();
        this.minZ = baseBounds.getMinZ();
        this.maxX = baseBounds.getMaxX();
        this.maxY = baseBounds.getMaxY();
        this.maxZ = baseBounds.getMaxZ();
        return this;
    }

    @Override
    public BaseBounds deriveWithNewBounds(float f, float f2, float f3, float f4, float f5, float f6) {
        if (f4 < f || f5 < f2 || f6 < f3) {
            return this.makeEmpty();
        }
        this.minX = f;
        this.minY = f2;
        this.minZ = f3;
        this.maxX = f4;
        this.maxY = f5;
        this.maxZ = f6;
        return this;
    }

    @Override
    public BaseBounds deriveWithNewBoundsAndSort(float f, float f2, float f3, float f4, float f5, float f6) {
        this.setBoundsAndSort(f, f2, f3, f4, f5, f6);
        return this;
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

    public final void setBounds(BaseBounds baseBounds) {
        this.minX = baseBounds.getMinX();
        this.minY = baseBounds.getMinY();
        this.minZ = baseBounds.getMinZ();
        this.maxX = baseBounds.getMaxX();
        this.maxY = baseBounds.getMaxY();
        this.maxZ = baseBounds.getMaxZ();
    }

    public final void setBounds(float f, float f2, float f3, float f4, float f5, float f6) {
        this.minX = f;
        this.minY = f2;
        this.minZ = f3;
        this.maxX = f4;
        this.maxY = f5;
        this.maxZ = f6;
    }

    @Override
    public void setBoundsAndSort(float f, float f2, float f3, float f4, float f5, float f6) {
        this.setBounds(f, f2, f3, f4, f5, f6);
        this.sortMinMax();
    }

    @Override
    public void setBoundsAndSort(Point2D point2D, Point2D point2D2) {
        this.setBoundsAndSort(point2D.x, point2D.y, 0.0f, point2D2.x, point2D2.y, 0.0f);
    }

    public void unionWith(BaseBounds baseBounds) {
        if (baseBounds.isEmpty()) {
            return;
        }
        if (this.isEmpty()) {
            this.setBounds(baseBounds);
            return;
        }
        this.minX = Math.min(this.minX, baseBounds.getMinX());
        this.minY = Math.min(this.minY, baseBounds.getMinY());
        this.minZ = Math.min(this.minZ, baseBounds.getMinZ());
        this.maxX = Math.max(this.maxX, baseBounds.getMaxX());
        this.maxY = Math.max(this.maxY, baseBounds.getMaxY());
        this.maxZ = Math.max(this.maxZ, baseBounds.getMaxZ());
    }

    public void unionWith(float f, float f2, float f3, float f4, float f5, float f6) {
        if (f4 < f || f5 < f2 || f6 < f3) {
            return;
        }
        if (this.isEmpty()) {
            this.setBounds(f, f2, f3, f4, f5, f6);
            return;
        }
        this.minX = Math.min(this.minX, f);
        this.minY = Math.min(this.minY, f2);
        this.minZ = Math.min(this.minZ, f3);
        this.maxX = Math.max(this.maxX, f4);
        this.maxY = Math.max(this.maxY, f5);
        this.maxZ = Math.max(this.maxZ, f6);
    }

    @Override
    public void add(float f, float f2, float f3) {
        this.unionWith(f, f2, f3, f, f2, f3);
    }

    @Override
    public void add(Point2D point2D) {
        this.add(point2D.x, point2D.y, 0.0f);
    }

    @Override
    public void intersectWith(Rectangle rectangle) {
        float f = rectangle.x;
        float f2 = rectangle.y;
        this.intersectWith(f, f2, 0.0f, f + (float)rectangle.width, f2 + (float)rectangle.height, 0.0f);
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
        this.minZ = Math.max(this.minZ, baseBounds.getMinZ());
        this.maxX = Math.min(this.maxX, baseBounds.getMaxX());
        this.maxY = Math.min(this.maxY, baseBounds.getMaxY());
        this.maxZ = Math.min(this.maxZ, baseBounds.getMaxZ());
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
        this.minZ = Math.max(this.minZ, f3);
        this.maxX = Math.min(this.maxX, f4);
        this.maxY = Math.min(this.maxY, f5);
        this.maxZ = Math.min(this.maxZ, f6);
    }

    @Override
    public boolean contains(Point2D point2D) {
        if (point2D == null || this.isEmpty()) {
            return false;
        }
        return this.contains(point2D.x, point2D.y, 0.0f);
    }

    @Override
    public boolean contains(float f, float f2) {
        if (this.isEmpty()) {
            return false;
        }
        return this.contains(f, f2, 0.0f);
    }

    public boolean contains(float f, float f2, float f3) {
        if (this.isEmpty()) {
            return false;
        }
        return f >= this.minX && f <= this.maxX && f2 >= this.minY && f2 <= this.maxY && f3 >= this.minZ && f3 <= this.maxZ;
    }

    public boolean contains(float f, float f2, float f3, float f4, float f5, float f6) {
        if (this.isEmpty()) {
            return false;
        }
        return this.contains(f, f2, f3) && this.contains(f + f4, f2 + f5, f3 + f6);
    }

    @Override
    public boolean intersects(float f, float f2, float f3, float f4) {
        return this.intersects(f, f2, 0.0f, f3, f4, 0.0f);
    }

    public boolean intersects(float f, float f2, float f3, float f4, float f5, float f6) {
        if (this.isEmpty()) {
            return false;
        }
        return f + f4 >= this.minX && f2 + f5 >= this.minY && f3 + f6 >= this.minZ && f <= this.maxX && f2 <= this.maxY && f3 <= this.maxZ;
    }

    public boolean intersects(BaseBounds baseBounds) {
        if (baseBounds == null || baseBounds.isEmpty() || this.isEmpty()) {
            return false;
        }
        return baseBounds.getMaxX() >= this.minX && baseBounds.getMaxY() >= this.minY && baseBounds.getMaxZ() >= this.minZ && baseBounds.getMinX() <= this.maxX && baseBounds.getMinY() <= this.maxY && baseBounds.getMinZ() <= this.maxZ;
    }

    @Override
    public boolean disjoint(float f, float f2, float f3, float f4) {
        return this.disjoint(f, f2, 0.0f, f3, f4, 0.0f);
    }

    public boolean disjoint(float f, float f2, float f3, float f4, float f5, float f6) {
        if (this.isEmpty()) {
            return true;
        }
        return f + f4 < this.minX || f2 + f5 < this.minY || f3 + f6 < this.minZ || f > this.maxX || f2 > this.maxY || f3 > this.maxZ;
    }

    @Override
    public boolean isEmpty() {
        return this.maxX < this.minX || this.maxY < this.minY || this.maxZ < this.minZ;
    }

    @Override
    public void roundOut() {
        this.minX = (float)Math.floor(this.minX);
        this.minY = (float)Math.floor(this.minY);
        this.minZ = (float)Math.floor(this.minZ);
        this.maxX = (float)Math.ceil(this.maxX);
        this.maxY = (float)Math.ceil(this.maxY);
        this.maxZ = (float)Math.ceil(this.maxZ);
    }

    public void grow(float f, float f2, float f3) {
        this.minX -= f;
        this.maxX += f;
        this.minY -= f2;
        this.maxY += f2;
        this.minZ -= f3;
        this.maxZ += f3;
    }

    @Override
    public BaseBounds deriveWithPadding(float f, float f2, float f3) {
        this.grow(f, f2, f3);
        return this;
    }

    @Override
    public BoxBounds makeEmpty() {
        this.minZ = 0.0f;
        this.minY = 0.0f;
        this.minX = 0.0f;
        this.maxZ = -1.0f;
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
        if (this.minZ > this.maxZ) {
            f = this.maxZ;
            this.maxZ = this.minZ;
            this.minZ = f;
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
        BoxBounds boxBounds = (BoxBounds)object;
        if (this.minX != boxBounds.getMinX()) {
            return false;
        }
        if (this.minY != boxBounds.getMinY()) {
            return false;
        }
        if (this.minZ != boxBounds.getMinZ()) {
            return false;
        }
        if (this.maxX != boxBounds.getMaxX()) {
            return false;
        }
        if (this.maxY != boxBounds.getMaxY()) {
            return false;
        }
        return this.maxZ == boxBounds.getMaxZ();
    }

    public int hashCode() {
        int n = 7;
        n = 79 * n + Float.floatToIntBits(this.minX);
        n = 79 * n + Float.floatToIntBits(this.minY);
        n = 79 * n + Float.floatToIntBits(this.minZ);
        n = 79 * n + Float.floatToIntBits(this.maxX);
        n = 79 * n + Float.floatToIntBits(this.maxY);
        n = 79 * n + Float.floatToIntBits(this.maxZ);
        return n;
    }

    public String toString() {
        return "BoxBounds { minX:" + this.minX + ", minY:" + this.minY + ", minZ:" + this.minZ + ", maxX:" + this.maxX + ", maxY:" + this.maxY + ", maxZ:" + this.maxZ + "}";
    }
}

