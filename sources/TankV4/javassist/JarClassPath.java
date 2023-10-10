package javassist;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

final class JarClassPath implements ClassPath {
   JarFile jarfile;
   String jarfileURL;

   JarClassPath(String var1) throws NotFoundException {
      try {
         this.jarfile = new JarFile(var1);
         this.jarfileURL = (new File(var1)).getCanonicalFile().toURI().toURL().toString();
      } catch (IOException var3) {
         throw new NotFoundException(var1);
      }
   }

   public InputStream openClassfile(String var1) throws NotFoundException {
      try {
         String var2 = var1.replace('.', '/') + ".class";
         JarEntry var3 = this.jarfile.getJarEntry(var2);
         return var3 != null ? this.jarfile.getInputStream(var3) : null;
      } catch (IOException var4) {
         throw new NotFoundException("broken jar file?: " + this.jarfile.getName());
      }
   }

   public URL find(String var1) {
      String var2 = var1.replace('.', '/') + ".class";
      JarEntry var3 = this.jarfile.getJarEntry(var2);
      if (var3 != null) {
         try {
            return new URL("jar:" + this.jarfileURL + "!/" + var2);
         } catch (MalformedURLException var5) {
         }
      }

      return null;
   }

   public void close() {
      try {
         this.jarfile.close();
         this.jarfile = null;
      } catch (IOException var2) {
      }

   }

   public String toString() {
      return this.jarfile == null ? "<null>" : this.jarfile.toString();
   }
}
