/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.geom;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.PathIterator;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.NoninvertibleTransformException;

public abstract class TransformedShape
extends Shape {
    protected final Shape delegate;
    private Shape cachedTransformedShape;

    public static TransformedShape transformedShape(Shape shape, BaseTransform baseTransform) {
        if (baseTransform.isTranslateOrIdentity()) {
            return TransformedShape.translatedShape(shape, baseTransform.getMxt(), baseTransform.getMyt());
        }
        return new General(shape, baseTransform.copy());
    }

    public static TransformedShape translatedShape(Shape shape, double d, double d2) {
        return new Translate(shape, (float)d, (float)d2);
    }

    protected TransformedShape(Shape shape) {
        this.delegate = shape;
    }

    public Shape getDelegateNoClone() {
        return this.delegate;
    }

    public abstract BaseTransform getTransformNoClone();

    public abstract BaseTransform adjust(BaseTransform var1);

    protected Point2D untransform(float f, float f2) {
        Point2D point2D = new Point2D(f, f2);
        try {
            point2D = this.getTransformNoClone().inverseTransform(point2D, point2D);
        }
        catch (NoninvertibleTransformException noninvertibleTransformException) {
            // empty catch block
        }
        return point2D;
    }

    protected BaseBounds untransformedBounds(float f, float f2, float f3, float f4) {
        RectBounds rectBounds = new RectBounds(f, f2, f + f3, f2 + f4);
        try {
            return this.getTransformNoClone().inverseTransform(rectBounds, rectBounds);
        }
        catch (NoninvertibleTransformException noninvertibleTransformException) {
            return rectBounds.makeEmpty();
        }
    }

    @Override
    public RectBounds getBounds() {
        float[] arrf = new float[4];
        Shape.accumulate(arrf, this.delegate, this.getTransformNoClone());
        return new RectBounds(arrf[0], arrf[1], arrf[2], arrf[3]);
    }

    @Override
    public boolean contains(float f, float f2) {
        return this.delegate.contains(this.untransform(f, f2));
    }

    private Shape getCachedTransformedShape() {
        if (this.cachedTransformedShape == null) {
            this.cachedTransformedShape = this.copy();
        }
        return this.cachedTransformedShape;
    }

    @Override
    public boolean intersects(float f, float f2, float f3, float f4) {
        return this.getCachedTransformedShape().intersects(f, f2, f3, f4);
    }

    @Override
    public boolean contains(float f, float f2, float f3, float f4) {
        return this.getCachedTransformedShape().contains(f, f2, f3, f4);
    }

    @Override
    public PathIterator getPathIterator(BaseTransform baseTransform) {
        return this.delegate.getPathIterator(this.adjust(baseTransform));
    }

    @Override
    public PathIterator getPathIterator(BaseTransform baseTransform, float f) {
        return this.delegate.getPathIterator(this.adjust(baseTransform), f);
    }

    @Override
    public Shape copy() {
        return this.getTransformNoClone().createTransformedShape(this.delegate);
    }

    static final class General
    extends TransformedShape {
        BaseTransform transform;

        General(Shape shape, BaseTransform baseTransform) {
            super(shape);
            this.transform = baseTransform;
        }

        @Override
        public BaseTransform getTransformNoClone() {
            return this.transform;
        }

        @Override
        public BaseTransform adjust(BaseTransform baseTransform) {
            if (baseTransform == null || baseTransform.isIdentity()) {
                return this.transform.copy();
            }
            return baseTransform.copy().deriveWithConcatenation(this.transform);
        }
    }

    static final class Translate
    extends TransformedShape {
        private final float tx;
        private final float ty;
        private BaseTransform cachedTx;

        public Translate(Shape shape, float f, float f2) {
            super(shape);
            this.tx = f;
            this.ty = f2;
        }

        @Override
        public BaseTransform getTransformNoClone() {
            if (this.cachedTx == null) {
                this.cachedTx = BaseTransform.getTranslateInstance(this.tx, this.ty);
            }
            return this.cachedTx;
        }

        @Override
        public BaseTransform adjust(BaseTransform baseTransform) {
            if (baseTransform == null || baseTransform.isIdentity()) {
                return BaseTransform.getTranslateInstance(this.tx, this.ty);
            }
            return baseTransform.copy().deriveWithTranslation(this.tx, this.ty);
        }

        @Override
        public RectBounds getBounds() {
            RectBounds rectBounds = this.delegate.getBounds();
            rectBounds.setBounds(rectBounds.getMinX() + this.tx, rectBounds.getMinY() + this.ty, rectBounds.getMaxX() + this.tx, rectBounds.getMaxY() + this.ty);
            return rectBounds;
        }

        @Override
        public boolean contains(float f, float f2) {
            return this.delegate.contains(f - this.tx, f2 - this.ty);
        }

        @Override
        public boolean intersects(float f, float f2, float f3, float f4) {
            return this.delegate.intersects(f - this.tx, f2 - this.ty, f3, f4);
        }

        @Override
        public boolean contains(float f, float f2, float f3, float f4) {
            return this.delegate.contains(f - this.tx, f2 - this.ty, f3, f4);
        }
    }
}

