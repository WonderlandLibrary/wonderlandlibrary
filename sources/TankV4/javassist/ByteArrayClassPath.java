package javassist;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class ByteArrayClassPath implements ClassPath {
   protected String classname;
   protected byte[] classfile;

   public ByteArrayClassPath(String var1, byte[] var2) {
      this.classname = var1;
      this.classfile = var2;
   }

   public void close() {
   }

   public String toString() {
      return "byte[]:" + this.classname;
   }

   public InputStream openClassfile(String var1) {
      return this.classname.equals(var1) ? new ByteArrayInputStream(this.classfile) : null;
   }

   public URL find(String var1) {
      if (this.classname.equals(var1)) {
         String var2 = var1.replace('.', '/') + ".class";

         try {
            return new URL("file:/ByteArrayClassPath/" + var2);
         } catch (MalformedURLException var4) {
         }
      }

      return null;
   }
}
