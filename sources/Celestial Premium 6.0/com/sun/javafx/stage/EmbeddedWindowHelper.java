/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.stage.Window
 */
package com.sun.javafx.stage;

import com.sun.javafx.stage.EmbeddedWindow;
import com.sun.javafx.stage.WindowHelper;
import com.sun.javafx.util.Utils;
import javafx.stage.Window;

public class EmbeddedWindowHelper
extends WindowHelper {
    private static final EmbeddedWindowHelper theInstance = new EmbeddedWindowHelper();
    private static EmbeddedWindowAccessor embeddedWindowAccessor;

    private static WindowHelper getInstance() {
        return theInstance;
    }

    public static void initHelper(EmbeddedWindow embeddedWindow) {
        EmbeddedWindowHelper.setHelper(embeddedWindow, EmbeddedWindowHelper.getInstance());
    }

    @Override
    protected void visibleChangingImpl(Window window, boolean bl) {
        super.visibleChangingImpl(window, bl);
        embeddedWindowAccessor.doVisibleChanging(window, bl);
    }

    public static void setEmbeddedWindowAccessor(EmbeddedWindowAccessor embeddedWindowAccessor) {
        if (EmbeddedWindowHelper.embeddedWindowAccessor != null) {
            throw new IllegalStateException();
        }
        EmbeddedWindowHelper.embeddedWindowAccessor = embeddedWindowAccessor;
    }

    static {
        Utils.forceInit(EmbeddedWindow.class);
    }

    public static interface EmbeddedWindowAccessor {
        public void doVisibleChanging(Window var1, boolean var2);
    }
}

