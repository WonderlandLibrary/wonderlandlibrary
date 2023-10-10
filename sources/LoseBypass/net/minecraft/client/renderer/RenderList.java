/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package net.minecraft.client.renderer;

import java.util.Iterator;
import net.minecraft.client.renderer.ChunkRenderContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.chunk.ListedRenderChunk;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.util.EnumWorldBlockLayer;
import org.lwjgl.opengl.GL11;

public class RenderList
extends ChunkRenderContainer {
    @Override
    public void renderChunkLayer(EnumWorldBlockLayer layer) {
        if (!this.initialized) return;
        Iterator iterator = this.renderChunks.iterator();
        while (true) {
            if (!iterator.hasNext()) {
                GlStateManager.resetColor();
                this.renderChunks.clear();
                return;
            }
            RenderChunk renderchunk = (RenderChunk)iterator.next();
            ListedRenderChunk listedrenderchunk = (ListedRenderChunk)renderchunk;
            GlStateManager.pushMatrix();
            this.preRenderChunk(renderchunk);
            GL11.glCallList((int)listedrenderchunk.getDisplayList(layer, listedrenderchunk.getCompiledChunk()));
            GlStateManager.popMatrix();
        }
    }
}

