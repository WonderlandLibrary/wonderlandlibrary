/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.Node
 *  javafx.scene.shape.Ellipse
 *  javafx.scene.shape.Shape
 */
package com.sun.javafx.scene.shape;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.shape.ShapeHelper;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.util.Utils;
import javafx.scene.Node;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Shape;

public class EllipseHelper
extends ShapeHelper {
    private static final EllipseHelper theInstance = new EllipseHelper();
    private static EllipseAccessor ellipseAccessor;

    private static EllipseHelper getInstance() {
        return theInstance;
    }

    public static void initHelper(Ellipse ellipse) {
        EllipseHelper.setHelper((Node)ellipse, EllipseHelper.getInstance());
    }

    @Override
    protected NGNode createPeerImpl(Node node) {
        return ellipseAccessor.doCreatePeer(node);
    }

    @Override
    protected void updatePeerImpl(Node node) {
        super.updatePeerImpl(node);
        ellipseAccessor.doUpdatePeer(node);
    }

    @Override
    protected BaseBounds computeGeomBoundsImpl(Node node, BaseBounds baseBounds, BaseTransform baseTransform) {
        return ellipseAccessor.doComputeGeomBounds(node, baseBounds, baseTransform);
    }

    @Override
    protected com.sun.javafx.geom.Shape configShapeImpl(Shape shape) {
        return ellipseAccessor.doConfigShape(shape);
    }

    public static void setEllipseAccessor(EllipseAccessor ellipseAccessor) {
        if (EllipseHelper.ellipseAccessor != null) {
            throw new IllegalStateException();
        }
        EllipseHelper.ellipseAccessor = ellipseAccessor;
    }

    static {
        Utils.forceInit(Ellipse.class);
    }

    public static interface EllipseAccessor {
        public NGNode doCreatePeer(Node var1);

        public void doUpdatePeer(Node var1);

        public BaseBounds doComputeGeomBounds(Node var1, BaseBounds var2, BaseTransform var3);

        public com.sun.javafx.geom.Shape doConfigShape(Shape var1);
    }
}

