/*
 * Decompiled with CFR 0.152.
 */
package org.newdawn.slick.geom;

import java.util.ArrayList;
import org.newdawn.slick.geom.Curve;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.geom.Vector2f;

public class Path
extends Shape {
    private ArrayList localPoints = new ArrayList();
    private float cx;
    private float cy;
    private boolean closed;
    private ArrayList holes = new ArrayList();
    private ArrayList hole;

    public Path(float sx, float sy) {
        this.localPoints.add(new float[]{sx, sy});
        this.cx = sx;
        this.cy = sy;
        this.pointsDirty = true;
    }

    public void startHole(float sx, float sy) {
        this.hole = new ArrayList();
        this.holes.add(this.hole);
    }

    public void lineTo(float x2, float y2) {
        if (this.hole != null) {
            this.hole.add(new float[]{x2, y2});
        } else {
            this.localPoints.add(new float[]{x2, y2});
        }
        this.cx = x2;
        this.cy = y2;
        this.pointsDirty = true;
    }

    public void close() {
        this.closed = true;
    }

    public void curveTo(float x2, float y2, float cx1, float cy1, float cx2, float cy2) {
        this.curveTo(x2, y2, cx1, cy1, cx2, cy2, 10);
    }

    public void curveTo(float x2, float y2, float cx1, float cy1, float cx2, float cy2, int segments) {
        if (this.cx == x2 && this.cy == y2) {
            return;
        }
        Curve curve = new Curve(new Vector2f(this.cx, this.cy), new Vector2f(cx1, cy1), new Vector2f(cx2, cy2), new Vector2f(x2, y2));
        float step = 1.0f / (float)segments;
        for (int i2 = 1; i2 < segments + 1; ++i2) {
            float t2 = (float)i2 * step;
            Vector2f p2 = curve.pointAt(t2);
            if (this.hole != null) {
                this.hole.add(new float[]{p2.x, p2.y});
            } else {
                this.localPoints.add(new float[]{p2.x, p2.y});
            }
            this.cx = p2.x;
            this.cy = p2.y;
        }
        this.pointsDirty = true;
    }

    protected void createPoints() {
        this.points = new float[this.localPoints.size() * 2];
        for (int i2 = 0; i2 < this.localPoints.size(); ++i2) {
            float[] p2 = (float[])this.localPoints.get(i2);
            this.points[i2 * 2] = p2[0];
            this.points[i2 * 2 + 1] = p2[1];
        }
    }

    public Shape transform(Transform transform) {
        Path p2 = new Path(this.cx, this.cy);
        p2.localPoints = this.transform(this.localPoints, transform);
        for (int i2 = 0; i2 < this.holes.size(); ++i2) {
            p2.holes.add(this.transform((ArrayList)this.holes.get(i2), transform));
        }
        p2.closed = this.closed;
        return p2;
    }

    private ArrayList transform(ArrayList pts, Transform t2) {
        float[] in = new float[pts.size() * 2];
        float[] out = new float[pts.size() * 2];
        for (int i2 = 0; i2 < pts.size(); ++i2) {
            in[i2 * 2] = ((float[])pts.get(i2))[0];
            in[i2 * 2 + 1] = ((float[])pts.get(i2))[1];
        }
        t2.transform(in, 0, out, 0, pts.size());
        ArrayList<float[]> outList = new ArrayList<float[]>();
        for (int i3 = 0; i3 < pts.size(); ++i3) {
            outList.add(new float[]{out[i3 * 2], out[i3 * 2 + 1]});
        }
        return outList;
    }

    public boolean closed() {
        return this.closed;
    }
}

