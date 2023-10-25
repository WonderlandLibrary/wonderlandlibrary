package net.minecraft.world.chunk;

import java.util.List;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

public interface IChunkProvider {
  boolean chunkExists(int paramInt1, int paramInt2);
  
  Chunk provideChunk(int paramInt1, int paramInt2);
  
  Chunk provideChunk(BlockPos paramBlockPos);
  
  void populate(IChunkProvider paramIChunkProvider, int paramInt1, int paramInt2);
  
  boolean func_177460_a(IChunkProvider paramIChunkProvider, Chunk paramChunk, int paramInt1, int paramInt2);
  
  boolean saveChunks(boolean paramBoolean, IProgressUpdate paramIProgressUpdate);
  
  boolean unloadQueuedChunks();
  
  boolean canSave();
  
  String makeString();
  
  List<BiomeGenBase.SpawnListEntry> getPossibleCreatures(EnumCreatureType paramEnumCreatureType, BlockPos paramBlockPos);
  
  BlockPos getStrongholdGen(World paramWorld, String paramString, BlockPos paramBlockPos);
  
  int getLoadedChunkCount();
  
  void recreateStructures(Chunk paramChunk, int paramInt1, int paramInt2);
  
  void saveExtraData();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\world\chunk\IChunkProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */