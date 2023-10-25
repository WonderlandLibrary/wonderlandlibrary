/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.LightBase
 *  javafx.scene.Node
 */
package com.sun.javafx.scene;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.DirtyBits;
import com.sun.javafx.scene.NodeHelper;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.util.Utils;
import javafx.scene.LightBase;
import javafx.scene.Node;

public class LightBaseHelper
extends NodeHelper {
    private static final LightBaseHelper theInstance = new LightBaseHelper();
    private static LightBaseAccessor lightBaseAccessor;

    private static LightBaseHelper getInstance() {
        return theInstance;
    }

    public static void initHelper(LightBase lightBase) {
        LightBaseHelper.setHelper((Node)lightBase, LightBaseHelper.getInstance());
    }

    @Override
    protected NGNode createPeerImpl(Node node) {
        throw new UnsupportedOperationException("Applications should not extend the LightBase class directly.");
    }

    @Override
    protected void updatePeerImpl(Node node) {
        super.updatePeerImpl(node);
        lightBaseAccessor.doUpdatePeer(node);
    }

    @Override
    protected void markDirtyImpl(Node node, DirtyBits dirtyBits) {
        super.markDirtyImpl(node, dirtyBits);
        lightBaseAccessor.doMarkDirty(node, dirtyBits);
    }

    @Override
    protected BaseBounds computeGeomBoundsImpl(Node node, BaseBounds baseBounds, BaseTransform baseTransform) {
        return lightBaseAccessor.doComputeGeomBounds(node, baseBounds, baseTransform);
    }

    @Override
    protected boolean computeContainsImpl(Node node, double d, double d2) {
        return lightBaseAccessor.doComputeContains(node, d, d2);
    }

    public static void setLightBaseAccessor(LightBaseAccessor lightBaseAccessor) {
        if (LightBaseHelper.lightBaseAccessor != null) {
            throw new IllegalStateException();
        }
        LightBaseHelper.lightBaseAccessor = lightBaseAccessor;
    }

    static {
        Utils.forceInit(LightBase.class);
    }

    public static interface LightBaseAccessor {
        public void doMarkDirty(Node var1, DirtyBits var2);

        public void doUpdatePeer(Node var1);

        public BaseBounds doComputeGeomBounds(Node var1, BaseBounds var2, BaseTransform var3);

        public boolean doComputeContains(Node var1, double var2, double var4);
    }
}

