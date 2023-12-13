/*
 * Decompiled with CFR 0.152.
 */
package org.newdawn.slick.geom;

import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;

public class Rectangle
extends Shape {
    protected float width;
    protected float height;

    public Rectangle(float x2, float y2, float width, float height) {
        this.x = x2;
        this.y = y2;
        this.width = width;
        this.height = height;
        this.maxX = x2 + width;
        this.maxY = y2 + height;
        this.checkPoints();
    }

    public boolean contains(float xp, float yp) {
        if (xp <= this.getX()) {
            return false;
        }
        if (yp <= this.getY()) {
            return false;
        }
        if (xp >= this.maxX) {
            return false;
        }
        return !(yp >= this.maxY);
    }

    public void setBounds(Rectangle other) {
        this.setBounds(other.getX(), other.getY(), other.getWidth(), other.getHeight());
    }

    public void setBounds(float x2, float y2, float width, float height) {
        this.setX(x2);
        this.setY(y2);
        this.setSize(width, height);
    }

    public void setSize(float width, float height) {
        this.setWidth(width);
        this.setHeight(height);
    }

    public float getWidth() {
        return this.width;
    }

    public float getHeight() {
        return this.height;
    }

    public void grow(float h2, float v2) {
        this.setX(this.getX() - h2);
        this.setY(this.getY() - v2);
        this.setWidth(this.getWidth() + h2 * 2.0f);
        this.setHeight(this.getHeight() + v2 * 2.0f);
    }

    public void scaleGrow(float h2, float v2) {
        this.grow(this.getWidth() * (h2 - 1.0f), this.getHeight() * (v2 - 1.0f));
    }

    public void setWidth(float width) {
        if (width != this.width) {
            this.pointsDirty = true;
            this.width = width;
            this.maxX = this.x + width;
        }
    }

    public void setHeight(float height) {
        if (height != this.height) {
            this.pointsDirty = true;
            this.height = height;
            this.maxY = this.y + height;
        }
    }

    public boolean intersects(Shape shape) {
        if (shape instanceof Rectangle) {
            Rectangle other = (Rectangle)shape;
            if (this.x > other.x + other.width || this.x + this.width < other.x) {
                return false;
            }
            return !(this.y > other.y + other.height) && !(this.y + this.height < other.y);
        }
        if (shape instanceof Circle) {
            return this.intersects((Circle)shape);
        }
        return super.intersects(shape);
    }

    protected void createPoints() {
        float useWidth = this.width;
        float useHeight = this.height;
        this.points = new float[8];
        this.points[0] = this.x;
        this.points[1] = this.y;
        this.points[2] = this.x + useWidth;
        this.points[3] = this.y;
        this.points[4] = this.x + useWidth;
        this.points[5] = this.y + useHeight;
        this.points[6] = this.x;
        this.points[7] = this.y + useHeight;
        this.maxX = this.points[2];
        this.maxY = this.points[5];
        this.minX = this.points[0];
        this.minY = this.points[1];
        this.findCenter();
        this.calculateRadius();
    }

    private boolean intersects(Circle other) {
        return other.intersects((Shape)this);
    }

    public String toString() {
        return "[Rectangle " + this.width + "x" + this.height + "]";
    }

    public static boolean contains(float xp, float yp, float xr, float yr, float widthr, float heightr) {
        return xp >= xr && yp >= yr && xp <= xr + widthr && yp <= yr + heightr;
    }

    public Shape transform(Transform transform) {
        this.checkPoints();
        Polygon resultPolygon = new Polygon();
        float[] result = new float[this.points.length];
        transform.transform(this.points, 0, result, 0, this.points.length / 2);
        resultPolygon.points = result;
        resultPolygon.findCenter();
        resultPolygon.checkPoints();
        return resultPolygon;
    }
}

