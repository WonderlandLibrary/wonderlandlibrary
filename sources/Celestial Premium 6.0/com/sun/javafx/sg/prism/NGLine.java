/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.sg.prism;

import com.sun.javafx.geom.Line2D;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.sg.prism.NGShape;
import com.sun.prism.Graphics;

public class NGLine
extends NGShape {
    private Line2D line = new Line2D();

    public void updateLine(float f, float f2, float f3, float f4) {
        this.line.x1 = f;
        this.line.y1 = f2;
        this.line.x2 = f3;
        this.line.y2 = f4;
        this.geometryChanged();
    }

    @Override
    protected void renderContent2D(Graphics graphics, boolean bl) {
        if ((this.mode == NGShape.Mode.STROKE || this.mode == NGShape.Mode.STROKE_FILL) && this.drawStroke.getLineWidth() > 0.0f && this.drawStroke.getType() != 1) {
            graphics.setPaint(this.drawPaint);
            graphics.setStroke(this.drawStroke);
            graphics.drawLine(this.line.x1, this.line.y1, this.line.x2, this.line.y2);
        }
    }

    @Override
    public final Shape getShape() {
        return this.line;
    }
}

