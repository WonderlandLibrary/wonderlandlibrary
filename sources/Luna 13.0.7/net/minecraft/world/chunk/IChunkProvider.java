package net.minecraft.world.chunk;

import java.util.List;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.World;

public abstract interface IChunkProvider
{
  public abstract boolean chunkExists(int paramInt1, int paramInt2);
  
  public abstract Chunk provideChunk(int paramInt1, int paramInt2);
  
  public abstract Chunk func_177459_a(BlockPos paramBlockPos);
  
  public abstract void populate(IChunkProvider paramIChunkProvider, int paramInt1, int paramInt2);
  
  public abstract boolean func_177460_a(IChunkProvider paramIChunkProvider, Chunk paramChunk, int paramInt1, int paramInt2);
  
  public abstract boolean saveChunks(boolean paramBoolean, IProgressUpdate paramIProgressUpdate);
  
  public abstract boolean unloadQueuedChunks();
  
  public abstract boolean canSave();
  
  public abstract String makeString();
  
  public abstract List func_177458_a(EnumCreatureType paramEnumCreatureType, BlockPos paramBlockPos);
  
  public abstract BlockPos func_180513_a(World paramWorld, String paramString, BlockPos paramBlockPos);
  
  public abstract int getLoadedChunkCount();
  
  public abstract void func_180514_a(Chunk paramChunk, int paramInt1, int paramInt2);
  
  public abstract void saveExtraData();
}
