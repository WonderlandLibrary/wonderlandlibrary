package net.minecraft.world.storage;

import java.io.File;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.storage.IChunkLoader;

public class SaveHandlerMP implements ISaveHandler {
   public IPlayerFileData getPlayerNBTManager() {
      return null;
   }

   public void flush() {
   }

   public void checkSessionLock() throws MinecraftException {
   }

   public IChunkLoader getChunkLoader(WorldProvider var1) {
      return null;
   }

   public void saveWorldInfoWithPlayer(WorldInfo var1, NBTTagCompound var2) {
   }

   public File getWorldDirectory() {
      return null;
   }

   public String getWorldDirectoryName() {
      return "none";
   }

   public File getMapFileFromName(String var1) {
      return null;
   }

   public WorldInfo loadWorldInfo() {
      return null;
   }

   public void saveWorldInfo(WorldInfo var1) {
   }
}
