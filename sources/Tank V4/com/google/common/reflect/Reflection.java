package com.google.common.reflect;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

@Beta
public final class Reflection {
   public static String getPackageName(Class var0) {
      return getPackageName(var0.getName());
   }

   public static String getPackageName(String var0) {
      int var1 = var0.lastIndexOf(46);
      return var1 < 0 ? "" : var0.substring(0, var1);
   }

   public static void initialize(Class... var0) {
      Class[] var1 = var0;
      int var2 = var0.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         Class var4 = var1[var3];

         try {
            Class.forName(var4.getName(), true, var4.getClassLoader());
         } catch (ClassNotFoundException var6) {
            throw new AssertionError(var6);
         }
      }

   }

   public static Object newProxy(Class var0, InvocationHandler var1) {
      Preconditions.checkNotNull(var1);
      Preconditions.checkArgument(var0.isInterface(), "%s is not an interface", var0);
      Object var2 = Proxy.newProxyInstance(var0.getClassLoader(), new Class[]{var0}, var1);
      return var0.cast(var2);
   }

   private Reflection() {
   }
}
