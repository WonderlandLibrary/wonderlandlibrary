/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.geom;

import com.sun.javafx.geom.PathIterator;
import com.sun.javafx.geom.QuadCurve2D;
import com.sun.javafx.geom.transform.BaseTransform;
import java.util.NoSuchElementException;

class QuadIterator
implements PathIterator {
    QuadCurve2D quad;
    BaseTransform transform;
    int index;

    QuadIterator(QuadCurve2D quadCurve2D, BaseTransform baseTransform) {
        this.quad = quadCurve2D;
        this.transform = baseTransform;
    }

    @Override
    public int getWindingRule() {
        return 1;
    }

    @Override
    public boolean isDone() {
        return this.index > 1;
    }

    @Override
    public void next() {
        ++this.index;
    }

    @Override
    public int currentSegment(float[] arrf) {
        int n;
        if (this.isDone()) {
            throw new NoSuchElementException("quad iterator iterator out of bounds");
        }
        if (this.index == 0) {
            arrf[0] = this.quad.x1;
            arrf[1] = this.quad.y1;
            n = 0;
        } else {
            arrf[0] = this.quad.ctrlx;
            arrf[1] = this.quad.ctrly;
            arrf[2] = this.quad.x2;
            arrf[3] = this.quad.y2;
            n = 2;
        }
        if (this.transform != null) {
            this.transform.transform(arrf, 0, arrf, 0, this.index == 0 ? 1 : 2);
        }
        return n;
    }
}

