/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.impl.ps;

import com.sun.javafx.geom.Ellipse2D;
import com.sun.javafx.geom.Shape;
import com.sun.prism.Graphics;
import com.sun.prism.impl.ps.CachingShapeRepState;

class CachingEllipseRepState
extends CachingShapeRepState {
    CachingEllipseRepState() {
    }

    @Override
    void fillNoCache(Graphics graphics, Shape shape) {
        Ellipse2D ellipse2D = (Ellipse2D)shape;
        graphics.fillEllipse(ellipse2D.x, ellipse2D.y, ellipse2D.width, ellipse2D.height);
    }

    @Override
    void drawNoCache(Graphics graphics, Shape shape) {
        Ellipse2D ellipse2D = (Ellipse2D)shape;
        graphics.drawEllipse(ellipse2D.x, ellipse2D.y, ellipse2D.width, ellipse2D.height);
    }
}

