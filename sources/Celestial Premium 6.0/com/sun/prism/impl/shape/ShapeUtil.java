/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.impl.shape;

import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.prism.BasicStroke;
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.impl.shape.DMarlinRasterizer;
import com.sun.prism.impl.shape.MaskData;
import com.sun.prism.impl.shape.ShapeRasterizer;

public class ShapeUtil {
    private static final ShapeRasterizer shapeRasterizer;

    public static MaskData rasterizeShape(Shape shape, BasicStroke basicStroke, RectBounds rectBounds, BaseTransform baseTransform, boolean bl, boolean bl2) {
        return shapeRasterizer.getMaskData(shape, basicStroke, rectBounds, baseTransform, bl, bl2);
    }

    public static Shape createCenteredStrokedShape(Shape shape, BasicStroke basicStroke) {
        return DMarlinRasterizer.createCenteredStrokedShape(shape, basicStroke);
    }

    private ShapeUtil() {
    }

    static {
        switch (PrismSettings.rasterizerSpec) {
            default: 
        }
        shapeRasterizer = new DMarlinRasterizer();
    }
}

