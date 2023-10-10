package org.apache.http.client.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.apache.http.annotation.Immutable;

@Immutable
public class CloneUtils {
   public static Object cloneObject(Object var0) throws CloneNotSupportedException {
      if (var0 == null) {
         return null;
      } else if (var0 instanceof Cloneable) {
         Class var1 = var0.getClass();

         Method var2;
         try {
            var2 = var1.getMethod("clone", (Class[])null);
         } catch (NoSuchMethodException var5) {
            throw new NoSuchMethodError(var5.getMessage());
         }

         try {
            Object var3 = var2.invoke(var0, (Object[])null);
            return var3;
         } catch (InvocationTargetException var6) {
            Throwable var4 = var6.getCause();
            if (var4 instanceof CloneNotSupportedException) {
               throw (CloneNotSupportedException)var4;
            } else {
               throw new Error("Unexpected exception", var4);
            }
         } catch (IllegalAccessException var7) {
            throw new IllegalAccessError(var7.getMessage());
         }
      } else {
         throw new CloneNotSupportedException();
      }
   }

   public static Object clone(Object var0) throws CloneNotSupportedException {
      return cloneObject(var0);
   }

   private CloneUtils() {
   }
}
