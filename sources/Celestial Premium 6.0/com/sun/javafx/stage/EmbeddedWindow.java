/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.Scene
 *  javafx.stage.Window
 */
package com.sun.javafx.stage;

import com.sun.javafx.embed.HostInterface;
import com.sun.javafx.stage.EmbeddedWindowHelper;
import com.sun.javafx.stage.WindowHelper;
import com.sun.javafx.stage.WindowPeerListener;
import com.sun.javafx.tk.Toolkit;
import javafx.scene.Scene;
import javafx.stage.Window;

public class EmbeddedWindow
extends Window {
    private HostInterface host;

    public EmbeddedWindow(HostInterface hostInterface) {
        this.host = hostInterface;
        EmbeddedWindowHelper.initHelper(this);
    }

    public HostInterface getHost() {
        return this.host;
    }

    public final void setScene(Scene scene) {
        super.setScene(scene);
    }

    public final void show() {
        super.show();
    }

    private void doVisibleChanging(boolean bl) {
        Toolkit toolkit = Toolkit.getToolkit();
        if (bl && WindowHelper.getPeer(this) == null) {
            WindowHelper.setPeer(this, toolkit.createTKEmbeddedStage(this.host, WindowHelper.getAccessControlContext(this)));
            WindowHelper.setPeerListener(this, new WindowPeerListener(this));
        }
    }

    static {
        EmbeddedWindowHelper.setEmbeddedWindowAccessor(new EmbeddedWindowHelper.EmbeddedWindowAccessor(){

            @Override
            public void doVisibleChanging(Window window, boolean bl) {
                ((EmbeddedWindow)window).doVisibleChanging(bl);
            }
        });
    }
}

