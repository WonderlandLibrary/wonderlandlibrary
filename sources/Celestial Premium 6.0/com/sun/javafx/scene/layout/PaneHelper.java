/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.Node
 *  javafx.scene.layout.Pane
 */
package com.sun.javafx.scene.layout;

import com.sun.javafx.scene.layout.RegionHelper;
import com.sun.javafx.util.Utils;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class PaneHelper
extends RegionHelper {
    private static final PaneHelper theInstance = new PaneHelper();
    private static PaneAccessor paneAccessor;

    private static PaneHelper getInstance() {
        return theInstance;
    }

    public static void initHelper(Pane pane) {
        PaneHelper.setHelper((Node)pane, PaneHelper.getInstance());
    }

    public static void setPaneAccessor(PaneAccessor paneAccessor) {
        if (PaneHelper.paneAccessor != null) {
            throw new IllegalStateException();
        }
        PaneHelper.paneAccessor = paneAccessor;
    }

    static {
        Utils.forceInit(Pane.class);
    }

    public static interface PaneAccessor {
    }
}

