/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.geometry.Bounds
 *  javafx.scene.Node
 *  javafx.scene.paint.Paint
 *  javafx.scene.shape.Path
 *  javafx.scene.shape.Shape
 */
package com.sun.javafx.scene.shape;

import com.sun.javafx.scene.shape.ShapeHelper;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.util.Utils;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Path;
import javafx.scene.shape.Shape;

public class PathHelper
extends ShapeHelper {
    private static final PathHelper theInstance = new PathHelper();
    private static PathAccessor pathAccessor;

    private static PathHelper getInstance() {
        return theInstance;
    }

    public static void initHelper(Path path) {
        PathHelper.setHelper((Node)path, PathHelper.getInstance());
    }

    @Override
    protected NGNode createPeerImpl(Node node) {
        return pathAccessor.doCreatePeer(node);
    }

    @Override
    protected void updatePeerImpl(Node node) {
        super.updatePeerImpl(node);
        pathAccessor.doUpdatePeer(node);
    }

    @Override
    protected Bounds computeLayoutBoundsImpl(Node node) {
        Bounds bounds = pathAccessor.doComputeLayoutBounds(node);
        if (bounds != null) {
            return bounds;
        }
        return super.computeLayoutBoundsImpl(node);
    }

    @Override
    protected Paint cssGetFillInitialValueImpl(Shape shape) {
        return pathAccessor.doCssGetFillInitialValue(shape);
    }

    @Override
    protected Paint cssGetStrokeInitialValueImpl(Shape shape) {
        return pathAccessor.doCssGetStrokeInitialValue(shape);
    }

    @Override
    protected com.sun.javafx.geom.Shape configShapeImpl(Shape shape) {
        return pathAccessor.doConfigShape(shape);
    }

    public static void setPathAccessor(PathAccessor pathAccessor) {
        if (PathHelper.pathAccessor != null) {
            throw new IllegalStateException();
        }
        PathHelper.pathAccessor = pathAccessor;
    }

    static {
        Utils.forceInit(Path.class);
    }

    public static interface PathAccessor {
        public NGNode doCreatePeer(Node var1);

        public void doUpdatePeer(Node var1);

        public Bounds doComputeLayoutBounds(Node var1);

        public Paint doCssGetFillInitialValue(Shape var1);

        public Paint doCssGetStrokeInitialValue(Shape var1);

        public com.sun.javafx.geom.Shape doConfigShape(Shape var1);
    }
}

