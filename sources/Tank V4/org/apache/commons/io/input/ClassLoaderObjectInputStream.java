package org.apache.commons.io.input;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.io.StreamCorruptedException;
import java.lang.reflect.Proxy;

public class ClassLoaderObjectInputStream extends ObjectInputStream {
   private final ClassLoader classLoader;

   public ClassLoaderObjectInputStream(ClassLoader var1, InputStream var2) throws IOException, StreamCorruptedException {
      super(var2);
      this.classLoader = var1;
   }

   protected Class resolveClass(ObjectStreamClass var1) throws IOException, ClassNotFoundException {
      Class var2 = Class.forName(var1.getName(), false, this.classLoader);
      return var2 != null ? var2 : super.resolveClass(var1);
   }

   protected Class resolveProxyClass(String[] var1) throws IOException, ClassNotFoundException {
      Class[] var2 = new Class[var1.length];

      for(int var3 = 0; var3 < var1.length; ++var3) {
         var2[var3] = Class.forName(var1[var3], false, this.classLoader);
      }

      try {
         return Proxy.getProxyClass(this.classLoader, var2);
      } catch (IllegalArgumentException var4) {
         return super.resolveProxyClass(var1);
      }
   }
}
