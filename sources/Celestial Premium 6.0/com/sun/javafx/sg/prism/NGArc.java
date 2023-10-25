/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.shape.ArcType
 */
package com.sun.javafx.sg.prism;

import com.sun.javafx.geom.Arc2D;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.sg.prism.NGShape;
import com.sun.prism.Graphics;
import com.sun.prism.shape.ShapeRep;
import javafx.scene.shape.ArcType;

public class NGArc
extends NGShape {
    private Arc2D arc = new Arc2D();

    public void updateArc(float f, float f2, float f3, float f4, float f5, float f6, ArcType arcType) {
        this.arc.x = f - f3;
        this.arc.width = f3 * 2.0f;
        this.arc.y = f2 - f4;
        this.arc.height = f4 * 2.0f;
        this.arc.start = f5;
        this.arc.extent = f6;
        if (arcType == ArcType.CHORD) {
            this.arc.setArcType(1);
        } else if (arcType == ArcType.OPEN) {
            this.arc.setArcType(0);
        } else if (arcType == ArcType.ROUND) {
            this.arc.setArcType(2);
        } else {
            throw new AssertionError((Object)"Unknown arc type specified");
        }
        this.geometryChanged();
    }

    @Override
    public Shape getShape() {
        return this.arc;
    }

    @Override
    protected ShapeRep createShapeRep(Graphics graphics) {
        return graphics.getResourceFactory().createArcRep();
    }
}

