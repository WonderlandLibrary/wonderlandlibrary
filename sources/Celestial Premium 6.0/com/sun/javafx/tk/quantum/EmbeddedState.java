/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.tk.quantum;

import com.sun.glass.ui.Pixels;
import com.sun.javafx.tk.quantum.EmbeddedScene;
import com.sun.javafx.tk.quantum.GlassScene;
import com.sun.javafx.tk.quantum.SceneState;
import com.sun.prism.PixelSource;

final class EmbeddedState
extends SceneState {
    public EmbeddedState(GlassScene glassScene) {
        super(glassScene);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void uploadPixels(PixelSource pixelSource) {
        if (this.isValid()) {
            Pixels pixels = pixelSource.getLatestPixels();
            if (pixels != null) {
                try {
                    EmbeddedScene embeddedScene = (EmbeddedScene)this.scene;
                    embeddedScene.uploadPixels(pixels);
                }
                finally {
                    pixelSource.doneWithPixels(pixels);
                }
            }
        } else {
            pixelSource.skipLatestPixels();
        }
    }

    @Override
    public boolean isValid() {
        return this.scene != null && this.getWidth() > 0 && this.getHeight() > 0;
    }

    @Override
    public void update() {
        super.update();
        float f = ((EmbeddedScene)this.scene).getRenderScaleX();
        float f2 = ((EmbeddedScene)this.scene).getRenderScaleY();
        this.update(1.0f, 1.0f, f, f2, f, f2);
        if (this.scene != null) {
            this.isWindowVisible = true;
            this.isWindowMinimized = false;
        }
    }
}

