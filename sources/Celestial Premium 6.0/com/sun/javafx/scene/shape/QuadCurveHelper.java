/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.Node
 *  javafx.scene.shape.QuadCurve
 *  javafx.scene.shape.Shape
 */
package com.sun.javafx.scene.shape;

import com.sun.javafx.scene.shape.ShapeHelper;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.util.Utils;
import javafx.scene.Node;
import javafx.scene.shape.QuadCurve;
import javafx.scene.shape.Shape;

public class QuadCurveHelper
extends ShapeHelper {
    private static final QuadCurveHelper theInstance = new QuadCurveHelper();
    private static QuadCurveAccessor quadCurveAccessor;

    private static QuadCurveHelper getInstance() {
        return theInstance;
    }

    public static void initHelper(QuadCurve quadCurve) {
        QuadCurveHelper.setHelper((Node)quadCurve, QuadCurveHelper.getInstance());
    }

    @Override
    protected NGNode createPeerImpl(Node node) {
        return quadCurveAccessor.doCreatePeer(node);
    }

    @Override
    protected void updatePeerImpl(Node node) {
        super.updatePeerImpl(node);
        quadCurveAccessor.doUpdatePeer(node);
    }

    @Override
    protected com.sun.javafx.geom.Shape configShapeImpl(Shape shape) {
        return quadCurveAccessor.doConfigShape(shape);
    }

    public static void setQuadCurveAccessor(QuadCurveAccessor quadCurveAccessor) {
        if (QuadCurveHelper.quadCurveAccessor != null) {
            throw new IllegalStateException();
        }
        QuadCurveHelper.quadCurveAccessor = quadCurveAccessor;
    }

    static {
        Utils.forceInit(QuadCurve.class);
    }

    public static interface QuadCurveAccessor {
        public NGNode doCreatePeer(Node var1);

        public void doUpdatePeer(Node var1);

        public com.sun.javafx.geom.Shape doConfigShape(Shape var1);
    }
}

