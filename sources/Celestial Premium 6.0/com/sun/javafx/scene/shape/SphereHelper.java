/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.Node
 *  javafx.scene.shape.Sphere
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
import javafx.scene.shape.Sphere;

public class SphereHelper
extends Shape3DHelper {
    private static final SphereHelper theInstance = new SphereHelper();
    private static SphereAccessor sphereAccessor;

    private static SphereHelper getInstance() {
        return theInstance;
    }

    public static void initHelper(Sphere sphere) {
        SphereHelper.setHelper((Node)sphere, SphereHelper.getInstance());
    }

    @Override
    protected NGNode createPeerImpl(Node node) {
        return sphereAccessor.doCreatePeer(node);
    }

    @Override
    protected void updatePeerImpl(Node node) {
        super.updatePeerImpl(node);
        sphereAccessor.doUpdatePeer(node);
    }

    @Override
    protected BaseBounds computeGeomBoundsImpl(Node node, BaseBounds baseBounds, BaseTransform baseTransform) {
        return sphereAccessor.doComputeGeomBounds(node, baseBounds, baseTransform);
    }

    @Override
    protected boolean computeContainsImpl(Node node, double d, double d2) {
        return sphereAccessor.doComputeContains(node, d, d2);
    }

    @Override
    protected boolean computeIntersectsImpl(Node node, PickRay pickRay, PickResultChooser pickResultChooser) {
        return sphereAccessor.doComputeIntersects(node, pickRay, pickResultChooser);
    }

    public static void setSphereAccessor(SphereAccessor sphereAccessor) {
        if (SphereHelper.sphereAccessor != null) {
            throw new IllegalStateException();
        }
        SphereHelper.sphereAccessor = sphereAccessor;
    }

    static {
        Utils.forceInit(Sphere.class);
    }

    public static interface SphereAccessor {
        public NGNode doCreatePeer(Node var1);

        public void doUpdatePeer(Node var1);

        public BaseBounds doComputeGeomBounds(Node var1, BaseBounds var2, BaseTransform var3);

        public boolean doComputeContains(Node var1, double var2, double var4);

        public boolean doComputeIntersects(Node var1, PickRay var2, PickResultChooser var3);
    }
}

