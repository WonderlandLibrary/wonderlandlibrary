/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer;

import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.WorldVertexBufferUploader;

public class Tessellator {
    private static final Tessellator instance = new Tessellator(0x200000);
    private WorldVertexBufferUploader vboUploader = new WorldVertexBufferUploader();
    private WorldRenderer worldRenderer;

    public Tessellator(int n) {
        this.worldRenderer = new WorldRenderer(n);
    }

    public WorldRenderer getWorldRenderer() {
        return this.worldRenderer;
    }

    public void draw() {
        this.worldRenderer.finishDrawing();
        this.vboUploader.func_181679_a(this.worldRenderer);
    }

    public static Tessellator getInstance() {
        return instance;
    }
}

