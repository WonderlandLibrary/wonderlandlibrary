/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.TransformFilter;
import java.awt.Point;
import java.awt.Rectangle;

public class RotateFilter
extends TransformFilter {
    private float angle;
    private float cos;
    private float sin;
    private boolean resize = true;

    public RotateFilter() {
        this((float)Math.PI);
    }

    public RotateFilter(float angle) {
        this(angle, true);
    }

    public RotateFilter(float angle, boolean resize) {
        this.setAngle(angle);
        this.resize = resize;
    }

    public void setAngle(float angle) {
        this.angle = angle;
        this.cos = (float)Math.cos(this.angle);
        this.sin = (float)Math.sin(this.angle);
    }

    public float getAngle() {
        return this.angle;
    }

    @Override
    protected void transformSpace(Rectangle rect) {
        if (this.resize) {
            Point out = new Point(0, 0);
            int minx = Integer.MAX_VALUE;
            int miny = Integer.MAX_VALUE;
            int maxx = Integer.MIN_VALUE;
            int maxy = Integer.MIN_VALUE;
            int w = rect.width;
            int h = rect.height;
            int x = rect.x;
            int y = rect.y;
            for (int i = 0; i < 4; ++i) {
                switch (i) {
                    case 0: {
                        this.transform(x, y, out);
                        break;
                    }
                    case 1: {
                        this.transform(x + w, y, out);
                        break;
                    }
                    case 2: {
                        this.transform(x, y + h, out);
                        break;
                    }
                    case 3: {
                        this.transform(x + w, y + h, out);
                    }
                }
                minx = Math.min(minx, out.x);
                miny = Math.min(miny, out.y);
                maxx = Math.max(maxx, out.x);
                maxy = Math.max(maxy, out.y);
            }
            rect.x = minx;
            rect.y = miny;
            rect.width = maxx - rect.x;
            rect.height = maxy - rect.y;
        }
    }

    private void transform(int x, int y, Point out) {
        out.x = (int)((float)x * this.cos + (float)y * this.sin);
        out.y = (int)((float)y * this.cos - (float)x * this.sin);
    }

    @Override
    protected void transformInverse(int x, int y, float[] out) {
        out[0] = (float)x * this.cos - (float)y * this.sin;
        out[1] = (float)y * this.cos + (float)x * this.sin;
    }

    public String toString() {
        return "Rotate " + (int)((double)(this.angle * 180.0f) / Math.PI);
    }
}

