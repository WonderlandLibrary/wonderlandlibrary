package org.newdawn.slick.opengl;

import java.security.AccessController;
import java.security.PrivilegedAction;
import org.newdawn.slick.util.Log;

public class ImageDataFactory {
   private static boolean usePngLoader = true;
   private static boolean pngLoaderPropertyChecked = false;
   private static final String PNG_LOADER = "org.newdawn.slick.pngloader";

   private static void checkProperty() {
      if (!pngLoaderPropertyChecked) {
         pngLoaderPropertyChecked = true;

         try {
            AccessController.doPrivileged(new PrivilegedAction() {
               public Object run() {
                  String var1 = System.getProperty("org.newdawn.slick.pngloader");
                  if ("false".equalsIgnoreCase(var1)) {
                     ImageDataFactory.access$002(false);
                  }

                  Log.info("Use Java PNG Loader = " + ImageDataFactory.access$000());
                  return null;
               }
            });
         } catch (Throwable var1) {
         }
      }

   }

   public static LoadableImageData getImageDataFor(String var0) {
      checkProperty();
      var0 = var0.toLowerCase();
      if (var0.endsWith(".tga")) {
         return new TGAImageData();
      } else if (var0.endsWith(".png")) {
         CompositeImageData var2 = new CompositeImageData();
         if (usePngLoader) {
            var2.add(new PNGImageData());
         }

         var2.add(new ImageIOImageData());
         return var2;
      } else {
         return new ImageIOImageData();
      }
   }

   static boolean access$002(boolean var0) {
      usePngLoader = var0;
      return var0;
   }

   static boolean access$000() {
      return usePngLoader;
   }
}
