// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.world.chunk;

import net.minecraft.world.World;
import java.util.List;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.BlockPos;

public interface IChunkProvider
{
    boolean chunkExists(final int p0, final int p1);
    
    Chunk provideChunk(final int p0, final int p1);
    
    Chunk func_177459_a(final BlockPos p0);
    
    void populate(final IChunkProvider p0, final int p1, final int p2);
    
    boolean func_177460_a(final IChunkProvider p0, final Chunk p1, final int p2, final int p3);
    
    boolean saveChunks(final boolean p0, final IProgressUpdate p1);
    
    boolean unloadQueuedChunks();
    
    boolean canSave();
    
    String makeString();
    
    List func_177458_a(final EnumCreatureType p0, final BlockPos p1);
    
    BlockPos func_180513_a(final World p0, final String p1, final BlockPos p2);
    
    int getLoadedChunkCount();
    
    void func_180514_a(final Chunk p0, final int p1, final int p2);
    
    void saveExtraData();
}
