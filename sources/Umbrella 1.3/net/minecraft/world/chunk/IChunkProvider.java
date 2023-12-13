/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.world.chunk;

import java.util.List;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public interface IChunkProvider {
    public boolean chunkExists(int var1, int var2);

    public Chunk provideChunk(int var1, int var2);

    public Chunk func_177459_a(BlockPos var1);

    public void populate(IChunkProvider var1, int var2, int var3);

    public boolean func_177460_a(IChunkProvider var1, Chunk var2, int var3, int var4);

    public boolean saveChunks(boolean var1, IProgressUpdate var2);

    public boolean unloadQueuedChunks();

    public boolean canSave();

    public String makeString();

    public List func_177458_a(EnumCreatureType var1, BlockPos var2);

    public BlockPos func_180513_a(World var1, String var2, BlockPos var3);

    public int getLoadedChunkCount();

    public void func_180514_a(Chunk var1, int var2, int var3);

    public void saveExtraData();
}

