/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.impl.shape;

import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.prism.BasicStroke;
import com.sun.prism.impl.shape.MaskData;

public interface ShapeRasterizer {
    public MaskData getMaskData(Shape var1, BasicStroke var2, RectBounds var3, BaseTransform var4, boolean var5, boolean var6);
}

