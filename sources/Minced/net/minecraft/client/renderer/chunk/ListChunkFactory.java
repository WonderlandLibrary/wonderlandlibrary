// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.chunk;

import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.world.World;

public class ListChunkFactory implements IRenderChunkFactory
{
    @Override
    public RenderChunk create(final World worldIn, final RenderGlobal renderGlobalIn, final int index) {
        return new ListedRenderChunk(worldIn, renderGlobalIn, index);
    }
}
