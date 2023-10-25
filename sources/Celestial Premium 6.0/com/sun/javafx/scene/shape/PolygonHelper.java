/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.Node
 *  javafx.scene.shape.Polygon
 *  javafx.scene.shape.Shape
 */
package com.sun.javafx.scene.shape;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.shape.ShapeHelper;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.util.Utils;
import javafx.scene.Node;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

public class PolygonHelper
extends ShapeHelper {
    private static final PolygonHelper theInstance = new PolygonHelper();
    private static PolygonAccessor polygonAccessor;

    private static PolygonHelper getInstance() {
        return theInstance;
    }

    public static void initHelper(Polygon polygon) {
        PolygonHelper.setHelper((Node)polygon, PolygonHelper.getInstance());
    }

    @Override
    protected NGNode createPeerImpl(Node node) {
        return polygonAccessor.doCreatePeer(node);
    }

    @Override
    protected void updatePeerImpl(Node node) {
        super.updatePeerImpl(node);
        polygonAccessor.doUpdatePeer(node);
    }

    @Override
    protected BaseBounds computeGeomBoundsImpl(Node node, BaseBounds baseBounds, BaseTransform baseTransform) {
        return polygonAccessor.doComputeGeomBounds(node, baseBounds, baseTransform);
    }

    @Override
    protected com.sun.javafx.geom.Shape configShapeImpl(Shape shape) {
        return polygonAccessor.doConfigShape(shape);
    }

    public static void setPolygonAccessor(PolygonAccessor polygonAccessor) {
        if (PolygonHelper.polygonAccessor != null) {
            throw new IllegalStateException();
        }
        PolygonHelper.polygonAccessor = polygonAccessor;
    }

    static {
        Utils.forceInit(Polygon.class);
    }

    public static interface PolygonAccessor {
        public NGNode doCreatePeer(Node var1);

        public void doUpdatePeer(Node var1);

        public BaseBounds doComputeGeomBounds(Node var1, BaseBounds var2, BaseTransform var3);

        public com.sun.javafx.geom.Shape doConfigShape(Shape var1);
    }
}

