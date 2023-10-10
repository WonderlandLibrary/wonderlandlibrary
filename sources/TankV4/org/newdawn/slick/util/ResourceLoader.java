package org.newdawn.slick.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class ResourceLoader {
   private static ArrayList locations = new ArrayList();

   public static void addResourceLocation(ResourceLocation var0) {
      locations.add(var0);
   }

   public static void removeResourceLocation(ResourceLocation var0) {
      locations.remove(var0);
   }

   public static void removeAllResourceLocations() {
      locations.clear();
   }

   public static InputStream getResourceAsStream(String var0) {
      InputStream var1 = null;

      for(int var2 = 0; var2 < locations.size(); ++var2) {
         ResourceLocation var3 = (ResourceLocation)locations.get(var2);
         var1 = var3.getResourceAsStream(var0);
         if (var1 != null) {
            break;
         }
      }

      if (var1 == null) {
         throw new RuntimeException("Resource not found: " + var0);
      } else {
         return new BufferedInputStream(var1);
      }
   }

   public static boolean resourceExists(String var0) {
      URL var1 = null;

      for(int var2 = 0; var2 < locations.size(); ++var2) {
         ResourceLocation var3 = (ResourceLocation)locations.get(var2);
         var1 = var3.getResource(var0);
         if (var1 != null) {
            return true;
         }
      }

      return false;
   }

   public static URL getResource(String var0) {
      URL var1 = null;

      for(int var2 = 0; var2 < locations.size(); ++var2) {
         ResourceLocation var3 = (ResourceLocation)locations.get(var2);
         var1 = var3.getResource(var0);
         if (var1 != null) {
            break;
         }
      }

      if (var1 == null) {
         throw new RuntimeException("Resource not found: " + var0);
      } else {
         return var1;
      }
   }

   static {
      locations.add(new ClasspathLocation());
      locations.add(new FileSystemLocation(new File(".")));
   }
}
