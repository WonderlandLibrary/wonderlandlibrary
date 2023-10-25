/*
 * Decompiled with CFR 0.152.
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
import org.lwjgl.opengl.GL11;

public class RenderList
extends ChunkRenderContainer {
    @Override
    public void renderChunkLayer(EnumWorldBlockLayer enumWorldBlockLayer) {
        if (this.initialized) {
            for (RenderChunk renderChunk : this.renderChunks) {
                ListedRenderChunk listedRenderChunk = (ListedRenderChunk)renderChunk;
                GlStateManager.pushMatrix();
                this.preRenderChunk(renderChunk);
                GL11.glCallList((int)listedRenderChunk.getDisplayList(enumWorldBlockLayer, listedRenderChunk.getCompiledChunk()));
                GlStateManager.popMatrix();
            }
            GlStateManager.resetColor();
            this.renderChunks.clear();
        }
    }
}

