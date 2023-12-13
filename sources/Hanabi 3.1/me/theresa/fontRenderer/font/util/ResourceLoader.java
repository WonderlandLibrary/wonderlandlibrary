package me.theresa.fontRenderer.font.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

public class ResourceLoader {
   private static final ArrayList locations = new ArrayList();

   public static void addResourceLocation(SlickResourceLocation location) {
      locations.add(location);
   }

   public static void removeResourceLocation(SlickResourceLocation location) {
      locations.remove(location);
   }

   public static void removeAllResourceLocations() {
      locations.clear();
   }

   public static InputStream getResourceAsStream(String ref) {
      InputStream in = null;
      Iterator var2 = locations.iterator();

      while(var2.hasNext()) {
         Object o = var2.next();
         SlickResourceLocation location = (SlickResourceLocation)o;
         in = location.getResourceAsStream(ref);
         if (in != null) {
            break;
         }
      }

      if (in == null) {
         throw new RuntimeException("Resource not found: " + ref);
      } else {
         return new BufferedInputStream(in);
      }
   }

   public static boolean resourceExists(String ref) {
      URL url = null;
      Iterator var2 = locations.iterator();

      do {
         if (!var2.hasNext()) {
            return false;
         }

         Object o = var2.next();
         SlickResourceLocation location = (SlickResourceLocation)o;
         url = location.getResource(ref);
      } while(url == null);

      return true;
   }

   public static URL getResource(String ref) {
      URL url = null;
      Iterator var2 = locations.iterator();

      while(var2.hasNext()) {
         Object o = var2.next();
         SlickResourceLocation location = (SlickResourceLocation)o;
         url = location.getResource(ref);
         if (url != null) {
            break;
         }
      }

      if (url == null) {
         throw new RuntimeException("Resource not found: " + ref);
      } else {
         return url;
      }
   }

   static {
      locations.add(new ClasspathLocation());
      locations.add(new FileSystemLocation(new File(".")));
   }
}
