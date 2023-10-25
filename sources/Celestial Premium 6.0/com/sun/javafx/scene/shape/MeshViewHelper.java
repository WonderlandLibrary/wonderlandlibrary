/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.Node
 *  javafx.scene.shape.MeshView
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
import javafx.scene.shape.MeshView;

public class MeshViewHelper
extends Shape3DHelper {
    private static final MeshViewHelper theInstance = new MeshViewHelper();
    private static MeshViewAccessor meshViewAccessor;

    private static MeshViewHelper getInstance() {
        return theInstance;
    }

    public static void initHelper(MeshView meshView) {
        MeshViewHelper.setHelper((Node)meshView, MeshViewHelper.getInstance());
    }

    @Override
    protected NGNode createPeerImpl(Node node) {
        return meshViewAccessor.doCreatePeer(node);
    }

    @Override
    protected void updatePeerImpl(Node node) {
        super.updatePeerImpl(node);
        meshViewAccessor.doUpdatePeer(node);
    }

    @Override
    protected BaseBounds computeGeomBoundsImpl(Node node, BaseBounds baseBounds, BaseTransform baseTransform) {
        return meshViewAccessor.doComputeGeomBounds(node, baseBounds, baseTransform);
    }

    @Override
    protected boolean computeContainsImpl(Node node, double d, double d2) {
        return meshViewAccessor.doComputeContains(node, d, d2);
    }

    @Override
    protected boolean computeIntersectsImpl(Node node, PickRay pickRay, PickResultChooser pickResultChooser) {
        return meshViewAccessor.doComputeIntersects(node, pickRay, pickResultChooser);
    }

    public static void setMeshViewAccessor(MeshViewAccessor meshViewAccessor) {
        if (MeshViewHelper.meshViewAccessor != null) {
            throw new IllegalStateException();
        }
        MeshViewHelper.meshViewAccessor = meshViewAccessor;
    }

    static {
        Utils.forceInit(MeshView.class);
    }

    public static interface MeshViewAccessor {
        public NGNode doCreatePeer(Node var1);

        public void doUpdatePeer(Node var1);

        public BaseBounds doComputeGeomBounds(Node var1, BaseBounds var2, BaseTransform var3);

        public boolean doComputeContains(Node var1, double var2, double var4);

        public boolean doComputeIntersects(Node var1, PickRay var2, PickResultChooser var3);
    }
}

