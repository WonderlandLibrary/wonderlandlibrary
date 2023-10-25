/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.Node
 *  javafx.scene.shape.Shape3D
 */
package com.sun.javafx.scene.shape;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.NodeHelper;
import com.sun.javafx.util.Utils;
import javafx.scene.Node;
import javafx.scene.shape.Shape3D;

public abstract class Shape3DHelper
extends NodeHelper {
    private static Shape3DAccessor shape3DAccessor;

    @Override
    protected void updatePeerImpl(Node node) {
        super.updatePeerImpl(node);
        shape3DAccessor.doUpdatePeer(node);
    }

    @Override
    protected BaseBounds computeGeomBoundsImpl(Node node, BaseBounds baseBounds, BaseTransform baseTransform) {
        return shape3DAccessor.doComputeGeomBounds(node, baseBounds, baseTransform);
    }

    @Override
    protected boolean computeContainsImpl(Node node, double d, double d2) {
        return shape3DAccessor.doComputeContains(node, d, d2);
    }

    public static void setShape3DAccessor(Shape3DAccessor shape3DAccessor) {
        if (Shape3DHelper.shape3DAccessor != null) {
            throw new IllegalStateException();
        }
        Shape3DHelper.shape3DAccessor = shape3DAccessor;
    }

    static {
        Utils.forceInit(Shape3D.class);
    }

    public static interface Shape3DAccessor {
        public void doUpdatePeer(Node var1);

        public BaseBounds doComputeGeomBounds(Node var1, BaseBounds var2, BaseTransform var3);

        public boolean doComputeContains(Node var1, double var2, double var4);
    }
}

