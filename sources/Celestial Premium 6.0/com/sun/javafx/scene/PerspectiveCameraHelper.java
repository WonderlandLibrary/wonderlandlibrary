/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.Node
 *  javafx.scene.PerspectiveCamera
 */
package com.sun.javafx.scene;

import com.sun.javafx.scene.CameraHelper;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.util.Utils;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;

public class PerspectiveCameraHelper
extends CameraHelper {
    private static final PerspectiveCameraHelper theInstance = new PerspectiveCameraHelper();
    private static PerspectiveCameraAccessor perspectiveCameraAccessor;

    private static PerspectiveCameraHelper getInstance() {
        return theInstance;
    }

    public static void initHelper(PerspectiveCamera perspectiveCamera) {
        PerspectiveCameraHelper.setHelper((Node)perspectiveCamera, PerspectiveCameraHelper.getInstance());
    }

    @Override
    protected NGNode createPeerImpl(Node node) {
        return perspectiveCameraAccessor.doCreatePeer(node);
    }

    @Override
    protected void updatePeerImpl(Node node) {
        super.updatePeerImpl(node);
        perspectiveCameraAccessor.doUpdatePeer(node);
    }

    public static void setPerspectiveCameraAccessor(PerspectiveCameraAccessor perspectiveCameraAccessor) {
        if (PerspectiveCameraHelper.perspectiveCameraAccessor != null) {
            throw new IllegalStateException();
        }
        PerspectiveCameraHelper.perspectiveCameraAccessor = perspectiveCameraAccessor;
    }

    static {
        Utils.forceInit(PerspectiveCamera.class);
    }

    public static interface PerspectiveCameraAccessor {
        public NGNode doCreatePeer(Node var1);

        public void doUpdatePeer(Node var1);
    }
}

