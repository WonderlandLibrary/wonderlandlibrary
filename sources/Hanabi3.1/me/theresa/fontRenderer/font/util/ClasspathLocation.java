package me.theresa.fontRenderer.font.util;

import java.io.InputStream;
import java.net.URL;

public class ClasspathLocation implements SlickResourceLocation {
   public URL getResource(String ref) {
      String cpRef = ref.replace('\\', '/');
      return ResourceLoader.class.getClassLoader().getResource(cpRef);
   }

   public InputStream getResourceAsStream(String ref) {
      String cpRef = ref.replace('\\', '/');
      return ResourceLoader.class.getClassLoader().getResourceAsStream(cpRef);
   }
}
