/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.renderer;

import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.WorldVertexBufferUploader;

public class Tessellator {
    private WorldRenderer worldRenderer;
    private WorldVertexBufferUploader field_178182_b = new WorldVertexBufferUploader();
    private static final Tessellator instance = new Tessellator(0x200000);
    private static final String __OBFID = "CL_00000960";

    public static Tessellator getInstance() {
        return instance;
    }

    public Tessellator(int p_i1250_1_) {
        this.worldRenderer = new WorldRenderer(p_i1250_1_);
    }

    public int draw() {
        return this.field_178182_b.draw(this.worldRenderer, this.worldRenderer.draw());
    }

    public WorldRenderer getWorldRenderer() {
        return this.worldRenderer;
    }
}

