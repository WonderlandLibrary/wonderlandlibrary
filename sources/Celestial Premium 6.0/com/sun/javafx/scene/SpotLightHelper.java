/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.Node
 *  javafx.scene.SpotLight
 */
package com.sun.javafx.scene;

import com.sun.javafx.scene.PointLightHelper;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.util.Utils;
import javafx.scene.Node;
import javafx.scene.SpotLight;

public class SpotLightHelper
extends PointLightHelper {
    private static final SpotLightHelper theInstance = new SpotLightHelper();
    private static SpotLightAccessor spotLightAccessor;

    private static SpotLightHelper getInstance() {
        return theInstance;
    }

    public static void initHelper(SpotLight spotLight) {
        SpotLightHelper.setHelper((Node)spotLight, SpotLightHelper.getInstance());
    }

    @Override
    protected NGNode createPeerImpl(Node node) {
        return spotLightAccessor.doCreatePeer(node);
    }

    @Override
    protected void updatePeerImpl(Node node) {
        super.updatePeerImpl(node);
        spotLightAccessor.doUpdatePeer(node);
    }

    public static void setSpotLightAccessor(SpotLightAccessor spotLightAccessor) {
        if (SpotLightHelper.spotLightAccessor != null) {
            throw new IllegalStateException("Accessor already exists");
        }
        SpotLightHelper.spotLightAccessor = spotLightAccessor;
    }

    static {
        Utils.forceInit(SpotLight.class);
    }

    public static interface SpotLightAccessor {
        public NGNode doCreatePeer(Node var1);

        public void doUpdatePeer(Node var1);
    }
}

