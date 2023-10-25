/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.Node
 *  javafx.scene.paint.Paint
 *  javafx.scene.shape.Shape
 */
package com.sun.javafx.scene.shape;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.DirtyBits;
import com.sun.javafx.scene.NodeHelper;
import com.sun.javafx.sg.prism.NGShape;
import com.sun.javafx.util.Utils;
import javafx.scene.Node;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Shape;

public abstract class ShapeHelper
extends NodeHelper {
    private static ShapeAccessor shapeAccessor;

    public static Paint cssGetFillInitialValue(Shape shape) {
        return ((ShapeHelper)ShapeHelper.getHelper((Node)shape)).cssGetFillInitialValueImpl(shape);
    }

    public static Paint cssGetStrokeInitialValue(Shape shape) {
        return ((ShapeHelper)ShapeHelper.getHelper((Node)shape)).cssGetStrokeInitialValueImpl(shape);
    }

    public static com.sun.javafx.geom.Shape configShape(Shape shape) {
        return ((ShapeHelper)ShapeHelper.getHelper((Node)shape)).configShapeImpl(shape);
    }

    @Override
    protected void updatePeerImpl(Node node) {
        super.updatePeerImpl(node);
        shapeAccessor.doUpdatePeer(node);
    }

    @Override
    protected void markDirtyImpl(Node node, DirtyBits dirtyBits) {
        shapeAccessor.doMarkDirty(node, dirtyBits);
        super.markDirtyImpl(node, dirtyBits);
    }

    @Override
    protected BaseBounds computeGeomBoundsImpl(Node node, BaseBounds baseBounds, BaseTransform baseTransform) {
        return shapeAccessor.doComputeGeomBounds(node, baseBounds, baseTransform);
    }

    @Override
    protected boolean computeContainsImpl(Node node, double d, double d2) {
        return shapeAccessor.doComputeContains(node, d, d2);
    }

    protected Paint cssGetFillInitialValueImpl(Shape shape) {
        return shapeAccessor.doCssGetFillInitialValue(shape);
    }

    protected Paint cssGetStrokeInitialValueImpl(Shape shape) {
        return shapeAccessor.doCssGetStrokeInitialValue(shape);
    }

    protected abstract com.sun.javafx.geom.Shape configShapeImpl(Shape var1);

    public static NGShape.Mode getMode(Shape shape) {
        return shapeAccessor.getMode(shape);
    }

    public static void setMode(Shape shape, NGShape.Mode mode) {
        shapeAccessor.setMode(shape, mode);
    }

    public static void setShapeChangeListener(Shape shape, Runnable runnable) {
        shapeAccessor.setShapeChangeListener(shape, runnable);
    }

    public static void setShapeAccessor(ShapeAccessor shapeAccessor) {
        if (ShapeHelper.shapeAccessor != null) {
            throw new IllegalStateException();
        }
        ShapeHelper.shapeAccessor = shapeAccessor;
    }

    static {
        Utils.forceInit(Shape.class);
    }

    public static interface ShapeAccessor {
        public void doUpdatePeer(Node var1);

        public void doMarkDirty(Node var1, DirtyBits var2);

        public BaseBounds doComputeGeomBounds(Node var1, BaseBounds var2, BaseTransform var3);

        public boolean doComputeContains(Node var1, double var2, double var4);

        public Paint doCssGetFillInitialValue(Shape var1);

        public Paint doCssGetStrokeInitialValue(Shape var1);

        public NGShape.Mode getMode(Shape var1);

        public void setMode(Shape var1, NGShape.Mode var2);

        public void setShapeChangeListener(Shape var1, Runnable var2);
    }
}

