/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer.chunk;

import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.chunk.IRenderChunkFactory;
import net.minecraft.client.renderer.chunk.ListedRenderChunk;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class ListChunkFactory
implements IRenderChunkFactory {
    @Override
    public RenderChunk makeRenderChunk(World world, RenderGlobal renderGlobal, BlockPos blockPos, int n) {
        return new ListedRenderChunk(world, renderGlobal, blockPos, n);
    }
}

