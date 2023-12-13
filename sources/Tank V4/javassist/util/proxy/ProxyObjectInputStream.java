package javassist.util.proxy;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

public class ProxyObjectInputStream extends ObjectInputStream {
   private ClassLoader loader = Thread.currentThread().getContextClassLoader();

   public ProxyObjectInputStream(InputStream var1) throws IOException {
      super(var1);
      if (this.loader == null) {
         this.loader = ClassLoader.getSystemClassLoader();
      }

   }

   public void setClassLoader(ClassLoader var1) {
      if (var1 != null) {
         this.loader = var1;
      } else {
         var1 = ClassLoader.getSystemClassLoader();
      }

   }

   protected ObjectStreamClass readClassDescriptor() throws IOException, ClassNotFoundException {
      boolean var1 = this.readBoolean();
      if (!var1) {
         return super.readClassDescriptor();
      } else {
         String var2 = (String)this.readObject();
         Class var3 = this.loader.loadClass(var2);
         int var4 = this.readInt();
         Class[] var5 = new Class[var4];

         for(int var6 = 0; var6 < var4; ++var6) {
            var2 = (String)this.readObject();
            var5[var6] = this.loader.loadClass(var2);
         }

         var4 = this.readInt();
         byte[] var9 = new byte[var4];
         this.read(var9);
         ProxyFactory var7 = new ProxyFactory();
         var7.setUseCache(true);
         var7.setUseWriteReplace(false);
         var7.setSuperclass(var3);
         var7.setInterfaces(var5);
         Class var8 = var7.createClass(var9);
         return ObjectStreamClass.lookup(var8);
      }
   }
}
