/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.Node
 *  javafx.scene.canvas.Canvas
 */
package com.sun.javafx.scene.canvas;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.NodeHelper;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.util.Utils;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;

public class CanvasHelper
extends NodeHelper {
    private static final CanvasHelper theInstance = new CanvasHelper();
    private static CanvasAccessor canvasAccessor;

    private static CanvasHelper getInstance() {
        return theInstance;
    }

    public static void initHelper(Canvas canvas) {
        CanvasHelper.setHelper((Node)canvas, CanvasHelper.getInstance());
    }

    @Override
    protected NGNode createPeerImpl(Node node) {
        return canvasAccessor.doCreatePeer(node);
    }

    @Override
    protected void updatePeerImpl(Node node) {
        super.updatePeerImpl(node);
        canvasAccessor.doUpdatePeer(node);
    }

    @Override
    protected BaseBounds computeGeomBoundsImpl(Node node, BaseBounds baseBounds, BaseTransform baseTransform) {
        return canvasAccessor.doComputeGeomBounds(node, baseBounds, baseTransform);
    }

    @Override
    protected boolean computeContainsImpl(Node node, double d, double d2) {
        return canvasAccessor.doComputeContains(node, d, d2);
    }

    public static void setCanvasAccessor(CanvasAccessor canvasAccessor) {
        if (CanvasHelper.canvasAccessor != null) {
            throw new IllegalStateException();
        }
        CanvasHelper.canvasAccessor = canvasAccessor;
    }

    static {
        Utils.forceInit(Canvas.class);
    }

    public static interface CanvasAccessor {
        public NGNode doCreatePeer(Node var1);

        public void doUpdatePeer(Node var1);

        public BaseBounds doComputeGeomBounds(Node var1, BaseBounds var2, BaseTransform var3);

        public boolean doComputeContains(Node var1, double var2, double var4);
    }
}

