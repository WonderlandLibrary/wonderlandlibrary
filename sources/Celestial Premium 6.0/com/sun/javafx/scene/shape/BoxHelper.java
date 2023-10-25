/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.Node
 *  javafx.scene.shape.Box
 */
package com.sun.javafx.scene.shape;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.PickRay;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.input.PickResultChooser;
import com.sun.javafx.scene.shape.Shape3DHelper;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.util.Utils;
import javafx.scene.Node;
import javafx.scene.shape.Box;

public class BoxHelper
extends Shape3DHelper {
    private static final BoxHelper theInstance = new BoxHelper();
    private static BoxAccessor boxAccessor;

    private static BoxHelper getInstance() {
        return theInstance;
    }

    public static void initHelper(Box box) {
        BoxHelper.setHelper((Node)box, BoxHelper.getInstance());
    }

    @Override
    protected NGNode createPeerImpl(Node node) {
        return boxAccessor.doCreatePeer(node);
    }

    @Override
    protected void updatePeerImpl(Node node) {
        super.updatePeerImpl(node);
        boxAccessor.doUpdatePeer(node);
    }

    @Override
    protected BaseBounds computeGeomBoundsImpl(Node node, BaseBounds baseBounds, BaseTransform baseTransform) {
        return boxAccessor.doComputeGeomBounds(node, baseBounds, baseTransform);
    }

    @Override
    protected boolean computeContainsImpl(Node node, double d, double d2) {
        return boxAccessor.doComputeContains(node, d, d2);
    }

    @Override
    protected boolean computeIntersectsImpl(Node node, PickRay pickRay, PickResultChooser pickResultChooser) {
        return boxAccessor.doComputeIntersects(node, pickRay, pickResultChooser);
    }

    public static void setBoxAccessor(BoxAccessor boxAccessor) {
        if (BoxHelper.boxAccessor != null) {
            throw new IllegalStateException();
        }
        BoxHelper.boxAccessor = boxAccessor;
    }

    static {
        Utils.forceInit(Box.class);
    }

    public static interface BoxAccessor {
        public NGNode doCreatePeer(Node var1);

        public void doUpdatePeer(Node var1);

        public BaseBounds doComputeGeomBounds(Node var1, BaseBounds var2, BaseTransform var3);

        public boolean doComputeContains(Node var1, double var2, double var4);

        public boolean doComputeIntersects(Node var1, PickRay var2, PickResultChooser var3);
    }
}

