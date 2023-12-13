/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package net.minecraft.client.renderer;

import net.minecraft.client.renderer.ChunkRenderContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.chunk.ListedRenderChunk;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.util.EnumWorldBlockLayer;
import optifine.Config;
import org.lwjgl.opengl.GL11;

public class RenderList
extends ChunkRenderContainer {
    @Override
    public void func_178001_a(EnumWorldBlockLayer p_178001_1_) {
        if (this.field_178007_b) {
            if (this.field_178009_a.size() == 0) {
                return;
            }
            for (RenderChunk var3 : this.field_178009_a) {
                ListedRenderChunk var4 = (ListedRenderChunk)var3;
                GlStateManager.pushMatrix();
                this.func_178003_a(var3);
                GL11.glCallList((int)var4.func_178600_a(p_178001_1_, var4.func_178571_g()));
                GlStateManager.popMatrix();
            }
            if (Config.isMultiTexture()) {
                GlStateManager.bindCurrentTexture();
            }
            GlStateManager.func_179117_G();
            this.field_178009_a.clear();
        }
    }
}

