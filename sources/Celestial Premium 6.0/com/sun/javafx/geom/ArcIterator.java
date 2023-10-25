/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.geom;

import com.sun.javafx.geom.Arc2D;
import com.sun.javafx.geom.PathIterator;
import com.sun.javafx.geom.transform.BaseTransform;
import java.util.NoSuchElementException;

class ArcIterator
implements PathIterator {
    double x;
    double y;
    double w;
    double h;
    double angStRad;
    double increment;
    double cv;
    BaseTransform transform;
    int index;
    int arcSegs;
    int lineSegs;

    ArcIterator(Arc2D arc2D, BaseTransform baseTransform) {
        this.w = arc2D.width / 2.0f;
        this.h = arc2D.height / 2.0f;
        this.x = (double)arc2D.x + this.w;
        this.y = (double)arc2D.y + this.h;
        this.angStRad = -Math.toRadians(arc2D.start);
        this.transform = baseTransform;
        double d = -arc2D.extent;
        if (d >= 360.0 || d <= -360.0) {
            this.arcSegs = 4;
            this.increment = 1.5707963267948966;
            this.cv = 0.5522847498307933;
            if (d < 0.0) {
                this.increment = -this.increment;
                this.cv = -this.cv;
            }
        } else {
            this.arcSegs = (int)Math.ceil(Math.abs(d) / 90.0);
            this.increment = Math.toRadians(d / (double)this.arcSegs);
            this.cv = ArcIterator.btan(this.increment);
            if (this.cv == 0.0) {
                this.arcSegs = 0;
            }
        }
        switch (arc2D.getArcType()) {
            case 0: {
                this.lineSegs = 0;
                break;
            }
            case 1: {
                this.lineSegs = 1;
                break;
            }
            case 2: {
                this.lineSegs = 2;
            }
        }
        if (this.w < 0.0 || this.h < 0.0) {
            this.lineSegs = -1;
            this.arcSegs = -1;
        }
    }

    @Override
    public int getWindingRule() {
        return 1;
    }

    @Override
    public boolean isDone() {
        return this.index > this.arcSegs + this.lineSegs;
    }

    @Override
    public void next() {
        ++this.index;
    }

    private static double btan(double d) {
        return 1.3333333333333333 * Math.sin(d /= 2.0) / (1.0 + Math.cos(d));
    }

    @Override
    public int currentSegment(float[] arrf) {
        if (this.isDone()) {
            throw new NoSuchElementException("arc iterator out of bounds");
        }
        double d = this.angStRad;
        if (this.index == 0) {
            arrf[0] = (float)(this.x + Math.cos(d) * this.w);
            arrf[1] = (float)(this.y + Math.sin(d) * this.h);
            if (this.transform != null) {
                this.transform.transform(arrf, 0, arrf, 0, 1);
            }
            return 0;
        }
        if (this.index > this.arcSegs) {
            if (this.index == this.arcSegs + this.lineSegs) {
                return 4;
            }
            arrf[0] = (float)this.x;
            arrf[1] = (float)this.y;
            if (this.transform != null) {
                this.transform.transform(arrf, 0, arrf, 0, 1);
            }
            return 1;
        }
        double d2 = Math.cos(d += this.increment * (double)(this.index - 1));
        double d3 = Math.sin(d);
        arrf[0] = (float)(this.x + (d2 - this.cv * d3) * this.w);
        arrf[1] = (float)(this.y + (d3 + this.cv * d2) * this.h);
        d2 = Math.cos(d += this.increment);
        d3 = Math.sin(d);
        arrf[2] = (float)(this.x + (d2 + this.cv * d3) * this.w);
        arrf[3] = (float)(this.y + (d3 - this.cv * d2) * this.h);
        arrf[4] = (float)(this.x + d2 * this.w);
        arrf[5] = (float)(this.y + d3 * this.h);
        if (this.transform != null) {
            this.transform.transform(arrf, 0, arrf, 0, 3);
        }
        return 3;
    }
}

