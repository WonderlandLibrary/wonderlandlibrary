package org.apache.commons.io;

import java.io.File;
import java.io.IOException;

public class FileDeleteStrategy {
   public static final FileDeleteStrategy NORMAL = new FileDeleteStrategy("Normal");
   public static final FileDeleteStrategy FORCE = new FileDeleteStrategy.ForceFileDeleteStrategy();
   private final String name;

   protected FileDeleteStrategy(String var1) {
      this.name = var1;
   }

   public boolean deleteQuietly(File var1) {
      if (var1 != null && var1.exists()) {
         try {
            return this.doDelete(var1);
         } catch (IOException var3) {
            return false;
         }
      } else {
         return true;
      }
   }

   public void delete(File var1) throws IOException {
      if (var1.exists() && !this.doDelete(var1)) {
         throw new IOException("Deletion failed: " + var1);
      }
   }

   protected boolean doDelete(File var1) throws IOException {
      return var1.delete();
   }

   public String toString() {
      return "FileDeleteStrategy[" + this.name + "]";
   }

   static class ForceFileDeleteStrategy extends FileDeleteStrategy {
      ForceFileDeleteStrategy() {
         super("Force");
      }

      protected boolean doDelete(File var1) throws IOException {
         FileUtils.forceDelete(var1);
         return true;
      }
   }
}
