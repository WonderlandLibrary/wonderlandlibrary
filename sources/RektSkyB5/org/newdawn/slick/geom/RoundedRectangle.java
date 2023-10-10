/*
 * Decompiled with CFR 0.152.
 */
package org.newdawn.slick.geom;

import java.util.ArrayList;
import java.util.List;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.util.FastTrig;

public class RoundedRectangle
extends Rectangle {
    public static final int TOP_LEFT = 1;
    public static final int TOP_RIGHT = 2;
    public static final int BOTTOM_RIGHT = 4;
    public static final int BOTTOM_LEFT = 8;
    public static final int ALL = 15;
    private static final int DEFAULT_SEGMENT_COUNT = 25;
    private float cornerRadius;
    private int segmentCount;
    private int cornerFlags;

    public RoundedRectangle(float x2, float y2, float width, float height, float cornerRadius) {
        this(x2, y2, width, height, cornerRadius, 25);
    }

    public RoundedRectangle(float x2, float y2, float width, float height, float cornerRadius, int segmentCount) {
        this(x2, y2, width, height, cornerRadius, segmentCount, 15);
    }

    public RoundedRectangle(float x2, float y2, float width, float height, float cornerRadius, int segmentCount, int cornerFlags) {
        super(x2, y2, width, height);
        if (cornerRadius < 0.0f) {
            throw new IllegalArgumentException("corner radius must be >= 0");
        }
        this.x = x2;
        this.y = y2;
        this.width = width;
        this.height = height;
        this.cornerRadius = cornerRadius;
        this.segmentCount = segmentCount;
        this.pointsDirty = true;
        this.cornerFlags = cornerFlags;
    }

    public float getCornerRadius() {
        return this.cornerRadius;
    }

    public void setCornerRadius(float cornerRadius) {
        if (cornerRadius >= 0.0f && cornerRadius != this.cornerRadius) {
            this.cornerRadius = cornerRadius;
            this.pointsDirty = true;
        }
    }

    public float getHeight() {
        return this.height;
    }

    public void setHeight(float height) {
        if (this.height != height) {
            this.height = height;
            this.pointsDirty = true;
        }
    }

    public float getWidth() {
        return this.width;
    }

    public void setWidth(float width) {
        if (width != this.width) {
            this.width = width;
            this.pointsDirty = true;
        }
    }

    protected void createPoints() {
        this.maxX = this.x + this.width;
        this.maxY = this.y + this.height;
        this.minX = this.x;
        this.minY = this.y;
        float useWidth = this.width - 1.0f;
        float useHeight = this.height - 1.0f;
        if (this.cornerRadius == 0.0f) {
            this.points = new float[8];
            this.points[0] = this.x;
            this.points[1] = this.y;
            this.points[2] = this.x + useWidth;
            this.points[3] = this.y;
            this.points[4] = this.x + useWidth;
            this.points[5] = this.y + useHeight;
            this.points[6] = this.x;
            this.points[7] = this.y + useHeight;
        } else {
            float doubleRadius = this.cornerRadius * 2.0f;
            if (doubleRadius > useWidth) {
                doubleRadius = useWidth;
                this.cornerRadius = doubleRadius / 2.0f;
            }
            if (doubleRadius > useHeight) {
                doubleRadius = useHeight;
                this.cornerRadius = doubleRadius / 2.0f;
            }
            ArrayList<Float> tempPoints = new ArrayList<Float>();
            if ((this.cornerFlags & 1) != 0) {
                tempPoints.addAll(this.createPoints(this.segmentCount, this.cornerRadius, this.x + this.cornerRadius, this.y + this.cornerRadius, 180.0f, 270.0f));
            } else {
                tempPoints.add(new Float(this.x));
                tempPoints.add(new Float(this.y));
            }
            if ((this.cornerFlags & 2) != 0) {
                tempPoints.addAll(this.createPoints(this.segmentCount, this.cornerRadius, this.x + useWidth - this.cornerRadius, this.y + this.cornerRadius, 270.0f, 360.0f));
            } else {
                tempPoints.add(new Float(this.x + useWidth));
                tempPoints.add(new Float(this.y));
            }
            if ((this.cornerFlags & 4) != 0) {
                tempPoints.addAll(this.createPoints(this.segmentCount, this.cornerRadius, this.x + useWidth - this.cornerRadius, this.y + useHeight - this.cornerRadius, 0.0f, 90.0f));
            } else {
                tempPoints.add(new Float(this.x + useWidth));
                tempPoints.add(new Float(this.y + useHeight));
            }
            if ((this.cornerFlags & 8) != 0) {
                tempPoints.addAll(this.createPoints(this.segmentCount, this.cornerRadius, this.x + this.cornerRadius, this.y + useHeight - this.cornerRadius, 90.0f, 180.0f));
            } else {
                tempPoints.add(new Float(this.x));
                tempPoints.add(new Float(this.y + useHeight));
            }
            this.points = new float[tempPoints.size()];
            for (int i2 = 0; i2 < tempPoints.size(); ++i2) {
                this.points[i2] = ((Float)tempPoints.get(i2)).floatValue();
            }
        }
        this.findCenter();
        this.calculateRadius();
    }

    private List createPoints(int numberOfSegments, float radius, float cx, float cy, float start, float end) {
        ArrayList<Float> tempPoints = new ArrayList<Float>();
        int step = 360 / numberOfSegments;
        for (float a2 = start; a2 <= end + (float)step; a2 += (float)step) {
            float ang = a2;
            if (ang > end) {
                ang = end;
            }
            float x2 = (float)((double)cx + FastTrig.cos(Math.toRadians(ang)) * (double)radius);
            float y2 = (float)((double)cy + FastTrig.sin(Math.toRadians(ang)) * (double)radius);
            tempPoints.add(new Float(x2));
            tempPoints.add(new Float(y2));
        }
        return tempPoints;
    }

    public Shape transform(Transform transform) {
        this.checkPoints();
        Polygon resultPolygon = new Polygon();
        float[] result = new float[this.points.length];
        transform.transform(this.points, 0, result, 0, this.points.length / 2);
        resultPolygon.points = result;
        resultPolygon.findCenter();
        return resultPolygon;
    }
}

