/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.Node
 */
package com.sun.javafx.tk.quantum;

import com.sun.javafx.scene.DirtyBits;
import com.sun.javafx.scene.GroupHelper;
import com.sun.javafx.tk.quantum.OverlayWarning;
import com.sun.javafx.util.Utils;
import javafx.scene.Node;

public class OverlayWarningHelper
extends GroupHelper {
    private static final OverlayWarningHelper theInstance = new OverlayWarningHelper();
    private static OverlayWarningAccessor overlayWarningAccessor;

    private static OverlayWarningHelper getInstance() {
        return theInstance;
    }

    public static void initHelper(OverlayWarning overlayWarning) {
        OverlayWarningHelper.setHelper((Node)overlayWarning, OverlayWarningHelper.getInstance());
    }

    @Override
    protected void updatePeerImpl(Node node) {
        overlayWarningAccessor.doUpdatePeer(node);
        super.updatePeerImpl(node);
    }

    @Override
    protected void markDirtyImpl(Node node, DirtyBits dirtyBits) {
        super.markDirtyImpl(node, dirtyBits);
        overlayWarningAccessor.doMarkDirty(node, dirtyBits);
    }

    public static void setOverlayWarningAccessor(OverlayWarningAccessor overlayWarningAccessor) {
        if (OverlayWarningHelper.overlayWarningAccessor != null) {
            throw new IllegalStateException();
        }
        OverlayWarningHelper.overlayWarningAccessor = overlayWarningAccessor;
    }

    static {
        Utils.forceInit(OverlayWarning.class);
    }

    public static interface OverlayWarningAccessor {
        public void doMarkDirty(Node var1, DirtyBits var2);

        public void doUpdatePeer(Node var1);
    }
}

