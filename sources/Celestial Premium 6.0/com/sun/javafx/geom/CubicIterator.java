/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.geom;

import com.sun.javafx.geom.CubicCurve2D;
import com.sun.javafx.geom.PathIterator;
import com.sun.javafx.geom.transform.BaseTransform;
import java.util.NoSuchElementException;

class CubicIterator
implements PathIterator {
    CubicCurve2D cubic;
    BaseTransform transform;
    int index;

    CubicIterator(CubicCurve2D cubicCurve2D, BaseTransform baseTransform) {
        this.cubic = cubicCurve2D;
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
            throw new NoSuchElementException("cubic iterator iterator out of bounds");
        }
        if (this.index == 0) {
            arrf[0] = this.cubic.x1;
            arrf[1] = this.cubic.y1;
            n = 0;
        } else {
            arrf[0] = this.cubic.ctrlx1;
            arrf[1] = this.cubic.ctrly1;
            arrf[2] = this.cubic.ctrlx2;
            arrf[3] = this.cubic.ctrly2;
            arrf[4] = this.cubic.x2;
            arrf[5] = this.cubic.y2;
            n = 3;
        }
        if (this.transform != null) {
            this.transform.transform(arrf, 0, arrf, 0, this.index == 0 ? 1 : 3);
        }
        return n;
    }
}

