/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.geom;

import com.sun.javafx.geom.PathIterator;
import com.sun.javafx.geom.RoundRectangle2D;
import com.sun.javafx.geom.transform.BaseTransform;
import java.util.NoSuchElementException;

class RoundRectIterator
implements PathIterator {
    double x;
    double y;
    double w;
    double h;
    double aw;
    double ah;
    BaseTransform transform;
    int index;
    private static final double angle = 0.7853981633974483;
    private static final double a = 1.0 - Math.cos(0.7853981633974483);
    private static final double b = Math.tan(0.7853981633974483);
    private static final double c = Math.sqrt(1.0 + b * b) - 1.0 + a;
    private static final double cv = 1.3333333333333333 * a * b / c;
    private static final double acv = (1.0 - cv) / 2.0;
    private static final double[][] ctrlpts = new double[][]{{0.0, 0.0, 0.0, 0.5}, {0.0, 0.0, 1.0, -0.5}, {0.0, 0.0, 1.0, -acv, 0.0, acv, 1.0, 0.0, 0.0, 0.5, 1.0, 0.0}, {1.0, -0.5, 1.0, 0.0}, {1.0, -acv, 1.0, 0.0, 1.0, 0.0, 1.0, -acv, 1.0, 0.0, 1.0, -0.5}, {1.0, 0.0, 0.0, 0.5}, {1.0, 0.0, 0.0, acv, 1.0, -acv, 0.0, 0.0, 1.0, -0.5, 0.0, 0.0}, {0.0, 0.5, 0.0, 0.0}, {0.0, acv, 0.0, 0.0, 0.0, 0.0, 0.0, acv, 0.0, 0.0, 0.0, 0.5}, new double[0]};
    private static final int[] types = new int[]{0, 1, 3, 1, 3, 1, 3, 1, 3, 4};

    RoundRectIterator(RoundRectangle2D roundRectangle2D, BaseTransform baseTransform) {
        this.x = roundRectangle2D.x;
        this.y = roundRectangle2D.y;
        this.w = roundRectangle2D.width;
        this.h = roundRectangle2D.height;
        this.aw = Math.min(this.w, (double)Math.abs(roundRectangle2D.arcWidth));
        this.ah = Math.min(this.h, (double)Math.abs(roundRectangle2D.arcHeight));
        this.transform = baseTransform;
        if (this.aw < 0.0 || this.ah < 0.0) {
            this.index = ctrlpts.length;
        }
    }

    @Override
    public int getWindingRule() {
        return 1;
    }

    @Override
    public boolean isDone() {
        return this.index >= ctrlpts.length;
    }

    @Override
    public void next() {
        ++this.index;
        if (this.index < ctrlpts.length && this.aw == 0.0 && this.ah == 0.0 && types[this.index] == 3) {
            ++this.index;
        }
    }

    @Override
    public int currentSegment(float[] arrf) {
        if (this.isDone()) {
            throw new NoSuchElementException("roundrect iterator out of bounds");
        }
        double[] arrd = ctrlpts[this.index];
        int n = 0;
        for (int i = 0; i < arrd.length; i += 4) {
            arrf[n++] = (float)(this.x + arrd[i + 0] * this.w + arrd[i + 1] * this.aw);
            arrf[n++] = (float)(this.y + arrd[i + 2] * this.h + arrd[i + 3] * this.ah);
        }
        if (this.transform != null) {
            this.transform.transform(arrf, 0, arrf, 0, n / 2);
        }
        return types[this.index];
    }
}

