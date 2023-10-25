/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.geometry.Point2D
 *  javafx.geometry.Point3D
 *  javafx.scene.Camera
 *  javafx.scene.Node
 */
package com.sun.javafx.scene;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.DirtyBits;
import com.sun.javafx.scene.NodeHelper;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.util.Utils;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.Camera;
import javafx.scene.Node;

public class CameraHelper
extends NodeHelper {
    private static final CameraHelper theInstance = new CameraHelper();
    private static CameraAccessor cameraAccessor;

    private static CameraHelper getInstance() {
        return theInstance;
    }

    public static void initHelper(Camera camera) {
        CameraHelper.setHelper((Node)camera, CameraHelper.getInstance());
    }

    @Override
    protected NGNode createPeerImpl(Node node) {
        throw new UnsupportedOperationException("Applications should not extend the Camera class directly.");
    }

    @Override
    protected void updatePeerImpl(Node node) {
        super.updatePeerImpl(node);
        cameraAccessor.doUpdatePeer(node);
    }

    @Override
    protected void markDirtyImpl(Node node, DirtyBits dirtyBits) {
        super.markDirtyImpl(node, dirtyBits);
        cameraAccessor.doMarkDirty(node, dirtyBits);
    }

    @Override
    protected BaseBounds computeGeomBoundsImpl(Node node, BaseBounds baseBounds, BaseTransform baseTransform) {
        return cameraAccessor.doComputeGeomBounds(node, baseBounds, baseTransform);
    }

    @Override
    protected boolean computeContainsImpl(Node node, double d, double d2) {
        return cameraAccessor.doComputeContains(node, d, d2);
    }

    public static Point2D project(Camera camera, Point3D point3D) {
        return cameraAccessor.project(camera, point3D);
    }

    public static Point2D pickNodeXYPlane(Camera camera, Node node, double d, double d2) {
        return cameraAccessor.pickNodeXYPlane(camera, node, d, d2);
    }

    public static Point3D pickProjectPlane(Camera camera, double d, double d2) {
        return cameraAccessor.pickProjectPlane(camera, d, d2);
    }

    public static void setCameraAccessor(CameraAccessor cameraAccessor) {
        if (CameraHelper.cameraAccessor != null) {
            throw new IllegalStateException();
        }
        CameraHelper.cameraAccessor = cameraAccessor;
    }

    static {
        Utils.forceInit(Camera.class);
    }

    public static interface CameraAccessor {
        public void doMarkDirty(Node var1, DirtyBits var2);

        public void doUpdatePeer(Node var1);

        public BaseBounds doComputeGeomBounds(Node var1, BaseBounds var2, BaseTransform var3);

        public boolean doComputeContains(Node var1, double var2, double var4);

        public Point2D project(Camera var1, Point3D var2);

        public Point2D pickNodeXYPlane(Camera var1, Node var2, double var3, double var5);

        public Point3D pickProjectPlane(Camera var1, double var2, double var4);
    }
}

