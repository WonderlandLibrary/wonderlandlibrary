/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.j2d.paint;

import com.sun.prism.j2d.paint.MultipleGradientPaint;
import com.sun.prism.j2d.paint.RadialGradientPaintContext;
import java.awt.Color;
import java.awt.PaintContext;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.ColorModel;

public final class RadialGradientPaint
extends MultipleGradientPaint {
    private final Point2D focus;
    private final Point2D center;
    private final float radius;

    public RadialGradientPaint(float f, float f2, float f3, float[] arrf, Color[] arrcolor) {
        this(f, f2, f3, f, f2, arrf, arrcolor, MultipleGradientPaint.CycleMethod.NO_CYCLE);
    }

    public RadialGradientPaint(Point2D point2D, float f, float[] arrf, Color[] arrcolor) {
        this(point2D, f, point2D, arrf, arrcolor, MultipleGradientPaint.CycleMethod.NO_CYCLE);
    }

    public RadialGradientPaint(float f, float f2, float f3, float[] arrf, Color[] arrcolor, MultipleGradientPaint.CycleMethod cycleMethod) {
        this(f, f2, f3, f, f2, arrf, arrcolor, cycleMethod);
    }

    public RadialGradientPaint(Point2D point2D, float f, float[] arrf, Color[] arrcolor, MultipleGradientPaint.CycleMethod cycleMethod) {
        this(point2D, f, point2D, arrf, arrcolor, cycleMethod);
    }

    public RadialGradientPaint(float f, float f2, float f3, float f4, float f5, float[] arrf, Color[] arrcolor, MultipleGradientPaint.CycleMethod cycleMethod) {
        this(new Point2D.Float(f, f2), f3, new Point2D.Float(f4, f5), arrf, arrcolor, cycleMethod);
    }

    public RadialGradientPaint(Point2D point2D, float f, Point2D point2D2, float[] arrf, Color[] arrcolor, MultipleGradientPaint.CycleMethod cycleMethod) {
        this(point2D, f, point2D2, arrf, arrcolor, cycleMethod, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform());
    }

    public RadialGradientPaint(Point2D point2D, float f, Point2D point2D2, float[] arrf, Color[] arrcolor, MultipleGradientPaint.CycleMethod cycleMethod, MultipleGradientPaint.ColorSpaceType colorSpaceType, AffineTransform affineTransform) {
        super(arrf, arrcolor, cycleMethod, colorSpaceType, affineTransform);
        if (point2D == null) {
            throw new NullPointerException("Center point must be non-null");
        }
        if (point2D2 == null) {
            throw new NullPointerException("Focus point must be non-null");
        }
        if (f < 0.0f) {
            throw new IllegalArgumentException("Radius must be non-negative");
        }
        this.center = new Point2D.Double(point2D.getX(), point2D.getY());
        this.focus = new Point2D.Double(point2D2.getX(), point2D2.getY());
        this.radius = f;
    }

    public RadialGradientPaint(Rectangle2D rectangle2D, float[] arrf, Color[] arrcolor, MultipleGradientPaint.CycleMethod cycleMethod) {
        this(new Point2D.Double(rectangle2D.getCenterX(), rectangle2D.getCenterY()), 1.0f, new Point2D.Double(rectangle2D.getCenterX(), rectangle2D.getCenterY()), arrf, arrcolor, cycleMethod, MultipleGradientPaint.ColorSpaceType.SRGB, RadialGradientPaint.createGradientTransform(rectangle2D));
        if (rectangle2D.isEmpty()) {
            throw new IllegalArgumentException("Gradient bounds must be non-empty");
        }
    }

    private static AffineTransform createGradientTransform(Rectangle2D rectangle2D) {
        double d = rectangle2D.getCenterX();
        double d2 = rectangle2D.getCenterY();
        AffineTransform affineTransform = AffineTransform.getTranslateInstance(d, d2);
        affineTransform.scale(rectangle2D.getWidth() / 2.0, rectangle2D.getHeight() / 2.0);
        affineTransform.translate(-d, -d2);
        return affineTransform;
    }

    @Override
    public PaintContext createContext(ColorModel colorModel, Rectangle rectangle, Rectangle2D rectangle2D, AffineTransform affineTransform, RenderingHints renderingHints) {
        affineTransform = new AffineTransform(affineTransform);
        affineTransform.concatenate(this.gradientTransform);
        return new RadialGradientPaintContext(this, colorModel, rectangle, rectangle2D, affineTransform, renderingHints, (float)this.center.getX(), (float)this.center.getY(), this.radius, (float)this.focus.getX(), (float)this.focus.getY(), this.fractions, this.colors, this.cycleMethod, this.colorSpace);
    }

    public Point2D getCenterPoint() {
        return new Point2D.Double(this.center.getX(), this.center.getY());
    }

    public Point2D getFocusPoint() {
        return new Point2D.Double(this.focus.getX(), this.focus.getY());
    }

    public float getRadius() {
        return this.radius;
    }
}

