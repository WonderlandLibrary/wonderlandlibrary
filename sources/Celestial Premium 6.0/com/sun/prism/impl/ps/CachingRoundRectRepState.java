/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.impl.ps;

import com.sun.javafx.geom.RoundRectangle2D;
import com.sun.javafx.geom.Shape;
import com.sun.prism.Graphics;
import com.sun.prism.impl.ps.CachingShapeRepState;
import com.sun.prism.impl.shape.BasicRoundRectRep;

class CachingRoundRectRepState
extends CachingShapeRepState {
    CachingRoundRectRepState() {
    }

    @Override
    void fillNoCache(Graphics graphics, Shape shape) {
        BasicRoundRectRep.fillRoundRect(graphics, (RoundRectangle2D)shape);
    }

    @Override
    void drawNoCache(Graphics graphics, Shape shape) {
        BasicRoundRectRep.drawRoundRect(graphics, (RoundRectangle2D)shape);
    }
}

