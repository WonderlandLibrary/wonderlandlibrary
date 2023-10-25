/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.stage.Stage
 *  javafx.stage.Window
 */
package com.sun.javafx.stage;

import com.sun.javafx.stage.WindowPeerListener;
import javafx.stage.Stage;
import javafx.stage.Window;

public class StagePeerListener
extends WindowPeerListener {
    private final Stage stage;
    private final StageAccessor stageAccessor;

    public StagePeerListener(Stage stage, StageAccessor stageAccessor) {
        super((Window)stage);
        this.stage = stage;
        this.stageAccessor = stageAccessor;
    }

    @Override
    public void changedIconified(boolean bl) {
        this.stageAccessor.setIconified(this.stage, bl);
    }

    @Override
    public void changedMaximized(boolean bl) {
        this.stageAccessor.setMaximized(this.stage, bl);
    }

    @Override
    public void changedResizable(boolean bl) {
        this.stageAccessor.setResizable(this.stage, bl);
    }

    @Override
    public void changedFullscreen(boolean bl) {
        this.stageAccessor.setFullScreen(this.stage, bl);
    }

    @Override
    public void changedAlwaysOnTop(boolean bl) {
        this.stageAccessor.setAlwaysOnTop(this.stage, bl);
    }

    public static interface StageAccessor {
        public void setIconified(Stage var1, boolean var2);

        public void setMaximized(Stage var1, boolean var2);

        public void setResizable(Stage var1, boolean var2);

        public void setFullScreen(Stage var1, boolean var2);

        public void setAlwaysOnTop(Stage var1, boolean var2);
    }
}

