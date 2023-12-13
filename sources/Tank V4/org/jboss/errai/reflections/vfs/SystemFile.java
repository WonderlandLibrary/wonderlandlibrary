package org.jboss.errai.reflections.vfs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class SystemFile implements Vfs.File {
   private final SystemDir dir;
   private final File file;

   public SystemFile(SystemDir var1, File var2) {
      this.dir = var1;
      this.file = var2;
   }

   public String getFullPath() {
      return this.file.getPath();
   }

   public String getName() {
      return this.file.getName();
   }

   public String getRelativePath() {
      return this.file.getPath().startsWith(this.dir.getPath()) ? this.file.getPath().substring(this.dir.getPath().length() + 1).replace('\\', '/') : null;
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
