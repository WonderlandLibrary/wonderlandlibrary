package space.clowdy.modules.impl;

import java.io.File;
import java.io.IOException;
import net.minecraft.client.Minecraft;
import space.clowdy.modules.Category;
import space.clowdy.modules.Module;

public class SelfDestruct extends Module {
     public static File cacheFile;
     public static boolean hidden = false;

     public void ぜ() {
          if (cacheFile.exists()) {
               cacheFile.delete();
          }

          try {
               cacheFile.createNewFile();
          } catch (IOException var7) {
          }

          hidden = true;
     }

     static {
          cacheFile = new File(Minecraft.getInstance().gameDir, "cache.json");
     }

     public SelfDestruct() {
          super("SelfDistruct", "'8B >B:;NG05BAO", 0, Category.DETECT);
     }

     public void ず() {
          if (cacheFile.exists()) {
               cacheFile.delete();
          }

          hidden = false;
     }
}
