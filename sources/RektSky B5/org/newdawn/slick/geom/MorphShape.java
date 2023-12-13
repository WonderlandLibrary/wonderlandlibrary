/*
 * Decompiled with CFR 0.152.
 */
package org.newdawn.slick.geom;

import java.util.ArrayList;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;

public class MorphShape
extends Shape {
    private ArrayList shapes = new ArrayList();
    private float offset;
    private Shape current;
    private Shape next;

    public MorphShape(Shape base) {
        this.shapes.add(base);
        float[] copy = base.points;
        this.points = new float[copy.length];
        this.current = base;
        this.next = base;
    }

    public void addShape(Shape shape) {
        if (shape.points.length != this.points.length) {
            throw new RuntimeException("Attempt to morph between two shapes with different vertex counts");
        }
        Shape prev = (Shape)this.shapes.get(this.shapes.size() - 1);
        if (this.equalShapes(prev, shape)) {
            this.shapes.add(prev);
        } else {
            this.shapes.add(shape);
        }
        if (this.shapes.size() == 2) {
            this.next = (Shape)this.shapes.get(1);
        }
    }

    private boolean equalShapes(Shape a2, Shape b2) {
        a2.checkPoints();
        b2.checkPoints();
        for (int i2 = 0; i2 < a2.points.length; ++i2) {
            if (a2.points[i2] == b2.points[i2]) continue;
            return false;
        }
        return true;
    }

    public void setMorphTime(float time) {
        int p2 = (int)time;
        int n2 = p2 + 1;
        float offset = time - (float)p2;
        p2 = this.rational(p2);
        n2 = this.rational(n2);
        this.setFrame(p2, n2, offset);
    }

    public void updateMorphTime(float delta) {
        this.offset += delta;
        if (this.offset < 0.0f) {
            int index = this.shapes.indexOf(this.current);
            if (index < 0) {
                index = this.shapes.size() - 1;
            }
            int nframe = this.rational(index + 1);
            this.setFrame(index, nframe, this.offset);
            this.offset += 1.0f;
        } else if (this.offset > 1.0f) {
            int index = this.shapes.indexOf(this.next);
            if (index < 1) {
                index = 0;
            }
            int nframe = this.rational(index + 1);
            this.setFrame(index, nframe, this.offset);
            this.offset -= 1.0f;
        } else {
            this.pointsDirty = true;
        }
    }

    public void setExternalFrame(Shape current) {
        this.current = current;
        this.next = (Shape)this.shapes.get(0);
        this.offset = 0.0f;
    }

    private int rational(int n2) {
        while (n2 >= this.shapes.size()) {
            n2 -= this.shapes.size();
        }
        while (n2 < 0) {
            n2 += this.shapes.size();
        }
        return n2;
    }

    private void setFrame(int a2, int b2, float offset) {
        this.current = (Shape)this.shapes.get(a2);
        this.next = (Shape)this.shapes.get(b2);
        this.offset = offset;
        this.pointsDirty = true;
    }

    protected void createPoints() {
        if (this.current == this.next) {
            System.arraycopy(this.current.points, 0, this.points, 0, this.points.length);
            return;
        }
        float[] apoints = this.current.points;
        float[] bpoints = this.next.points;
        for (int i2 = 0; i2 < this.points.length; ++i2) {
            this.points[i2] = apoints[i2] * (1.0f - this.offset);
            int n2 = i2;
            this.points[n2] = this.points[n2] + bpoints[i2] * this.offset;
        }
    }

    public Shape transform(Transform transform) {
        this.createPoints();
        Polygon poly = new Polygon(this.points);
        return poly;
    }
}

