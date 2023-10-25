/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.Node
 *  javafx.scene.paint.Paint
 *  javafx.scene.shape.Polyline
 *  javafx.scene.shape.Shape
 */
package com.sun.javafx.scene.shape;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.shape.ShapeHelper;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.util.Utils;
import javafx.scene.Node;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Shape;

public class PolylineHelper
extends ShapeHelper {
    private static final PolylineHelper theInstance = new PolylineHelper();
    private static PolylineAccessor polylineAccessor;

    private static PolylineHelper getInstance() {
        return theInstance;
    }

    public static void initHelper(Polyline polyline) {
        PolylineHelper.setHelper((Node)polyline, PolylineHelper.getInstance());
    }

    @Override
    protected NGNode createPeerImpl(Node node) {
        return polylineAccessor.doCreatePeer(node);
    }

    @Override
    protected void updatePeerImpl(Node node) {
        super.updatePeerImpl(node);
        polylineAccessor.doUpdatePeer(node);
    }

    @Override
    protected BaseBounds computeGeomBoundsImpl(Node node, BaseBounds baseBounds, BaseTransform baseTransform) {
        return polylineAccessor.doComputeGeomBounds(node, baseBounds, baseTransform);
    }

    @Override
    protected Paint cssGetFillInitialValueImpl(Shape shape) {
        return polylineAccessor.doCssGetFillInitialValue(shape);
    }

    @Override
    protected Paint cssGetStrokeInitialValueImpl(Shape shape) {
        return polylineAccessor.doCssGetStrokeInitialValue(shape);
    }

    @Override
    protected com.sun.javafx.geom.Shape configShapeImpl(Shape shape) {
        return polylineAccessor.doConfigShape(shape);
    }

    public static void setPolylineAccessor(PolylineAccessor polylineAccessor) {
        if (PolylineHelper.polylineAccessor != null) {
            throw new IllegalStateException();
        }
        PolylineHelper.polylineAccessor = polylineAccessor;
    }

    static {
        Utils.forceInit(Polyline.class);
    }

    public static interface PolylineAccessor {
        public NGNode doCreatePeer(Node var1);

        public void doUpdatePeer(Node var1);

        public BaseBounds doComputeGeomBounds(Node var1, BaseBounds var2, BaseTransform var3);

        public Paint doCssGetFillInitialValue(Shape var1);

        public Paint doCssGetStrokeInitialValue(Shape var1);

        public com.sun.javafx.geom.Shape doConfigShape(Shape var1);
    }
}

