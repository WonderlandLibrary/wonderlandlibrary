/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.impl.shape;

import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.marlin.DMarlinRenderingEngine;
import com.sun.marlin.MarlinRenderer;
import com.sun.marlin.MaskMarlinAlphaConsumer;
import com.sun.marlin.RendererContext;
import com.sun.prism.BasicStroke;
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.impl.shape.DMarlinPrismUtils;
import com.sun.prism.impl.shape.MaskData;
import com.sun.prism.impl.shape.ShapeRasterizer;

public final class DMarlinRasterizer
implements ShapeRasterizer {
    private static final MaskData EMPTY_MASK = MaskData.create(new byte[1], 0, 0, 1, 1);

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public MaskData getMaskData(Shape shape, BasicStroke basicStroke, RectBounds rectBounds, BaseTransform baseTransform, boolean bl, boolean bl2) {
        if (basicStroke != null && basicStroke.getType() != 0) {
            shape = basicStroke.createStrokedShape(shape);
            basicStroke = null;
        }
        if (rectBounds == null) {
            if (basicStroke != null) {
                shape = basicStroke.createStrokedShape(shape);
                basicStroke = null;
            }
            rectBounds = new RectBounds();
            rectBounds = (RectBounds)baseTransform.transform(shape.getBounds(), rectBounds);
        }
        if (rectBounds.isEmpty()) {
            return EMPTY_MASK;
        }
        RendererContext rendererContext = DMarlinRenderingEngine.getRendererContext();
        MarlinRenderer marlinRenderer = null;
        try {
            Rectangle rectangle = rendererContext.clip;
            rectangle.setBounds(rectBounds);
            marlinRenderer = DMarlinPrismUtils.setupRenderer(rendererContext, shape, basicStroke, baseTransform, rectangle, bl2);
            int n = marlinRenderer.getOutpixMinX();
            int n2 = marlinRenderer.getOutpixMaxX();
            int n3 = marlinRenderer.getOutpixMinY();
            int n4 = marlinRenderer.getOutpixMaxY();
            int n5 = n2 - n;
            int n6 = n4 - n3;
            if (n5 <= 0 || n6 <= 0) {
                MaskData maskData = EMPTY_MASK;
                return maskData;
            }
            MaskMarlinAlphaConsumer maskMarlinAlphaConsumer = rendererContext.consumer;
            if (maskMarlinAlphaConsumer == null || n5 * n6 > maskMarlinAlphaConsumer.getAlphaLength()) {
                int n7 = n5 * n6 + 4095 & 0xFFFFF000;
                rendererContext.consumer = maskMarlinAlphaConsumer = new MaskMarlinAlphaConsumer(n7);
                if (PrismSettings.verbose) {
                    System.out.println("new alphas with length = " + n7);
                }
            }
            maskMarlinAlphaConsumer.setBoundsNoClone(n, n3, n5, n6);
            marlinRenderer.produceAlphas(maskMarlinAlphaConsumer);
            MaskData maskData = maskMarlinAlphaConsumer.getMaskData();
            return maskData;
        }
        finally {
            if (marlinRenderer != null) {
                marlinRenderer.dispose();
            }
            DMarlinRenderingEngine.returnRendererContext(rendererContext);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    static Shape createCenteredStrokedShape(Shape shape, BasicStroke basicStroke) {
        float f = basicStroke.getType() == 0 ? basicStroke.getLineWidth() : basicStroke.getLineWidth() * 2.0f;
        RendererContext rendererContext = DMarlinRenderingEngine.getRendererContext();
        try {
            Path2D path2D = rendererContext.getPath2D();
            DMarlinPrismUtils.strokeTo(rendererContext, shape, basicStroke, f, rendererContext.transformerPC2D.wrapPath2D(path2D));
            Path2D path2D2 = new Path2D(path2D);
            return path2D2;
        }
        finally {
            DMarlinRenderingEngine.returnRendererContext(rendererContext);
        }
    }
}

