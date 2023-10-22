package me.theresa.fontRenderer.font.opengl;

import java.security.AccessController;
import me.theresa.fontRenderer.font.log.Log;

public class ImageDataFactory {
   private static boolean usePngLoader = true;
   private static boolean pngLoaderPropertyChecked = false;
   private static final String PNG_LOADER = "org.newdawn.slick.pngloader";

   private static void checkProperty() {
      if (!pngLoaderPropertyChecked) {
         pngLoaderPropertyChecked = true;

         try {
            AccessController.doPrivileged(() -> {
               String val = System.getProperty("org.newdawn.slick.pngloader");
               if ("false".equalsIgnoreCase(val)) {
                  usePngLoader = false;
               }

               Log.info("Use Java PNG Loader = " + usePngLoader);
               return null;
            });
         } catch (Throwable var1) {
         }
      }

   }

   public static LoadableImageData getImageDataFor(String ref) {
      checkProperty();
      ref = ref.toLowerCase();
      if (ref.endsWith(".tga")) {
         return new TGAImageData();
      } else if (ref.endsWith(".png")) {
         CompositeImageData data = new CompositeImageData();
         if (usePngLoader) {
            data.add(new PNGImageData());
         }

         data.add(new ImageIOImageData());
         return data;
      } else {
         return new ImageIOImageData();
      }
   }
}
