/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.Camera
 *  javafx.scene.Node
 *  javafx.scene.SubScene
 */
package com.sun.javafx.scene;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.PickRay;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.NodeHelper;
import com.sun.javafx.scene.input.PickResultChooser;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.util.Utils;
import javafx.scene.Camera;
import javafx.scene.Node;
import javafx.scene.SubScene;

public class SubSceneHelper
extends NodeHelper {
    private static final SubSceneHelper theInstance = new SubSceneHelper();
    private static SubSceneAccessor subSceneAccessor;

    private static SubSceneHelper getInstance() {
        return theInstance;
    }

    public static void initHelper(SubScene subScene) {
        SubSceneHelper.setHelper((Node)subScene, SubSceneHelper.getInstance());
    }

    public static void superProcessCSS(Node node) {
        ((SubSceneHelper)SubSceneHelper.getHelper(node)).superProcessCSSImpl(node);
    }

    @Override
    protected NGNode createPeerImpl(Node node) {
        return subSceneAccessor.doCreatePeer(node);
    }

    @Override
    protected void updatePeerImpl(Node node) {
        super.updatePeerImpl(node);
        subSceneAccessor.doUpdatePeer(node);
    }

    @Override
    protected BaseBounds computeGeomBoundsImpl(Node node, BaseBounds baseBounds, BaseTransform baseTransform) {
        return subSceneAccessor.doComputeGeomBounds(node, baseBounds, baseTransform);
    }

    @Override
    protected boolean computeContainsImpl(Node node, double d, double d2) {
        return subSceneAccessor.doComputeContains(node, d, d2);
    }

    void superProcessCSSImpl(Node node) {
        super.processCSSImpl(node);
    }

    @Override
    protected void processCSSImpl(Node node) {
        subSceneAccessor.doProcessCSS(node);
    }

    @Override
    protected void pickNodeLocalImpl(Node node, PickRay pickRay, PickResultChooser pickResultChooser) {
        subSceneAccessor.doPickNodeLocal(node, pickRay, pickResultChooser);
    }

    public static boolean isDepthBuffer(SubScene subScene) {
        return subSceneAccessor.isDepthBuffer(subScene);
    }

    public static Camera getEffectiveCamera(SubScene subScene) {
        return subSceneAccessor.getEffectiveCamera(subScene);
    }

    public static void setSubSceneAccessor(SubSceneAccessor subSceneAccessor) {
        if (SubSceneHelper.subSceneAccessor != null) {
            throw new IllegalStateException();
        }
        SubSceneHelper.subSceneAccessor = subSceneAccessor;
    }

    static {
        Utils.forceInit(SubScene.class);
    }

    public static interface SubSceneAccessor {
        public NGNode doCreatePeer(Node var1);

        public void doUpdatePeer(Node var1);

        public BaseBounds doComputeGeomBounds(Node var1, BaseBounds var2, BaseTransform var3);

        public boolean doComputeContains(Node var1, double var2, double var4);

        public void doProcessCSS(Node var1);

        public void doPickNodeLocal(Node var1, PickRay var2, PickResultChooser var3);

        public boolean isDepthBuffer(SubScene var1);

        public Camera getEffectiveCamera(SubScene var1);
    }
}

