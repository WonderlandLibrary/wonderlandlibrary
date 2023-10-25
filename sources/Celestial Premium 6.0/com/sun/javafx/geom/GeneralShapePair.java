/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.geom;

import com.sun.javafx.geom.FlatteningPathIterator;
import com.sun.javafx.geom.PathIterator;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.ShapePair;
import com.sun.javafx.geom.transform.BaseTransform;

public class GeneralShapePair
extends ShapePair {
    private final Shape outer;
    private final Shape inner;
    private final int combinationType;

    public GeneralShapePair(Shape shape, Shape shape2, int n) {
        this.outer = shape;
        this.inner = shape2;
        this.combinationType = n;
    }

    @Override
    public final int getCombinationType() {
        return this.combinationType;
    }

    @Override
    public final Shape getOuterShape() {
        return this.outer;
    }

    @Override
    public final Shape getInnerShape() {
        return this.inner;
    }

    @Override
    public Shape copy() {
        return new GeneralShapePair(this.outer.copy(), this.inner.copy(), this.combinationType);
    }

    @Override
    public boolean contains(float f, float f2) {
        if (this.combinationType == 4) {
            return this.outer.contains(f, f2) && this.inner.contains(f, f2);
        }
        return this.outer.contains(f, f2) && !this.inner.contains(f, f2);
    }

    @Override
    public boolean intersects(float f, float f2, float f3, float f4) {
        if (this.combinationType == 4) {
            return this.outer.intersects(f, f2, f3, f4) && this.inner.intersects(f, f2, f3, f4);
        }
        return this.outer.intersects(f, f2, f3, f4) && !this.inner.contains(f, f2, f3, f4);
    }

    @Override
    public boolean contains(float f, float f2, float f3, float f4) {
        if (this.combinationType == 4) {
            return this.outer.contains(f, f2, f3, f4) && this.inner.contains(f, f2, f3, f4);
        }
        return this.outer.contains(f, f2, f3, f4) && !this.inner.intersects(f, f2, f3, f4);
    }

    @Override
    public RectBounds getBounds() {
        RectBounds rectBounds = this.outer.getBounds();
        if (this.combinationType == 4) {
            rectBounds.intersectWith(this.inner.getBounds());
        }
        return rectBounds;
    }

    @Override
    public PathIterator getPathIterator(BaseTransform baseTransform) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public PathIterator getPathIterator(BaseTransform baseTransform, float f) {
        return new FlatteningPathIterator(this.getPathIterator(baseTransform), f);
    }
}

