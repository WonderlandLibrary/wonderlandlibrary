/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 */
package net.minecraft.client.renderer;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;

public abstract class ChunkRenderContainer {
    private double viewEntityZ;
    private double viewEntityY;
    protected List<RenderChunk> renderChunks = Lists.newArrayListWithCapacity((int)17424);
    protected boolean initialized;
    private double viewEntityX;

    public void initialize(double d, double d2, double d3) {
        this.initialized = true;
        this.renderChunks.clear();
        this.viewEntityX = d;
        this.viewEntityY = d2;
        this.viewEntityZ = d3;
    }

    public abstract void renderChunkLayer(EnumWorldBlockLayer var1);

    public void preRenderChunk(RenderChunk renderChunk) {
        BlockPos blockPos = renderChunk.getPosition();
        GlStateManager.translate((float)((double)blockPos.getX() - this.viewEntityX), (float)((double)blockPos.getY() - this.viewEntityY), (float)((double)blockPos.getZ() - this.viewEntityZ));
    }

    public void addRenderChunk(RenderChunk renderChunk, EnumWorldBlockLayer enumWorldBlockLayer) {
        this.renderChunks.add(renderChunk);
    }
}

