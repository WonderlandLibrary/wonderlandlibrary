package net.minecraft.world.storage;

import com.google.common.collect.Lists;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.AnvilConverterException;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IProgressUpdate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SaveFormatOld implements ISaveFormat {
   protected final File savesDirectory;
   private static final Logger logger = LogManager.getLogger();

   public ISaveHandler getSaveLoader(String var1, boolean var2) {
      return new SaveHandler(this.savesDirectory, var1, var2);
   }

   public void renameWorld(String var1, String var2) {
      File var3 = new File(this.savesDirectory, var1);
      if (var3.exists()) {
         File var4 = new File(var3, "level.dat");
         if (var4.exists()) {
            try {
               NBTTagCompound var5 = CompressedStreamTools.readCompressed(new FileInputStream(var4));
               NBTTagCompound var6 = var5.getCompoundTag("Data");
               var6.setString("LevelName", var2);
               CompressedStreamTools.writeCompressed(var5, new FileOutputStream(var4));
            } catch (Exception var7) {
               var7.printStackTrace();
            }
         }
      }

   }

   public void flushCache() {
   }

   public String getName() {
      return "Old Format";
   }

   public boolean canLoadWorld(String var1) {
      File var2 = new File(this.savesDirectory, var1);
      return var2.isDirectory();
   }

   public SaveFormatOld(File var1) {
      if (!var1.exists()) {
         var1.mkdirs();
      }

      this.savesDirectory = var1;
   }

   public WorldInfo getWorldInfo(String var1) {
      File var2 = new File(this.savesDirectory, var1);
      if (!var2.exists()) {
         return null;
      } else {
         File var3 = new File(var2, "level.dat");
         NBTTagCompound var4;
         NBTTagCompound var5;
         if (var3.exists()) {
            try {
               var4 = CompressedStreamTools.readCompressed(new FileInputStream(var3));
               var5 = var4.getCompoundTag("Data");
               return new WorldInfo(var5);
            } catch (Exception var7) {
               logger.error((String)("Exception reading " + var3), (Throwable)var7);
            }
         }

         var3 = new File(var2, "level.dat_old");
         if (var3.exists()) {
            try {
               var4 = CompressedStreamTools.readCompressed(new FileInputStream(var3));
               var5 = var4.getCompoundTag("Data");
               return new WorldInfo(var5);
            } catch (Exception var6) {
               logger.error((String)("Exception reading " + var3), (Throwable)var6);
            }
         }

         return null;
      }
   }

   public boolean convertMapFormat(String var1, IProgressUpdate var2) {
      return false;
   }

   public boolean deleteWorldDirectory(String param1) {
      // $FF: Couldn't be decompiled
   }

   public boolean isOldMapFormat(String var1) {
      return false;
   }

   public boolean func_154334_a(String var1) {
      return false;
   }

   public List getSaveList() throws AnvilConverterException {
      ArrayList var1 = Lists.newArrayList();

      for(int var2 = 0; var2 < 5; ++var2) {
         String var3 = "World" + (var2 + 1);
         WorldInfo var4 = this.getWorldInfo(var3);
         if (var4 != null) {
            var1.add(new SaveFormatComparator(var3, "", var4.getLastTimePlayed(), var4.getSizeOnDisk(), var4.getGameType(), false, var4.isHardcoreModeEnabled(), var4.areCommandsAllowed()));
         }
      }

      return var1;
   }

   public boolean func_154335_d(String var1) {
      File var2 = new File(this.savesDirectory, var1);
      if (var2.exists()) {
         return false;
      } else {
         try {
            var2.mkdir();
            var2.delete();
            return true;
         } catch (Throwable var4) {
            logger.warn("Couldn't make new level", var4);
            return false;
         }
      }
   }
}
