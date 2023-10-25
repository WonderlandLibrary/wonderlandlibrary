package net.minecraft.world.storage;

import java.io.File;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.storage.IChunkLoader;

public interface ISaveHandler {
  WorldInfo loadWorldInfo();
  
  void checkSessionLock() throws MinecraftException;
  
  IChunkLoader getChunkLoader(WorldProvider paramWorldProvider);
  
  void saveWorldInfoWithPlayer(WorldInfo paramWorldInfo, NBTTagCompound paramNBTTagCompound);
  
  void saveWorldInfo(WorldInfo paramWorldInfo);
  
  IPlayerFileData getPlayerNBTManager();
  
  void flush();
  
  File getWorldDirectory();
  
  File getMapFileFromName(String paramString);
  
  String getWorldDirectoryName();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\world\storage\ISaveHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */