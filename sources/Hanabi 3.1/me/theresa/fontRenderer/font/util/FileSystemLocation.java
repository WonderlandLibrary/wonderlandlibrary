package me.theresa.fontRenderer.font.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class FileSystemLocation implements SlickResourceLocation {
   private final File root;

   public FileSystemLocation(File root) {
      this.root = root;
   }

   public URL getResource(String ref) {
      try {
         File file = new File(this.root, ref);
         if (!file.exists()) {
            file = new File(ref);
         }

         return !file.exists() ? null : file.toURI().toURL();
      } catch (IOException var3) {
         return null;
      }
   }

   public InputStream getResourceAsStream(String ref) {
      try {
         File file = new File(this.root, ref);
         if (!file.exists()) {
            file = new File(ref);
         }

         return new FileInputStream(file);
      } catch (IOException var3) {
         return null;
      }
   }
}
