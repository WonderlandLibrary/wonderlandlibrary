/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.tk.quantum;

import com.sun.javafx.tk.CompletionListener;
import com.sun.javafx.tk.RenderJob;
import com.sun.javafx.tk.quantum.GlassScene;

class PaintRenderJob
extends RenderJob {
    private GlassScene scene;

    public PaintRenderJob(GlassScene glassScene, CompletionListener completionListener, Runnable runnable) {
        super(runnable, completionListener);
        this.scene = glassScene;
    }

    public GlassScene getScene() {
        return this.scene;
    }
}

