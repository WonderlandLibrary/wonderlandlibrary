/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.renderer;

import baritone.Baritone;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ChunkRenderContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.chunk.ListedRenderChunk;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.util.BlockRenderLayer;
import optifine.Config;

public class RenderList
extends ChunkRenderContainer {
    @Override
    public void renderChunkLayer(BlockRenderLayer layer) {
        if (this.initialized) {
            if (this.renderChunks.size() == 0) {
                return;
            }
            for (RenderChunk renderchunk : this.renderChunks) {
                ListedRenderChunk listedrenderchunk = (ListedRenderChunk)renderchunk;
                GlStateManager.pushMatrix();
                this.preRenderChunk(renderchunk);
                GlStateManager.callList(listedrenderchunk.getDisplayList(layer, listedrenderchunk.getCompiledChunk()));
                if (((Boolean)Baritone.settings().renderCachedChunks.value).booleanValue() && !Minecraft.getMinecraft().isSingleplayer()) {
                    GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                }
                GlStateManager.popMatrix();
            }
            if (Config.isMultiTexture()) {
                GlStateManager.bindCurrentTexture();
            }
            GlStateManager.resetColor();
            this.renderChunks.clear();
        }
    }
}

