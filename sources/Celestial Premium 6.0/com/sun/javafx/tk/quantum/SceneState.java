/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.tk.quantum;

import com.sun.glass.ui.Application;
import com.sun.javafx.sg.prism.NGCamera;
import com.sun.javafx.tk.quantum.GlassScene;
import com.sun.prism.PixelSource;
import com.sun.prism.PresentableState;
import com.sun.prism.paint.Color;
import com.sun.prism.paint.Paint;

class SceneState
extends PresentableState {
    final GlassScene scene;
    private Color clearColor;
    private Paint currentPaint;
    private NGCamera camera;

    public SceneState(GlassScene glassScene) {
        this.scene = glassScene;
    }

    @Override
    public boolean isMSAA() {
        return this.scene.isMSAA();
    }

    public GlassScene getScene() {
        return this.scene;
    }

    public boolean isValid() {
        return this.getWindow() != null && this.getView() != null && !this.isViewClosed() && this.getWidth() > 0 && this.getHeight() > 0;
    }

    @Override
    public void update() {
        this.view = this.scene.getPlatformView();
        this.clearColor = this.scene.getClearColor();
        this.currentPaint = this.scene.getCurrentPaint();
        super.update();
        this.camera = this.scene.getCamera();
        if (this.camera != null) {
            this.viewWidth = (int)this.camera.getViewWidth();
            this.viewHeight = (int)this.camera.getViewHeight();
        }
    }

    @Override
    public void uploadPixels(PixelSource pixelSource) {
        Application.invokeLater(() -> {
            if (this.isValid()) {
                SceneState.super.uploadPixels(pixelSource);
            } else {
                pixelSource.skipLatestPixels();
            }
        });
    }

    Color getClearColor() {
        return this.clearColor;
    }

    Paint getCurrentPaint() {
        return this.currentPaint;
    }

    NGCamera getCamera() {
        return this.camera;
    }
}

