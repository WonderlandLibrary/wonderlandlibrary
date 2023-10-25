/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.Node
 *  javafx.scene.shape.Arc
 *  javafx.scene.shape.Shape
 */
package com.sun.javafx.scene.shape;

import com.sun.javafx.scene.shape.ShapeHelper;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.util.Utils;
import javafx.scene.Node;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Shape;

public class ArcHelper
extends ShapeHelper {
    private static final ArcHelper theInstance = new ArcHelper();
    private static ArcAccessor arcAccessor;

    private static ArcHelper getInstance() {
        return theInstance;
    }

    public static void initHelper(Arc arc) {
        ArcHelper.setHelper((Node)arc, ArcHelper.getInstance());
    }

    @Override
    protected NGNode createPeerImpl(Node node) {
        return arcAccessor.doCreatePeer(node);
    }

    @Override
    protected void updatePeerImpl(Node node) {
        super.updatePeerImpl(node);
        arcAccessor.doUpdatePeer(node);
    }

    @Override
    protected com.sun.javafx.geom.Shape configShapeImpl(Shape shape) {
        return arcAccessor.doConfigShape(shape);
    }

    public static void setArcAccessor(ArcAccessor arcAccessor) {
        if (ArcHelper.arcAccessor != null) {
            throw new IllegalStateException();
        }
        ArcHelper.arcAccessor = arcAccessor;
    }

    static {
        Utils.forceInit(Arc.class);
    }

    public static interface ArcAccessor {
        public NGNode doCreatePeer(Node var1);

        public void doUpdatePeer(Node var1);

        public com.sun.javafx.geom.Shape doConfigShape(Shape var1);
    }
}

