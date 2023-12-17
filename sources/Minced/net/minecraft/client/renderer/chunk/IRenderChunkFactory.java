// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.chunk;

import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.world.World;

public interface IRenderChunkFactory
{
    RenderChunk create(final World p0, final RenderGlobal p1, final int p2);
}
