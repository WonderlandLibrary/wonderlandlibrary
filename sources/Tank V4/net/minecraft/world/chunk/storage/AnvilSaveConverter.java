package net.minecraft.world.chunk.storage;

import com.google.common.collect.Lists;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.AnvilConverterException;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.biome.WorldChunkManagerHell;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.SaveFormatComparator;
import net.minecraft.world.storage.SaveFormatOld;
import net.minecraft.world.storage.WorldInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AnvilSaveConverter extends SaveFormatOld {
   private static final Logger logger = LogManager.getLogger();

   private void createFile(String var1) {
      File var2 = new File(this.savesDirectory, var1);
      if (!var2.exists()) {
         logger.warn("Unable to create level.dat_mcr backup");
      } else {
         File var3 = new File(var2, "level.dat");
         if (!var3.exists()) {
            logger.warn("Unable to create level.dat_mcr backup");
         } else {
            File var4 = new File(var2, "level.dat_mcr");
            if (!var3.renameTo(var4)) {
               logger.warn("Unable to create level.dat_mcr backup");
            }
         }
      }

   }

   public List getSaveList() throws AnvilConverterException {
      if (this.savesDirectory != null && this.savesDirectory.exists() && this.savesDirectory.isDirectory()) {
         ArrayList var1 = Lists.newArrayList();
         File[] var2 = this.savesDirectory.listFiles();
         File[] var6 = var2;
         int var5 = var2.length;

         for(int var4 = 0; var4 < var5; ++var4) {
            File var3 = var6[var4];
            if (var3.isDirectory()) {
               String var7 = var3.getName();
               WorldInfo var8 = this.getWorldInfo(var7);
               if (var8 != null && (var8.getSaveVersion() == 19132 || var8.getSaveVersion() == 19133)) {
                  boolean var9 = var8.getSaveVersion() != this.getSaveVersion();
                  String var10 = var8.getWorldName();
                  if (StringUtils.isEmpty(var10)) {
                     var10 = var7;
                  }

                  long var11 = 0L;
                  var1.add(new SaveFormatComparator(var7, var10, var8.getLastTimePlayed(), var11, var8.getGameType(), var9, var8.isHardcoreModeEnabled(), var8.areCommandsAllowed()));
               }
            }
         }

         return var1;
      } else {
         throw new AnvilConverterException("Unable to read or access folder where game worlds are saved!");
      }
   }

   public boolean isOldMapFormat(String var1) {
      WorldInfo var2 = this.getWorldInfo(var1);
      return var2 != null && var2.getSaveVersion() != this.getSaveVersion();
   }

   public ISaveHandler getSaveLoader(String var1, boolean var2) {
      return new AnvilSaveHandler(this.savesDirectory, var1, var2);
   }

   private void addRegionFilesToCollection(File var1, Collection var2) {
      File var3 = new File(var1, "region");
      File[] var4 = var3.listFiles(new FilenameFilter(this) {
         final AnvilSaveConverter this$0;

         public boolean accept(File var1, String var2) {
            return var2.endsWith(".mcr");
         }

         {
            this.this$0 = var1;
         }
      });
      if (var4 != null) {
         Collections.addAll(var2, var4);
      }

   }

   private void convertFile(File var1, Iterable var2, WorldChunkManager var3, int var4, int var5, IProgressUpdate var6) {
      Iterator var8 = var2.iterator();

      while(var8.hasNext()) {
         File var7 = (File)var8.next();
         this.convertChunks(var1, var7, var3, var4, var5, var6);
         ++var4;
         int var9 = (int)Math.round(100.0D * (double)var4 / (double)var5);
         var6.setLoadingProgress(var9);
      }

   }

   public String getName() {
      return "Anvil";
   }

   public boolean func_154334_a(String var1) {
      WorldInfo var2 = this.getWorldInfo(var1);
      return var2 != null && var2.getSaveVersion() == 19132;
   }

   public AnvilSaveConverter(File var1) {
      super(var1);
   }

   protected int getSaveVersion() {
      return 19133;
   }

   private void convertChunks(File var1, File var2, WorldChunkManager var3, int var4, int var5, IProgressUpdate var6) {
      try {
         String var7 = var2.getName();
         RegionFile var8 = new RegionFile(var2);
         RegionFile var9 = new RegionFile(new File(var1, var7.substring(0, var7.length() - ".mcr".length()) + ".mca"));

         for(int var10 = 0; var10 < 32; ++var10) {
            int var11;
            for(var11 = 0; var11 < 32; ++var11) {
               if (var8.isChunkSaved(var10, var11) && !var9.isChunkSaved(var10, var11)) {
                  DataInputStream var12 = var8.getChunkDataInputStream(var10, var11);
                  if (var12 == null) {
                     logger.warn("Failed to fetch input stream");
                  } else {
                     NBTTagCompound var13 = CompressedStreamTools.read(var12);
                     var12.close();
                     NBTTagCompound var14 = var13.getCompoundTag("Level");
                     ChunkLoader.AnvilConverterData var15 = ChunkLoader.load(var14);
                     NBTTagCompound var16 = new NBTTagCompound();
                     NBTTagCompound var17 = new NBTTagCompound();
                     var16.setTag("Level", var17);
                     ChunkLoader.convertToAnvilFormat(var15, var17, var3);
                     DataOutputStream var18 = var9.getChunkDataOutputStream(var10, var11);
                     CompressedStreamTools.write(var16, (DataOutput)var18);
                     var18.close();
                  }
               }
            }

            var11 = (int)Math.round(100.0D * (double)(var4 * 1024) / (double)(var5 * 1024));
            int var21 = (int)Math.round(100.0D * (double)((var10 + 1) * 32 + var4 * 1024) / (double)(var5 * 1024));
            if (var21 > var11) {
               var6.setLoadingProgress(var21);
            }
         }

         var8.close();
         var9.close();
      } catch (IOException var20) {
         var20.printStackTrace();
      }

   }

   public void flushCache() {
      RegionFileCache.clearRegionFileReferences();
   }

   public boolean convertMapFormat(String var1, IProgressUpdate var2) {
      var2.setLoadingProgress(0);
      ArrayList var3 = Lists.newArrayList();
      ArrayList var4 = Lists.newArrayList();
      ArrayList var5 = Lists.newArrayList();
      File var6 = new File(this.savesDirectory, var1);
      File var7 = new File(var6, "DIM-1");
      File var8 = new File(var6, "DIM1");
      logger.info("Scanning folders...");
      this.addRegionFilesToCollection(var6, var3);
      if (var7.exists()) {
         this.addRegionFilesToCollection(var7, var4);
      }

      if (var8.exists()) {
         this.addRegionFilesToCollection(var8, var5);
      }

      int var9 = var3.size() + var4.size() + var5.size();
      logger.info("Total conversion count is " + var9);
      WorldInfo var10 = this.getWorldInfo(var1);
      Object var11 = null;
      if (var10.getTerrainType() == WorldType.FLAT) {
         var11 = new WorldChunkManagerHell(BiomeGenBase.plains, 0.5F);
      } else {
         var11 = new WorldChunkManager(var10.getSeed(), var10.getTerrainType(), var10.getGeneratorOptions());
      }

      this.convertFile(new File(var6, "region"), var3, (WorldChunkManager)var11, 0, var9, var2);
      this.convertFile(new File(var7, "region"), var4, new WorldChunkManagerHell(BiomeGenBase.hell, 0.0F), var3.size(), var9, var2);
      this.convertFile(new File(var8, "region"), var5, new WorldChunkManagerHell(BiomeGenBase.sky, 0.0F), var3.size() + var4.size(), var9, var2);
      var10.setSaveVersion(19133);
      if (var10.getTerrainType() == WorldType.DEFAULT_1_1) {
         var10.setTerrainType(WorldType.DEFAULT);
      }

      this.createFile(var1);
      ISaveHandler var12 = this.getSaveLoader(var1, false);
      var12.saveWorldInfo(var10);
      return true;
   }
}
