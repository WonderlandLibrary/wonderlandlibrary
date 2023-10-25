/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.Node
 *  javafx.scene.shape.Circle
 *  javafx.scene.shape.Shape
 */
package com.sun.javafx.scene.shape;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.shape.ShapeHelper;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.util.Utils;
import javafx.scene.Node;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

public class CircleHelper
extends ShapeHelper {
    private static final CircleHelper theInstance = new CircleHelper();
    private static CircleAccessor circleAccessor;

    private static CircleHelper getInstance() {
        return theInstance;
    }

    public static void initHelper(Circle circle) {
        CircleHelper.setHelper((Node)circle, CircleHelper.getInstance());
    }

    @Override
    protected NGNode createPeerImpl(Node node) {
        return circleAccessor.doCreatePeer(node);
    }

    @Override
    protected void updatePeerImpl(Node node) {
        super.updatePeerImpl(node);
        circleAccessor.doUpdatePeer(node);
    }

    @Override
    protected BaseBounds computeGeomBoundsImpl(Node node, BaseBounds baseBounds, BaseTransform baseTransform) {
        return circleAccessor.doComputeGeomBounds(node, baseBounds, baseTransform);
    }

    @Override
    protected com.sun.javafx.geom.Shape configShapeImpl(Shape shape) {
        return circleAccessor.doConfigShape(shape);
    }

    public static void setCircleAccessor(CircleAccessor circleAccessor) {
        if (CircleHelper.circleAccessor != null) {
            throw new IllegalStateException();
        }
        CircleHelper.circleAccessor = circleAccessor;
    }

    static {
        Utils.forceInit(Circle.class);
    }

    public static interface CircleAccessor {
        public NGNode doCreatePeer(Node var1);

        public void doUpdatePeer(Node var1);

        public BaseBounds doComputeGeomBounds(Node var1, BaseBounds var2, BaseTransform var3);

        public com.sun.javafx.geom.Shape doConfigShape(Shape var1);
    }
}

