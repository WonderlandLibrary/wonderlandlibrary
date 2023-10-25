/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.Node
 *  javafx.scene.shape.CubicCurve
 *  javafx.scene.shape.Shape
 */
package com.sun.javafx.scene.shape;

import com.sun.javafx.scene.shape.ShapeHelper;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.util.Utils;
import javafx.scene.Node;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.Shape;

public class CubicCurveHelper
extends ShapeHelper {
    private static final CubicCurveHelper theInstance = new CubicCurveHelper();
    private static CubicCurveAccessor cubicCurveAccessor;

    private static CubicCurveHelper getInstance() {
        return theInstance;
    }

    public static void initHelper(CubicCurve cubicCurve) {
        CubicCurveHelper.setHelper((Node)cubicCurve, CubicCurveHelper.getInstance());
    }

    @Override
    protected NGNode createPeerImpl(Node node) {
        return cubicCurveAccessor.doCreatePeer(node);
    }

    @Override
    protected void updatePeerImpl(Node node) {
        super.updatePeerImpl(node);
        cubicCurveAccessor.doUpdatePeer(node);
    }

    @Override
    protected com.sun.javafx.geom.Shape configShapeImpl(Shape shape) {
        return cubicCurveAccessor.doConfigShape(shape);
    }

    public static void setCubicCurveAccessor(CubicCurveAccessor cubicCurveAccessor) {
        if (CubicCurveHelper.cubicCurveAccessor != null) {
            throw new IllegalStateException();
        }
        CubicCurveHelper.cubicCurveAccessor = cubicCurveAccessor;
    }

    static {
        Utils.forceInit(CubicCurve.class);
    }

    public static interface CubicCurveAccessor {
        public NGNode doCreatePeer(Node var1);

        public void doUpdatePeer(Node var1);

        public com.sun.javafx.geom.Shape doConfigShape(Shape var1);
    }
}

