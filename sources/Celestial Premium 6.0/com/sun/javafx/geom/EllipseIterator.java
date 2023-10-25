/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.geom;

import com.sun.javafx.geom.Ellipse2D;
import com.sun.javafx.geom.PathIterator;
import com.sun.javafx.geom.transform.BaseTransform;
import java.util.NoSuchElementException;

class EllipseIterator
implements PathIterator {
    double x;
    double y;
    double w;
    double h;
    BaseTransform transform;
    int index;
    public static final double CtrlVal = 0.5522847498307933;
    private static final double pcv = 0.7761423749153966;
    private static final double ncv = 0.22385762508460333;
    private static final double[][] ctrlpts = new double[][]{{1.0, 0.7761423749153966, 0.7761423749153966, 1.0, 0.5, 1.0}, {0.22385762508460333, 1.0, 0.0, 0.7761423749153966, 0.0, 0.5}, {0.0, 0.22385762508460333, 0.22385762508460333, 0.0, 0.5, 0.0}, {0.7761423749153966, 0.0, 1.0, 0.22385762508460333, 1.0, 0.5}};

    EllipseIterator(Ellipse2D ellipse2D, BaseTransform baseTransform) {
        this.x = ellipse2D.x;
        this.y = ellipse2D.y;
        this.w = ellipse2D.width;
        this.h = ellipse2D.height;
        this.transform = baseTransform;
        if (this.w < 0.0 || this.h < 0.0) {
            this.index = 6;
        }
    }

    @Override
    public int getWindingRule() {
        return 1;
    }

    @Override
    public boolean isDone() {
        return this.index > 5;
    }

    @Override
    public void next() {
        ++this.index;
    }

    @Override
    public int currentSegment(float[] arrf) {
        if (this.isDone()) {
            throw new NoSuchElementException("ellipse iterator out of bounds");
        }
        if (this.index == 5) {
            return 4;
        }
        if (this.index == 0) {
            double[] arrd = ctrlpts[3];
            arrf[0] = (float)(this.x + arrd[4] * this.w);
            arrf[1] = (float)(this.y + arrd[5] * this.h);
            if (this.transform != null) {
                this.transform.transform(arrf, 0, arrf, 0, 1);
            }
            return 0;
        }
        double[] arrd = ctrlpts[this.index - 1];
        arrf[0] = (float)(this.x + arrd[0] * this.w);
        arrf[1] = (float)(this.y + arrd[1] * this.h);
        arrf[2] = (float)(this.x + arrd[2] * this.w);
        arrf[3] = (float)(this.y + arrd[3] * this.h);
        arrf[4] = (float)(this.x + arrd[4] * this.w);
        arrf[5] = (float)(this.y + arrd[5] * this.h);
        if (this.transform != null) {
            this.transform.transform(arrf, 0, arrf, 0, 3);
        }
        return 3;
    }
}

