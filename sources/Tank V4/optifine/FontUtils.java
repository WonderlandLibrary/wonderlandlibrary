package optifine;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;
import net.minecraft.util.ResourceLocation;

public class FontUtils {
   public static Properties readFontProperties(ResourceLocation var0) {
      String var1 = var0.getResourcePath();
      Properties var2 = new Properties();
      String var3 = ".png";
      if (!var1.endsWith(var3)) {
         return var2;
      } else {
         String var4 = var1.substring(0, var1.length() - var3.length()) + ".properties";

         try {
            ResourceLocation var5 = new ResourceLocation(var0.getResourceDomain(), var4);
            InputStream var6 = Config.getResourceStream(Config.getResourceManager(), var5);
            if (var6 == null) {
               return var2;
            }

            Config.log("Loading " + var4);
            var2.load(var6);
         } catch (FileNotFoundException var7) {
         } catch (IOException var8) {
            var8.printStackTrace();
         }

         return var2;
      }
   }

   public static void readCustomCharWidths(Properties var0, float[] var1) {
      Iterator var3 = var0.keySet().iterator();

      while(var3.hasNext()) {
         Object var2 = var3.next();
         String var4 = (String)var2;
         String var5 = "width.";
         if (var4.startsWith(var5)) {
            String var6 = var4.substring(var5.length());
            int var7 = Config.parseInt(var6, -1);
            if (var7 >= 0 && var7 < var1.length) {
               String var8 = var0.getProperty(var4);
               float var9 = Config.parseFloat(var8, -1.0F);
               if (var9 >= 0.0F) {
                  var1[var7] = var9;
               }
            }
         }
      }

   }

   public static float readFloat(Properties var0, String var1, float var2) {
      String var3 = var0.getProperty(var1);
      if (var3 == null) {
         return var2;
      } else {
         float var4 = Config.parseFloat(var3, Float.MIN_VALUE);
         if (var4 == Float.MIN_VALUE) {
            Config.warn("Invalid value for " + var1 + ": " + var3);
            return var2;
         } else {
            return var4;
         }
      }
   }

   public static ResourceLocation getHdFontLocation(ResourceLocation var0) {
      if (!Config.isCustomFonts()) {
         return var0;
      } else if (var0 == null) {
         return var0;
      } else {
         String var1 = var0.getResourcePath();
         String var2 = "textures/";
         String var3 = "mcpatcher/";
         if (!var1.startsWith(var2)) {
            return var0;
         } else {
            var1 = var1.substring(var2.length());
            var1 = var3 + var1;
            ResourceLocation var4 = new ResourceLocation(var0.getResourceDomain(), var1);
            return Config.hasResource(Config.getResourceManager(), var4) ? var4 : var0;
         }
      }
   }
}
