package org.newdawn.slick.util;

import java.io.InputStream;
import java.net.URL;

public class ClasspathLocation implements ResourceLocation {
   static Class class$org$newdawn$slick$util$ResourceLoader;

   public URL getResource(String var1) {
      String var2 = var1.replace('\\', '/');
      return (class$org$newdawn$slick$util$ResourceLoader == null ? (class$org$newdawn$slick$util$ResourceLoader = class$("org.newdawn.slick.util.ResourceLoader")) : class$org$newdawn$slick$util$ResourceLoader).getClassLoader().getResource(var2);
   }

   public InputStream getResourceAsStream(String var1) {
      String var2 = var1.replace('\\', '/');
      return (class$org$newdawn$slick$util$ResourceLoader == null ? (class$org$newdawn$slick$util$ResourceLoader = class$("org.newdawn.slick.util.ResourceLoader")) : class$org$newdawn$slick$util$ResourceLoader).getClassLoader().getResourceAsStream(var2);
   }

   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw (new NoClassDefFoundError()).initCause(var2);
      }
   }
}
