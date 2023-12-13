package javassist;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;

public class LoaderClassPath implements ClassPath {
   private WeakReference clref;

   public LoaderClassPath(ClassLoader var1) {
      this.clref = new WeakReference(var1);
   }

   public String toString() {
      Object var1 = null;
      if (this.clref != null) {
         var1 = this.clref.get();
      }

      return var1 == null ? "<null>" : var1.toString();
   }

   public InputStream openClassfile(String var1) {
      String var2 = var1.replace('.', '/') + ".class";
      ClassLoader var3 = (ClassLoader)this.clref.get();
      return var3 == null ? null : var3.getResourceAsStream(var2);
   }

   public URL find(String var1) {
      String var2 = var1.replace('.', '/') + ".class";
      ClassLoader var3 = (ClassLoader)this.clref.get();
      return var3 == null ? null : var3.getResource(var2);
   }

   public void close() {
      this.clref = null;
   }
}
