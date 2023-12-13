package javassist.util.proxy;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.OutputStream;

public class ProxyObjectOutputStream extends ObjectOutputStream {
   public ProxyObjectOutputStream(OutputStream var1) throws IOException {
      super(var1);
   }

   protected void writeClassDescriptor(ObjectStreamClass var1) throws IOException {
      Class var2 = var1.forClass();
      if (ProxyFactory.isProxyClass(var2)) {
         this.writeBoolean(true);
         Class var3 = var2.getSuperclass();
         Class[] var4 = var2.getInterfaces();
         byte[] var5 = ProxyFactory.getFilterSignature(var2);
         String var6 = var3.getName();
         this.writeObject(var6);
         this.writeInt(var4.length - 1);

         for(int var7 = 0; var7 < var4.length; ++var7) {
            Class var8 = var4[var7];
            if (var8 != ProxyObject.class && var8 != Proxy.class) {
               var6 = var4[var7].getName();
               this.writeObject(var6);
            }
         }

         this.writeInt(var5.length);
         this.write(var5);
      } else {
         this.writeBoolean(false);
         super.writeClassDescriptor(var1);
      }

   }
}
