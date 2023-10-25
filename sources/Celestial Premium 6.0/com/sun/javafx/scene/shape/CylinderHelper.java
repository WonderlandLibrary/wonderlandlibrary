/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.Node
 *  javafx.scene.shape.Cylinder
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
import javafx.scene.shape.Cylinder;

public class CylinderHelper
extends Shape3DHelper {
    private static final CylinderHelper theInstance = new CylinderHelper();
    private static CylinderAccessor cylinderAccessor;

    private static CylinderHelper getInstance() {
        return theInstance;
    }

    public static void initHelper(Cylinder cylinder) {
        CylinderHelper.setHelper((Node)cylinder, CylinderHelper.getInstance());
    }

    @Override
    protected NGNode createPeerImpl(Node node) {
        return cylinderAccessor.doCreatePeer(node);
    }

    @Override
    protected void updatePeerImpl(Node node) {
        super.updatePeerImpl(node);
        cylinderAccessor.doUpdatePeer(node);
    }

    @Override
    protected BaseBounds computeGeomBoundsImpl(Node node, BaseBounds baseBounds, BaseTransform baseTransform) {
        return cylinderAccessor.doComputeGeomBounds(node, baseBounds, baseTransform);
    }

    @Override
    protected boolean computeContainsImpl(Node node, double d, double d2) {
        return cylinderAccessor.doComputeContains(node, d, d2);
    }

    @Override
    protected boolean computeIntersectsImpl(Node node, PickRay pickRay, PickResultChooser pickResultChooser) {
        return cylinderAccessor.doComputeIntersects(node, pickRay, pickResultChooser);
    }

    public static void setCylinderAccessor(CylinderAccessor cylinderAccessor) {
        if (CylinderHelper.cylinderAccessor != null) {
            throw new IllegalStateException();
        }
        CylinderHelper.cylinderAccessor = cylinderAccessor;
    }

    static {
        Utils.forceInit(Cylinder.class);
    }

    public static interface CylinderAccessor {
        public NGNode doCreatePeer(Node var1);

        public void doUpdatePeer(Node var1);

        public BaseBounds doComputeGeomBounds(Node var1, BaseBounds var2, BaseTransform var3);

        public boolean doComputeContains(Node var1, double var2, double var4);

        public boolean doComputeIntersects(Node var1, PickRay var2, PickResultChooser var3);
    }
}

