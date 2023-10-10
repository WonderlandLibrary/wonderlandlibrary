package net.minecraft.world.chunk.storage;

import java.io.File;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.WorldProviderHell;
import net.minecraft.world.storage.SaveHandler;
import net.minecraft.world.storage.ThreadedFileIOBase;
import net.minecraft.world.storage.WorldInfo;

public class AnvilSaveHandler extends SaveHandler {
   public void flush() {
      try {
         ThreadedFileIOBase.getThreadedIOInstance().waitForFinish();
      } catch (InterruptedException var3) {
         var3.printStackTrace();
      }

      RegionFileCache.clearRegionFileReferences();
   }

   public void saveWorldInfoWithPlayer(WorldInfo var1, NBTTagCompound var2) {
      var1.setSaveVersion(19133);
      super.saveWorldInfoWithPlayer(var1, var2);
   }

   public IChunkLoader getChunkLoader(WorldProvider var1) {
      File var2 = this.getWorldDirectory();
      File var3;
      if (var1 instanceof WorldProviderHell) {
         var3 = new File(var2, "DIM-1");
         var3.mkdirs();
         return new AnvilChunkLoader(var3);
      } else if (var1 instanceof WorldProviderEnd) {
         var3 = new File(var2, "DIM1");
         var3.mkdirs();
         return new AnvilChunkLoader(var3);
      } else {
         return new AnvilChunkLoader(var2);
      }
   }

   public AnvilSaveHandler(File var1, String var2, boolean var3) {
      super(var1, var2, var3);
   }
}
