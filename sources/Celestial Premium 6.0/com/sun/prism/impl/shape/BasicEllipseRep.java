/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.impl.shape;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.Ellipse2D;
import com.sun.javafx.geom.Shape;
import com.sun.prism.Graphics;
import com.sun.prism.impl.shape.BasicShapeRep;

public class BasicEllipseRep
extends BasicShapeRep {
    @Override
    public void fill(Graphics graphics, Shape shape, BaseBounds baseBounds) {
        Ellipse2D ellipse2D = (Ellipse2D)shape;
        graphics.fillEllipse(ellipse2D.x, ellipse2D.y, ellipse2D.width, ellipse2D.height);
    }

    @Override
    public void draw(Graphics graphics, Shape shape, BaseBounds baseBounds) {
        Ellipse2D ellipse2D = (Ellipse2D)shape;
        graphics.drawEllipse(ellipse2D.x, ellipse2D.y, ellipse2D.width, ellipse2D.height);
    }
}

