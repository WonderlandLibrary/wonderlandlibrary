package javassist;

import java.io.InputStream;
import java.net.URL;

public class ClassClassPath implements ClassPath {
   private Class thisClass;

   public ClassClassPath(Class var1) {
      this.thisClass = var1;
   }

   ClassClassPath() {
      this(Object.class);
   }

   public InputStream openClassfile(String var1) {
      String var2 = "/" + var1.replace('.', '/') + ".class";
      return this.thisClass.getResourceAsStream(var2);
   }

   public URL find(String var1) {
      String var2 = "/" + var1.replace('.', '/') + ".class";
      return this.thisClass.getResource(var2);
   }

   public void close() {
   }

   public String toString() {
      return this.thisClass.getName() + ".class";
   }
}
