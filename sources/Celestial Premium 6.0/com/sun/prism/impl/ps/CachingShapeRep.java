/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.impl.ps;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Shape;
import com.sun.prism.Graphics;
import com.sun.prism.impl.ps.CachingShapeRepState;
import com.sun.prism.shape.ShapeRep;

public class CachingShapeRep
implements ShapeRep {
    private CachingShapeRepState fillState;
    private CachingShapeRepState drawState;

    CachingShapeRepState createState() {
        return new CachingShapeRepState();
    }

    @Override
    public boolean is3DCapable() {
        return false;
    }

    @Override
    public void invalidate(ShapeRep.InvalidationType invalidationType) {
        if (this.fillState != null) {
            this.fillState.invalidate();
        }
        if (this.drawState != null) {
            this.drawState.invalidate();
        }
    }

    @Override
    public void fill(Graphics graphics, Shape shape, BaseBounds baseBounds) {
        if (this.fillState == null) {
            this.fillState = this.createState();
        }
        this.fillState.render(graphics, shape, (RectBounds)baseBounds, null);
    }

    @Override
    public void draw(Graphics graphics, Shape shape, BaseBounds baseBounds) {
        if (this.drawState == null) {
            this.drawState = this.createState();
        }
        this.drawState.render(graphics, shape, (RectBounds)baseBounds, graphics.getStroke());
    }

    @Override
    public void dispose() {
        if (this.fillState != null) {
            this.fillState.dispose();
            this.fillState = null;
        }
        if (this.drawState != null) {
            this.drawState.dispose();
            this.drawState = null;
        }
    }
}

