package net.minecraft.world.storage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.storage.IChunkLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SaveHandler implements ISaveHandler, IPlayerFileData {
   private final File playersDirectory;
   private final File worldDirectory;
   private final File mapDataDir;
   private final String saveDirectoryName;
   private static final Logger logger = LogManager.getLogger();
   private final long initializationTime = MinecraftServer.getCurrentTimeMillis();

   public IChunkLoader getChunkLoader(WorldProvider var1) {
      throw new RuntimeException("Old Chunk Storage is no longer supported.");
   }

   public String getWorldDirectoryName() {
      return this.saveDirectoryName;
   }

   public NBTTagCompound readPlayerData(EntityPlayer var1) {
      NBTTagCompound var2 = null;

      try {
         File var3 = new File(this.playersDirectory, var1.getUniqueID().toString() + ".dat");
         if (var3.exists() && var3.isFile()) {
            var2 = CompressedStreamTools.readCompressed(new FileInputStream(var3));
         }
      } catch (Exception var4) {
         logger.warn("Failed to load player data for " + var1.getName());
      }

      if (var2 != null) {
         var1.readFromNBT(var2);
      }

      return var2;
   }

   public void writePlayerData(EntityPlayer var1) {
      try {
         NBTTagCompound var2 = new NBTTagCompound();
         var1.writeToNBT(var2);
         File var3 = new File(this.playersDirectory, var1.getUniqueID().toString() + ".dat.tmp");
         File var4 = new File(this.playersDirectory, var1.getUniqueID().toString() + ".dat");
         CompressedStreamTools.writeCompressed(var2, new FileOutputStream(var3));
         if (var4.exists()) {
            var4.delete();
         }

         var3.renameTo(var4);
      } catch (Exception var5) {
         logger.warn("Failed to save player data for " + var1.getName());
      }

   }

   public void saveWorldInfoWithPlayer(WorldInfo var1, NBTTagCompound var2) {
      NBTTagCompound var3 = var1.cloneNBTCompound(var2);
      NBTTagCompound var4 = new NBTTagCompound();
      var4.setTag("Data", var3);

      try {
         File var5 = new File(this.worldDirectory, "level.dat_new");
         File var6 = new File(this.worldDirectory, "level.dat_old");
         File var7 = new File(this.worldDirectory, "level.dat");
         CompressedStreamTools.writeCompressed(var4, new FileOutputStream(var5));
         if (var6.exists()) {
            var6.delete();
         }

         var7.renameTo(var6);
         if (var7.exists()) {
            var7.delete();
         }

         var5.renameTo(var7);
         if (var5.exists()) {
            var5.delete();
         }
      } catch (Exception var8) {
         var8.printStackTrace();
      }

   }

   public void saveWorldInfo(WorldInfo var1) {
      NBTTagCompound var2 = var1.getNBTTagCompound();
      NBTTagCompound var3 = new NBTTagCompound();
      var3.setTag("Data", var2);

      try {
         File var4 = new File(this.worldDirectory, "level.dat_new");
         File var5 = new File(this.worldDirectory, "level.dat_old");
         File var6 = new File(this.worldDirectory, "level.dat");
         CompressedStreamTools.writeCompressed(var3, new FileOutputStream(var4));
         if (var5.exists()) {
            var5.delete();
         }

         var6.renameTo(var5);
         if (var6.exists()) {
            var6.delete();
         }

         var4.renameTo(var6);
         if (var4.exists()) {
            var4.delete();
         }
      } catch (Exception var7) {
         var7.printStackTrace();
      }

   }

   public void flush() {
   }

   public File getMapFileFromName(String var1) {
      return new File(this.mapDataDir, var1 + ".dat");
   }

   public SaveHandler(File var1, String var2, boolean var3) {
      this.worldDirectory = new File(var1, var2);
      this.worldDirectory.mkdirs();
      this.playersDirectory = new File(this.worldDirectory, "playerdata");
      this.mapDataDir = new File(this.worldDirectory, "data");
      this.mapDataDir.mkdirs();
      this.saveDirectoryName = var2;
      if (var3) {
         this.playersDirectory.mkdirs();
      }

      this.setSessionLock();
   }

   public IPlayerFileData getPlayerNBTManager() {
      return this;
   }

   public void checkSessionLock() throws MinecraftException {
      try {
         File var1 = new File(this.worldDirectory, "session.lock");
         DataInputStream var2 = new DataInputStream(new FileInputStream(var1));
         if (var2.readLong() != this.initializationTime) {
            throw new MinecraftException("The save is being accessed from another location, aborting");
         } else {
            var2.close();
         }
      } catch (IOException var4) {
         throw new MinecraftException("Failed to check session lock, aborting");
      }
   }

   public WorldInfo loadWorldInfo() {
      File var1 = new File(this.worldDirectory, "level.dat");
      NBTTagCompound var2;
      NBTTagCompound var3;
      if (var1.exists()) {
         try {
            var2 = CompressedStreamTools.readCompressed(new FileInputStream(var1));
            var3 = var2.getCompoundTag("Data");
            return new WorldInfo(var3);
         } catch (Exception var5) {
            var5.printStackTrace();
         }
      }

      var1 = new File(this.worldDirectory, "level.dat_old");
      if (var1.exists()) {
         try {
            var2 = CompressedStreamTools.readCompressed(new FileInputStream(var1));
            var3 = var2.getCompoundTag("Data");
            return new WorldInfo(var3);
         } catch (Exception var4) {
            var4.printStackTrace();
         }
      }

      return null;
   }

   private void setSessionLock() {
      try {
         File var1 = new File(this.worldDirectory, "session.lock");
         DataOutputStream var2 = new DataOutputStream(new FileOutputStream(var1));
         var2.writeLong(this.initializationTime);
         var2.close();
      } catch (IOException var4) {
         var4.printStackTrace();
         throw new RuntimeException("Failed to check session lock, aborting");
      }
   }

   public String[] getAvailablePlayerDat() {
      String[] var1 = this.playersDirectory.list();
      if (var1 == null) {
         var1 = new String[0];
      }

      for(int var2 = 0; var2 < var1.length; ++var2) {
         if (var1[var2].endsWith(".dat")) {
            var1[var2] = var1[var2].substring(0, var1[var2].length() - 4);
         }
      }

      return var1;
   }

   public File getWorldDirectory() {
      return this.worldDirectory;
   }
}
