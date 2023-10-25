/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.Node
 *  javafx.scene.paint.Paint
 *  javafx.scene.shape.Line
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
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;

public class LineHelper
extends ShapeHelper {
    private static final LineHelper theInstance = new LineHelper();
    private static LineAccessor lineAccessor;

    private static LineHelper getInstance() {
        return theInstance;
    }

    public static void initHelper(Line line) {
        LineHelper.setHelper((Node)line, LineHelper.getInstance());
    }

    @Override
    protected NGNode createPeerImpl(Node node) {
        return lineAccessor.doCreatePeer(node);
    }

    @Override
    protected void updatePeerImpl(Node node) {
        super.updatePeerImpl(node);
        lineAccessor.doUpdatePeer(node);
    }

    @Override
    protected BaseBounds computeGeomBoundsImpl(Node node, BaseBounds baseBounds, BaseTransform baseTransform) {
        return lineAccessor.doComputeGeomBounds(node, baseBounds, baseTransform);
    }

    @Override
    protected Paint cssGetFillInitialValueImpl(Shape shape) {
        return lineAccessor.doCssGetFillInitialValue(shape);
    }

    @Override
    protected Paint cssGetStrokeInitialValueImpl(Shape shape) {
        return lineAccessor.doCssGetStrokeInitialValue(shape);
    }

    @Override
    protected com.sun.javafx.geom.Shape configShapeImpl(Shape shape) {
        return lineAccessor.doConfigShape(shape);
    }

    public static void setLineAccessor(LineAccessor lineAccessor) {
        if (LineHelper.lineAccessor != null) {
            throw new IllegalStateException();
        }
        LineHelper.lineAccessor = lineAccessor;
    }

    static {
        Utils.forceInit(Line.class);
    }

    public static interface LineAccessor {
        public NGNode doCreatePeer(Node var1);

        public void doUpdatePeer(Node var1);

        public BaseBounds doComputeGeomBounds(Node var1, BaseBounds var2, BaseTransform var3);

        public Paint doCssGetFillInitialValue(Shape var1);

        public Paint doCssGetStrokeInitialValue(Shape var1);

        public com.sun.javafx.geom.Shape doConfigShape(Shape var1);
    }
}

