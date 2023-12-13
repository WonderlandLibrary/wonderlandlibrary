package org.newdawn.slick.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class FileSystemLocation implements ResourceLocation {
   private File root;

   public FileSystemLocation(File var1) {
      this.root = var1;
   }

   public URL getResource(String var1) {
      try {
         File var2 = new File(this.root, var1);
         if (!var2.exists()) {
            var2 = new File(var1);
         }

         return !var2.exists() ? null : var2.toURI().toURL();
      } catch (IOException var3) {
         return null;
      }
   }

   public InputStream getResourceAsStream(String var1) {
      try {
         File var2 = new File(this.root, var1);
         if (!var2.exists()) {
            var2 = new File(var1);
         }

         return new FileInputStream(var2);
      } catch (IOException var3) {
         return null;
      }
   }
}
