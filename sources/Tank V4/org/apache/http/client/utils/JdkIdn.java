package org.apache.http.client.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.apache.http.annotation.Immutable;

@Immutable
public class JdkIdn implements Idn {
   private final Method toUnicode;

   public JdkIdn() throws ClassNotFoundException {
      Class var1 = Class.forName("java.net.IDN");

      try {
         this.toUnicode = var1.getMethod("toUnicode", String.class);
      } catch (SecurityException var3) {
         throw new IllegalStateException(var3.getMessage(), var3);
      } catch (NoSuchMethodException var4) {
         throw new IllegalStateException(var4.getMessage(), var4);
      }
   }

   public String toUnicode(String var1) {
      try {
         return (String)this.toUnicode.invoke((Object)null, var1);
      } catch (IllegalAccessException var4) {
         throw new IllegalStateException(var4.getMessage(), var4);
      } catch (InvocationTargetException var5) {
         Throwable var3 = var5.getCause();
         throw new RuntimeException(var3.getMessage(), var3);
      }
   }
}
