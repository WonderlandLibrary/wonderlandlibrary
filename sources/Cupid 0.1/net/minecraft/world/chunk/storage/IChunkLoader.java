package net.minecraft.world.chunk.storage;

import java.io.IOException;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public interface IChunkLoader {
  Chunk loadChunk(World paramWorld, int paramInt1, int paramInt2) throws IOException;
  
  void saveChunk(World paramWorld, Chunk paramChunk) throws MinecraftException, IOException;
  
  void saveExtraChunkData(World paramWorld, Chunk paramChunk) throws IOException;
  
  void chunkTick();
  
  void saveExtraData();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\world\chunk\storage\IChunkLoader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */