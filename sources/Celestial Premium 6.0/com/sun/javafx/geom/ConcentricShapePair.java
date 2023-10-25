/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.geom;

import com.sun.javafx.geom.PathIterator;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.ShapePair;
import com.sun.javafx.geom.transform.BaseTransform;

public final class ConcentricShapePair
extends ShapePair {
    private final Shape outer;
    private final Shape inner;

    public ConcentricShapePair(Shape shape, Shape shape2) {
        this.outer = shape;
        this.inner = shape2;
    }

    @Override
    public int getCombinationType() {
        return 1;
    }

    @Override
    public Shape getOuterShape() {
        return this.outer;
    }

    @Override
    public Shape getInnerShape() {
        return this.inner;
    }

    @Override
    public Shape copy() {
        return new ConcentricShapePair(this.outer.copy(), this.inner.copy());
    }

    @Override
    public boolean contains(float f, float f2) {
        return this.outer.contains(f, f2) && !this.inner.contains(f, f2);
    }

    @Override
    public boolean intersects(float f, float f2, float f3, float f4) {
        return this.outer.intersects(f, f2, f3, f4) && !this.inner.contains(f, f2, f3, f4);
    }

    @Override
    public boolean contains(float f, float f2, float f3, float f4) {
        return this.outer.contains(f, f2, f3, f4) && !this.inner.intersects(f, f2, f3, f4);
    }

    @Override
    public RectBounds getBounds() {
        return this.outer.getBounds();
    }

    @Override
    public PathIterator getPathIterator(BaseTransform baseTransform) {
        return new PairIterator(this.outer.getPathIterator(baseTransform), this.inner.getPathIterator(baseTransform));
    }

    @Override
    public PathIterator getPathIterator(BaseTransform baseTransform, float f) {
        return new PairIterator(this.outer.getPathIterator(baseTransform, f), this.inner.getPathIterator(baseTransform, f));
    }

    static class PairIterator
    implements PathIterator {
        PathIterator outer;
        PathIterator inner;

        PairIterator(PathIterator pathIterator, PathIterator pathIterator2) {
            this.outer = pathIterator;
            this.inner = pathIterator2;
        }

        @Override
        public int getWindingRule() {
            return 0;
        }

        @Override
        public int currentSegment(float[] arrf) {
            if (this.outer.isDone()) {
                return this.inner.currentSegment(arrf);
            }
            return this.outer.currentSegment(arrf);
        }

        @Override
        public boolean isDone() {
            return this.outer.isDone() && this.inner.isDone();
        }

        @Override
        public void next() {
            if (this.outer.isDone()) {
                this.inner.next();
            } else {
                this.outer.next();
            }
        }
    }
}

