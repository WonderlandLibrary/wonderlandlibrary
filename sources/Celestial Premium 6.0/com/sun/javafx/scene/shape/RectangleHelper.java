/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.Node
 *  javafx.scene.shape.Rectangle
 *  javafx.scene.shape.Shape
 */
package com.sun.javafx.scene.shape;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.shape.ShapeHelper;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.util.Utils;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class RectangleHelper
extends ShapeHelper {
    private static final RectangleHelper theInstance = new RectangleHelper();
    private static RectangleAccessor rectangleAccessor;

    private static RectangleHelper getInstance() {
        return theInstance;
    }

    public static void initHelper(Rectangle rectangle) {
        RectangleHelper.setHelper((Node)rectangle, RectangleHelper.getInstance());
    }

    @Override
    protected NGNode createPeerImpl(Node node) {
        return rectangleAccessor.doCreatePeer(node);
    }

    @Override
    protected void updatePeerImpl(Node node) {
        super.updatePeerImpl(node);
        rectangleAccessor.doUpdatePeer(node);
    }

    @Override
    protected BaseBounds computeGeomBoundsImpl(Node node, BaseBounds baseBounds, BaseTransform baseTransform) {
        return rectangleAccessor.doComputeGeomBounds(node, baseBounds, baseTransform);
    }

    @Override
    protected com.sun.javafx.geom.Shape configShapeImpl(Shape shape) {
        return rectangleAccessor.doConfigShape(shape);
    }

    public static void setRectangleAccessor(RectangleAccessor rectangleAccessor) {
        if (RectangleHelper.rectangleAccessor != null) {
            throw new IllegalStateException();
        }
        RectangleHelper.rectangleAccessor = rectangleAccessor;
    }

    static {
        Utils.forceInit(Rectangle.class);
    }

    public static interface RectangleAccessor {
        public NGNode doCreatePeer(Node var1);

        public void doUpdatePeer(Node var1);

        public BaseBounds doComputeGeomBounds(Node var1, BaseBounds var2, BaseTransform var3);

        public com.sun.javafx.geom.Shape doConfigShape(Shape var1);
    }
}

