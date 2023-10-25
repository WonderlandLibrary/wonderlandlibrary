/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.geom;

import com.sun.javafx.geom.Line2D;
import com.sun.javafx.geom.PathIterator;
import com.sun.javafx.geom.transform.BaseTransform;
import java.util.NoSuchElementException;

class LineIterator
implements PathIterator {
    Line2D line;
    BaseTransform transform;
    int index;

    LineIterator(Line2D line2D, BaseTransform baseTransform) {
        this.line = line2D;
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
            throw new NoSuchElementException("line iterator out of bounds");
        }
        if (this.index == 0) {
            arrf[0] = this.line.x1;
            arrf[1] = this.line.y1;
            n = 0;
        } else {
            arrf[0] = this.line.x2;
            arrf[1] = this.line.y2;
            n = 1;
        }
        if (this.transform != null) {
            this.transform.transform(arrf, 0, arrf, 0, 1);
        }
        return n;
    }
}

