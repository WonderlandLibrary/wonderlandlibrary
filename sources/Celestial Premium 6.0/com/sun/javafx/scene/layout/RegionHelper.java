/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.geometry.Bounds
 *  javafx.scene.Node
 *  javafx.scene.layout.Region
 */
package com.sun.javafx.scene.layout;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.PickRay;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.ParentHelper;
import com.sun.javafx.scene.input.PickResultChooser;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.util.Utils;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.layout.Region;

public class RegionHelper
extends ParentHelper {
    private static final RegionHelper theInstance = new RegionHelper();
    private static RegionAccessor regionAccessor;

    private static RegionHelper getInstance() {
        return theInstance;
    }

    public static void initHelper(Region region) {
        RegionHelper.setHelper((Node)region, RegionHelper.getInstance());
    }

    public static BaseBounds superComputeGeomBounds(Node node, BaseBounds baseBounds, BaseTransform baseTransform) {
        return ((RegionHelper)RegionHelper.getHelper(node)).superComputeGeomBoundsImpl(node, baseBounds, baseTransform);
    }

    @Override
    protected NGNode createPeerImpl(Node node) {
        return regionAccessor.doCreatePeer(node);
    }

    @Override
    protected void updatePeerImpl(Node node) {
        super.updatePeerImpl(node);
        regionAccessor.doUpdatePeer(node);
    }

    BaseBounds superComputeGeomBoundsImpl(Node node, BaseBounds baseBounds, BaseTransform baseTransform) {
        return super.computeGeomBoundsImpl(node, baseBounds, baseTransform);
    }

    @Override
    protected Bounds computeLayoutBoundsImpl(Node node) {
        return regionAccessor.doComputeLayoutBounds(node);
    }

    @Override
    protected BaseBounds computeGeomBoundsImpl(Node node, BaseBounds baseBounds, BaseTransform baseTransform) {
        return regionAccessor.doComputeGeomBounds(node, baseBounds, baseTransform);
    }

    @Override
    protected boolean computeContainsImpl(Node node, double d, double d2) {
        return regionAccessor.doComputeContains(node, d, d2);
    }

    @Override
    protected void notifyLayoutBoundsChangedImpl(Node node) {
        regionAccessor.doNotifyLayoutBoundsChanged(node);
    }

    @Override
    protected void pickNodeLocalImpl(Node node, PickRay pickRay, PickResultChooser pickResultChooser) {
        regionAccessor.doPickNodeLocal(node, pickRay, pickResultChooser);
    }

    public static void setRegionAccessor(RegionAccessor regionAccessor) {
        if (RegionHelper.regionAccessor != null) {
            throw new IllegalStateException();
        }
        RegionHelper.regionAccessor = regionAccessor;
    }

    static {
        Utils.forceInit(Region.class);
    }

    public static interface RegionAccessor {
        public void doUpdatePeer(Node var1);

        public NGNode doCreatePeer(Node var1);

        public Bounds doComputeLayoutBounds(Node var1);

        public BaseBounds doComputeGeomBounds(Node var1, BaseBounds var2, BaseTransform var3);

        public boolean doComputeContains(Node var1, double var2, double var4);

        public void doNotifyLayoutBoundsChanged(Node var1);

        public void doPickNodeLocal(Node var1, PickRay var2, PickResultChooser var3);
    }
}

