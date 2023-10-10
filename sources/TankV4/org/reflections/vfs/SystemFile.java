package org.reflections.vfs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class SystemFile implements Vfs.File {
   private final SystemDir root;
   private final File file;

   public SystemFile(SystemDir var1, File var2) {
      this.root = var1;
      this.file = var2;
   }

   public String getName() {
      return this.file.getName();
   }

   public String getRelativePath() {
      String var1 = this.file.getPath().replace("\\", "/");
      return var1.startsWith(this.root.getPath()) ? var1.substring(this.root.getPath().length() + 1) : null;
   }

   public InputStream openInputStream() {
      try {
         return new FileInputStream(this.file);
      } catch (FileNotFoundException var2) {
         throw new RuntimeException(var2);
      }
   }

   public String toString() {
      return this.file.toString();
   }
}
