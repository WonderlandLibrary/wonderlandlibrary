/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.sg.prism;

import com.sun.javafx.geom.Shape;
import com.sun.javafx.sg.prism.NGShape;

public class NGSVGPath
extends NGShape {
    private Shape path;

    public void setContent(Object object) {
        this.path = (Shape)object;
        this.geometryChanged();
    }

    public Object getGeometry() {
        return this.path;
    }

    @Override
    public Shape getShape() {
        return this.path;
    }

    public boolean acceptsPath2dOnUpdate() {
        return true;
    }
}

