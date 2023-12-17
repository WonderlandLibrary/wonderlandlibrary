// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.chunk;

import javax.annotation.Nullable;

public interface IChunkProvider
{
    @Nullable
    Chunk getLoadedChunk(final int p0, final int p1);
    
    Chunk provideChunk(final int p0, final int p1);
    
    boolean tick();
    
    String makeString();
    
    boolean isChunkGeneratedAt(final int p0, final int p1);
}
