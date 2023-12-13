package javassist;

import java.io.File;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.net.URL;

final class JarDirClassPath implements ClassPath {
   JarClassPath[] jars;

   JarDirClassPath(String var1) throws NotFoundException {
      File[] var2 = (new File(var1)).listFiles(new FilenameFilter(this) {
         final JarDirClassPath this$0;

         {
            this.this$0 = var1;
         }

         public boolean accept(File var1, String var2) {
            var2 = var2.toLowerCase();
            return var2.endsWith(".jar") || var2.endsWith(".zip");
         }
      });
      if (var2 != null) {
         this.jars = new JarClassPath[var2.length];

         for(int var3 = 0; var3 < var2.length; ++var3) {
            this.jars[var3] = new JarClassPath(var2[var3].getPath());
         }
      }

   }

   public InputStream openClassfile(String var1) throws NotFoundException {
      if (this.jars != null) {
         for(int var2 = 0; var2 < this.jars.length; ++var2) {
            InputStream var3 = this.jars[var2].openClassfile(var1);
            if (var3 != null) {
               return var3;
            }
         }
      }

      return null;
   }

   public URL find(String var1) {
      if (this.jars != null) {
         for(int var2 = 0; var2 < this.jars.length; ++var2) {
            URL var3 = this.jars[var2].find(var1);
            if (var3 != null) {
               return var3;
            }
         }
      }

      return null;
   }

   public void close() {
      if (this.jars != null) {
         for(int var1 = 0; var1 < this.jars.length; ++var1) {
            this.jars[var1].close();
         }
      }

   }
}
