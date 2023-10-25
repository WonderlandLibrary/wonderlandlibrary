/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package net.minecraft.client.renderer;

import net.minecraft.client.renderer.ChunkRenderContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.util.EnumWorldBlockLayer;
import org.lwjgl.opengl.GL11;

public class VboRenderList
extends ChunkRenderContainer {
    private void setupArrayPointers() {
        GL11.glVertexPointer((int)3, (int)5126, (int)28, (long)0L);
        GL11.glColorPointer((int)4, (int)5121, (int)28, (long)12L);
        GL11.glTexCoordPointer((int)2, (int)5126, (int)28, (long)16L);
        OpenGlHelper.setClientActiveTexture(OpenGlHelper.lightmapTexUnit);
        GL11.glTexCoordPointer((int)2, (int)5122, (int)28, (long)24L);
        OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
    }

    @Override
    public void renderChunkLayer(EnumWorldBlockLayer enumWorldBlockLayer) {
        if (this.initialized) {
            for (RenderChunk renderChunk : this.renderChunks) {
                VertexBuffer vertexBuffer = renderChunk.getVertexBufferByLayer(enumWorldBlockLayer.ordinal());
                GlStateManager.pushMatrix();
                this.preRenderChunk(renderChunk);
                renderChunk.multModelviewMatrix();
                vertexBuffer.bindBuffer();
                this.setupArrayPointers();
                vertexBuffer.drawArrays(7);
                GlStateManager.popMatrix();
            }
            OpenGlHelper.glBindBuffer(OpenGlHelper.GL_ARRAY_BUFFER, 0);
            GlStateManager.resetColor();
            this.renderChunks.clear();
        }
    }
}

