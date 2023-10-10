package javassist.util.proxy;

import java.io.InvalidClassException;
import java.io.InvalidObjectException;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

class SerializedProxy implements Serializable {
   private String superClass;
   private String[] interfaces;
   private byte[] filterSignature;
   private MethodHandler handler;

   SerializedProxy(Class var1, byte[] var2, MethodHandler var3) {
      this.filterSignature = var2;
      this.handler = var3;
      this.superClass = var1.getSuperclass().getName();
      Class[] var4 = var1.getInterfaces();
      int var5 = var4.length;
      this.interfaces = new String[var5 - 1];
      String var6 = ProxyObject.class.getName();
      String var7 = Proxy.class.getName();

      for(int var8 = 0; var8 < var5; ++var8) {
         String var9 = var4[var8].getName();
         if (!var9.equals(var6) && !var9.equals(var7)) {
            this.interfaces[var8] = var9;
         }
      }

   }

   protected Class loadClass(String var1) throws ClassNotFoundException {
      try {
         return (Class)AccessController.doPrivileged(new PrivilegedExceptionAction(this, var1) {
            final String val$className;
            final SerializedProxy this$0;

            {
               this.this$0 = var1;
               this.val$className = var2;
            }

            public Object run() throws Exception {
               ClassLoader var1 = Thread.currentThread().getContextClassLoader();
               return Class.forName(this.val$className, true, var1);
            }
         });
      } catch (PrivilegedActionException var3) {
         throw new RuntimeException("cannot load the class: " + var1, var3.getException());
      }
   }

   Object readResolve() throws ObjectStreamException {
      try {
         int var1 = this.interfaces.length;
         Class[] var2 = new Class[var1];

         for(int var3 = 0; var3 < var1; ++var3) {
            var2[var3] = this.loadClass(this.interfaces[var3]);
         }

         ProxyFactory var8 = new ProxyFactory();
         var8.setSuperclass(this.loadClass(this.superClass));
         var8.setInterfaces(var2);
         Proxy var4 = (Proxy)var8.createClass(this.filterSignature).newInstance();
         var4.setHandler(this.handler);
         return var4;
      } catch (ClassNotFoundException var5) {
         throw new InvalidClassException(var5.getMessage());
      } catch (InstantiationException var6) {
         throw new InvalidObjectException(var6.getMessage());
      } catch (IllegalAccessException var7) {
         throw new InvalidClassException(var7.getMessage());
      }
   }
}
