/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.impl.shape;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.Shape;
import com.sun.prism.Graphics;
import com.sun.prism.shape.ShapeRep;

public class BasicShapeRep
implements ShapeRep {
    @Override
    public boolean is3DCapable() {
        return false;
    }

    @Override
    public void invalidate(ShapeRep.InvalidationType invalidationType) {
    }

    @Override
    public void fill(Graphics graphics, Shape shape, BaseBounds baseBounds) {
        graphics.fill(shape);
    }

    @Override
    public void draw(Graphics graphics, Shape shape, BaseBounds baseBounds) {
        graphics.draw(shape);
    }

    @Override
    public void dispose() {
    }
}

