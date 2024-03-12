// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.util;

import net.minecraft.util.MathHelper;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraft.client.renderer.chunk.RenderChunk;

public class RenderChunkUtils
{
    public static int getCountBlocks(final RenderChunk renderChunk) {
        final ExtendedBlockStorage[] aextendedblockstorage = renderChunk.getChunk().getBlockStorageArray();
        if (aextendedblockstorage == null) {
            return 0;
        }
        final int i = renderChunk.getPosition().getY() >> 4;
        final ExtendedBlockStorage extendedblockstorage = aextendedblockstorage[i];
        return (extendedblockstorage == null) ? 0 : extendedblockstorage.getBlockRefCount();
    }
    
    public static double getRelativeBufferSize(final RenderChunk renderChunk) {
        final int i = getCountBlocks(renderChunk);
        final double d0 = getRelativeBufferSize(i);
        return d0;
    }
    
    public static double getRelativeBufferSize(final int blockCount) {
        double d0 = blockCount / 4096.0;
        d0 *= 0.995;
        double d2 = d0 * 2.0 - 1.0;
        d2 = MathHelper.clamp_double(d2, -1.0, 1.0);
        return MathHelper.sqrt_double(1.0 - d2 * d2);
    }
}
