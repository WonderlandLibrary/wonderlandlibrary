package net.minecraft.world.chunk.storage;

import com.google.common.collect.Maps;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public class RegionFileCache {
   private static final Map regionsByFilename = Maps.newHashMap();

   public static DataInputStream getChunkInputStream(File var0, int var1, int var2) {
      RegionFile var3 = createOrLoadRegionFile(var0, var1, var2);
      return var3.getChunkDataInputStream(var1 & 31, var2 & 31);
   }

   public static synchronized RegionFile createOrLoadRegionFile(File var0, int var1, int var2) {
      File var3 = new File(var0, "region");
      File var4 = new File(var3, "r." + (var1 >> 5) + "." + (var2 >> 5) + ".mca");
      RegionFile var5 = (RegionFile)regionsByFilename.get(var4);
      if (var5 != null) {
         return var5;
      } else {
         if (!var3.exists()) {
            var3.mkdirs();
         }

         if (regionsByFilename.size() >= 256) {
            clearRegionFileReferences();
         }

         RegionFile var6 = new RegionFile(var4);
         regionsByFilename.put(var4, var6);
         return var6;
      }
   }

   public static DataOutputStream getChunkOutputStream(File var0, int var1, int var2) {
      RegionFile var3 = createOrLoadRegionFile(var0, var1, var2);
      return var3.getChunkDataOutputStream(var1 & 31, var2 & 31);
   }

   public static synchronized void clearRegionFileReferences() {
      Iterator var1 = regionsByFilename.values().iterator();

      while(var1.hasNext()) {
         RegionFile var0 = (RegionFile)var1.next();

         try {
            if (var0 != null) {
               var0.close();
            }
         } catch (IOException var3) {
            var3.printStackTrace();
         }
      }

      regionsByFilename.clear();
   }
}
