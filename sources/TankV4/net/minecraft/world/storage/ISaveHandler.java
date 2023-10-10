package net.minecraft.world.storage;

import java.io.File;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.storage.IChunkLoader;

public interface ISaveHandler {
   File getMapFileFromName(String var1);

   void checkSessionLock() throws MinecraftException;

   void saveWorldInfoWithPlayer(WorldInfo var1, NBTTagCompound var2);

   IChunkLoader getChunkLoader(WorldProvider var1);

   String getWorldDirectoryName();

   IPlayerFileData getPlayerNBTManager();

   WorldInfo loadWorldInfo();

   void flush();

   void saveWorldInfo(WorldInfo var1);

   File getWorldDirectory();
}
