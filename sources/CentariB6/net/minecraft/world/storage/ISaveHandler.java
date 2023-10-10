package net.minecraft.world.storage;

import java.io.File;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.storage.IChunkLoader;
import net.minecraft.world.storage.IPlayerFileData;
import net.minecraft.world.storage.WorldInfo;

public interface ISaveHandler {
   void flush();

   void checkSessionLock() throws MinecraftException;

   IChunkLoader getChunkLoader(WorldProvider var1);

   void saveWorldInfo(WorldInfo var1);

   WorldInfo loadWorldInfo();

   File getWorldDirectory();

   String getWorldDirectoryName();

   IPlayerFileData getPlayerNBTManager();

   void saveWorldInfoWithPlayer(WorldInfo var1, NBTTagCompound var2);

   File getMapFileFromName(String var1);
}
