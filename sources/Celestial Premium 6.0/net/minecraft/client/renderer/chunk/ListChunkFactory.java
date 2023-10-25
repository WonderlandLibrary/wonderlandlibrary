/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.renderer.chunk;

import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.chunk.IRenderChunkFactory;
import net.minecraft.client.renderer.chunk.ListedRenderChunk;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.world.World;

public class ListChunkFactory
implements IRenderChunkFactory {
    @Override
    public RenderChunk create(World worldIn, RenderGlobal p_189565_2_, int p_189565_3_) {
        return new ListedRenderChunk(worldIn, p_189565_2_, p_189565_3_);
    }
}

