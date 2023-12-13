package org.newdawn.slick.imageout;

import java.util.HashMap;
import javax.imageio.ImageIO;
import org.newdawn.slick.SlickException;

public class ImageWriterFactory {
   private static HashMap writers = new HashMap();

   public static void registerWriter(String var0, ImageWriter var1) {
      writers.put(var0, var1);
   }

   public static String[] getSupportedFormats() {
      return (String[])((String[])writers.keySet().toArray(new String[0]));
   }

   public static ImageWriter getWriterForFormat(String var0) throws SlickException {
      ImageWriter var1 = (ImageWriter)writers.get(var0);
      if (var1 != null) {
         return var1;
      } else {
         var1 = (ImageWriter)writers.get(var0.toLowerCase());
         if (var1 != null) {
            return var1;
         } else {
            var1 = (ImageWriter)writers.get(var0.toUpperCase());
            if (var1 != null) {
               return var1;
            } else {
               throw new SlickException("No image writer available for: " + var0);
            }
         }
      }
   }

   static {
      String[] var0 = ImageIO.getWriterFormatNames();
      ImageIOWriter var1 = new ImageIOWriter();

      for(int var2 = 0; var2 < var0.length; ++var2) {
         registerWriter(var0[var2], var1);
      }

      TGAWriter var3 = new TGAWriter();
      registerWriter("tga", var3);
   }
}
