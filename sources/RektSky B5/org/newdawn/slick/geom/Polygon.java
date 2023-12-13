/*
 * Decompiled with CFR 0.152.
 */
package org.newdawn.slick.geom;

import java.util.ArrayList;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;

public class Polygon
extends Shape {
    private boolean allowDups = false;
    private boolean closed = true;

    public Polygon(float[] points) {
        int length = points.length;
        this.points = new float[length];
        this.maxX = -1.4E-45f;
        this.maxY = -1.4E-45f;
        this.minX = Float.MAX_VALUE;
        this.minY = Float.MAX_VALUE;
        this.x = Float.MAX_VALUE;
        this.y = Float.MAX_VALUE;
        for (int i2 = 0; i2 < length; ++i2) {
            this.points[i2] = points[i2];
            if (i2 % 2 == 0) {
                if (points[i2] > this.maxX) {
                    this.maxX = points[i2];
                }
                if (points[i2] < this.minX) {
                    this.minX = points[i2];
                }
                if (!(points[i2] < this.x)) continue;
                this.x = points[i2];
                continue;
            }
            if (points[i2] > this.maxY) {
                this.maxY = points[i2];
            }
            if (points[i2] < this.minY) {
                this.minY = points[i2];
            }
            if (!(points[i2] < this.y)) continue;
            this.y = points[i2];
        }
        this.findCenter();
        this.calculateRadius();
        this.pointsDirty = true;
    }

    public Polygon() {
        this.points = new float[0];
        this.maxX = -1.4E-45f;
        this.maxY = -1.4E-45f;
        this.minX = Float.MAX_VALUE;
        this.minY = Float.MAX_VALUE;
    }

    public void setAllowDuplicatePoints(boolean allowDups) {
        this.allowDups = allowDups;
    }

    public void addPoint(float x2, float y2) {
        if (this.hasVertex(x2, y2) && !this.allowDups) {
            return;
        }
        ArrayList<Float> tempPoints = new ArrayList<Float>();
        for (int i2 = 0; i2 < this.points.length; ++i2) {
            tempPoints.add(new Float(this.points[i2]));
        }
        tempPoints.add(new Float(x2));
        tempPoints.add(new Float(y2));
        int length = tempPoints.size();
        this.points = new float[length];
        for (int i3 = 0; i3 < length; ++i3) {
            this.points[i3] = ((Float)tempPoints.get(i3)).floatValue();
        }
        if (x2 > this.maxX) {
            this.maxX = x2;
        }
        if (y2 > this.maxY) {
            this.maxY = y2;
        }
        if (x2 < this.minX) {
            this.minX = x2;
        }
        if (y2 < this.minY) {
            this.minY = y2;
        }
        this.findCenter();
        this.calculateRadius();
        this.pointsDirty = true;
    }

    public Shape transform(Transform transform) {
        this.checkPoints();
        Polygon resultPolygon = new Polygon();
        float[] result = new float[this.points.length];
        transform.transform(this.points, 0, result, 0, this.points.length / 2);
        resultPolygon.points = result;
        resultPolygon.findCenter();
        resultPolygon.closed = this.closed;
        return resultPolygon;
    }

    public void setX(float x2) {
        super.setX(x2);
        this.pointsDirty = false;
    }

    public void setY(float y2) {
        super.setY(y2);
        this.pointsDirty = false;
    }

    protected void createPoints() {
    }

    public boolean closed() {
        return this.closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public Polygon copy() {
        float[] copyPoints = new float[this.points.length];
        System.arraycopy(this.points, 0, copyPoints, 0, copyPoints.length);
        return new Polygon(copyPoints);
    }
}

