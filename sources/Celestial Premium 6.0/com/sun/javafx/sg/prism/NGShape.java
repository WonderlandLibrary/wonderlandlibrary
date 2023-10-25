/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.shape.StrokeLineCap
 *  javafx.scene.shape.StrokeLineJoin
 *  javafx.scene.shape.StrokeType
 */
package com.sun.javafx.sg.prism;

import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.prism.BasicStroke;
import com.sun.prism.Graphics;
import com.sun.prism.PrinterGraphics;
import com.sun.prism.RTTexture;
import com.sun.prism.Texture;
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.paint.Paint;
import com.sun.prism.shape.ShapeRep;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;

public abstract class NGShape
extends NGNode {
    private RTTexture cached3D;
    private double cachedW;
    private double cachedH;
    protected Paint fillPaint;
    protected Paint drawPaint;
    protected BasicStroke drawStroke;
    protected Mode mode = Mode.FILL;
    protected ShapeRep shapeRep;
    private boolean smooth;
    static final double THRESHOLD = 0.00390625;

    public void setMode(Mode mode) {
        if (mode != this.mode) {
            this.mode = mode;
            this.geometryChanged();
        }
    }

    public Mode getMode() {
        return this.mode;
    }

    public void setSmooth(boolean bl) {
        boolean bl2 = bl = !PrismSettings.forceNonAntialiasedShape && bl;
        if (bl != this.smooth) {
            this.smooth = bl;
            this.visualsChanged();
        }
    }

    public boolean isSmooth() {
        return this.smooth;
    }

    public void setFillPaint(Object object) {
        if (object != this.fillPaint || this.fillPaint != null && this.fillPaint.isMutable()) {
            this.fillPaint = (Paint)object;
            this.visualsChanged();
            this.invalidateOpaqueRegion();
        }
    }

    public Paint getFillPaint() {
        return this.fillPaint;
    }

    public void setDrawPaint(Object object) {
        if (object != this.drawPaint || this.drawPaint != null && this.drawPaint.isMutable()) {
            this.drawPaint = (Paint)object;
            this.visualsChanged();
        }
    }

    public void setDrawStroke(BasicStroke basicStroke) {
        if (this.drawStroke != basicStroke) {
            this.drawStroke = basicStroke;
            this.geometryChanged();
        }
    }

    public void setDrawStroke(float f, StrokeType strokeType, StrokeLineCap strokeLineCap, StrokeLineJoin strokeLineJoin, float f2, float[] arrf, float f3) {
        int n = strokeType == StrokeType.CENTERED ? 0 : (strokeType == StrokeType.INSIDE ? 1 : 2);
        int n2 = strokeLineCap == StrokeLineCap.BUTT ? 0 : (strokeLineCap == StrokeLineCap.SQUARE ? 2 : 1);
        int n3 = strokeLineJoin == StrokeLineJoin.BEVEL ? 2 : (strokeLineJoin == StrokeLineJoin.MITER ? 0 : 1);
        if (this.drawStroke == null) {
            this.drawStroke = new BasicStroke(n, f, n2, n3, f2);
        } else {
            this.drawStroke.set(n, f, n2, n3, f2);
        }
        if (arrf.length > 0) {
            this.drawStroke.set(arrf, f3);
        } else {
            this.drawStroke.set((float[])null, 0.0f);
        }
        this.geometryChanged();
    }

    public abstract Shape getShape();

    protected ShapeRep createShapeRep(Graphics graphics) {
        return graphics.getResourceFactory().createPathRep();
    }

    @Override
    protected void visualsChanged() {
        super.visualsChanged();
        if (this.cached3D != null) {
            this.cached3D.dispose();
            this.cached3D = null;
        }
    }

    private static double hypot(double d, double d2, double d3) {
        return Math.sqrt(d * d + d2 * d2 + d3 * d3);
    }

    @Override
    protected void renderContent(Graphics graphics) {
        boolean bl;
        if (this.mode == Mode.EMPTY) {
            return;
        }
        boolean bl2 = graphics instanceof PrinterGraphics;
        BaseTransform baseTransform = graphics.getTransformNoClone();
        boolean bl3 = bl = !baseTransform.is2D();
        if (bl) {
            int n;
            int n2;
            double d = NGShape.hypot(baseTransform.getMxx(), baseTransform.getMyx(), baseTransform.getMzx());
            double d2 = NGShape.hypot(baseTransform.getMxy(), baseTransform.getMyy(), baseTransform.getMzy());
            double d3 = d * (double)this.contentBounds.getWidth();
            double d4 = d2 * (double)this.contentBounds.getHeight();
            if (this.cached3D != null) {
                this.cached3D.lock();
                if (this.cached3D.isSurfaceLost() || Math.max(Math.abs(d3 - this.cachedW), Math.abs(d4 - this.cachedH)) > 0.00390625) {
                    this.cached3D.unlock();
                    this.cached3D.dispose();
                    this.cached3D = null;
                }
            }
            if (this.cached3D == null) {
                n2 = (int)Math.ceil(d3);
                n = (int)Math.ceil(d4);
                this.cachedW = d3;
                this.cachedH = d4;
                if (n2 <= 0 || n <= 0) {
                    return;
                }
                this.cached3D = graphics.getResourceFactory().createRTTexture(n2, n, Texture.WrapMode.CLAMP_TO_ZERO, false);
                this.cached3D.setLinearFiltering(this.isSmooth());
                this.cached3D.contentsUseful();
                Graphics graphics2 = this.cached3D.createGraphics();
                graphics2.scale((float)d, (float)d2);
                graphics2.translate(-this.contentBounds.getMinX(), -this.contentBounds.getMinY());
                this.renderContent2D(graphics2, bl2);
            }
            n2 = this.cached3D.getContentWidth();
            n = this.cached3D.getContentHeight();
            float f = this.contentBounds.getMinX();
            float f2 = this.contentBounds.getMinY();
            float f3 = f + (float)((double)n2 / d);
            float f4 = f2 + (float)((double)n / d2);
            graphics.drawTexture(this.cached3D, f, f2, f3, f4, 0.0f, 0.0f, n2, n);
            this.cached3D.unlock();
        } else {
            if (this.cached3D != null) {
                this.cached3D.dispose();
                this.cached3D = null;
            }
            this.renderContent2D(graphics, bl2);
        }
    }

    protected void renderContent2D(Graphics graphics, boolean bl) {
        ShapeRep shapeRep;
        boolean bl2 = graphics.isAntialiasedShape();
        boolean bl3 = this.isSmooth();
        if (bl3 != bl2) {
            graphics.setAntialiasedShape(bl3);
        }
        ShapeRep shapeRep2 = shapeRep = bl ? null : this.shapeRep;
        if (shapeRep == null) {
            shapeRep = this.createShapeRep(graphics);
        }
        Shape shape = this.getShape();
        if (this.mode != Mode.STROKE) {
            graphics.setPaint(this.fillPaint);
            shapeRep.fill(graphics, shape, this.contentBounds);
        }
        if (this.mode != Mode.FILL && this.drawStroke.getLineWidth() > 0.0f) {
            graphics.setPaint(this.drawPaint);
            graphics.setStroke(this.drawStroke);
            shapeRep.draw(graphics, shape, this.contentBounds);
        }
        if (bl3 != bl2) {
            graphics.setAntialiasedShape(bl2);
        }
        if (!bl) {
            this.shapeRep = shapeRep;
        }
    }

    @Override
    protected boolean hasOverlappingContents() {
        return this.mode == Mode.STROKE_FILL;
    }

    protected Shape getStrokeShape() {
        return this.drawStroke.createStrokedShape(this.getShape());
    }

    @Override
    protected void geometryChanged() {
        super.geometryChanged();
        if (this.shapeRep != null) {
            this.shapeRep.invalidate(ShapeRep.InvalidationType.LOCATION_AND_GEOMETRY);
        }
        if (this.cached3D != null) {
            this.cached3D.dispose();
            this.cached3D = null;
        }
    }

    @Override
    protected boolean hasOpaqueRegion() {
        Mode mode = this.getMode();
        Paint paint = this.getFillPaint();
        return super.hasOpaqueRegion() && (mode == Mode.FILL || mode == Mode.STROKE_FILL) && paint != null && paint.isOpaque();
    }

    public static final class Mode
    extends Enum<Mode> {
        public static final /* enum */ Mode EMPTY = new Mode();
        public static final /* enum */ Mode FILL = new Mode();
        public static final /* enum */ Mode STROKE = new Mode();
        public static final /* enum */ Mode STROKE_FILL = new Mode();
        private static final /* synthetic */ Mode[] $VALUES;

        public static Mode[] values() {
            return (Mode[])$VALUES.clone();
        }

        public static Mode valueOf(String string) {
            return Enum.valueOf(Mode.class, string);
        }

        private static /* synthetic */ Mode[] $values() {
            return new Mode[]{EMPTY, FILL, STROKE, STROKE_FILL};
        }

        static {
            $VALUES = Mode.$values();
        }
    }
}

