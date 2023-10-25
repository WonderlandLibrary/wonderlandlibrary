/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.AmbientLight
 *  javafx.scene.Node
 */
package com.sun.javafx.scene;

import com.sun.javafx.scene.LightBaseHelper;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.util.Utils;
import javafx.scene.AmbientLight;
import javafx.scene.Node;

public class AmbientLightHelper
extends LightBaseHelper {
    private static final AmbientLightHelper theInstance = new AmbientLightHelper();
    private static AmbientLightAccessor ambientLightAccessor;

    private static AmbientLightHelper getInstance() {
        return theInstance;
    }

    public static void initHelper(AmbientLight ambientLight) {
        AmbientLightHelper.setHelper((Node)ambientLight, AmbientLightHelper.getInstance());
    }

    @Override
    protected NGNode createPeerImpl(Node node) {
        return ambientLightAccessor.doCreatePeer(node);
    }

    public static void setAmbientLightAccessor(AmbientLightAccessor ambientLightAccessor) {
        if (AmbientLightHelper.ambientLightAccessor != null) {
            throw new IllegalStateException();
        }
        AmbientLightHelper.ambientLightAccessor = ambientLightAccessor;
    }

    static {
        Utils.forceInit(AmbientLight.class);
    }

    public static interface AmbientLightAccessor {
        public NGNode doCreatePeer(Node var1);
    }
}

