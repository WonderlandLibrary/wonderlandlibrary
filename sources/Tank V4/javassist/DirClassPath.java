package javassist;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

final class DirClassPath implements ClassPath {
   String directory;

   DirClassPath(String var1) {
      this.directory = var1;
   }

   public InputStream openClassfile(String var1) {
      try {
         char var2 = File.separatorChar;
         String var3 = this.directory + var2 + var1.replace('.', var2) + ".class";
         return new FileInputStream(var3.toString());
      } catch (FileNotFoundException var4) {
      } catch (SecurityException var5) {
      }

      return null;
   }

   public URL find(String var1) {
      char var2 = File.separatorChar;
      String var3 = this.directory + var2 + var1.replace('.', var2) + ".class";
      File var4 = new File(var3);
      if (var4.exists()) {
         try {
            return var4.getCanonicalFile().toURI().toURL();
         } catch (MalformedURLException var6) {
         } catch (IOException var7) {
         }
      }

      return null;
   }

   public void close() {
   }

   public String toString() {
      return this.directory;
   }
}
