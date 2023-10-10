package org.jboss.errai.reflections.vfs;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;

public class ZipFile implements Vfs.File {
   private final ZipDir dir;
   private final ZipEntry entry;

   public ZipFile(ZipDir var1, ZipEntry var2) {
      this.dir = var1;
      this.entry = var2;
   }

   public String getFullPath() {
      return this.dir.getPath() + "/" + this.entry.getName();
   }

   public String getName() {
      String var1 = this.entry.getName();
      return var1.substring(var1.lastIndexOf("/") + 1);
   }

   public String getPath() {
      return this.dir.getPath();
   }

   public String getRelativePath() {
      return this.entry.getName();
   }

   public InputStream openInputStream() throws IOException {
      return this.dir.zipFile.getInputStream(this.entry);
   }

   public String toString() {
      return this.dir.getPath() + "!" + File.separatorChar + this.entry.toString();
   }
}
