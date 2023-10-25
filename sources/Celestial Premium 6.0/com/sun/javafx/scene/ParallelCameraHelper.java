/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.Node
 *  javafx.scene.ParallelCamera
 */
package com.sun.javafx.scene;

import com.sun.javafx.scene.CameraHelper;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.util.Utils;
import javafx.scene.Node;
import javafx.scene.ParallelCamera;

public class ParallelCameraHelper
extends CameraHelper {
    private static final ParallelCameraHelper theInstance = new ParallelCameraHelper();
    private static ParallelCameraAccessor parallelCameraAccessor;

    private static ParallelCameraHelper getInstance() {
        return theInstance;
    }

    public static void initHelper(ParallelCamera parallelCamera) {
        ParallelCameraHelper.setHelper((Node)parallelCamera, ParallelCameraHelper.getInstance());
    }

    @Override
    protected NGNode createPeerImpl(Node node) {
        return parallelCameraAccessor.doCreatePeer(node);
    }

    public static void setParallelCameraAccessor(ParallelCameraAccessor parallelCameraAccessor) {
        if (ParallelCameraHelper.parallelCameraAccessor != null) {
            throw new IllegalStateException();
        }
        ParallelCameraHelper.parallelCameraAccessor = parallelCameraAccessor;
    }

    static {
        Utils.forceInit(ParallelCamera.class);
    }

    public static interface ParallelCameraAccessor {
        public NGNode doCreatePeer(Node var1);
    }
}

